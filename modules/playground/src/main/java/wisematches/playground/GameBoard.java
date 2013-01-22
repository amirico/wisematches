package wisematches.playground;

import wisematches.core.personality.Player;

import java.util.List;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface GameBoard<S extends GameSettings, H extends GamePlayerHand> extends BoardDescription<S, H> {
	int getMovesCount();

	/**
	 * Returns unmodifiable collection of done moves.
	 *
	 * @return the unmodifiable collection of done moves.
	 */
	List<GameMove> getGameMoves();

	/**
	 * Closes this game. State changed to interrupted.
	 *
	 * @param player the player who closed the game
	 * @throws UnsuitablePlayerException if specified player doesn't belong to this game.
	 * @throws GameFinishedException     if game already finished.
	 * @see GameResolution#RESIGNED
	 */
	void resign(Player player) throws BoardUpdatingException;
}
