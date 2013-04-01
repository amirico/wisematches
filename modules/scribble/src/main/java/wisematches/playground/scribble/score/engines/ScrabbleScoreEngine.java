package wisematches.playground.scribble.score.engines;

import wisematches.playground.scribble.score.ScoreBonus;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScrabbleScoreEngine extends AbstractScoreEngine {
	public ScrabbleScoreEngine() {
		super(getScoreBonusesImpl(), 50); // All hand bonus
	}

	private static ScoreBonus[] getScoreBonusesImpl() {
		return new ScoreBonus[]{
				new ScoreBonus(0, 0, ScoreBonus.Type.W3),
				new ScoreBonus(3, 0, ScoreBonus.Type.L2),
				new ScoreBonus(7, 0, ScoreBonus.Type.W3),
				new ScoreBonus(1, 1, ScoreBonus.Type.W2),
				new ScoreBonus(5, 1, ScoreBonus.Type.L3),
				new ScoreBonus(2, 2, ScoreBonus.Type.W2),
				new ScoreBonus(6, 2, ScoreBonus.Type.L2),
				new ScoreBonus(3, 3, ScoreBonus.Type.W2),
				new ScoreBonus(7, 3, ScoreBonus.Type.L2),
				new ScoreBonus(4, 4, ScoreBonus.Type.W2),
				new ScoreBonus(5, 5, ScoreBonus.Type.L3),
				new ScoreBonus(6, 6, ScoreBonus.Type.L2),
				new ScoreBonus(7, 7, ScoreBonus.Type.W2) //first move is double...
		};
	}
}
