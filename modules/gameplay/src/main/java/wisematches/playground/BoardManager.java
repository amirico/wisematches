package wisematches.playground;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface BoardManager<S extends GameSettings, B extends GameBoard<S, ?, ?>> {
	void addBoardListener(BoardListener l);

	void removeBoardListener(BoardListener l);


	/**
	 * Returns game by it's id.
	 *
	 * @param boardId the game id
	 * @return the game by specified id or <code>null</code> if game is unknown.
	 * @throws BoardLoadingException if board can't be loaded by some reasons.
	 */
	B openBoard(long boardId) throws BoardLoadingException;
}
