package wisematches.server.web.services.notice.publisher;

import wisematches.personality.Personality;
import wisematches.server.web.services.notice.NotificationDescription;
import wisematches.server.web.services.notice.NotificationManager;
import wisematches.server.web.services.state.PlayerStateListener;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class NotificationPublisherCenter {
	private PlayerStateManager playerStateManager;
	private NotificationManager notificationManager;

	private final Lock lock = new ReentrantLock();
	private final Map<Personality, List<Notification>> bufferedNotification = new HashMap<Personality, List<Notification>>();

	private final PlayerStateListener stateListener = new ThePlayerStateListener();
	private final Collection<NotificationPublisher> publishers = new ArrayList<NotificationPublisher>();

	public NotificationPublisherCenter() {
	}

	public void processNotification(Notification notification) {
		final NotificationDescription description = notification.getDescription();
		final Personality personality = notification.getPersonality();

		if (notificationManager.isNotificationEnabled(description.getName(), personality)) {
			if (description.isEvenOnline()) {
				fireNotification(notification);
			} else {
				if (!playerStateManager.isPlayerOnline(personality)) {
					fireNotification(notification);
				} else {
					lock.lock();
					try {
						List<Notification> notifications = bufferedNotification.get(personality);
						if (notifications == null) {
							notifications = new ArrayList<Notification>();
							bufferedNotification.put(personality, notifications);
						}
						notifications.add(notification);
					} finally {
						lock.unlock();
					}
				}
			}
		}
	}

	public void processNotifications(Collection<Notification> notifications) {
		final Set<String> series = new HashSet<String>();

		for (Notification n : notifications) {
			final NotificationDescription d = n.getDescription();
			final String s = d.getSeries();
			if (s != null && !s.isEmpty()) {
				if (series.contains(s)) {
					continue;
				}
				series.add(s);
			}
			processNotification(n);
		}
	}


	protected void fireNotification(Notification notification) {
		for (NotificationPublisher publisher : publishers) {
			publisher.publishNotification(notification);
		}
	}


	public void setPublishers(Collection<NotificationPublisher> publishers) {
		this.publishers.clear();

		if (publishers != null) {
			this.publishers.addAll(publishers);
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

	public void setNotificationManager(NotificationManager notificationManager) {
		this.notificationManager = notificationManager;
	}


	private class ThePlayerStateListener implements PlayerStateListener {
		private ThePlayerStateListener() {
		}

		@Override
		public void playerOnline(Personality person) {
		}

		@Override
		public void playerAlive(Personality person) {
			lock.lock();
			try {
				bufferedNotification.remove(person);
			} finally {
				lock.unlock();
			}
		}

		@Override
		public void playerOffline(Personality person) {
			final List<Notification> remove;
			lock.lock();
			try {
				remove = bufferedNotification.remove(person);
			} finally {
				lock.unlock();
			}
			if (remove != null) {
				processNotifications(remove);
			}
		}
	}
}
