package wisematches.playground;

import wisematches.core.Personality;

import java.util.List;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface GameBoard<S extends GameSettings, H extends GamePlayerHand, M extends GameMove> extends BoardDescription<S, H> {
	/**
	 * Returns unmodifiable collection of done moves.
	 *
	 * @return the unmodifiable collection of done moves.
	 */
	List<M> getGameMoves();

	/**
	 * Returns list of all changes from last move for specified player.
	 *
	 * @param player the player to be checked.
	 * @return the list of all changes from last move for specified player.
	 */
	List<M> getGameChanges(Personality player);


	/**
	 * Closes this game. State changed to interrupted.
	 *
	 * @param player the player who closed the game
	 * @throws UnsuitablePlayerException if specified player doesn't belong to this game.
	 * @throws GameFinishedException     if game already finished.
	 * @see GameResolution#RESIGNED
	 */
	void resign(Personality player) throws BoardUpdatingException;
}
