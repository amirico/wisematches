package wisematches.server.utils.sessions.chouse;

import wisematches.server.personality.Personality;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class PlayerCustomHouseSupport implements PlayerCustomHouse {
	private final AbstractPlayerCustomHouse pch = new AbstractPlayerCustomHouse();

	public PlayerCustomHouseSupport() {
	}

	public void addPlayerCustomHouseListener(PlayerCustomHouseListener l) {
		pch.addPlayerCustomHouseListener(l);
	}

	public void removePlayerCustomHouseListener(PlayerCustomHouseListener l) {
		pch.removePlayerCustomHouseListener(l);
	}

	public void firePlayerMoveIn(Personality player, String sessionKey) {
		pch.firePlayerMoveIn(player, sessionKey);
	}

	public void firePlayerMoveOut(Personality player, String sessionKey) {
		pch.firePlayerMoveOut(player, sessionKey);
	}
}
