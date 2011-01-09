package wisematches.server.core.account;

import wisematches.kernel.player.Player;

/**
 * {@code AccountListener} notifies about new accounts or removed players.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface AccountListener {
    /**
     * Indicates that new account was created.
     *
     * @param player the craeted player account.
     */
    void accountCreated(Player player);

    /**
     * Indicates that account was removed.
     *
     * @param player the removed account.
     */
    void accountDeleted(Player player);
}
