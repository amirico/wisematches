package wisematches.server.gameplaying.scribble.scores;

/**
 * <code>ScoreCalculation</code> object contains information about word's score: word's points,
 * taken bonuses, all hand bonus and calculation formula.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface ScoreCalculation {
	/**
	 * Returns points.
	 *
	 * @return the word's points.
	 */
	short getPoints();

	/**
	 * If all tiles from hand is used.
	 *
	 * @return <code>true</code> if all tiles from hand is used; <code>false</code> - otherwise.
	 */
	boolean isAllFromHand();

	/**
	 * Returns array of all taken bonuses.
	 * <p/>
	 * Warning: this array is mutable.
	 *
	 * @return the array of all taken bonuses.
	 */
	ScoreBonus.Type[] getBonuses();

	/**
	 * Returns string that contains calculation formula.
	 * <p/>
	 * For example: <tt>(1*2 + 3 + 2 + 3)*2 + 45</tt>.
	 *
	 * @return the string that contains calculation formula.
	 */
	String getFormula();
}