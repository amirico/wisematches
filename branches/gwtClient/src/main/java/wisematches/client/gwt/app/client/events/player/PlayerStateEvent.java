package wisematches.client.gwt.app.client.events.player;

import wisematches.client.gwt.core.client.events.AbstractEvent;

/**
 * This is base event that is sent to notify about changes in player profile, rating and so on.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerStateEvent extends AbstractEvent {
    private long playerId;

    /**
     * This is GWT Serialization only constructor.
     */
    @Deprecated
    public PlayerStateEvent() {
    }

    /**
     * Creates new event with specified player id.
     *
     * @param playerId the id of player. Can't be zero.
     * @throws IllegalArgumentException if {@code playerId} is zero.
     */
    public PlayerStateEvent(long playerId) {
        this.playerId = playerId;
    }

    /**
     * Returns the id of player whos information was changed.
     *
     * @return the id of player whos information was changed.
     */
    public long getPlayerId() {
        return playerId;
    }

    @Override
    public boolean isEventForPlayer(long playerId) {
        return this.playerId == playerId;
    }
}
