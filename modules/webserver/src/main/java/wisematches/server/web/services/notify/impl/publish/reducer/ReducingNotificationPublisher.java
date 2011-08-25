package wisematches.server.web.services.notify.impl.publish.reducer;

import wisematches.personality.Personality;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notify.NotificationDescription;
import wisematches.server.web.services.notify.NotificationManager;
import wisematches.server.web.services.notify.NotificationMover;
import wisematches.server.web.services.notify.impl.publish.NotificationPublisherWrapper;
import wisematches.server.web.services.state.PlayerStateListener;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ReducingNotificationPublisher extends NotificationPublisherWrapper {
	private PlayerStateManager playerStateManager;
	private NotificationManager notificationManager;

	private final Lock lock = new ReentrantLock();
	private final PlayerStateListener stateListener = new ThePlayerStateListener();

	private final Map<MemberPlayer, NotificationContainer> bufferedNotification = new HashMap<MemberPlayer, NotificationContainer>();

	public ReducingNotificationPublisher() {
	}

	@Override
	public Future<Void> raiseNotification(String code, MemberPlayer player, NotificationMover mover, Map<String, Object> model) {
		final NotificationDescription description = notificationManager.getDescription(code);
		if (description == null) {
			return notificationPublisher.raiseNotification(code, player, mover, model);
		}

		if (!notificationManager.isNotificationEnabled(description, player)) {
			return null;
		}

		lock.lock();
		try {
			if (!playerStateManager.isPlayerOnline(player) || description.isEvenOnline()) {
				return notificationPublisher.raiseNotification(code, player, mover, model);
			} else {
				NotificationContainer container = bufferedNotification.get(player);
				if (container == null) {
					container = new NotificationContainer();
					bufferedNotification.put(player, container);
				}
				container.addNotification(new NotificationInfo(code, player, mover, description, model));
				return null;
			}
		} finally {
			lock.unlock();
		}
	}

	private void processNotification(NotificationInfo n) {
		notificationPublisher.raiseNotification(n.code, n.player, n.mover, n.model);
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
			if (person instanceof MemberPlayer) {
				MemberPlayer player = (MemberPlayer) person;
				lock.lock();
				try {
					NotificationContainer notificationContainer = bufferedNotification.get(player);
					if (notificationContainer != null) {
						notificationContainer.clear();
					}
				} finally {
					lock.unlock();
				}
			}
		}

		@Override
		public void playerOffline(Personality person) {
			if (person instanceof MemberPlayer) {
				MemberPlayer player = (MemberPlayer) person;
				final NotificationContainer remove;
				lock.lock();
				try {
					remove = bufferedNotification.remove(player);
				} finally {
					lock.unlock();
				}
				if (remove != null) {
					for (NotificationInfo notificationInfo : remove.getNotifications()) {
						processNotification(notificationInfo);
					}
				}
			}
		}
	}
}
