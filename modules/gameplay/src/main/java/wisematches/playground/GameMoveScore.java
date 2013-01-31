package wisematches.playground;

/**
 * {@code GameMoveScore} interface represents one points change after move.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameMoveScore {
	/**
	 * Returns points.
	 *
	 * @return the word's points.
	 */
	short getPoints();

	/**
	 * Returns string that contains calculation formula.
	 * <p/>
	 * For example: <tt>(1*2 + 3 + 2 + 3)*2 + 45</tt>.
	 *
	 * @return the string that contains calculation formula.
	 */
	String getFormula();
}
