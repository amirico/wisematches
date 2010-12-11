package wisematches.client.gwt.app.client.content.playboard;

import wisematches.server.games.scribble.core.Word;

import java.io.Serializable;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public final class PlayerMoveBean implements Serializable {
    private long playerId;
    private int moveNumber;
    private int points;
    private Word word;
    private Type moveType;
    private long moveTime;

    public PlayerMoveBean() {
    }

    public PlayerMoveBean(long playerId, int moveNumber, long moveTime, Word word, int points) {
        this(playerId, moveNumber, moveTime, Type.MOVED, points);
        this.word = word;
    }

    public PlayerMoveBean(long playerId, int moveNumber, long moveTime, Type moveType, int points) {
        this.playerId = playerId;
        this.moveNumber = moveNumber;
        this.moveTime = moveTime;
        this.points = points;
        this.moveType = moveType;
    }

    public long getPlayerId() {
        return playerId;
    }

    public int getPoints() {
        return points;
    }

    public Word getWord() {
        return word;
    }

    public Type getMoveType() {
        return moveType;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public void setMoveType(Type moveType) {
        this.moveType = moveType;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public long getMoveTime() {
        return moveTime;
    }

    public void setMoveTime(long moveTime) {
        this.moveTime = moveTime;
    }

    public static enum Type {
        MOVED,
        PASSED,
        EXCHANGE
    }
}
