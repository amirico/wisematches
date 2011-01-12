package wisematches.server.games.board;

/**
 * The <code>GameMoveListener</code> notifies about moves on the board.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameMoveListener {
	/**
	 * Indicates that player has made move.
	 *
	 * @param event the move event
	 */
	void playerMoved(GameMoveEvent event);
}
