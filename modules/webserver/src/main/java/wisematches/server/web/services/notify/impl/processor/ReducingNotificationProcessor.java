package wisematches.server.web.services.notify.impl.processor;

import wisematches.personality.Personality;
import wisematches.server.web.services.notify.NotificationTemplate;
import wisematches.server.web.services.state.PlayerStateListener;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ReducingNotificationProcessor extends FilteringNotificationProcessor {
	private PlayerStateManager playerStateManager;

//	private final Map<Personality, >

	private final Collection<Set<String>> groups = new ArrayList<Set<String>>();

	private final Set<String> stateIndependentNotifications = new HashSet<String>();
	private final ThePlayerStateListener playerStateListener = new ThePlayerStateListener();

	public ReducingNotificationProcessor() {
	}

	@Override
	public boolean publishNotification(NotificationTemplate template) throws Exception {
		if (!playerStateManager.isPlayerOnline(template.getRecipient()) ||
				stateIndependentNotifications.contains(template.getCode())) {

		}
		return super.publishNotification(template);
	}

	public void setGroups(Collection<Set<String>> groups) {
		this.groups.clear();

		if (groups != null) {
			this.groups.addAll(groups);
		}
	}

	public void setPlayerStateManager(PlayerStateManager playerStateManager) {
		if (this.playerStateManager != null) {
			this.playerStateManager.removePlayerStateListener(playerStateListener);
		}

		this.playerStateManager = playerStateManager;

		if (this.playerStateManager != null) {
			this.playerStateManager.addPlayerStateListener(playerStateListener);
		}
	}

	public void setStateIndependentNotifications(Set<String> stateIndependentNotifications) {
		this.stateIndependentNotifications.clear();
		if (stateIndependentNotifications != null) {
			this.stateIndependentNotifications.addAll(stateIndependentNotifications);
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
		}

		@Override
		public void playerOffline(Personality person) {
		}
	}
}
