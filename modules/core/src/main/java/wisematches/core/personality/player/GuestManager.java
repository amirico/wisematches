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
public final class GuestManager extends AbstractPlayerManager implements PlayerManager {
	private final Map<Long, Guest> playerMap = new HashMap<>();

	public GuestManager() {
		final Collection<Guest> init = Guest.init(Language.values());
		for (Guest p : init) {
			playerMap.put(p.getId(), p);
		}
	}

	@Override
	public Player getPlayer(Long id) {
		return playerMap.get(id);
	}
}
