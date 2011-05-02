package wisematches.server.standing.statistic.impl;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import wisematches.server.personality.Personality;
import wisematches.server.standing.statistic.PlayerStatistic;
import wisematches.server.standing.statistic.PlayerStatisticWord;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Entity
@Table(name = "rating_statistic")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class HibernatePlayerStatistic implements Serializable, PlayerStatistic {
    /**
     * Id of player who this statistic belongs to
     */
    @Id
    @Column(name = "playerId")
    private long playerId;
    /**
     * The time when this statistic was updated
     */
    @Column(name = "updateTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    /**
     * Time when last cleanup was performed.        12
     */
    @Column(name = "lastCleanupTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastCleanupTime;

    @Column(name = "activeGames")
    private int activeGames;

    /**
     * Number of won games
     */
    @Column(name = "wonGames")
    private int wonGames;
    /**
     * Number of lost games
     */
    @Column(name = "lostGames")
    private int lostGames;
    /**
     * Number of games that was finished with draw
     */
    @Column(name = "drawGames")
    private int drawGames;
    /**
     * Number of games that was lost by timeout
     */
    @Column(name = "timeouts")
    private int timeouts;

    /**
     * Number of all turns in all games
     */
    @Column(name = "turnsCount")
    private int turnsCount;
    /**
     * Average time between two moves in one game in milliseconds.
     */
    @Column(name = "averageTurnTime")
    private int averageTurnTime;

    /**
     * Time when last move was made
     */
    @Column(name = "lastMoveTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastMoveTime;

/*
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "averageRating", column = @Column(name = "thirtyAverageRating")),
			@AttributeOverride(name = "highestRating", column = @Column(name = "thirtyHighestRating")),
			@AttributeOverride(name = "lowestRating", column = @Column(name = "thirtyLowestRating")),
			@AttributeOverride(name = "averageOpponentRating", column = @Column(name = "thirtyAverageOpponentRating")),
			@AttributeOverride(name = "highestWonOpponentRating", column = @Column(name = "thirtyHighestWonOpponent")),
			@AttributeOverride(name = "highestWonOpponentId", column = @Column(name = "thirtyHighestWonOpponentId")),
			@AttributeOverride(name = "lowestLostOpponentRating", column = @Column(name = "thirtyLowestLostOpponent")),
			@AttributeOverride(name = "lowestLostOpponentId", column = @Column(name = "thirtyLowestLostOpponentId")),
			@AttributeOverride(name = "averageMovesPerGame", column = @Column(name = "thirtyAverageMovesPerGame"))
	})
	private HibernatePlayerStatisticRating thirtyDaysRatingInfo = new HibernatePlayerStatisticRating();

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "averageRating", column = @Column(name = "ninetyAverageRating")),
			@AttributeOverride(name = "highestRating", column = @Column(name = "ninetyHighestRating")),
			@AttributeOverride(name = "lowestRating", column = @Column(name = "ninetyLowestRating")),
			@AttributeOverride(name = "averageOpponentRating", column = @Column(name = "ninetyAverageOpponentRating")),
			@AttributeOverride(name = "highestWonOpponentRating", column = @Column(name = "ninetyHighestWonOpponent")),
			@AttributeOverride(name = "highestWonOpponentId", column = @Column(name = "ninetyHighestWonOpponentId")),
			@AttributeOverride(name = "lowestLostOpponentRating", column = @Column(name = "ninetyLowestLostOpponent")),
			@AttributeOverride(name = "lowestLostOpponentId", column = @Column(name = "ninetyLowestLostOpponentId")),
			@AttributeOverride(name = "averageMovesPerGame", column = @Column(name = "ninetyAverageMovesPerGame"))
	})
	private HibernatePlayerStatisticRating ninetyDaysRatingInfo = new HibernatePlayerStatisticRating();

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "averageRating", column = @Column(name = "yearAverageRating")),
			@AttributeOverride(name = "highestRating", column = @Column(name = "yearHighestRating")),
			@AttributeOverride(name = "lowestRating", column = @Column(name = "yearLowestRating")),
			@AttributeOverride(name = "averageOpponentRating", column = @Column(name = "yearAverageOpponentRating")),
			@AttributeOverride(name = "highestWonOpponentRating", column = @Column(name = "yearHighestWonOpponent")),
			@AttributeOverride(name = "highestWonOpponentId", column = @Column(name = "yearHighestWonOpponentId")),
			@AttributeOverride(name = "lowestLostOpponentRating", column = @Column(name = "yearLowestLostOpponent")),
			@AttributeOverride(name = "lowestLostOpponentId", column = @Column(name = "yearLowestLostOpponentId")),
			@AttributeOverride(name = "averageMovesPerGame", column = @Column(name = "yearAverageMovesPerGame"))
	})
	private HibernatePlayerStatisticRating yearRatingInfo = new HibernatePlayerStatisticRating();
*/

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "averageRating", column = @Column(name = "allAverageRating")),
            @AttributeOverride(name = "highestRating", column = @Column(name = "allHighestRating")),
            @AttributeOverride(name = "lowestRating", column = @Column(name = "allLowestRating")),
            @AttributeOverride(name = "averageOpponentRating", column = @Column(name = "allAverageOpponentRating")),
            @AttributeOverride(name = "highestWonOpponentRating", column = @Column(name = "allHighestWonOpponent")),
            @AttributeOverride(name = "highestWonOpponentId", column = @Column(name = "allHighestWonOpponentId")),
            @AttributeOverride(name = "lowestLostOpponentRating", column = @Column(name = "allLowestLostOpponent")),
            @AttributeOverride(name = "lowestLostOpponentId", column = @Column(name = "allLowestLostOpponentId")),
            @AttributeOverride(name = "averageMovesPerGame", column = @Column(name = "allAverageMovesPerGame"))
    })
    private HibernatePlayerStatisticRating allGamesStatisticRating = new HibernatePlayerStatisticRating();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "wordsCount", column = @Column(name = "wordsCount")),
            @AttributeOverride(name = "avgWordLength", column = @Column(name = "avgWordLength")),
            @AttributeOverride(name = "maxWordLength", column = @Column(name = "maxWordLength")),
            @AttributeOverride(name = "avgWordPoints", column = @Column(name = "avgWordPoints")),
            @AttributeOverride(name = "maxWordPoints", column = @Column(name = "maxWordPoints"))
    })
    private HibernatePlayerStatisticWord wordStatistic = new HibernatePlayerStatisticWord();

    /**
     * This is Hibernate constructor
     */
    HibernatePlayerStatistic() {
    }

    public HibernatePlayerStatistic(Personality personality) {
        this.playerId = personality.getId();
    }

    @Override
    public long getPlayerId() {
        return playerId;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int getActiveGames() {
        return activeGames;
    }

    public void incrementActiveGames() {
        this.activeGames++;
    }

    public void decrementActiveGames() {
        this.activeGames--;
    }

    @Override
    public int getWonGames() {
        return wonGames;
    }

    public void incrementWonGames() {
        this.wonGames++;
    }

    @Override
    public int getLostGames() {
        return lostGames;
    }

    public void incrementLostGames() {
        this.lostGames++;
    }

    @Override
    public int getDrawGames() {
        return drawGames;
    }

    public void incrementDrawGames() {
        this.drawGames++;
    }

    @Override
    public int getTimeouts() {
        return timeouts;
    }

    public void incrementTimeouts() {
        this.timeouts++;
    }

    @Override
    public int getFinishedGames() {
        return wonGames + lostGames + drawGames;
    }

    @Override
    public int getTurnsCount() {
        return turnsCount;
    }

    public void incrementTurnsCount() {
        this.turnsCount++;
    }

    @Override
    public int getAverageTurnTime() {
        return averageTurnTime;
    }

    public void setAverageTurnTime(int averageTurnTime) {
        this.averageTurnTime = averageTurnTime;
    }

    @Override
    public Date getLastMoveTime() {
        return lastMoveTime;
    }

    public void setLastMoveTime(Date lastMoveTime) {
        this.lastMoveTime = lastMoveTime;
    }

/*
	public HibernatePlayerStatisticRating getThirtyDaysRatingInfo() {
		return thirtyDaysRatingInfo;
	}

	public HibernatePlayerStatisticRating getNinetyDaysRatingInfo() {
		return ninetyDaysRatingInfo;
	}

	public HibernatePlayerStatisticRating getYearRatingInfo() {
		return yearRatingInfo;
	}
*/

    @Override
    public HibernatePlayerStatisticRating getAllGamesStatisticRating() {
        return allGamesStatisticRating;
    }

    @Override
    public HibernatePlayerStatisticWord getWordStatistic() {
        return wordStatistic;
    }

    public void setWordStatistic(HibernatePlayerStatisticWord wordStatistic) {
        this.wordStatistic = wordStatistic;
    }

    @Override
    public Date getLastCleanupTime() {
        return lastCleanupTime;
    }

    public void setLastCleanupTime(Date lastCleanupTime) {
        this.lastCleanupTime = lastCleanupTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PlayerStatistic");
        sb.append("{playerId=").append(playerId);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", lastCleanupTime=").append(lastCleanupTime);
        sb.append(", activeGames=").append(activeGames);
        sb.append(", wonGames=").append(wonGames);
        sb.append(", lostGames=").append(lostGames);
        sb.append(", drawGames=").append(drawGames);
        sb.append(", timeouts=").append(timeouts);
        sb.append(", turnsCount=").append(turnsCount);
        sb.append(", averageTurnTime=").append(averageTurnTime);
        sb.append(", lastMoveTime=").append(lastMoveTime);
        sb.append(", allGamesStatisticRating=").append(allGamesStatisticRating);
        sb.append('}');
        return sb.toString();
    }
}