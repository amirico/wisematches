package wisematches.server.web.services.notify.impl.delivery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.TaskExecutor;
import wisematches.personality.Personality;
import wisematches.personality.account.Account;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notify.*;
import wisematches.server.web.services.notify.impl.delivery.converter.NotificationConverter;
import wisematches.server.web.services.state.PlayerStateListener;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultNotificationDeliveryService implements NotificationDeliveryService {
	private TaskExecutor taskExecutor;
	private PlayerStateManager playerStateManager;
	private NotificationManager notificationManager;
	private NotificationConverter notificationConverter;
	private Collection<NotificationPublisher> notificationPublishers = new ArrayList<>();

	private final Lock lock = new ReentrantLock();
	private final ThePlayerStateListener stateListener = new ThePlayerStateListener();

	private final Set<String> redundantNotifications = new HashSet<>();
	private final Set<String> mandatoryNotifications = new HashSet<>();
	private final Map<Personality, Collection<NotificationContainer>> waitingNotifications = new HashMap<>();
	private final Collection<NotificationDeliveryListener> listeners = new CopyOnWriteArraySet<>();

	private static final Log log = LogFactory.getLog("wisematches.server.notify.distributor");

	public DefaultNotificationDeliveryService() {
	}

	@Override
	public void addNotificationDeliveryListener(NotificationDeliveryListener l) {
		if (l != null) {
			listeners.add(l);
		}
	}

	@Override
	public void removeNotificationDeliveryListener(NotificationDeliveryListener l) {
		listeners.remove(l);
	}

	@Override
	public NotificationPublisher getNotificationPublisher(String name) {
		for (NotificationPublisher publisher : notificationPublishers) {
			if (publisher.getName().equals(name)) {
				return publisher;
			}
		}
		return null;
	}

	@Override
	public Collection<NotificationPublisher> getNotificationPublishers() {
		return Collections.unmodifiableCollection(notificationPublishers);
	}

	@Override
	public Collection<NotificationPublisher> getNotificationPublishers(NotificationScope scope) {
		if (scope == NotificationScope.GLOBAL) {
			return getNotificationPublishers();
		} else {
			final Collection<NotificationPublisher> res = new ArrayList<>(notificationPublishers.size());
			for (NotificationPublisher publisher : notificationPublishers) {
				if (publisher.getNotificationScope() == scope) {
					res.add(publisher);
				}
			}
			return res;
		}
	}

	@Override
	public void raiseNotification(String code, MemberPlayer recipient, NotificationSender sender, Object context) {
		raiseNotification(code, recipient.getAccount(), sender, context);
	}

	@Override
	public void raiseNotification(final String code, final Account recipient, final NotificationSender sender, final Object context) {
		final NotificationDescriptor descriptor = notificationManager.getDescriptor(code);
		if (descriptor == null) {
			throw new IllegalArgumentException("There is no notification with code " + code);
		}
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				lock.lock();
				try {
					processNotification(new NotificationContainer(recipient, sender, descriptor, context));
				} finally {
					lock.unlock();
				}
			}
		});
	}


	protected void processNotification(NotificationContainer notification) {
		processInternalNotification(notification);
		processExternalNotification(notification);
	}

	protected void processInternalNotification(NotificationContainer notification) {
		publishNotification(notification, PublicationType.INTERNAL);
	}

	protected void processExternalNotification(NotificationContainer notification) {
		final String code = notification.getDescriptor().getCode();
		final Personality recipient = notification.getRecipient();
		if (notificationManager.isNotificationEnabled(recipient, code)) {
			if (playerStateManager.isPlayerOnline(recipient)) {
				if (mandatoryNotifications.contains(code)) {
					publishNotification(notification, PublicationType.EXTERNAL);
				} else {
					if (redundantNotifications.contains(code)) {
						final Collection<NotificationContainer> notifications = waitingNotifications.get(recipient);
						if (notifications != null) {
							for (NotificationContainer n : notifications) {
								if (code.equals(n.getDescriptor().getCode())) {
									return; // ignore
								}
							}
						}
						postponeNotification(notification);
					} else {
						postponeNotification(notification);
					}
				}
			} else {
				if (redundantNotifications.contains(code)) {
					final Date activityDate = playerStateManager.getLastActivityDate(recipient);
					final Date notificationDate = notificationManager.getNotificationDate(recipient, code);
					if (notificationDate == null || (activityDate != null && notificationDate.before(activityDate))) {
						publishNotification(notification, PublicationType.EXTERNAL);
					}
					// ignore
				} else {
					publishNotification(notification, PublicationType.EXTERNAL);
				}
			}
		} else {
			log.debug("Notification '" + code + "' was ignored: disabled by player");
		}
	}


	private void postponeNotification(final NotificationContainer notification) {
		Collection<NotificationContainer> notifications = waitingNotifications.get(notification.getRecipient());
		if (notifications == null) {
			notifications = new ArrayList<>();
			waitingNotifications.put(notification.getRecipient(), notifications);
		}
		notifications.add(notification);
	}

	private void processUnpublishedNotifications(final Personality person) {
		final Collection<NotificationContainer> notifications = waitingNotifications.remove(person);
		if (notifications != null) {
			taskExecutor.execute(new Runnable() {
				@Override
				public void run() {
					for (NotificationContainer notification : notifications) {
						publishNotification(notification, PublicationType.EXTERNAL);
					}
				}
			});
		}
	}

	private void publishNotification(final NotificationContainer n, final boolean internal) {
		try {
			final Notification notification = notificationConverter.createMessage(n.getDescriptor(), n.getRecipient(), n.getSender(), n.getContext());

			boolean res;
			if (type == PublicationType.EXTERNAL) {
				if (externalPublisher != null) {
					res = externalPublisher.publishNotification(notification);
				} else {
					return;
				}
			} else if (type == PublicationType.INTERNAL) {
				if (internalPublisher != null) {
					res = internalPublisher.publishNotification(notification);
				} else {
					return;
				}
			} else {
				throw new UnsupportedOperationException("Publisher type is not supported: " + type);
			}

			if (res) {
				for (NotificationDeliveryListener listener : listeners) {
					listener.notificationPublished(notification, type);
				}
			} else {
				for (NotificationDeliveryListener listener : listeners) {
					listener.notificationRejected(notification, type);
				}
			}
		} catch (Exception e) {
			log.error("External notification can't be sent: " + n, e);
		}
	}


	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void setPlayerStateManager(PlayerStateManager playerStateManager) {
		if (this.playerStateManager != null) {
			this.playerStateManager.removePlayerStateListener(stateListener);
		}

		this.playerStateManager = playerStateManager;

		if (this.playerStateManager != null) {
			this.playerStateManager.addPlayerStateListener(stateListener);
		}
	}

	public void setNotificationConverter(NotificationConverter notificationConverter) {
		this.notificationConverter = notificationConverter;
	}

	public void setNotificationPublishers(Collection<NotificationPublisher> notificationPublishers) {
		this.notificationPublishers.clear();

		if (notificationPublishers != null) {
			this.notificationPublishers.addAll(notificationPublishers);
		}
	}

	public void setMandatoryNotifications(Set<String> mandatoryNotifications) {
		this.mandatoryNotifications.clear();

		if (mandatoryNotifications != null) {
			this.mandatoryNotifications.addAll(mandatoryNotifications);
		}
	}

	public void setRedundantNotifications(Set<String> redundantNotifications) {
		this.redundantNotifications.clear();

		if (redundantNotifications != null) {
			this.redundantNotifications.addAll(redundantNotifications);
		}
	}

	public void setNotificationManager(NotificationManager notificationManager) {
		this.notificationManager = notificationManager;
	}


	private class ThePlayerStateListener implements PlayerStateListener {
		private ThePlayerStateListener() {
		}

		@Override
		public void playerOnline(Personality person) {
			playerAlive(person);
		}

		@Override
		public void playerAlive(Personality person) {
			lock.lock();
			try {
				waitingNotifications.remove(person);
			} finally {
				lock.unlock();
			}
		}

		@Override
		public void playerOffline(Personality person) {
			lock.lock();
			try {
				processUnpublishedNotifications(person);
			} finally {
				lock.unlock();
			}
		}
	}
}
