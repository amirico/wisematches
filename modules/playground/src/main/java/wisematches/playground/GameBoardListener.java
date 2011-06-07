package wisematches.playground;

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
	void gameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move);

	/**
	 * Indicates that game has been finished
	 *
	 * @param board	  the game board
	 * @param resolution the game finalization resolution
	 * @param wonPlayers the winners list or empty list if no winners (draw).
	 */
	void gameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameResolution resolution, Collection<? extends GamePlayerHand> wonPlayers);
}