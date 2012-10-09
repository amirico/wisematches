package wisematches.playground.scribble.score;

import wisematches.playground.scribble.ScribbleMoveScore;
import wisematches.playground.scribble.TilesPlacement;
import wisematches.playground.scribble.Word;

/**
 * <code>ScoreEngine</code> is base interface that allows calculate scores of specified word. Object of
 * this interface also contains bonuses matrix for board and score calculation.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface ScoreEngine {
	/**
	 * Returns array of bonuses for nord-west corner of board. In all board has six corners: from nord-west to
	 * west-nord (nord-west, nord-east, east-nord, east-south, south-east, south-west, west-south, west-nord).
	 *
	 * @return the array of placements nord-west corner.
	 */
	ScoreBonus[] getScoreBonuses();

	/**
	 * Returns score calculation for specified word.
	 *
	 * @param word		   the for for score calculation.
	 * @param tilesPlacement the placements of tiles.
	 * @return the score calculation.
	 * @throws NullPointerException is specified word is <code>null</code>.
	 */
	ScribbleMoveScore calculateWordScore(Word word, TilesPlacement tilesPlacement);
}
