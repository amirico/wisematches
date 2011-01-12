package wisematches.server.games.scribble.board;

import wisematches.server.games.scribble.Tile;

/**
 * <code>TilesPlacement</code> is interface that allows checks where a tile is placed: on board, in
 * hands or in bank.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface TilesPlacement {
    /**
     * Checks that tile with specified number is placed on the board.
     *
     * @param tileNumber the tile number to be checked.
     * @return <code>true</code> if tile is placed on board; <code>false</code> - otherwise.
     */
    boolean isBoardTile(int tileNumber);

    /**
     * Returns tile board at specified position. If there is no tile at specified position {@code null} will
     * be returned.
     *
     * @param row    the tile row.
     * @param column the tile column
     * @return the board tile at specified position or {@code null} if no tile.
     * @throws IndexOutOfBoundsException if {@code row < 0 || column < 0 || row > 15 || column > 15}
     */
    Tile getBoardTile(int row, int column);
}
