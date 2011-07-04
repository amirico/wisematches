package wisematches.playground.scribble.scores.engines;

import wisematches.playground.scribble.scores.ScoreBonus;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScrabbleScoreEngine extends AbstractScoreEngine {
	public ScrabbleScoreEngine() {
		super(getScoreBonusesImpl(), 50); // All hand bonus
	}

	private static ScoreBonus[] getScoreBonusesImpl() {
		return new ScoreBonus[]{
				new ScoreBonus(0, 0, ScoreBonus.Type.TRIPLE_WORD),
				new ScoreBonus(3, 0, ScoreBonus.Type.DOUBLE_LETTER),
				new ScoreBonus(7, 0, ScoreBonus.Type.TRIPLE_WORD),
				new ScoreBonus(1, 1, ScoreBonus.Type.DOUBLE_WORD),
				new ScoreBonus(5, 1, ScoreBonus.Type.TRIPLE_LETTER),
				new ScoreBonus(2, 2, ScoreBonus.Type.DOUBLE_WORD),
				new ScoreBonus(6, 2, ScoreBonus.Type.DOUBLE_LETTER),
				new ScoreBonus(3, 3, ScoreBonus.Type.DOUBLE_WORD),
				new ScoreBonus(7, 3, ScoreBonus.Type.DOUBLE_LETTER),
				new ScoreBonus(4, 4, ScoreBonus.Type.DOUBLE_WORD),
				new ScoreBonus(5, 5, ScoreBonus.Type.TRIPLE_LETTER),
				new ScoreBonus(6, 6, ScoreBonus.Type.DOUBLE_LETTER),
				new ScoreBonus(7, 7, ScoreBonus.Type.DOUBLE_WORD) //first move is double...
		};
	}
}
