package wisematches.server.utils.sessions;

import wisematches.server.personality.Personality;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class PlayerSessionsEvent {
	private final Personality player;
	private final String sessionKey;

	public PlayerSessionsEvent(Personality player, String sessionKey) {
		this.player = player;
		this.sessionKey = sessionKey;
	}

	public Personality getPlayer() {
		return player;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	@Override
	public String toString() {
		return "PlayerSessionsEvent{" +
				"player=" + player +
				", sessionKey='" + sessionKey + '\'' +
				'}';
	}
}
