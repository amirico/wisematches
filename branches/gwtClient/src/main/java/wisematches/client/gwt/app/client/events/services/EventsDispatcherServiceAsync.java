package wisematches.client.gwt.app.client.events.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import wisematches.client.gwt.core.client.events.Event;

import java.util.Collection;

public interface EventsDispatcherServiceAsync {
    /**
     * Returns all events.
     *
     * @return all received events from last receiving time.
     */
    void getEvents(AsyncCallback<Collection<Event>> async);
}
