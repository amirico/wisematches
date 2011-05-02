package wisematches.server.standing.statistic.impl;

import wisematches.server.standing.statistic.PlayerStatisticWord;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Embeddable
public class HibernatePlayerStatisticWord implements PlayerStatisticWord, Serializable {
    /**
     * Total number of words
     */
    private int wordsCount;
    /**
     * Average word's length
     */
    private int avgWordLength;
    /**
     * Max word's length
     */
    private int maxWordLength;
    /**
     * Average word's points
     */
    private int avgWordPoints;
    /**
     * Max word's points
     */
    private int maxWordPoints;

    HibernatePlayerStatisticWord() {
    }

    @Override
    public int getWordsCount() {
        return wordsCount;
    }

    @Override
    public int getAvgWordLength() {
        return avgWordLength;
    }

    @Override
    public int getMaxWordLength() {
        return maxWordLength;
    }

    @Override
    public int getAvgWordPoints() {
        return avgWordPoints;
    }

    @Override
    public int getMaxWordPoints() {
        return maxWordPoints;
    }

    public void setWordsCount(int wordsCount) {
        this.wordsCount = wordsCount;
    }

    public void setAvgWordLength(int avgWordLength) {
        this.avgWordLength = avgWordLength;
    }

    public void setMaxWordLength(int maxWordLength) {
        this.maxWordLength = maxWordLength;
    }

    public void setAvgWordPoints(int avgWordPoints) {
        this.avgWordPoints = avgWordPoints;
    }

    public void setMaxWordPoints(int maxWordPoints) {
        this.maxWordPoints = maxWordPoints;
    }
}
