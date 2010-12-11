package wisematches.client.gwt.app.client.events;

import wisematches.client.gwt.core.client.events.Event;

import java.util.Collection;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface EventsListener<T extends Event> {
    void eventsReceived(Collection<T> evens);
}
