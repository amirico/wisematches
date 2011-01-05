package wisematches.client.gwt.app.client.content.gameboard;

import com.smartgwt.client.widgets.Canvas;

/**
 * This interface is view for active games. UI component for this view can be received using
 * {@code getViewComponent()} method.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface GameboardTableView {
	void addGameboardSelectionListener(GameboardSelectionListener l);

	long[] getSelectedBoards();

	Canvas getViewComponent();
}
