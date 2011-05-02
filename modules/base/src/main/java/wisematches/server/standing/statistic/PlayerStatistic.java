package wisematches.server.standing.statistic;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerStatistic {
    long getPlayerId();

    Date getUpdateTime();

    int getActiveGames();

    int getWonGames();

    int getLostGames();

    int getDrawGames();

    int getTimeouts();

    int getFinishedGames();

    int getTurnsCount();

    /**
     * Returns average turn time in milliseconds.
     *
     * @return the average turn time in milliseconds.
     */
    int getAverageTurnTime();

    Date getLastMoveTime();

    Date getLastCleanupTime();

    PlayerStatisticWord getWordStatistic();

    PlayerStatisticRating getAllGamesStatisticRating();
}
