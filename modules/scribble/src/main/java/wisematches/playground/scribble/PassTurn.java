package wisematches.playground.scribble;

import wisematches.core.personality.Player;
import wisematches.playground.GameMove;

import java.util.Date;

/**
 * Predefined move that indicates that turn was passed.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class PassTurn extends GameMove {
	public PassTurn(Player player, int points, int moveNumber, Date moveTime) {
		super(player, points, moveNumber, moveTime);
	}
}
