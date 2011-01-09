package wisematches.server.core.guest;

import wisematches.kernel.player.Player;
import wisematches.server.core.account.PlayerManager;
import wisematches.server.core.account.PlayerListener;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GuestPlayerManager implements PlayerManager {
    public void addPlayerListener(PlayerListener l) {

    }

    public void removePlayerListener(PlayerListener l) {
        
    }

    public Player getPlayer(long playerId) {
        if (playerId == GuestPlayer.PLAYER_ID) {
            return GuestPlayer.GUEST_PLAYER;
        }
        return null;
    }

    public void updatePlayer(Player player) {
    }

    public boolean isGuestPlayer(long playerId) {
        return playerId == GuestPlayer.PLAYER_ID;
    }

    public boolean isGuestPlayer(Player player) {
        return player == GuestPlayer.GUEST_PLAYER;
    }
}
