package wisematches.server.web.services.notify.impl.publisher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.TaskExecutor;
import wisematches.personality.Personality;
import wisematches.server.web.services.notify.NotificationTemplate;
import wisematches.server.web.services.notify.PublicationException;
import wisematches.server.web.services.state.PlayerStateListener;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ReducingNotificationPublisher extends FilteringNotificationPublisher {
	private TaskExecutor taskExecutor;
	private PlayerStateManager playerStateManager;

	private final Lock lock = new ReentrantLock();

	private final Set<String> stateIndependentNotifications = new HashSet<String>();
	private final Map<String, String> groupedNotifications = new HashMap<String, String>();
	private final Map<Personality, Collection<NotificationTemplate>> waitingNotifications = new HashMap<Personality, Collection<NotificationTemplate>>();

	private final ThePlayerStateListener playerStateListener = new ThePlayerStateListener();

	private static final Log log = LogFactory.getLog("wisematches.server.notify.distributor");

	public ReducingNotificationPublisher() {
	}

	@Override
	public boolean publishNotification(NotificationTemplate template) throws PublicationException {
		lock.lock();
		try {
			if (!playerStateManager.isPlayerOnline(template.getRecipient())) {

			} else {

			}

//			 ||
//					stateIndependentNotifications.contains(template.getCode())) {
//				return super.publishNotification(template);
//			}

			Collection<NotificationTemplate> templates = waitingNotifications.get(template.getRecipient());
			if (templates == null) { // new notification for a player
				templates = new ArrayList<NotificationTemplate>();
				waitingNotifications.put(template.getRecipient(), templates);
			}

			String group = groupedNotifications.get(template.getCode());
			if (group != null) { // if grouped notification: try to find is there any
				for (NotificationTemplate t : templates) {
					if (group.equals(groupedNotifications.get(t.getCode()))) {
						return false; // already has the same group
					}
				}
			}
			return templates.add(template);
		} finally {
			lock.unlock();
		}
	}

	private void processUnpublishedNotifications(final Personality person) {
		final Collection<NotificationTemplate> templates = waitingNotifications.remove(person);
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				for (NotificationTemplate template : templates) {
					try {
						ReducingNotificationPublisher.super.publishNotification(template);
					} catch (PublicationException ex) {
						log.error("Notification can't be post processed", ex);
					}
				}
			}
		});
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void setPlayerStateManager(PlayerStateManager playerStateManager) {
		lock.lock();
		try {
			if (this.playerStateManager != null) {
				this.playerStateManager.removePlayerStateListener(playerStateListener);
			}

			this.playerStateManager = playerStateManager;

			if (this.playerStateManager != null) {
				this.playerStateManager.addPlayerStateListener(playerStateListener);
			}
		} finally {
			lock.unlock();
		}
	}

	public void setGroupedNotifications(Map<String, String> groupedNotifications) {
		lock.lock();
		try {
			this.groupedNotifications.clear();
			if (groupedNotifications != null) {
				this.groupedNotifications.putAll(groupedNotifications);
			}
		} finally {
			lock.unlock();
		}
	}

	public void setStateIndependentNotifications(Set<String> stateIndependentNotifications) {
		lock.lock();
		try {
			this.stateIndependentNotifications.clear();
			if (stateIndependentNotifications != null) {
				this.stateIndependentNotifications.addAll(stateIndependentNotifications);
			}
		} finally {
			lock.unlock();
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
