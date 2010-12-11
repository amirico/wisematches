package wisematches.client.gwt.core.client.widget;

import com.google.gwt.user.client.ui.HTML;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMErrorField extends HTML {
    public WMErrorField() {
        setVisible(false);
        setStyleName("error-field");
    }

    public void showError(String message) {
        setHTML(message);

        if (!isVisible()) {
            setVisible(true);
        }
    }

    public void hideError() {
        setVisible(false);
    }
}