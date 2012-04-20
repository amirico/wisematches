package wisematches.server.web.services.notify.impl.distributor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.TaskExecutor;
import wisematches.personality.Personality;
import wisematches.personality.account.Account;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notify.*;
import wisematches.server.web.services.state.PlayerStateListener;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultNotificationDistributor implements NotificationDistributor {
	private TaskExecutor taskExecutor;
	private PlayerStateManager playerStateManager;
	private NotificationManager notificationManager;
	private NotificationPublisher internalPublisher;
	private NotificationPublisher externalPublisher;

	private final Lock lock = new ReentrantLock();
	private final ThePlayerStateListener stateListener = new ThePlayerStateListener();

	private final Set<String> redundantNotifications = new HashSet<String>();
	private final Set<String> mandatoryNotifications = new HashSet<String>();
	private final Map<Personality, Collection<Notification>> waitingNotifications = new HashMap<Personality, Collection<Notification>>();
	private final Collection<NotificationDistributorListener> listeners = new CopyOnWriteArraySet<NotificationDistributorListener>();

	private static final Log log = LogFactory.getLog("wisematches.server.notify.distributor");

	public DefaultNotificationDistributor() {
	}

	@Override
	public void addNotificationDistributorListener(NotificationDistributorListener l) {
		if (l != null) {
			listeners.add(l);
		}
	}

	@Override
	public void removeNotificationDistributorListener(NotificationDistributorListener l) {
		listeners.remove(l);
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
					processNotification(new Notification(descriptor.getCode(), descriptor.getTemplate(), recipient, sender, context));
				} finally {
					lock.unlock();
				}
			}
		});
	}


	private void processNotification(Notification notification) {
		processInternalNotification(notification);
		processExternalNotification(notification);
	}

	private void processInternalNotification(Notification notification) {
		publishNotification(notification, PublicationType.INTERNAL);
	}

	private void processExternalNotification(Notification notification) {
		final String code = notification.getCode();
		final Personality recipient = notification.getRecipient();
		if (notificationManager.isNotificationEnabled(recipient, code)) {
			if (playerStateManager.isPlayerOnline(recipient)) {
				if (mandatoryNotifications.contains(code)) {
					publishNotification(notification, PublicationType.EXTERNAL);
				} else {
					if (redundantNotifications.contains(code)) {
						for (Notification n : waitingNotifications.get(recipient)) {
							if (code.equals(n.getCode())) {
								return; // ignore
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
					} else {
						return; // ignore
					}
				} else {
					publishNotification(notification, PublicationType.EXTERNAL);
				}
			}
		} else {
			log.debug("Notification '" + code + "' was ignored: disabled by player");
		}
	}


	private void postponeNotification(final Notification notification) {
		Collection<Notification> notifications = waitingNotifications.get(notification.getRecipient());
		if (notifications == null) {
			notifications = new ArrayList<Notification>();
			waitingNotifications.put(notification.getRecipient(), notifications);
		}
		notifications.add(notification);
	}

	private void processUnpublishedNotifications(final Personality person) {
		final Collection<Notification> notifications = waitingNotifications.remove(person);
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				for (Notification notification : notifications) {
					publishNotification(notification, PublicationType.EXTERNAL);
				}
			}
		});
	}

	private void publishNotification(final Notification notification, final PublicationType type) {
		try {
			boolean res;
			if (type == PublicationType.EXTERNAL) {
				res = externalPublisher.publishNotification(notification);
			} else if (type == PublicationType.INTERNAL) {
				res = internalPublisher.publishNotification(notification);
			} else {
				throw new UnsupportedOperationException("Publisher type is not supported: " + type);
			}

			if (res) {
				for (NotificationDistributorListener listener : listeners) {
					listener.notificationPublished(notification, type);
				}
			} else {
				for (NotificationDistributorListener listener : listeners) {
					listener.notificationRejected(notification, type);
				}
			}
		} catch (Exception e) {
			log.error("External notification can't be sent: " + notification);
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

	public void setInternalPublisher(NotificationPublisher internalPublisher) {
		this.internalPublisher = internalPublisher;
	}

	public void setExternalPublisher(NotificationPublisher externalPublisher) {
		this.externalPublisher = externalPublisher;
	}

	public void setMandatoryNotifications(Set<String> mandatoryNotifications) {
		if (this.mandatoryNotifications != null) {
			this.mandatoryNotifications.clear();
		}

		if (mandatoryNotifications != null) {
			this.mandatoryNotifications.addAll(mandatoryNotifications);
		}
	}

	public void setRedundantNotifications(Set<String> redundantNotifications) {
		if (this.redundantNotifications != null) {
			this.redundantNotifications.clear();
		}

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
