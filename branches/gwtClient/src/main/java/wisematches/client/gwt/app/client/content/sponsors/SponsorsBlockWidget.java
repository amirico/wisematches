package wisematches.client.gwt.app.client.content.sponsors;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import static wisematches.client.gwt.app.client.content.i18n.AppRes.APP;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class SponsorsBlockWidget extends FlexTable {
    private final SponsorsBlockType blockType;

    public SponsorsBlockWidget(SponsorsBlockType blockType) {
        this.blockType = blockType;
        initPanel();
    }

    private void initPanel() {
        setStyleName("sponsors-block");
        setCellPadding(0);
        setCellSpacing(0);
        setBorderWidth(0);

        setHTML(0, 0, "<h2 class=\"header\">" + APP.lblSponsoreLinks() + "</h2>");

        final Widget panel = blockType.getWidget();
        if (panel != null) {
            panel.removeFromParent();
            panel.setVisible(true);

            setWidget(1, 0, panel);
        }
    }
}
