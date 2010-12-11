package wisematches.client.gwt.core.client.widget;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class WMErrorMarker extends Widget {
    private Element tipElement;
    private Element invalidElement;

    public WMErrorMarker() {
        initWidget();
    }

    private void initWidget() {
        final Element div = DOM.createDiv();
        div.setClassName("x-form-invalid-tip");
        setElement(div);

        tipElement = DOM.createSpan();
        tipElement.setClassName("x-tip-body");
        tipElement.setAttribute("ext:qclass", "x-form-invalid-tip");
        div.appendChild(tipElement);

        setVisible(false);
    }

    public void showInvalidMessage(String message) {
        tipElement.setAttribute("ext:qtip", message);
        if (invalidElement != null) {
            invalidElement.setClassName("x-form-invalid");
        }
        setVisible(true);
    }

    public void hideInvalidMessage() {
        invalidElement.setClassName("");
        setVisible(false);
    }

    public Element getInvalidElement() {
        return invalidElement;
    }

    public void setInvalidElement(Element invalidElement) {
        this.invalidElement = invalidElement;
        invalidElement.setAttribute("style", "background-color: transparent; height: 4px;");
    }
}
