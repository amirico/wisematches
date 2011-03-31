package wisematches.server.player.computer.guest;

import wisematches.server.player.Membership;
import wisematches.server.player.Player;
import wisematches.server.player.computer.ComputerPlayer;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class GuestPlayer extends ComputerPlayer {
	public static final Player GUEST = new GuestPlayer(1, "guest");

	public GuestPlayer(long id, String nickname) {
		super(id, nickname, Membership.GUEST);
	}
}
