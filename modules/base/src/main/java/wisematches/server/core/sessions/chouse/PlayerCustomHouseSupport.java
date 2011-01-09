package wisematches.server.core.sessions.chouse;

import wisematches.kernel.player.Player;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class PlayerCustomHouseSupport implements PlayerCustomHouse {
    private final AbstractPlayerCustomHouse pch = new AbstractPlayerCustomHouse();

    public PlayerCustomHouseSupport() {
    }

    public void addPlayerCustomHouseListener(PlayerCustomHouseListener l) {
        pch.addPlayerCustomHouseListener(l);
    }

    public void removePlayerCustomHouseListener(PlayerCustomHouseListener l) {
        pch.removePlayerCustomHouseListener(l);
    }

    public void firePlayerMoveIn(Player player, String sessionKey) {
        pch.firePlayerMoveIn(player, sessionKey);
    }

    public void firePlayerMoveOut(Player player, String sessionKey) {
        pch.firePlayerMoveOut(player, sessionKey);
    }
}
