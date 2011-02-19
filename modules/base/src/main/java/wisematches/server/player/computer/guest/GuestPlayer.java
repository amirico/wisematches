package wisematches.server.player.computer.guest;

import wisematches.server.player.Player;
import wisematches.server.player.computer.ComputerPlayer;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class GuestPlayer extends ComputerPlayer {
	public static final Player BASIC = new GuestPlayer(1, "Guest", 1200);

	public GuestPlayer(long id, String nickname, int rating) {
		super(id, nickname, rating);
	}
}