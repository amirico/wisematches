package wisematches.client.gwt.core.client.widget.links;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Image;

/**
 * This component taken from here:
 * <a href="http://gwt.components.googlepages.com/imagehyperlink">http://gwt.components.googlepages.com/imagehyperlink</a>
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class WMImageHyperlink extends WMHyperlink {
    public WMImageHyperlink(Image img) {
        this(img, null);
    }

    public WMImageHyperlink(Image img, String targetHistoryToken) {
        DOM.appendChild(DOM.getFirstChild(getElement()), img.getElement());
        if (targetHistoryToken != null) {
            setTargetHistoryToken(targetHistoryToken);
        }

        img.unsinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);
        sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);
    }
}