package wisematches.client.gwt.core.client.widget.links;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.impl.HyperlinkImpl;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class WMHyperlink extends Hyperlink {
	private static HyperlinkImpl impl = GWT.create(HyperlinkImpl.class);

	public WMHyperlink() {
	}

	public WMHyperlink(String text) {
		this(text, false);
	}

	public WMHyperlink(String text, boolean asHTML) {
		this(text, asHTML, null);
	}

	public WMHyperlink(String text, String targetHistoryToken) {
		super(text, targetHistoryToken);
	}

	public WMHyperlink(String text, boolean asHTML, String targetHistoryToken) {
		super(text, asHTML, targetHistoryToken);
	}


	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);
		if (DOM.eventGetType(event) == Event.ONCLICK && impl.handleAsClick(event)) {
			String targetHistoryToken = getTargetHistoryToken();
			if (targetHistoryToken != null) {
				History.newItem(getTargetHistoryToken());
			}
			DOM.eventPreventDefault(event);
		}
	}

	@Override
	public void setTargetHistoryToken(String targetHistoryToken) {
		super.setTargetHistoryToken(targetHistoryToken);

		if (targetHistoryToken == null) {
			final Element anchorElem = DOM.getFirstChild(getElement());
			DOM.setElementProperty(anchorElem, "href", "javascript: void(0);");
		}
	}
}