package wisematches.server.web.server.sessions;

import wisematches.server.player.Player;
import wisematches.server.utils.sessions.chouse.PlayerCustomHouse;

import javax.servlet.http.HttpSession;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface WebSessionCustomHouse extends PlayerCustomHouse {
	void performLogin(Player player, HttpSession session);

	void performLogout(Player player, HttpSession session);

	Player getLoggedInPlayer(HttpSession session);
}
