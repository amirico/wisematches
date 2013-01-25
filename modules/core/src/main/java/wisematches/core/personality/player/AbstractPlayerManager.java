package wisematches.core.personality.player;

import wisematches.core.Player;
import wisematches.core.personality.PlayerListener;
import wisematches.core.personality.PlayerManager;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractPlayerManager implements PlayerManager {
	private final Collection<PlayerListener> listeners = new CopyOnWriteArraySet<>();

	protected AbstractPlayerManager() {
	}

	@Override
	public void addPlayerListener(PlayerListener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	@Override
	public void removePlayerListener(PlayerListener listener) {
		listeners.remove(listener);
	}

	protected void firePlayerRegistered(Player player) {
		for (PlayerListener listener : listeners) {
			listener.playerRegistered(player);
		}
	}

	protected void firePlayerUnregistered(Player player) {
		for (PlayerListener listener : listeners) {
			listener.playerUnregistered(player);
		}
	}
}
