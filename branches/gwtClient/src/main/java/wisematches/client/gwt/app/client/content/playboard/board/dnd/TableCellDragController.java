package wisematches.client.gwt.app.client.content.playboard.board.dnd;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import wisematches.client.gwt.app.client.content.playboard.board.TileWidget;
import wisematches.server.games.scribble.core.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class TableCellDragController extends PickupDragController {

    private final Map<Widget, TileWidgetInfo> savedLocation = new HashMap<Widget, TileWidgetInfo>();

    public TableCellDragController(AbsolutePanel absolutePanel, boolean b) {
        super(absolutePanel, b);
    }

    @Override
    public void dragEnd() {
        if (context.finalDropController != null) {
            final List<Widget> widgetList = context.selectedWidgets;
            for (Widget widget : widgetList) {
                savedLocation.remove(widget);
            }
        }
        super.dragEnd();
    }

    @Override
    protected void restoreSelectedWidgetsLocation() {
        final List<Widget> widgetList = context.selectedWidgets;
        for (Widget widget : widgetList) {
            final TileWidget tw = (TileWidget) widget;
            final TileWidgetInfo saved = savedLocation.remove(tw);
            if (saved != null) {
                tw.setPosition(saved.position);
                saved.table.setWidget(saved.position.row, saved.position.column, tw);
            }
        }
    }

    @Override
    protected void restoreSelectedWidgetsStyle() {
    }

    @Override
    protected void saveSelectedWidgetsLocationAndStyle() {
        final List<Widget> widgetList = context.selectedWidgets;
        for (Widget widget : widgetList) {
            final TileWidget tw = (TileWidget) widget;
            final FlexTable parent = (FlexTable) widget.getParent();
            final TileWidgetInfo info = new TileWidgetInfo(parent, tw.getPosition());
            savedLocation.put(tw, info);
        }
    }

    private static class TileWidgetInfo {
        private FlexTable table;
        private Position position;

        private TileWidgetInfo(FlexTable table, Position position) {
            this.table = table;
            this.position = position;
        }
    }
}
