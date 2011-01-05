package wisematches.client.gwt.core.client.widget.links;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.impl.HyperlinkImpl;

/**
 * This widget looks like hyperlink but is not. It's {@code span} element that is clickable.
 * <p/>
 * This widget has {@code like-hyperlink} style.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class WMFlatHyperlink extends Widget implements HasHTML, HasClickHandlers {
    private static HyperlinkImpl impl = GWT.create(HyperlinkImpl.class);

    public WMFlatHyperlink() {
        this("");
    }

    public WMFlatHyperlink(String text) {
        this(text, false);
    }

    public WMFlatHyperlink(String text, String styleName) {
        this(text, styleName, false);
    }

    public WMFlatHyperlink(String text, boolean asHTML) {
        this(text, "blue-flat-hyperlink", asHTML);
    }

    public WMFlatHyperlink(String text, String styleName, boolean asHTML) {
        setElement(DOM.createSpan());
        sinkEvents(Event.ONCLICK);

        addStyleName("flat-hyperlink");
        addStyleName(styleName);

        if (asHTML) {
            setHTML(text);
        } else {
            setText(text);
        }
    }

    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return addHandler(handler, ClickEvent.getType());
    }

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        if (DOM.eventGetType(event) == Event.ONCLICK && impl.handleAsClick(event)) {
            DOM.eventPreventDefault(event);
        }
    }

    public String getHTML() {
        return DOM.getInnerHTML(getElement());
    }

    public void setHTML(String html) {
        DOM.setInnerHTML(getElement(), html);
    }

    public String getText() {
        return DOM.getInnerText(getElement());
    }

    public void setText(String text) {
        DOM.setInnerText(getElement(), text);
    }
}