package wisematches.playground.scribble;

import wisematches.core.Personality;
import wisematches.playground.GameMove;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class ScribbleMove extends GameMove {
	protected ScribbleMove(Personality player) {
		super(player);
	}

	protected ScribbleMove(Personality player, int points, Date moveTime) {
		super(player, points, moveTime);
	}

	public abstract ScribbleMoveType getMoveType();
}
