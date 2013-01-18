package wisematches.core.personality.generic;

import wisematches.core.Personality;
import wisematches.core.personality.Player;
import wisematches.core.personality.PlayerManager;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GenericPlayerManager implements PlayerManager {
	private final Collection<PlayerManager> playerManagers = new ArrayList<>();

	public GenericPlayerManager() {
	}

	@Override
	public Player getPlayer(long pid) {
		for (PlayerManager playerManager : playerManagers) {
			final Player player = playerManager.getPlayer(pid);
			if (player != null) {
				return player;
			}
		}
		return null;
	}

	@Override
	public Player getPlayer(Personality personality) {
		for (PlayerManager playerManager : playerManagers) {
			final Player player = playerManager.getPlayer(personality);
			if (player != null) {
				return player;
			}
		}
		return null;
	}


	public void setPlayerManagers(Collection<PlayerManager> playerManagers) {
		this.playerManagers.clear();

		if (playerManagers != null) {
			this.playerManagers.addAll(playerManagers);
		}
	}
}
