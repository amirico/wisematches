package wisematches.playground.scribble;

import wisematches.playground.GameMoveScore;
import wisematches.playground.scribble.score.ScoreBonus;

/**
 * <code>ScribbleMoveScore</code> object contains information about word's score: word's points,
 * taken bonuses, all hand bonus and calculation formula.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class ScribbleMoveScore implements GameMoveScore {
	private final short points;
	private final boolean allFromHand;
	private final ScoreBonus.Type[] bonuses;
	private final String formula;

	public ScribbleMoveScore(short points, boolean allFromHand, ScoreBonus.Type[] bonuses, String formula) {
		this.points = points;
		this.allFromHand = allFromHand;
		this.bonuses = bonuses;
		this.formula = formula;
	}

	public short getPoints() {
		return points;
	}

	public boolean isAllFromHand() {
		return allFromHand;
	}

	public ScoreBonus.Type[] getBonuses() {
		return bonuses;
	}

	public String getFormula() {
		return formula;
	}
}
