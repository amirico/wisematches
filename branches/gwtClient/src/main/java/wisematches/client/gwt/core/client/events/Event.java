package wisematches.client.gwt.core.client.events;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Event is base item that is transfered between client and server.
 * <p/>
 * Each event must be {@code java.io.Serializable} and {@code IsSerializable}.
 * <p/>
 * Each event can have associated players that can receive this event.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 * @see java.io.Serializable
 * @see com.google.gwt.user.client.rpc.IsSerializable
 */
public interface Event extends Serializable, IsSerializable {
    /**
     * Checks that this event is destined for player with specified id.
     *
     * @param playerId the id of player to be checked.
     * @return {@code true} is this event is destined to player; {@code false} - otherwise.
     */
    boolean isEventForPlayer(long playerId);
}