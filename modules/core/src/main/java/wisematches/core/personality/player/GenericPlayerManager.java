package wisematches.core.personality.player;

import wisematches.core.Player;
import wisematches.core.personality.PlayerManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class GenericPlayerManager implements PlayerManager {
	private GuestPlayerManager guestPlayerManager;
	private MemberPlayerManager memberPlayerManager;

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

	public void setGuestPlayerManager(GuestPlayerManager guestPlayerManager) {
		this.guestPlayerManager = guestPlayerManager;
	}

	public void setMemberPlayerManager(MemberPlayerManager memberPlayerManager) {
		this.memberPlayerManager = memberPlayerManager;
	}
}
