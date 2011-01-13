package wisematches.server.player;

/**
 * The {@code AccountListener} notifies about new accounts or removed players.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AccountListener {
	/**
	 * Indicates that new account has been created.
	 *
	 * @param player the created player account.
	 */
	void accountCreated(Player player);

	/**
	 * Indicates that account has been removed.
	 *
	 * @param player the removed account.
	 */
	void accountRemove(Player player);
}
