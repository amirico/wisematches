package wisematches.server.standing.statistic;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerStatisticWord {
    int getWordsCount();

    int getAvgWordLength();

    int getMaxWordLength();

    int getAvgWordPoints();

    int getMaxWordPoints();
}
