package wisematches.client.gwt.core.client.widget;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ClickEvent;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class MessagesBox {
    private static PopupPanel messagePanel = new PopupPanel(true, true);

    public static void showMessage(String title, String message) {
        messagePanel.setWidth("400px");

        final HTML html = new HTML(getMessageHtml(title, message));
        html.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                messagePanel.clear();
            }
        });

        messagePanel.setWidget(html);
        messagePanel.center();
        messagePanel.show();
    }

    private static native String getMessageHtml(String title, String message) /*-{
                                                          return ['<div class="msg">',
                                                                  '<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
                                                                  '<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc"><h3>', title, '</h3>', message, '</div></div></div>',
                                                                  '<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
                                                                  '</div>'].join('');
                                                      }-*/;
}
