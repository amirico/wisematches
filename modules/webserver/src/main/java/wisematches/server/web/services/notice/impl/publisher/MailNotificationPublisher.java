package wisematches.server.web.services.notice.impl.publisher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.personality.Personality;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.mail.MailService;
import wisematches.server.mail.SenderName;
import wisematches.server.web.services.notice.Notification;
import wisematches.server.web.services.notice.NotificationPublisher;
import wisematches.server.web.services.notify.settings.NotificationDescription;
import wisematches.server.web.services.state.PlayerStateListener;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MailNotificationPublisher implements NotificationPublisher {
	private MailService mailService;
	private PlayerStateManager playerStateManager;

	private final Lock lock = new ReentrantLock();
	private final PlayerStateListener stateListener = new ThePlayerStateListener();

	private final Map<MemberPlayer, List<Notification>> bufferedNotification = new HashMap<MemberPlayer, List<Notification>>();

	private static final Log log = LogFactory.getLog("wisematches.server.notice.mail");

	public MailNotificationPublisher() {
	}

	@Override
	public void publishNotification(Notification notification, boolean enabled) {
		if (!enabled) {
			return;
		}

		final MemberPlayer member = notification.getMember();
		if (log.isDebugEnabled()) {
			log.debug("Send mail notification " + notification.getDescription().getName() + " to " + member + " ");
		}
		final NotificationDescription description = notification.getDescription();
		if (description.isEvenOnline() || !playerStateManager.isPlayerOnline(member)) {
			processNotification(notification);
		} else {
			lock.lock();
			try {
				List<Notification> notifications = bufferedNotification.get(member);
				if (notifications == null) {
					notifications = new ArrayList<Notification>();
					bufferedNotification.put(member, notifications);
				}
				notifications.add(notification);
			} finally {
				lock.unlock();
			}
		}
	}

	private void processNotification(Notification notification) {
		MemberPlayer member = notification.getMember();
		mailService.sendMail(SenderName.GAME, member.getAccount(), notification.getDescription().getName().replaceAll("\\.", "/"), Collections.singletonMap("context", notification.getContext()));
	}

	private void processNotifications(List<Notification> notifications) {
		final Set<String> series = new HashSet<String>();

		Collections.reverse(notifications);
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

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
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
					processNotifications(remove);
				}
			}
		}
	}
}
