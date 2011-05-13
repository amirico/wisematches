/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.playground.scribble.scores.engines;

import wisematches.playground.scribble.scores.ScoreBonus;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleScoreEngine extends AbstractScoreEngine {
	public ScribbleScoreEngine() {
		super(getScoreBonusesImpl(), 30); // All hand bonus
	}

	private static ScoreBonus[] getScoreBonusesImpl() {
		return new ScoreBonus[]{
				new ScoreBonus(0, 3, ScoreBonus.Type.TRIPLE_WORD),
				new ScoreBonus(0, 6, ScoreBonus.Type.TRIPLE_LETTER),
				new ScoreBonus(1, 2, ScoreBonus.Type.DOUBLE_LETTER),
				new ScoreBonus(1, 5, ScoreBonus.Type.DOUBLE_WORD),
				new ScoreBonus(2, 1, ScoreBonus.Type.DOUBLE_LETTER),
				new ScoreBonus(2, 4, ScoreBonus.Type.DOUBLE_LETTER),
				new ScoreBonus(3, 0, ScoreBonus.Type.TRIPLE_WORD),
				new ScoreBonus(3, 3, ScoreBonus.Type.TRIPLE_LETTER),
				new ScoreBonus(3, 7, ScoreBonus.Type.DOUBLE_WORD),
				new ScoreBonus(4, 2, ScoreBonus.Type.DOUBLE_LETTER),
				new ScoreBonus(4, 6, ScoreBonus.Type.DOUBLE_LETTER),
				new ScoreBonus(5, 1, ScoreBonus.Type.DOUBLE_WORD),
				new ScoreBonus(5, 5, ScoreBonus.Type.TRIPLE_LETTER),
				new ScoreBonus(6, 0, ScoreBonus.Type.TRIPLE_LETTER),
				new ScoreBonus(6, 4, ScoreBonus.Type.DOUBLE_LETTER),
				new ScoreBonus(7, 3, ScoreBonus.Type.DOUBLE_WORD),
		};
	}
}
