package wisematches.server.games.scribble.robot;

import wisematches.server.games.scribble.Tile;
import wisematches.server.games.scribble.Position;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
class WorkTile {
    private final Tile tile;
    private final Position position;

    WorkTile(Tile tile, Position position) {
        this.tile = tile;
        this.position = position;
    }

    public Tile getTile() {
        return tile;
    }

    public Position getPosition() {
        return position;
    }
}
