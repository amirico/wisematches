package wisematches.client.gwt.core.client.events;

/**
 * This is abstract implementation of {@code Event} class that is applied for all players
 * and neutral to all other events.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public abstract class AbstractEvent implements Event {
    protected AbstractEvent() {
    }

    public boolean isEventForPlayer(long playerId) {
        return true;
    }
}
