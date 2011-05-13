package wisematches.personality.player.computer.guest;

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
}
