package wisematches.core.personality.player.profile;

import wisematches.core.Player;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerProfileListener {
	void playerProfileChanged(Player player, PlayerProfile playerProfile);
}
