package wisematches.client.gwt.core.client.widget.links;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Image;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class WMImageExternalHyperlink extends WMExternalHyperlink {
    public WMImageExternalHyperlink(Image img) {
        this(img, "");
    }

    public WMImageExternalHyperlink(Image img, String url) {
        super();
        DOM.appendChild(DOM.getFirstChild(getElement()), img.getElement());
        setUrl(url);

        img.unsinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);
        sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);
    }
}