package wisematches.server.gameplaying.room.waiting;

import wisematches.server.player.Player;

import java.util.Collection;

/**
 * {@code WaitingGameManager} provide access to waiting games information. Waiting games don't have a board and can
 * be managed via this manager.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface WaitingGameManager<I extends WaitingGameInfo> {
	void addWaitingGameListener(WaitingGameListener l);

	void removeWaitingGameListener(WaitingGameListener l);

	/**
	 * Opens new waiting game in this manager.
	 *
	 * @param waitingInfo the waiting game info.
	 */
	void openWaitingGame(I waitingInfo);

	/**
	 * Attaches specified player to the waiting game.
	 * <p/>
	 * If game is marked as a ready after attachment the waiting info
	 * is removed from the manager so the returned waiting info must be checked after this method execution.
	 *
	 * @param waitingId the waiting game id
	 * @param player	the player to be added.
	 * @return the modified waiting game info.
	 */
	I attachPlayer(long waitingId, Player player);

	/**
	 * Detaches specified player from the waiting game.
	 *
	 * @param waitingId the waiting game id.
	 * @param player	the player to be attached.
	 * @return the modified waiting game info.
	 */
	I detachPlayer(long waitingId, Player player);

	/**
	 * Close specified waiting game.
	 *
	 * @param waitingGame the waiting game to be closed.
	 */
	void closeWaitingGame(I waitingGame);

	/**
	 * Returns list of all waiting games.
	 *
	 * @return the list of all waiting games.
	 */
	Collection<I> getWaitingGames();
}