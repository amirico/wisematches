package wisematches.server.games.scribble.robot;

import wisematches.server.games.scribble.core.Position;
import wisematches.server.games.scribble.core.Tile;
import wisematches.server.games.scribble.core.TilesPlacement;

import java.util.List;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
class MockTilesPlacement implements TilesPlacement {
    private List<WorkTile> tileList;

    MockTilesPlacement(List<WorkTile> tileList) {
        this.tileList = tileList;
    }

    public boolean isBoardTile(int tileNumber) {
        for (WorkTile workTile : tileList) {
            if (workTile.getTile().getNumber() == tileNumber && workTile.getPosition() != null) {
                return true;
            }
        }
        return false;
    }

    public Tile getBoardTile(int row, int column) {
        if (row < 0 || column < 0 || row >= 15 || column >= 15) {
            throw new ArrayIndexOutOfBoundsException("Incorrect tiles placement: row - " + row + ", column - " + column);
        }

        for (WorkTile workTile : tileList) {
            final Position position = workTile.getPosition();
            if (position != null && position.row == row && position.column == column) {
                return workTile.getTile();
            }
        }
        return null;
    }
}
