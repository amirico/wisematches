package wisematches.server.web.services.notify.impl.publisher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.TaskExecutor;
import wisematches.personality.Personality;
import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.Notification;
import wisematches.server.web.services.notify.NotificationException;
import wisematches.server.web.services.notify.NotificationScope;
import wisematches.server.web.services.notify.impl.NotificationPublisher;
import wisematches.server.web.services.state.PlayerStateListener;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TimelyNotificationPublisher implements NotificationPublisher {
	private TaskExecutor taskExecutor;
	private PlayerStateManager playerStateManager;
	private NotificationPublisher notificationPublisher;

	private final Lock lock = new ReentrantLock();
	private final ThePlayerStateListener stateListener = new ThePlayerStateListener();

	private final Set<String> mandatoryNotifications = new HashSet<>();
	private final Map<Personality, Collection<Notification>> waitingNotifications = new HashMap<>();

	private static final Log log = LogFactory.getLog("wisematches.server.notify.essential");

	public TimelyNotificationPublisher() {
	}

	@Override
	public String getName() {
		return notificationPublisher.getName() + "-timely";
	}

	@Override
	public NotificationScope getNotificationScope() {
		return notificationPublisher.getNotificationScope();
	}

	@Override
	public void publishNotification(Notification notification) throws NotificationException {
		final Personality recipient = notification.getTarget();
		if (mandatoryNotifications.contains(notification.getCode()) || !playerStateManager.isPlayerOnline(recipient)) {
			notificationPublisher.publishNotification(notification);
		} else {
			postponeNotification(notification);
		}
	}

	private void postponeNotification(final Notification notification) {
		lock.lock();
		try {
			final Account account = notification.getTarget();
			Collection<Notification> notifications = waitingNotifications.get(account);
			if (notifications == null) {
				notifications = new ArrayList<>();
				waitingNotifications.put(account, notifications);
			}
			notifications.add(notification);
		} finally {
			lock.unlock();
		}
	}

	private void clearUnpublishedNotifications(final Personality person) {
		lock.lock();
		try {
			waitingNotifications.remove(person);
		} finally {
			lock.unlock();
		}
	}

	private void publishUnpublishedNotifications(final Personality person) {
		lock.lock();
		try {
			final Collection<Notification> notifications = waitingNotifications.remove(person);
			if (notifications != null && notifications.size() != 0) {
				taskExecutor.execute(new Runnable() {
					@Override
					public void run() {
						for (Notification notification : notifications) {
							try {
								notificationPublisher.publishNotification(notification);
							} catch (NotificationException ex) {
								log.error("Notification can't be delivered correctly", ex);
							}
						}
					}
				});
			}
		} finally {
			lock.unlock();
		}
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
		this.notificationPublisher = notificationPublisher;
	}

	public void setMandatoryNotifications(Set<String> mandatoryNotifications) {
		this.mandatoryNotifications.clear();

		if (mandatoryNotifications != null) {
			this.mandatoryNotifications.addAll(mandatoryNotifications);
		}
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

	private class ThePlayerStateListener implements PlayerStateListener {
		private ThePlayerStateListener() {
		}

		@Override
		public void playerOnline(Personality person) {
			playerAlive(person);
		}

		@Override
		public void playerAlive(Personality person) {
			clearUnpublishedNotifications(person);
		}

		@Override
		public void playerOffline(Personality person) {
			publishUnpublishedNotifications(person);
		}
	}
}
