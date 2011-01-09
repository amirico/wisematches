package wisematches.server.core.account;

import wisematches.kernel.player.Player;

/**
 * Manager for get and authenticate player. Allows get player by it's id and authenticate it's
 * by <code>Player</code> object or it's <code>username</code> and <code>password</code>.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerManager {
    void addPlayerListener(PlayerListener l);

    void removePlayerListener(PlayerListener l);

    /**
     * Returns player by it's id.
     *
     * @param playerId the player's id.
     * @return the player by it's id or <code>null</code> if player is unknown.
     */
    Player getPlayer(long playerId);

    /**
     * Updates information about player.
     *
     * @param player the player who should be updated.
     */
    void updatePlayer(Player player);
}