package wisematches.server.utils.sessions;

import wisematches.server.player.Player;

import java.io.Serializable;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PlayerSessionBean extends Serializable {
	Player getPlayer();

	String getSessionKey();
}
