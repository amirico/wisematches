package wisematches.server.utils.sessions.chouse;

import wisematches.server.player.Player;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PlayerCustomHouseListener {
	void playerMoveIn(Player player, String sessionKey);

	void playerMoveOut(Player player, String sessionKey);
}