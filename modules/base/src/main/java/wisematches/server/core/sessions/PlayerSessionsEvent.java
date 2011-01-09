package wisematches.server.core.sessions;

import wisematches.kernel.player.Player;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class PlayerSessionsEvent {
    private final Player player;
    private final String sessionKey;

    public PlayerSessionsEvent(Player player, String sessionKey) {
        this.player = player;
        this.sessionKey = sessionKey;
    }

    public Player getPlayer() {
        return player;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    @Override
    public String toString() {
        return "PlayerSessionsEvent{" +
                "player=" + player +
                ", sessionKey='" + sessionKey + '\'' +
                '}';
    }
}
