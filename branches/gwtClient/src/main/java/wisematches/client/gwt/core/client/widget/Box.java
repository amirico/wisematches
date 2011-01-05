package wisematches.client.gwt.core.client.widget;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class Box extends FlowPanel {
	private Box(int size, boolean vertical) {
		if (vertical) {
			setHeight(String.valueOf(size) + "px");
		} else {
			setWidth(String.valueOf(size) + "px");
		}
	}

	public static Box createVerticalBox(int size) {
		return new Box(size, true);
	}

	public static Box createHorizontalBox(int size) {
		return new Box(size, false);
	}
}
