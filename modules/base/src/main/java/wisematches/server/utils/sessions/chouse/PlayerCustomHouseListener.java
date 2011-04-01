package wisematches.server.utils.sessions.chouse;

import wisematches.server.personality.Personality;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PlayerCustomHouseListener {
	void playerMoveIn(Personality player, String sessionKey);

	void playerMoveOut(Personality player, String sessionKey);
}