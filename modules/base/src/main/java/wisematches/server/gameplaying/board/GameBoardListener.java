package wisematches.server.gameplaying.board;

/**
 * The listeners of the state a game.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameBoardListener {
	/**
	 * Indicates that player has made move.
	 *
	 * @param board the board
	 * @param move  the made move
	 */
	void gameMoveMade(GameBoard board, GameMove move);

	/**
	 * No one player has won.
	 *
	 * @param board the board game that was finished
	 * @see GameState#DREW
	 */
	void gameDrew(GameBoard board);

	/**
	 * Game was finished and someone has won
	 *
	 * @param board	 the board game that was finished
	 * @param wonPlayer the player who won  @see wisematches.server.games.board.GameState#FINISHED
	 */
	void gameFinished(GameBoard board, GamePlayerHand wonPlayer);

	/**
	 * Game was interrupted by specified player.
	 *
	 * @param board			 the interrupted board.
	 * @param interrupterPlayer the interrupter.  @see wisematches.server.games.board.GameState#INTERRUPTED
	 * @param byTimeout		 indicates that game was interrupted by timeout.
	 */
	void gameInterrupted(GameBoard board, GamePlayerHand interrupterPlayer, boolean byTimeout);
}
