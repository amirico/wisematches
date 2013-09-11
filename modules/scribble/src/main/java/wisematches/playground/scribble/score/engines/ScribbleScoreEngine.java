/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.playground.scribble.score.engines;

import wisematches.playground.scribble.score.ScoreBonus;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleScoreEngine extends AbstractScoreEngine {
	public ScribbleScoreEngine() {
		super(getScoreBonusesImpl(), 30); // All hand bonus
	}

	private static ScoreBonus[] getScoreBonusesImpl() {
		return new ScoreBonus[]{
				new ScoreBonus(0, 3, ScoreBonus.Type.W3),
				new ScoreBonus(0, 6, ScoreBonus.Type.L3),
				new ScoreBonus(1, 2, ScoreBonus.Type.L2),
				new ScoreBonus(1, 5, ScoreBonus.Type.W2),
				new ScoreBonus(2, 1, ScoreBonus.Type.L2),
				new ScoreBonus(2, 4, ScoreBonus.Type.L2),
				new ScoreBonus(3, 0, ScoreBonus.Type.W3),
				new ScoreBonus(3, 3, ScoreBonus.Type.L3),
				new ScoreBonus(3, 7, ScoreBonus.Type.W2),
				new ScoreBonus(4, 2, ScoreBonus.Type.L2),
				new ScoreBonus(4, 6, ScoreBonus.Type.L2),
				new ScoreBonus(5, 1, ScoreBonus.Type.W2),
				new ScoreBonus(5, 5, ScoreBonus.Type.L3),
				new ScoreBonus(6, 0, ScoreBonus.Type.L3),
				new ScoreBonus(6, 4, ScoreBonus.Type.L2),
				new ScoreBonus(7, 3, ScoreBonus.Type.W2),
		};
	}
}
