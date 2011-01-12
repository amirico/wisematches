package wisematches.server.player.statistic;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Entity
@Table(name = "stats_info")
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PlayerStatistic implements Serializable {
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
	private long updateTime;

	/**
	 * Time when last cleanup was performed.
	 */
	@Column(name = "lastCleanupTime")
	private long lastCleanupTime;

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
	 * Time when last move was maden
	 */
	@Column(name = "lastMoveTime")
	private long lastMoveTime;

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
	private RatingInfo thirtyDaysRaingInfo = new RatingInfo();

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
	private RatingInfo ninetyDaysRaingInfo = new RatingInfo();

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
	private RatingInfo yearRaingInfo = new RatingInfo();

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
	private RatingInfo allGamesRaingInfo = new RatingInfo();

	/**
	 * This is Hibernate constructor
	 */
	PlayerStatistic() {
	}

	public PlayerStatistic(long playerId) {
		this.playerId = playerId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public int getActiveGames() {
		return activeGames;
	}

	public void setActiveGames(int activeGames) {
		this.activeGames = activeGames;
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

	public int getTimeouts() {
		return timeouts;
	}

	public void setTimeouts(int timeouts) {
		this.timeouts = timeouts;
	}

	public int getFinishedGames() {
		return wonGames + lostGames + drawGames;
	}

	public int getTurnsCount() {
		return turnsCount;
	}

	public void setTurnsCount(int turnsCount) {
		this.turnsCount = turnsCount;
	}

	/**
	 * Returns average turn time in milliseconds.
	 *
	 * @return the average turn time in milliseconds.
	 */
	public int getAverageTurnTime() {
		return averageTurnTime;
	}

	public void setAverageTurnTime(int averageTurnTime) {
		this.averageTurnTime = averageTurnTime;
	}

	public long getLastMoveTime() {
		return lastMoveTime;
	}

	public void setLastMoveTime(long lastMoveTime) {
		this.lastMoveTime = lastMoveTime;
	}

	public RatingInfo getThirtyDaysRaingInfo() {
		return thirtyDaysRaingInfo;
	}

	public RatingInfo getNinetyDaysRaingInfo() {
		return ninetyDaysRaingInfo;
	}

	public RatingInfo getYearRaingInfo() {
		return yearRaingInfo;
	}

	public RatingInfo getAllGamesRaingInfo() {
		return allGamesRaingInfo;
	}

	public long getLastCleanupTime() {
		return lastCleanupTime;
	}

	public void setLastCleanupTime(long lastCleanupTime) {
		this.lastCleanupTime = lastCleanupTime;
	}
}