/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.utils.sessions;

import wisematches.server.player.Player;

/**
 * Instance of this interface can be added to {@code PlayerSessionsManager} to listen state
 * of player: is it online or offline.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PlayerOnlineStateListener {
	/**
	 * Indicates that specified player creates it's first session and now online.
	 *
	 * @param player the player who is online now
	 */
	void playerIsOnline(Player player);

	/**
	 * Indicates that specified player does not have any active sessions any more.
	 *
	 * @param player the player who is offline
	 */
	void playerIsOffline(Player player);
}
