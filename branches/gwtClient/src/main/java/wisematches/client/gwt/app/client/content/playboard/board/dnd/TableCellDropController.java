package wisematches.client.gwt.app.client.content.playboard.board.dnd;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbstractDropController;
import com.google.gwt.user.client.ui.FlexTable;
import wisematches.client.gwt.app.client.content.playboard.board.TileWidget;
import wisematches.server.games.scribble.core.Position;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class TableCellDropController extends AbstractDropController {
    private FlexTable table;

    public TableCellDropController(FlexTable table) {
        super(table);
        this.table = table;
    }

    public void onPreviewDrop(DragContext dragContext) throws VetoDragException {
        final int r = getRowPosition(dragContext);
        final int c = getColPosition(dragContext);
        if (!isValid(r, c)) {
            throw new VetoDragException();
        }
    }

    public void onDrop(DragContext dragContext) {
        final int r = getRowPosition(dragContext);
        final int c = getColPosition(dragContext);

        if (isValid(r, c)) {
            table.setWidget(r, c, dragContext.draggable);
        }
    }

    public int getColPosition(DragContext dragContext) {
        final int absoluteLeft = table.getAbsoluteLeft();
        return (dragContext.mouseX - absoluteLeft - 6) / TileWidget.TILE_SIZE;
    }

    public int getRowPosition(DragContext dragContext) {
        final int absoluteTop = table.getAbsoluteTop();
        return (dragContext.mouseY - absoluteTop - 6) / TileWidget.TILE_SIZE;
    }

    public Position getPosition(DragContext dragContext) {
        return new Position(getRowPosition(dragContext), getColPosition(dragContext));
    }

    private boolean isValid(int r, int c) {
        return isBoundValid(r, c) && (table.getWidget(r, c) == null);
    }

    private boolean isBoundValid(int r, int c) {
        return r >= 0 && r < table.getRowCount() && c >= 0 && c < table.getCellCount(0);
    }
}
