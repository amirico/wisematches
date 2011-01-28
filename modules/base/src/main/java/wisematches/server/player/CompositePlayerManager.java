package wisematches.server.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class CompositePlayerManager implements PlayerManager {
	private final PlayerListener playerListener = new ThePlayerListener();
	private final Set<PlayerListener> listeners = new CopyOnWriteArraySet<PlayerListener>();
	private final Collection<PlayerManager> playerManagers = new ArrayList<PlayerManager>();

	public CompositePlayerManager() {
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

	@Override
	public Player getPlayer(long playerId) {
		Player player = null;
		for (Iterator<PlayerManager> iterator = playerManagers.iterator(); iterator.hasNext() && player == null;) {
			player = iterator.next().getPlayer(playerId);
		}
		return null;
	}

	@Override
	public Player findByEmail(String email) {
		Player player = null;
		for (Iterator<PlayerManager> iterator = playerManagers.iterator(); iterator.hasNext() && player == null;) {
			player = iterator.next().findByEmail(email);
		}
		return null;
	}

	@Override
	public Player findByUsername(String username) {
		Player player = null;
		for (Iterator<PlayerManager> iterator = playerManagers.iterator(); iterator.hasNext() && player == null;) {
			player = iterator.next().findByUsername(username);
		}
		return null;
	}

	private final class ThePlayerListener implements PlayerListener {
		private ThePlayerListener() {
		}

		@Override
		public void playerUpdated(Player oldInfo, Player newInfo) {
			for (PlayerListener listener : listeners) {
				listener.playerUpdated(oldInfo, newInfo);
			}
		}
	}

	public void setPlayerManagers(Collection<PlayerManager> playerManagers) {
		this.playerManagers.clear();
		for (PlayerManager playerManager : this.playerManagers) {
			playerManager.removePlayerListener(playerListener);
		}

		if (playerManagers != null) {
			this.playerManagers.addAll(playerManagers);

			for (PlayerManager playerManager : playerManagers) {
				playerManager.addPlayerListener(playerListener);
			}
		}
	}
}
