package wisematches.server.web.services.search.player;

/**
 * Players search area. Only players from specified area will be searched.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum PlayerSearchArea {
	/**
	 * Players will be searched only in friends list.
	 */
	FRIENDS,
	/**
	 * Players will be searched only in played formerly.
	 */
	FORMERLY,
	/**
	 * All players will be searched.
	 */
	PLAYERS
}
