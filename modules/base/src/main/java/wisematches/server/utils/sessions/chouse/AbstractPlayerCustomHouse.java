package wisematches.server.utils.sessions.chouse;

import wisematches.server.personality.Personality;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class AbstractPlayerCustomHouse implements PlayerCustomHouse {
	private final Collection<PlayerCustomHouseListener> listeners = new CopyOnWriteArraySet<PlayerCustomHouseListener>();

	protected AbstractPlayerCustomHouse() {
	}

	public void addPlayerCustomHouseListener(PlayerCustomHouseListener l) {
		listeners.add(l);
	}

	public void removePlayerCustomHouseListener(PlayerCustomHouseListener l) {
		listeners.remove(l);
	}

	protected void firePlayerMoveIn(Personality player, String sessionKey) {
		for (PlayerCustomHouseListener listener : listeners) {
			listener.playerMoveIn(player, sessionKey);
		}
	}

	protected void firePlayerMoveOut(Personality player, String sessionKey) {
		for (PlayerCustomHouseListener listener : listeners) {
			listener.playerMoveOut(player, sessionKey);
		}
	}
}
