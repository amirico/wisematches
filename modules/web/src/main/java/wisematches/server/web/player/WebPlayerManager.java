package wisematches.server.web.player;

import wisematches.server.player.Player;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface WebPlayerManager {
	WebPlayer getPlayer(long playerId);

	WebPlayer getPlayer(Player player);
}
