package wisematches.client.gwt.core.client.analiytics;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class PageTracker {
	private PageTracker() {
	}

	public static void attachTrackerToListener(final String applicationPrefix) {
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> stringValueChangeEvent) {
				trackPageVisit(applicationPrefix + "/" + stringValueChangeEvent.getValue());
			}
		});
	}

	public static native void trackPageVisit(String pageName) /*-{
        if ($wnd.trackSystemEnabled) {
            $wnd.trackPageVisit(pageName);
        }
    }-*/;
}
