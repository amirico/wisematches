/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.client.gwt.app.client.content.sponsors;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public enum SponsorsBlockType {
    // Средний прямоугольник (300 x 250)
    DASHBOARD(300, 250, "sponsors-dashboard"),

    // Широкий небоскреб (160 x 600)
    GAMEBOARD(160, 600, "sponsors-gameboard"),

    // Небоскреб (120x600)
    PLAYBOARD(120, 600, "sponsors-playboard"),

    // (0, 0, )
    SEARCH(0, 0, "sponsors-search");

    private final int width;
    private final int height;
    private final String elementName;

    private Widget widget = null;

    SponsorsBlockType(int width, int height, String elementName) {
        this.width = width;
        this.height = height;
        this.elementName = elementName;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Widget getWidget() {
        if (widget == null) {
            widget = RootPanel.get(elementName);
        }
        return widget;
    }
}
