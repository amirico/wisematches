package wisematches.core.personality.player;

import wisematches.core.Player;
import wisematches.core.personality.PlayerListener;
import wisematches.core.personality.PlayerManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class GenericPlayerManager extends AbstractPlayerManager implements PlayerManager {
	private GuestManager guestPlayerManager;
	private MemberManager memberPlayerManager;

	private final ThePlayerListener playerListener = new ThePlayerListener();

	public GenericPlayerManager() {
	}

	@Override
	public Player getPlayer(Long id) {
		final Player player = guestPlayerManager.getPlayer(id);
		if (player == null) {
			return memberPlayerManager.getPlayer(id);
		}
		return null;
	}

	public void setGuestPlayerManager(GuestManager guestPlayerManager) {
		this.guestPlayerManager = guestPlayerManager;
	}

	public void setMemberPlayerManager(MemberManager memberPlayerManager) {
		if (this.memberPlayerManager != null) {
			this.memberPlayerManager.removePlayerListener(playerListener);
		}

		this.memberPlayerManager = memberPlayerManager;

		if (this.memberPlayerManager != null) {
			this.memberPlayerManager.addPlayerListener(playerListener);
		}
	}

	private final class ThePlayerListener implements PlayerListener {
		private ThePlayerListener() {
		}

		@Override
		public void playerRegistered(Player player) {
			firePlayerRegistered(player);
		}

		@Override
		public void playerUnregistered(Player player) {
			firePlayerUnregistered(player);
		}
	}
}
