package wisematches.client.android.app.playground.scribble.model;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScoreEngine {
	private final int allHandBonus;
	private final ScoreBonus[] scoreBonuses;

	private final ScoreBonus[][] bonusesMatrix = new ScoreBonus[8][8];

	public ScoreEngine(ScoreBonus[] scoreBonuses, int allHandBonus) {
		this.allHandBonus = allHandBonus;
		this.scoreBonuses = scoreBonuses;

		for (ScoreBonus scoreBonus : scoreBonuses) {
			bonusesMatrix[scoreBonus.getRow()][scoreBonus.getColumn()] = scoreBonus;
		}
	}

	public int getAllHandBonus() {
		return allHandBonus;
	}

	public ScoreBonus[] getScoreBonuses() {
		return scoreBonuses;
	}

	public ScoreBonus getScoreBonus(int row, int col) {
		return bonusesMatrix[row < 8 ? row : 14 - row][col < 8 ? col : 14 - col];
	}
}
