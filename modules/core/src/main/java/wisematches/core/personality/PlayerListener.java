package wisematches.core.personality;

import wisematches.core.Player;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerListener {
	/**
	 * Indicates that new account has been created.
	 *
	 * @param player the created player account.
	 */
	void playerRegistered(Player player);

	/**
	 * Indicates that account has been removed.
	 *
	 * @param player the removed account.
	 */
	void playerUnregistered(Player player);
}
