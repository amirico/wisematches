package wisematches.server.utils.sessions;

import wisematches.server.personality.Personality;

import java.io.Serializable;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PlayerSessionBean extends Serializable {
	Personality getPlayer();

	String getSessionKey();
}
