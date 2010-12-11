package wisematches.client.gwt.app.client.content.playboard.infos;

import com.google.gwt.user.client.ui.FlexTable;
import wisematches.client.gwt.app.client.content.playboard.board.TileWidget;
import wisematches.server.games.scribble.core.Tile;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
class TilesSetWidget extends FlexTable {
    private int tilesCount = 0;

    public TilesSetWidget() {
        setBorderWidth(0);
        setCellPadding(0);
        setCellSpacing(0);
        setHeight("22px");
        setStyleName("info-move-tiles");

        setHTML(0, 0, "No selection");
    }

    public void setTiles(Tile[] tiles) {
        if (getRowCount() == 1) {
            tilesCount = 0;
            removeRow(0);
            setHTML(0, 0, "No selection");
        }
        if (tiles != null && tiles.length != 0) {
            for (Tile tile : tiles) {
                setWidget(0, tilesCount++, new TileWidget(tile));
            }
        }
    }
}
