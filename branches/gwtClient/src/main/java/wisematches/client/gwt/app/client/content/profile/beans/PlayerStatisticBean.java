package wisematches.client.gwt.app.client.content.profile.beans;

import java.io.Serializable;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerStatisticBean implements Serializable {
    private int currentRating;
    private long currentPosition;
    private int activeGames;
    private int finishedGames;
    private int wonGames;
    private int lostGames;
    private int drawGames;

    public PlayerStatisticBean() {
    }

    public int getCurrentRating() {
        return currentRating;
    }

    public void setCurrentRating(int currentRating) {
        this.currentRating = currentRating;
    }

    public long getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(long currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getActiveGames() {
        return activeGames;
    }

    public void setActiveGames(int activeGames) {
        this.activeGames = activeGames;
    }

    public int getFinishedGames() {
        return finishedGames;
    }

    public void setFinishedGames(int finishedGames) {
        this.finishedGames = finishedGames;
    }

    public int getWonGames() {
        return wonGames;
    }

    public void setWonGames(int wonGames) {
        this.wonGames = wonGames;
    }

    public int getLostGames() {
        return lostGames;
    }

    public void setLostGames(int lostGames) {
        this.lostGames = lostGames;
    }

    public int getDrawGames() {
        return drawGames;
    }

    public void setDrawGames(int drawGames) {
        this.drawGames = drawGames;
    }

    @Override
    public String toString() {
        return "PlayerStatisticBean{" +
                "currentRating=" + currentRating +
                ", currentPosition=" + currentPosition +
                ", activeGames=" + activeGames +
                ", finishedGames=" + finishedGames +
                ", wonGames=" + wonGames +
                ", lostGames=" + lostGames +
                ", drawGames=" + drawGames +
                '}';
    }
}
