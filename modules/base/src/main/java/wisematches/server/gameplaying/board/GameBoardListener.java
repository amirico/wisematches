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
	 * @param <S>   the game settings type
	 * @param <P>   the player's hand type
	 */
	<S extends GameSettings, P extends GamePlayerHand> void gameMoveDone(GameBoard<S, P> board, GameMove move);

	/**
	 * Indicates that game has been finished
	 *
	 * @param board	  the game board
	 * @param resolution the game finalization resolution
	 * @param wonPlayers the winners list or empty list if no winners (draw).
	 * @param <S>        the game settings type
	 * @param <P>        the player's hand type
	 */
	<S extends GameSettings, P extends GamePlayerHand> void gameFinished(GameBoard<S, P> board, GameResolution resolution, Collection<P> wonPlayers);
}
