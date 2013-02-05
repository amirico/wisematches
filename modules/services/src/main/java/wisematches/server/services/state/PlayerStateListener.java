package wisematches.server.services.state;

import wisematches.core.Personality;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerStateListener {
	void playerOnline(Personality person);

	void playerAlive(Personality person);

	void playerOffline(Personality person);
}
