package wisematches.playground.search.player;

import wisematches.playground.search.DesiredEntityContext;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum PlayerSearchArea implements DesiredEntityContext {
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