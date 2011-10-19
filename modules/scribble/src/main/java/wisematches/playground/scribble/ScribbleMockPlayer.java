package wisematches.playground.scribble;

import wisematches.personality.Membership;
import wisematches.personality.player.Player;
import wisematches.personality.player.computer.ComputerPlayer;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleMockPlayer extends ComputerPlayer {
	public static final Player MOCK1 = new ScribbleMockPlayer(101L, "mock1", Membership.GUEST, (short) 1200);

	public static final Player MOCK2 = new ScribbleMockPlayer(102L, "mock2", Membership.GUEST, (short) 1200);

	/**
	 * Creates new computer player with specified parameters.
	 *
	 * @param id		 the computer player id. The id must be between 1 and 1000. This ids are
	 *                   reserved for computer players.
	 * @param nickname   the player's nickname.
	 * @param membership the player's membership
	 * @param rating	 the player's rating
	 */
	protected ScribbleMockPlayer(long id, String nickname, Membership membership, short rating) {
		super(id, nickname, membership, rating);
	}
}
