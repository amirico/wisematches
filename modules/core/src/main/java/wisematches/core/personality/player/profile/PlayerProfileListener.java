package wisematches.core.personality.player.profile;

import wisematches.core.personality.player.account.Account;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerProfileListener {
	void playerProfileChanged(Account account, PlayerProfile playerProfile);
}
