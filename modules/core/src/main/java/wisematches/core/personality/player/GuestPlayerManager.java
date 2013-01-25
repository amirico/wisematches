package wisematches.core.personality.player;

import wisematches.core.Language;
import wisematches.core.Player;
import wisematches.core.personality.PlayerManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class GuestPlayerManager extends AbstractPlayerManager implements PlayerManager {
	private final Map<Long, GuestPlayer> playerMap = new HashMap<>();

	public GuestPlayerManager() {
		final Collection<GuestPlayer> init = GuestPlayer.init(Language.values());
		for (GuestPlayer p : init) {
			playerMap.put(p.getId(), p);
		}
	}

	@Override
	public Player getPlayer(Long id) {
		return playerMap.get(id);
	}
}
