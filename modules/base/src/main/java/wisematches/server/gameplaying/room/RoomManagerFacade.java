package wisematches.server.gameplaying.room;

import wisematches.server.gameplaying.board.GameBoard;
import wisematches.server.gameplaying.board.GameBoardListener;
import wisematches.server.gameplaying.board.GameSettings;

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
 *     board.addGameBoardListener(...);
 *  }
 * roomManager.addRoomBoardsListener(new RoomListener() {
 *   public void boardOpened(Room room, long boardId) {
 *        try {
 *            GameBoard board = roomManager.openBoard(boardId);
 *            board.addGameBoardListener(...);
 *				} catch(BoardLoadingException ex) {
 *             ; // ignore exception
 *				}
 *   }
 *
 *   public void boardClosed(Room room, long boardId) {
 *   }
 * });
 *
 * </pre>
 * <p/>
 * All this code now can be replaced by:
 * <pre>
 *  RoomManagerFacade facade = ...;
 *  facade.addGameBoardListener(...);
 * </pre>
 * <p/>
 * Instance of this object can be taken from configuration file.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface RoomManagerFacade<B extends GameBoard<S, ?>, S extends GameSettings> {
	/**
	 * @param listener the game state listener to be added
	 * @see wisematches.server.gameplaying.board.GameBoard#addGameBoardListener(wisematches.server.gameplaying.board.GameBoardListener)
	 */
	void addGameBoardListener(GameBoardListener listener);

	/**
	 * @param listener the game state listener to be removed
	 * @see wisematches.server.gameplaying.board.GameBoard#removeGameBoardListener(wisematches.server.gameplaying.board.GameBoardListener)
	 */
	void removeGameBoardListener(GameBoardListener listener);


	/**
	 * @param gameId the game id to be opened
	 * @return the game by specified id
	 * @throws BoardLoadingException if board can't be loaded by some reason
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