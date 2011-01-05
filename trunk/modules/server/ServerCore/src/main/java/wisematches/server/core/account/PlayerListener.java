package wisematches.server.core.account;

import wisematches.kernel.player.Player;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PlayerListener {
    /**
     * Indicates that information about player is updated.
     *
     * @param player the updated player.
     */
    void playerUpdated(Player player);
}
