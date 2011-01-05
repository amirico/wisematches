package wisematches.client.gwt.app.client.content.playboard;

import wisematches.server.games.scribble.core.Tile;

import java.io.Serializable;

/**
 * Turn result contains information about maden turn: added tiles
 * to hand, points and next player's turn.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TurnResult implements Serializable {
    private Tile[] handTile;
    private int points;
    private long nextPlayerTurn;

    /**
     * This is GWT constructor.
     */
    TurnResult() {
    }

    public TurnResult(Tile[] handTile, int points, long nextPlayerTurn) {
        this.handTile = handTile;
        this.points = points;
        this.nextPlayerTurn = nextPlayerTurn;
    }

    /**
     * Returns tiles that should be added into hand after move.
     *
     * @return the tiles that should be added into hand after move.
     */
    public Tile[] getHandTiles() {
        return handTile;
    }

    /**
     * Returns points for move
     *
     * @return the move points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Returns player who has next turn.
     *
     * @return the player who has next turn or {@code 0} if game finished.
     */
    public long getNextPlayerTurn() {
        return nextPlayerTurn;
    }
}
