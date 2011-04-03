package wisematches.server.gameplaying.board;

import javax.persistence.*;

/**
 * <code>GamePlayerHand</code> is a hand of the player. It contains information about player on the board, like
 * it's point, it's items in hand and so on.
 *
 * @param <B> reference to GameBoard that can use this hand. This reference required for Hibernate mapping because
 *            each hand must have link to a board that contains it.
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Embeddable
@MappedSuperclass
public class GamePlayerHand<B extends GameBoard<?, ? extends GamePlayerHand<B>>> {
    @Column(name = "playerId", updatable = false)
    private long playerId;

    @Column(name = "playerIndex", updatable = false)
    private int playerIndex;

    @Column(name = "points")
    private int points;

    /**
     * This is Hibernate constructor. In subclasses in must be declared as package visibile.
     */
    protected GamePlayerHand() {
    }

    /**
     * Creates new player hand with specified player id and statr points.
     *
     * @param playerId the player id.
     */
    protected GamePlayerHand(long playerId) {
        this.playerId = playerId;
    }

    /**
     * Returns player id who has this hand.
     *
     * @return the player id
     */
    public long getPlayerId() {
        return playerId;
    }

    /**
     * Returns points of the player on the board.
     *
     * @return the player's points
     */
    public int getPoints() {
        return points;
    }

    /**
     * Returns permanent unique index of this player on the board.
     * <p/>
     * Each player has it own index that doesn't changed during restarting.
     *
     * @return the permanent unique player index.
     */
    public int getPlayerIndex() {
        return playerIndex;
    }

    /**
     * Increases points of the player and returns new value.
     *
     * @param delta the delta
     * @return increased points.
     */
    int increasePoints(int delta) {
        points = points + delta;
        return points;
    }

    @Override
    public String toString() {
        return "playerId=" + getPlayerId() +
                ", playerIndex=" + playerIndex +
                ", points=" + points;
    }
}
