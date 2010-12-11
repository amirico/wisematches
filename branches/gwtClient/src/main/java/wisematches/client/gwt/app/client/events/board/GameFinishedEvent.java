package wisematches.client.gwt.app.client.events.board;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Event that indicates that game was fnished. Contains information about finished state, winner and so on.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GameFinishedEvent extends GameBoardEvent {
    private Type type;
    private boolean rated;
    private long playerId;
    private long finishedTime;
    private FinalPoint[] finalPoints;

    /**
     * This is GWT Serialization only constructor.
     */
    @Deprecated
    public GameFinishedEvent() {
    }

    public GameFinishedEvent(long boardId, Type type, boolean rated, long playerId, long finishedTime, FinalPoint[] finalPoints) {
        super(boardId);
        this.type = type;
        this.rated = rated;
        this.playerId = playerId;
        this.finishedTime = finishedTime;
        this.finalPoints = finalPoints;
    }

    /**
     * Returns how game was finished.
     *
     * @return how game was finished.
     */
    public Type getType() {
        return type;
    }

    /**
     * Indicates is game rated or not.
     *
     * @return {@code true} if game is rated; {@code false} - otherwise.
     */
    public boolean isRated() {
        return rated;
    }

    /**
     * Returns:
     * <ul>
     * <li>id of winner if game was finished correctly and game has a winner.
     * <li>id of player who resign game or who was timed out.
     * <li>zero if game was drawn.
     * </ul>
     *
     * @return the information about winner or interruptor.
     */
    public long getPlayerId() {
        return playerId;
    }

    /**
     * Returns time when game was finished.
     *
     * @return the time when game was finished.
     */
    public long getFinishedTime() {
        return finishedTime;
    }

    /**
     * Returns final points of this game.
     *
     * @return the array of final points.
     */
    public FinalPoint[] getFinalPoints() {
        return finalPoints;
    }

    /**
     * Enumeration that contains how game was finished.
     */
    public static enum Type {
        /**
         * Indicates that game was finished correctly
         */
        FINISHED,
        /**
         * Indicates that game was resigned by one of player.
         */
        RESIGNED,
        /**
         * Indicates that game was interrupted by timeout.
         */
        TIMEDOUT
    }

    /**
     * Final point contains player id and it's points.
     *
     * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
     */
    public static class FinalPoint implements Serializable, IsSerializable {
        private long playerId;
        private int points;

        /**
         * This is GWT only constructor.
         */
        @Deprecated
        public FinalPoint() {
        }

        public FinalPoint(long playerId, int points) {
            this.playerId = playerId;
            this.points = points;
        }

        public long getPlayerId() {
            return playerId;
        }

        public int getPoints() {
            return points;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof FinalPoint)) {
                return false;
            }

            FinalPoint that = (FinalPoint) o;
            return playerId == that.playerId && points == that.points;
        }

        @Override
        public int hashCode() {
            int result = (int) (playerId ^ (playerId >>> 32));
            result = 31 * result + points;
            return result;
        }

        @Override
        public String toString() {
            return "FinalPoint{" +
                    "playerId=" + playerId +
                    ", points=" + points +
                    '}';
        }
    }
}
