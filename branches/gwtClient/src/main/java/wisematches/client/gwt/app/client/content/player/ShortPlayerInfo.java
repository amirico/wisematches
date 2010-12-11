package wisematches.client.gwt.app.client.content.player;

import java.io.Serializable;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ShortPlayerInfo extends PlayerInfoBean implements Serializable {
    private int maxRatings;
    private int finishedGames;
    private int averageTimePerMove;

    public ShortPlayerInfo() {
    }

    public ShortPlayerInfo(long playerId, String playerName, MemberType memberType, int currentRating) {
        super(playerId, playerName, memberType, currentRating);
    }

    public int getFinishedGames() {
        return finishedGames;
    }

    public int getAverageTimePerMove() {
        return averageTimePerMove;
    }

    public void setFinishedGames(int finishedGames) {
        this.finishedGames = finishedGames;
    }

    public void setAverageTimePerMove(int averageTimePerMove) {
        this.averageTimePerMove = averageTimePerMove;
    }

    public int getMaxRatings() {
        return maxRatings;
    }

    public void setMaxRatings(int maxRatings) {
        this.maxRatings = maxRatings;
    }
}
