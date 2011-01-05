package wisematches.client.gwt.app.client.events;

import wisematches.client.gwt.app.client.ApplicationFrame;
import wisematches.client.gwt.app.client.ApplicationTool;
import wisematches.client.gwt.app.client.ToolReadyCallback;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class EventsDispatcherTool implements ApplicationTool {
	private final EventsDispatcher eventsDispatcher = new EventsDispatcher();

	public void registerJSCallbacks() {
	}

	public void initializeTool(ApplicationFrame applicationFrame, ToolReadyCallback callback) {
//        eventsDispatcher.setDefaultRefreshTime(eventsDispatcher.getDefaultRefreshTime());

		callback.toolReady(this);
	}

	public EventsDispatcher getEventsDispatcher() {
		return eventsDispatcher;
	}
}
