package wisematches.server.gameplaying.board;

import java.util.Collection;

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
	<S extends GameSettings, P extends GamePlayerHand> void gameMoveMade(GameBoard<S, P> board, GameMove move);

	/**
	 * Game was finished and someone has won
	 *
	 * @param board	  the board game that was finished
	 * @param wonPlayers the list of players who won  @see wisematches.server.games.board.GameState#FINISHED
	 */
	<S extends GameSettings, P extends GamePlayerHand> void gameFinished(GameBoard<S, P> board, Collection<P> wonPlayers);

	/**
	 * Game was interrupted by specified player.
	 *
	 * @param board			 the interrupted board.
	 * @param interrupterPlayer the interrupter.  @see wisematches.server.games.board.GameState#INTERRUPTED
	 * @param byTimeout		 indicates that game was interrupted by timeout.
	 */
	<S extends GameSettings, P extends GamePlayerHand> void gameInterrupted(GameBoard<S, P> board, P interrupterPlayer, boolean byTimeout);
}
