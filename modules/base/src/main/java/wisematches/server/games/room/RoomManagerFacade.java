package wisematches.server.games.room;

import wisematches.server.games.board.*;

/**
 * {@code RoomManagerFacade} is a interface according to {@code Facade} design pattern which simplified
 * listening state of boards.
 * <p/>
 * Implementation of this interface automatic listen all opened boards and fires appropriate events.
 * <p/>
 * This facade can be replaced by following code:
 * <pre>
 *  RoomManager roomManager = ...;
 *  Collection<GameBoard> boards = roomManager.getOpenedBoards();
 *  for (board: boards) {
 *     board.addGamePlayersListener(...);
 *     board.addGameMoveListener(...);
 *     board.addGameStateListener(...);
 *  }
 * roomManager.addRoomBoardsListener(new RoomBoardsListener() {
 *   public void boardOpened(Room room, long boardId) {
 *        try {
 *            GameBoard board = roomManager.openBoard(boardId);
 *            board.addGamePlayersListener(...);
 *            board.addGameMoveListener(...);
 *            board.addGameStateListener(...);
 *		  } catch(BoardLoadingException ex) {
 *             ; // ignore exception
 *		  }
 *   }
 * <p/>
 *   public void boardClosed(Room room, long boardId) {
 *   }
 * });
 * <p/>
 * </pre>
 * <p/>
 * All this code now can be replaced by:
 * <pre>
 *  RoomManagerFacade facade = ...;
 *  facade.addGamePlayersListener(...);
 *  facade.addGameMoveListener(...);
 *  facade.addGameStateListener(...);
 * </pre>
 * <p/>
 * Instance of this object can be taken from configuration file.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface RoomManagerFacade<B extends GameBoard<S, ?>, S extends GameSettings> {
	/**
	 * This method equals to {@code RoomManager#addRoomSeatesListener(RoomSeatesListener)}.
	 *
	 * @param listener the players listener to be added
	 * @see RoomManager#addRoomSeatesListener(RoomSeatesListener)
	 */
	void addGamePlayersListener(GamePlayersListener listener);

	/**
	 * This method equals to {@code RoomManager#addRoomSeatesListener(RoomSeatesListener)}.
	 *
	 * @param listener the players listener to be removed
	 * @see RoomManager#removeRoomSeatesListener(RoomSeatesListener)
	 */
	void removeGamePlayersListener(GamePlayersListener listener);


	/**
	 * @param listener the game moves listener to be added
	 * @see wisematches.server.games.board.GameBoard#addGameMoveListener(wisematches.server.games.board.GameMoveListener)
	 */
	void addGameMoveListener(GameMoveListener listener);

	/**
	 * @param listener the game moves listener to be removed
	 * @see wisematches.server.games.board.GameBoard#removeGameMoveListener(wisematches.server.games.board.GameMoveListener)
	 */
	void removeGameMoveListener(GameMoveListener listener);


	/**
	 * @param listener the game state listener to be added
	 * @see wisematches.server.games.board.GameBoard#addGameStateListener(wisematches.server.games.board.GameStateListener)
	 */
	void addGameStateListener(GameStateListener listener);

	/**
	 * @param listener the game state listener to be removed
	 * @see wisematches.server.games.board.GameBoard#removeGameStateListener(wisematches.server.games.board.GameStateListener)
	 */
	void removeGameStateListener(GameStateListener listener);


	/**
	 * @param gameId the game id to be opened
	 * @return the game by specified id
	 * @throws BoardLoadingException if board can't be loaded by some reasones
	 * @see RoomManager#openBoard(long)
	 */
	B openBoard(long gameId) throws BoardLoadingException;


	/**
	 * Returns room manager associated with this facade.
	 *
	 * @return the associated room manager.
	 */
	RoomManager<B, S> getRoomManager();
}