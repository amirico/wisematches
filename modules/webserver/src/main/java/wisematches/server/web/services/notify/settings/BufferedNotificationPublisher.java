package wisematches.server.web.services.notify.settings;

import wisematches.personality.Personality;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notice.Notification;
import wisematches.server.web.services.notify.NotificationPublisher;
import wisematches.server.web.services.notify.NotificationSender;
import wisematches.server.web.services.state.PlayerStateListener;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BufferedNotificationPublisher implements NotificationPublisher {
	private PlayerStateManager playerStateManager;
	private NotificationManager notificationManager;

	private final Lock lock = new ReentrantLock();
	private final PlayerStateListener stateListener = new ThePlayerStateListener();

	private final Map<MemberPlayer, List<Notification>> bufferedNotification = new HashMap<MemberPlayer, List<Notification>>();

	@Override
	public Future<Void> raiseNotification(MemberPlayer player, String code, NotificationSender sender, Map<String, Object> model) {
		final NotificationDescription description = notificationManager.getDescription(code);
		if (description == null) {
			return null;
		}
		return null;
	}

	public void setPlayerStateManager(PlayerStateManager playerStateManager) {
		this.playerStateManager = playerStateManager;
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
					bufferedNotification.remove(player);
				} finally {
					lock.unlock();
				}
			}
		}

		@Override
		public void playerOffline(Personality person) {
			if (person instanceof MemberPlayer) {
				MemberPlayer player = (MemberPlayer) person;
				final List<Notification> remove;
				lock.lock();
				try {
					remove = bufferedNotification.remove(player);
				} finally {
					lock.unlock();
				}
				if (remove != null) {
//					processNotifications(remove);
				}
			}
		}
	}
}
