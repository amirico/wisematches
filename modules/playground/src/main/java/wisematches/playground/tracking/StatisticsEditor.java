package wisematches.playground.tracking;

import wisematches.personality.Personality;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@MappedSuperclass
public class StatisticsEditor implements Statistics, Serializable {
	@Id
	@Column(name = "playerId")
	private long playerId;

	@Column(name = "rating")
	private short rating = 1200;

	@Column(name = "updateTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;

	@Column(name = "wins")
	private int wins;

	@Column(name = "loses")
	private int loses;

	@Column(name = "draws")
	private int draws;

	@Column(name = "resigned")
	private int resigned;

	@Column(name = "timeouts")
	private int timeouts;

	@Column(name = "stalemates")
	private int stalemates;

	@Column(name = "active")
	private int activeGames;

	@Column(name = "finished")
	private int finishedGames;

	@Column(name = "aRating")
	private float averageRating;

	@Column(name = "hRating")
	private short highestRating;

	@Column(name = "lRating")
	private short lowestRating;

	@Column(name = "aoRating")
	private float averageOpponentRating;

	@Column(name = "hoRating")
	private short highestWonOpponentRating;

	@Column(name = "hoId")
	private long highestWonOpponentId;

	@Column(name = "loRating")
	private short lowestLostOpponentRating;

	@Column(name = "loId")
	private long lowestLostOpponentId;

	@Column(name = "lastMoveTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastMoveTime;

	@Column(name = "avgMoveTime")
	private float averageMoveTime;

	@Column(name = "avgGameMoves")
	private float averageMovesPerGame;

	@Column(name = "turns")
	private int turnsCount;

	@Column(name = "passes")
	private int passesCount;

	@Column(name = "lPoints")
	private int lowestPoints;

	@Column(name = "aPoints")
	private float averagePoints;

	@Column(name = "hPoints")
	private int highestPoints;

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

	public int getResigned() {
		return resigned;
	}

	public int getStalemates() {
		return stalemates;
	}

	@Override
	public int getActiveGames() {
		return activeGames;
	}

	@Override
	public int getRatedGames() {
		return wins + loses + draws;
	}

	@Override
	public int getUnratedGames() {
		return finishedGames - getRatedGames();
	}

	@Override
	public int getFinishedGames() {
		return finishedGames;
	}

	@Override
	public float getAverageRating() {
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
	public float getAverageOpponentRating() {
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
	public float getAverageMoveTime() {
		return averageMoveTime;
	}

	@Override
	public float getAverageMovesPerGame() {
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
	public float getAveragePoints() {
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

	public void setResigned(int resigned) {
		this.resigned = resigned;
	}

	public void setStalemates(int stalemates) {
		this.stalemates = stalemates;
	}

	public void setActiveGames(int activeGames) {
		this.activeGames = activeGames;
	}

	public void setFinishedGames(int finishedGames) {
		this.finishedGames = finishedGames;
	}

	public void setAverageRating(float averageRating) {
		this.averageRating = averageRating;
	}

	public void setHighestRating(short highestRating) {
		this.highestRating = highestRating;
	}

	public void setLowestRating(short lowestRating) {
		this.lowestRating = lowestRating;
	}

	public void setAverageOpponentRating(float averageOpponentRating) {
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

	public void setAverageMoveTime(float averageMoveTime) {
		this.averageMoveTime = averageMoveTime;
	}

	public void setAverageMovesPerGame(float averageMovesPerGame) {
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

	public void setAveragePoints(float averagePoints) {
		this.averagePoints = averagePoints;
	}

	public void setHighestPoints(int highestPoints) {
		this.highestPoints = highestPoints;
	}

	public void setRating(short rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "StatisticsEditor{" +
				"playerId=" + playerId +
				", rating=" + rating +
				", updateTime=" + updateTime +
				", wins=" + wins +
				", loses=" + loses +
				", draws=" + draws +
				", resigned=" + resigned +
				", timeouts=" + timeouts +
				", stalemates=" + stalemates +
				", activeGames=" + activeGames +
				", finishedGames=" + finishedGames +
				", averageRating=" + averageRating +
				", highestRating=" + highestRating +
				", lowestRating=" + lowestRating +
				", averageOpponentRating=" + averageOpponentRating +
				", highestWonOpponentRating=" + highestWonOpponentRating +
				", highestWonOpponentId=" + highestWonOpponentId +
				", lowestLostOpponentRating=" + lowestLostOpponentRating +
				", lowestLostOpponentId=" + lowestLostOpponentId +
				", lastMoveTime=" + lastMoveTime +
				", averageMoveTime=" + averageMoveTime +
				", averageMovesPerGame=" + averageMovesPerGame +
				", turnsCount=" + turnsCount +
				", passesCount=" + passesCount +
				", lowestPoints=" + lowestPoints +
				", averagePoints=" + averagePoints +
				", highestPoints=" + highestPoints +
				'}';
	}
}
