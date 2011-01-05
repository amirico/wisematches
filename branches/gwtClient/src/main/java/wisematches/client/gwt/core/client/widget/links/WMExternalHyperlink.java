package wisematches.client.gwt.core.client.widget.links;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class WMExternalHyperlink extends WMHyperlink {
    private final Element anchorElem;
    private String url;

    public WMExternalHyperlink() {
        anchorElem = (Element) getElement().getChildNodes().getItem(0);
    }

    public WMExternalHyperlink(String url, String text, boolean asHTML) {
        this();
        if (asHTML) {
            setHTML(text);
        } else {
            setText(text);
        }
        setUrl(url);
    }

    public WMExternalHyperlink(String url, String text) {
        this(url, text, false);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;

        String tmp = url;
        if (getTargetHistoryToken() != null) {
            tmp = tmp + "#" + getTargetHistoryToken();
        }
        DOM.setElementProperty(anchorElem, "href", tmp);
    }

    @Override
    public void setTargetHistoryToken(String targetHistoryToken) {
        super.setTargetHistoryToken(targetHistoryToken);
        setUrl(url);
    }
}
