package wisematches.playground.scribble;

import wisematches.core.Personality;

import java.util.Date;

/**
 * Predefined move that indicates that turn was passed.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class PassTurn extends ScribbleMove {
	protected PassTurn(Personality player) {
		super(player);
	}

	public PassTurn(Personality player, int points, Date moveTime) {
		super(player, points, moveTime);
	}

	@Override
	public ScribbleMoveType getMoveType() {
		return ScribbleMoveType.PASS;
	}
}
