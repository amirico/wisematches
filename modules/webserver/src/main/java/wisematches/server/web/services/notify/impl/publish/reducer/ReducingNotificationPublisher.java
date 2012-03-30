package wisematches.server.web.services.notify.impl.publish.reducer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.personality.Personality;
import wisematches.personality.account.Account;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notify.NotificationDescription;
import wisematches.server.web.services.notify.NotificationManager;
import wisematches.server.web.services.notify.NotificationSender;
import wisematches.server.web.services.notify.impl.publish.NotificationPublisherWrapper;
import wisematches.server.web.services.state.PlayerStateListener;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * TODO: doesn't work as expected
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ReducingNotificationPublisher extends NotificationPublisherWrapper {
	private PlayerStateManager playerStateManager;
	private NotificationManager notificationManager;

	private final Lock lock = new ReentrantLock();
	private final PlayerStateListener stateListener = new ThePlayerStateListener();

	private final Map<Account, NotificationContainer> bufferedNotification = new HashMap<Account, NotificationContainer>();

	private static final Log log = LogFactory.getLog("wisematches.server.notify.reducing");

	public ReducingNotificationPublisher() {
	}

	@Override
	public Future<Void> raiseNotification(String code, Account account, NotificationSender sender, Map<String, Object> model) {
		final NotificationDescription description = notificationManager.getDescription(code);
		if (description == null) {
			if (log.isDebugEnabled()) {
				log.debug("No description for notification '" + code + "'. Raising in normal way.");
			}
			return notificationPublisher.raiseNotification(code, account, sender, model);
		}

		if (!notificationManager.isNotificationEnabled(description, account)) {
			if (log.isDebugEnabled()) {
				log.debug("Notification '" + code + "' is disabled for player " + account);
			}
			return null;
		}

		lock.lock();
		try {
			if (!playerStateManager.isPlayerOnline(account) || description.isEvenOnline()) {
				if (log.isDebugEnabled()) {
					log.debug("Player '" + account + "' is offline. Raise notification.");
				}
				return notificationPublisher.raiseNotification(code, account, sender, model);
			} else {
				NotificationContainer container = bufferedNotification.get(account);
				if (container == null) {
					container = new NotificationContainer();
					bufferedNotification.put(account, container);
				}
				if (!container.addNotification(new NotificationInfo(account, sender, description, model))) {
					if (log.isDebugEnabled()) {
						log.debug("Notification '" + code + "' was reduced: the same series already raised.");
					}
				}
				return null;
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Future<Void> raiseNotification(String code, MemberPlayer player, NotificationSender sender, Map<String, Object> model) {
		return raiseNotification(code, player.getAccount(), sender, model);
	}

	private void processNotification(NotificationInfo n) {
		notificationPublisher.raiseNotification(n.description.getName(), n.account, n.sender, n.model);
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
