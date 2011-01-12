package wisematches.server.games.room;

/**
 * Listener that notifies all interested clients about state of <code>RoomManager</code>.
 * <p/>
 * Methods of this listener is invoked when a board is opened or closed.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface RoomBoardsListener {
	/**
	 * Indicates that a board was opened. The board can be received from <code>RoomManager</code> using
	 * <code>openBoard</code> method.
	 *
	 * @param room	the room where board has been opened.
	 * @param boardId the id of opened board.
	 * @see RoomManager#openBoard(long)
	 */
	void boardOpened(Room room, long boardId);

	/**
	 * Indicates that a board was closed by room manager. This method is called when board has been closed and
	 * board's object has been remove from memory. If you call <code>RoomManager.openBoard</code> in this
	 * method, new board will be loaded and opened and <code>boardOpened</code> method of this listener will be invoked.
	 * <p/>
	 * <b>WARNING</b>: DON"T use {@link RoomManager#openBoard(long)} in this
	 * method because it's open new board! Old board doesn't exist when this method is calling.
	 *
	 * @param room	the room where board has been opened.
	 * @param boardId the id of closed board.
	 */
	void boardClosed(Room room, long boardId);
}
