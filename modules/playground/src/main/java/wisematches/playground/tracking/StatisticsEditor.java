package wisematches.playground.tracking;

import wisematches.personality.Personality;

import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@MappedSuperclass
@NamedQueries({
		@NamedQuery(name = "player.rating",
				query = "SELECT r.rating " +
						"FROM wisematches.playground.tracking.StatisticsEditor r where r.playerId=?",
				hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}
		),
		@NamedQuery(name = "player.position",
				query = "SELECT count( a.id ), a.id " +
						"FROM wisematches.playground.tracking.StatisticsEditor a, wisematches.playground.tracking.StatisticsEditor b " +
						"WHERE a.id = ? AND (b.rating > a.rating OR (b.rating = a.rating AND b.id <= a.id)) GROUP BY a.id",
				hints = {@QueryHint(name = "org.hibernate.cacheable", value = "true")}
		)
})
public class StatisticsEditor implements Statistics {
	private long playerId;
	private Date updateTime;
	private int wins;
	private int loses;
	private int draws;
	private int timeouts;
	private int activeGames;
	private int unratedGames;
	private int finishedGames;
	private short averageRating;
	private short highestRating;
	private short lowestRating;
	private short averageOpponentRating;
	private short highestWonOpponentRating;
	private long highestWonOpponentId;
	private short lowestLostOpponentRating;
	private long lowestLostOpponentId;
	private Date lastMoveTime;
	private int averageMoveTime;
	private int averageMovesPerGame;
	private int turnsCount;
	private int passesCount;
	private int lowestPoints;
	private int averagePoints;
	private int highestPoints;
	private short rating;

	@Deprecated
	protected StatisticsEditor() {
	}

	public StatisticsEditor(Personality personality) {
		this.playerId = personality.getId();
		this.updateTime = new Date();
	}

	@Override
	public long getPlayerId() {
		return playerId;
	}

	@Override
	public Date getUpdateTime() {
		return updateTime;
	}

	@Override
	public short getRating() {
		return rating;
	}

	@Override
	public int getWins() {
		return wins;
	}

	@Override
	public int getLoses() {
		return loses;
	}

	@Override
	public int getDraws() {
		return draws;
	}

	@Override
	public int getTimeouts() {
		return timeouts;
	}

	@Override
	public int getActiveGames() {
		return activeGames;
	}

	@Override
	public int getUnratedGames() {
		return unratedGames;
	}

	@Override
	public int getFinishedGames() {
		return finishedGames;
	}

	@Override
	public short getAverageRating() {
		return averageRating;
	}

	@Override
	public short getHighestRating() {
		return highestRating;
	}

	@Override
	public short getLowestRating() {
		return lowestRating;
	}

	@Override
	public short getAverageOpponentRating() {
		return averageOpponentRating;
	}

	@Override
	public short getHighestWonOpponentRating() {
		return highestWonOpponentRating;
	}

	@Override
	public long getHighestWonOpponentId() {
		return highestWonOpponentId;
	}

	@Override
	public short getLowestLostOpponentRating() {
		return lowestLostOpponentRating;
	}

	@Override
	public long getLowestLostOpponentId() {
		return lowestLostOpponentId;
	}

	@Override
	public Date getLastMoveTime() {
		return lastMoveTime;
	}

	@Override
	public int getAverageMoveTime() {
		return averageMoveTime;
	}

	@Override
	public int getAverageMovesPerGame() {
		return averageMovesPerGame;
	}

	@Override
	public int getTurnsCount() {
		return turnsCount;
	}

	@Override
	public int getPassesCount() {
		return passesCount;
	}

	@Override
	public int getLowestPoints() {
		return lowestPoints;
	}

	@Override
	public int getAveragePoints() {
		return averagePoints;
	}

	@Override
	public int getHighestPoints() {
		return highestPoints;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public void setLoses(int loses) {
		this.loses = loses;
	}

	public void setDraws(int draws) {
		this.draws = draws;
	}

	public void setTimeouts(int timeouts) {
		this.timeouts = timeouts;
	}

	public void setActiveGames(int activeGames) {
		this.activeGames = activeGames;
	}

	public void setUnratedGames(int unratedGames) {
		this.unratedGames = unratedGames;
	}

	public void setFinishedGames(int finishedGames) {
		this.finishedGames = finishedGames;
	}

	public void setAverageRating(short averageRating) {
		this.averageRating = averageRating;
	}

	public void setHighestRating(short highestRating) {
		this.highestRating = highestRating;
	}

	public void setLowestRating(short lowestRating) {
		this.lowestRating = lowestRating;
	}

	public void setAverageOpponentRating(short averageOpponentRating) {
		this.averageOpponentRating = averageOpponentRating;
	}

	public void setHighestWonOpponentRating(short highestWonOpponentRating) {
		this.highestWonOpponentRating = highestWonOpponentRating;
	}

	public void setHighestWonOpponentId(long highestWonOpponentId) {
		this.highestWonOpponentId = highestWonOpponentId;
	}

	public void setLowestLostOpponentRating(short lowestLostOpponentRating) {
		this.lowestLostOpponentRating = lowestLostOpponentRating;
	}

	public void setLowestLostOpponentId(long lowestLostOpponentId) {
		this.lowestLostOpponentId = lowestLostOpponentId;
	}

	public void setLastMoveTime(Date lastMoveTime) {
		this.lastMoveTime = lastMoveTime;
	}

	public void setAverageMoveTime(int averageMoveTime) {
		this.averageMoveTime = averageMoveTime;
	}

	public void setAverageMovesPerGame(int averageMovesPerGame) {
		this.averageMovesPerGame = averageMovesPerGame;
	}

	public void setTurnsCount(int turnsCount) {
		this.turnsCount = turnsCount;
	}

	public void setPassesCount(int passesCount) {
		this.passesCount = passesCount;
	}

	public void setLowestPoints(int lowestPoints) {
		this.lowestPoints = lowestPoints;
	}

	public void setAveragePoints(int averagePoints) {
		this.averagePoints = averagePoints;
	}

	public void setHighestPoints(int highestPoints) {
		this.highestPoints = highestPoints;
	}

	public void setRating(short rating) {
		this.rating = rating;
	}
}
