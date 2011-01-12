package wisematches.server.games.room;

/**
 * The <code>RoomListener</code> indicates that room has seats for player or when its ended.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface RoomSeatesListener {
	/**
	 * Indicates that player put his/hims ass on the board.
	 *
	 * @param event event that contains information about room, game board and number of free seats.
	 */
	void playerSitDown(RoomSeatesEvent event);

	/**
	 * Indicates that player put his/hims ass of the board.
	 *
	 * @param event event that contains information about room, game board and number of free seats.
	 */
	void playerStandUp(RoomSeatesEvent event);
}
