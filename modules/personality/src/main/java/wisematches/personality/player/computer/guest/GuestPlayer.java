package wisematches.personality.player.computer.guest;

import wisematches.personality.Membership;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.personality.player.computer.ComputerPlayer;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class GuestPlayer extends ComputerPlayer {
	public static final Player GUEST = new GuestPlayer(1, "guest");

	private GuestPlayer(long id, String nickname) {
		super(id, nickname, Membership.GUEST, (short) 1200);
	}

	public static boolean isGuestPlayer(long id) {
		return id == GUEST.getId();
	}

	public static boolean isGuestPlayer(Personality personality) {
		return GUEST.getId() == personality.getId();
	}
}
