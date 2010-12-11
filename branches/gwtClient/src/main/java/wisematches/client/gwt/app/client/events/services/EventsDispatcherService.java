package wisematches.client.gwt.app.client.events.services;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import wisematches.client.gwt.core.client.events.Event;

import java.util.Collection;

public interface EventsDispatcherService extends RemoteService {
    /**
     * Returns all events.
     *
     * @return all received events from last receiving time.
     */
    Collection<Event> getEvents();

    /**
     * Utility/Convenience class.
     * Use EventsLoaderService.App.getInstance() to access static instance of EventsLoaderServiceAsync
     */
    public static class App {
        private static final EventsDispatcherServiceAsync ourInstance;

        static {
            ourInstance = (EventsDispatcherServiceAsync) GWT.create(EventsDispatcherService.class);
            ((ServiceDefTarget) ourInstance).setServiceEntryPoint("/rpc/EventsDispatcherService");
        }

        public static EventsDispatcherServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
