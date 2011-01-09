package wisematches.server.core.sessions;

import wisematches.kernel.player.Player;

import java.io.Serializable;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PlayerSessionBean extends Serializable {
    Player getPlayer();

    String getSessionKey();
}
