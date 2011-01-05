package wisematches.client.gwt.app.client.content.playboard;

import wisematches.server.games.scribble.core.Tile;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface BoardTileListener {
    void tileSelected(Tile tile, boolean selected, boolean handTile);

    /**
     * Indicates that specified <code>tile</code> if moved.
     *
     * @param tile      the tile that was moved.
     * @param fromBoard <code>true</code> if tile was moved from board; <code>false</code> if tile was moved from hand.
     * @param toBoard   <code>true</code> if tile was moved to board; <code>false</code> if tile was moved to hand.
     */
    void tileMoved(Tile tile, boolean fromBoard, boolean toBoard);
}