/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.core.tournament;

/**
 * Indicates state of a game in one group. Each game can be active (in progress) or finished.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public enum TournamentGroupState {
    /**
     * Indicates that a game was finished and player won.
     */
    WON(1, "1"),
    /**
     * Indicates that a game was finished and player lost.
     */
    LOST(0, "0"),
    /**
     * Indicates that a game was finished with draw
     */
    DRAW(0.5f, "0.5"),
    /**
     * Indicates that a game is active at this moment.
     */
    ACTIVE(-1, "?");

    private final float points;
    private final String notation;

    TournamentGroupState(float points, String notation) {
        this.points = points;
        this.notation = notation;
    }

    /**
     * Returns points in this round for this state.
     *
     * @return the points.
     */
    public float getPoints() {
        return points;
    }

    /**
     * Returns string notation (to be shown) for this state.
     *
     * @return the string notation.
     */
    public String getNotation() {
        return notation;
    }
}
