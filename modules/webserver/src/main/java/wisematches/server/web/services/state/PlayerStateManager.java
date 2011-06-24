package wisematches.server.web.services.state;

import wisematches.personality.Personality;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerStateManager {
	void addPlayerStateListener(PlayerStateListener l);

	void removePlayerStateListener(PlayerStateListener l);

	boolean isPlayerOnline(Personality personality);
}
