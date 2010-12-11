package wisematches.client.gwt.app.client.widget;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class TopMenuBar extends HorizontalPanel {
    private int enabledLinkIndex;
    private Hyperlink enabledLink;

    private final TheEnableClickListener clickHandler = new TheEnableClickListener();

    public TopMenuBar() {
        setStyleName("top-menubar");
    }

    public void addMainHyperlink(Hyperlink link) {
        addMainHyperlink(link, true);
    }

    public void addMainHyperlink(Hyperlink link, boolean addToGroup) {
        if (addToGroup) {
            link.addClickHandler(clickHandler);
        }

        if (getChildren().size() != 0) {
            addSeparator();
        }
        add(link);
    }

    private void addSeparator() {
        add(new HTML("&nbsp;&nbsp;"));
    }

    public void setEnabledLink(Hyperlink link) {
        if (enabledLink == link) {
            return;
        }

        if (enabledLink != null) {
            remove(enabledLinkIndex);
            insert(enabledLink, enabledLinkIndex);
        }

        if (link != null) {
            final int index = getWidgetIndex(link);
            final HTML html = new HTML(link.getText());
            html.setStyleName("top-menubar-selected");

            remove(link);
            insert(html, index);
            enabledLink = link;
            enabledLinkIndex = index;
        }
    }

    private final class TheEnableClickListener implements ClickHandler {
        public void onClick(ClickEvent clickEvent) {
            setEnabledLink((Hyperlink) clickEvent.getSource());
        }
    }
}
