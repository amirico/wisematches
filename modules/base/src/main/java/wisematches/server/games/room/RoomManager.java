package wisematches.server.games.room;

import wisematches.kernel.player.Player;
import wisematches.server.games.board.GameBoard;
import wisematches.server.games.board.GameSettings;

import java.util.Collection;

/**
 * <code>RoomManager</code> allows manage a games in a room.
 * <p/>
 * <code>RoomManager</code> is a user-interface and delegates all work to  <code>GameController</code> but hides it
 * from users.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 * @see wisematches.server.core.game.GameController
 */
public interface RoomManager<B extends GameBoard<S, ?>, S extends GameSettings> {
	void addRoomSeatesListener(RoomSeatesListener roomListener);

	void removeRoomSeatesListener(RoomSeatesListener roomListener);


	void addRoomBoardsListener(RoomBoardsListener roomBoardsListener);

	void removeRoomBoardsListener(RoomBoardsListener roomBoardsListener);


	/**
	 * Returns type of this room.
	 *
	 * @return the type of this room.
	 */
	Room getRoomType();

	/**
	 * Creates new game board with specified settings. Specified player authomatical added to the game.
	 *
	 * @param owner		player who creating this game
	 * @param gameSettings the settings for new game
	 * @return the created game.
	 * @throws BoardCreationException if board can't be created by some reasones.
	 */
	B createBoard(Player owner, S gameSettings) throws BoardCreationException;

	/**
	 * Retruns game by it's id.
	 *
	 * @param gameId the game id
	 * @return the game by specified id or <code>null</code> if game is unknown.
	 * @throws BoardLoadingException if board can't be loaded by some reasones.
	 */
	B openBoard(long gameId) throws BoardLoadingException;

	/**
	 * Updates specified board in boards storage. Save all changes in board to storage.
	 * <p/>
	 * This operation should be used only if {@code GamePlayerHand} is changed. In other cases
	 * {@code wisematches.server.core.room.RoomManager} automatical store board into storage after
	 * any changes.
	 *
	 * @param board the board to be updated.
	 */
	void updateBoard(B board);

	/**
	 * Returns list of games with <code>GameState.WAITING</code>
	 *
	 * @return the list of waiting games.
	 * @see wisematches.server.games.board.GameState#WAITING
	 */
	Collection<B> getWaitingBoards();

	/**
	 * Returns collection of all opened boards at this moment. This methods returns copy of boards
	 * and should not be used in time/memory critical operations.
	 * <p/>
	 * Base scenario for this method: initialization of listeners for already opened boards.
	 *
	 * @return the copy of collection of all opened boards.
	 */
	Collection<B> getOpenedBoards();

	/**
	 * Returns list of all games for specified players that have specified state. If no ony game exist empty
	 * collection will be returned.
	 *
	 * @param player the player who games should be returned.
	 * @return the list of player's games or empty collection.
	 */
	Collection<B> getActiveBoards(Player player);


	/**
	 * Returns searches engine for this room. Searches engine allow do boards search by some criteria,
	 * for example expired boards.
	 *
	 * @return the search engine.
	 */
	SearchesEngine<B> getSearchesEngine();
}
