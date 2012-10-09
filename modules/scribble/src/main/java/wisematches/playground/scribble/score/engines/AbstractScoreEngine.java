package wisematches.playground.scribble.score.engines;

import wisematches.playground.scribble.ScribbleMoveScore;
import wisematches.playground.scribble.Tile;
import wisematches.playground.scribble.TilesPlacement;
import wisematches.playground.scribble.Word;
import wisematches.playground.scribble.score.ScoreBonus;
import wisematches.playground.scribble.score.ScoreEngine;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class AbstractScoreEngine implements ScoreEngine {
	private final ScoreBonus[] bonuses;
	private final int allHandsBonus;

	private final ScoreBonus.Type[][] matrix;

	private static final int CELLS_NUMBER = 15;
	private static final int LETTERS_IN_HAND = 7;

	protected AbstractScoreEngine(ScoreBonus[] bonuses, int allHandsBonus) {
		this.bonuses = bonuses;
		this.allHandsBonus = allHandsBonus;

		matrix = new ScoreBonus.Type[CELLS_NUMBER][CELLS_NUMBER];
		for (ScoreBonus bonus : bonuses) {
			final int row = bonus.getRow();
			final int col = bonus.getColumn();

			matrix[row][col] = bonus.getType();
			matrix[col][row] = bonus.getType();
			matrix[CELLS_NUMBER - row - 1][col] = bonus.getType();
			matrix[col][CELLS_NUMBER - row - 1] = bonus.getType();
			matrix[row][CELLS_NUMBER - col - 1] = bonus.getType();
			matrix[CELLS_NUMBER - col - 1][row] = bonus.getType();
			matrix[CELLS_NUMBER - row - 1][CELLS_NUMBER - col - 1] = bonus.getType();
			matrix[CELLS_NUMBER - col - 1][CELLS_NUMBER - row - 1] = bonus.getType();
		}
	}

	public ScoreBonus[] getScoreBonuses() {
		return bonuses;
	}

	public ScribbleMoveScore calculateWordScore(Word word, TilesPlacement tilesPlacement) {
		ScoreBonus.Type[] bonuses = new ScoreBonus.Type[word.length()];

		StringBuilder formula = new StringBuilder();
		StringBuilder mults = new StringBuilder();

		int index = 0;
		int handTilesCount = 0;
		short points = 0;
		int mult = 1;
		for (Word.IteratorItem item : word) {
			final int row = item.getRow();
			final int col = item.getColumn();
			final Tile tile = item.getTile();

			final ScoreBonus.Type bonus = matrix[row][col];

			points += tile.getCost();
			if (formula.length() != 0) {
				formula.append(" + ");
			}
			formula.append(tile.getCost());

			if (!tilesPlacement.isBoardTile(item.getTile().getNumber())) {
				handTilesCount++;

				if (bonus != null) {
					bonuses[index] = bonus;

					switch (bonus) {
						case DOUBLE_LETTER:
							formula.append("*2");
							points += tile.getCost();
							break;
						case TRIPLE_LETTER:
							formula.append("*3");
							points += tile.getCost() * 2;
							break;
						case DOUBLE_WORD:
							mults.append("*2");
							mult *= 2;
							break;
						case TRIPLE_WORD:
							mults.append("*3");
							mult *= 3;
							break;
					}
				}
			}
			index++;
		}

		final String multsString = mults.toString(); // In GWT sometimes exception is thrown...
		if (multsString.length() != 0) {
			formula.insert(0, '(');
			formula.append(')');
			formula.append(multsString);
		}
		points *= mult;

		if (allHandsBonus != 0 && handTilesCount == LETTERS_IN_HAND) {
			points += allHandsBonus;
			if (formula.charAt(0) != '(') {
				formula.insert(0, '(');
				formula.append(')');
			}
			formula.append(" + ");
			formula.append(allHandsBonus);
		}

		return new ScribbleMoveScore(points, handTilesCount == LETTERS_IN_HAND, bonuses, formula.toString());
	}
}
