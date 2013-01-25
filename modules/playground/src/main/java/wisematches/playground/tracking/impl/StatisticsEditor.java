package wisematches.playground.tracking.impl;

import wisematches.core.Player;
import wisematches.core.personality.machinery.RobotType;
import wisematches.playground.tourney.TourneyPlace;
import wisematches.playground.tracking.Statistics;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

	@Column(name = "lPoints")
	private int lowestPoints;

	@Column(name = "aPoints")
	private float averagePoints;

	@Column(name = "hPoints")
	private int highestPoints;

	@Column(name = "winsRD")
	private int robotWinsDull;

	@Column(name = "winsRT")
	private int robotWinsTrainee;

	@Column(name = "winsRE")
	private int robotWinsExpert;

	@Column(name = "winsTF")
	private int tourneyWinsFirst;

	@Column(name = "winsTS")
	private int tourneyWinsSecond;

	@Column(name = "winsTT")
	private int tourneyWinsThird;

	@Transient
	private final Set<String> changedProperties = new HashSet<>();

	protected StatisticsEditor() {
	}

	protected StatisticsEditor(Player player) {
		this.playerId = player.getId();
		this.updateTime = new Date();
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

	@Override
	public int getRobotWins(RobotType type) {
		if (type == null) {
			return robotWinsDull + robotWinsExpert + robotWinsTrainee;
		}
		switch (type) {
			case DULL:
				return robotWinsDull;
			case TRAINEE:
				return robotWinsTrainee;
			case EXPERT:
				return robotWinsExpert;
		}
		throw new IllegalArgumentException("Unsupported robot type: " + type);
	}


	@Override
	public int getTourneyWins(TourneyPlace place) {
		if (place == null) {
			return tourneyWinsFirst + tourneyWinsSecond + tourneyWinsThird;
		}
		switch (place) {
			case FIRST:
				return tourneyWinsFirst;
			case SECOND:
				return tourneyWinsSecond;
			case THIRD:
				return tourneyWinsThird;
		}
		throw new IllegalArgumentException("Unsupported place type: " + place);
	}

	public void setWins(int wins) {
		this.wins = wins;
		propertyChanged("wins");
	}

	public void setLoses(int loses) {
		this.loses = loses;
		propertyChanged("loses");
	}

	public void setDraws(int draws) {
		this.draws = draws;
		propertyChanged("draws");
	}

	public void setTimeouts(int timeouts) {
		this.timeouts = timeouts;
		propertyChanged("timeouts");
	}

	public void setResigned(int resigned) {
		this.resigned = resigned;
		propertyChanged("resigned");
	}

	public void setStalemates(int stalemates) {
		this.stalemates = stalemates;
		propertyChanged("stalemates");
	}

	public void setActiveGames(int activeGames) {
		this.activeGames = activeGames;
		propertyChanged("activeGames");
	}

	public void setFinishedGames(int finishedGames) {
		this.finishedGames = finishedGames;
		propertyChanged("finishedGames");
	}

	public void setAverageRating(float averageRating) {
		this.averageRating = averageRating;
		propertyChanged("averageRating");
	}

	public void setHighestRating(short highestRating) {
		this.highestRating = highestRating;
		propertyChanged("highestRating");
	}

	public void setLowestRating(short lowestRating) {
		this.lowestRating = lowestRating;
		propertyChanged("lowestRating");
	}

	public void setAverageOpponentRating(float averageOpponentRating) {
		this.averageOpponentRating = averageOpponentRating;
		propertyChanged("averageOpponentRating");
	}

	public void setHighestWonOpponentRating(short highestWonOpponentRating) {
		this.highestWonOpponentRating = highestWonOpponentRating;
		propertyChanged("highestWonOpponentRating");
	}

	public void setHighestWonOpponentId(long highestWonOpponentId) {
		this.highestWonOpponentId = highestWonOpponentId;
		propertyChanged("highestWonOpponentId");
	}

	public void setLowestLostOpponentRating(short lowestLostOpponentRating) {
		this.lowestLostOpponentRating = lowestLostOpponentRating;
		propertyChanged("lowestLostOpponentRating");
	}

	public void setLowestLostOpponentId(long lowestLostOpponentId) {
		this.lowestLostOpponentId = lowestLostOpponentId;
		propertyChanged("lowestLostOpponentId");
	}

	public void setLastMoveTime(Date lastMoveTime) {
		this.lastMoveTime = lastMoveTime;
		propertyChanged("lastMoveTime");
	}

	public void setAverageMoveTime(float averageMoveTime) {
		this.averageMoveTime = averageMoveTime;
		propertyChanged("averageMoveTime");
	}

	public void setAverageMovesPerGame(float averageMovesPerGame) {
		this.averageMovesPerGame = averageMovesPerGame;
		propertyChanged("averageMovesPerGame");
	}

	public void setTurnsCount(int turnsCount) {
		this.turnsCount = turnsCount;
		propertyChanged("turnsCount");
	}

	public void setLowestPoints(int lowestPoints) {
		this.lowestPoints = lowestPoints;
		propertyChanged("lowestPoints");
	}

	public void setAveragePoints(float averagePoints) {
		this.averagePoints = averagePoints;
		propertyChanged("averagePoints");
	}

	public void setHighestPoints(int highestPoints) {
		this.highestPoints = highestPoints;
		propertyChanged("highestPoints");
	}

	public void setRating(short rating) {
		this.rating = rating;
		propertyChanged("rating");
	}

	public void setRobotWins(RobotType type, int count) {
		switch (type) {
			case DULL:
				robotWinsDull = count;
				propertyChanged("robotWinsDull");
				return;
			case TRAINEE:
				robotWinsTrainee = count;
				propertyChanged("robotWinsTrainee");
				return;
			case EXPERT:
				robotWinsExpert = count;
				propertyChanged("robotWinsExpert");
				return;
		}
		throw new IllegalArgumentException("Unsupported robot type: " + type);
	}

	public void setTourneyWins(TourneyPlace place, int count) {
		switch (place) {
			case FIRST:
				tourneyWinsFirst = count;
				propertyChanged("tourneyWinsFirst");
				return;
			case SECOND:
				tourneyWinsSecond = count;
				propertyChanged("tourneyWinsSecond");
				return;
			case THIRD:
				tourneyWinsThird = count;
				propertyChanged("tourneyWinsThird");
				return;
		}
		throw new IllegalArgumentException("Unsupported place type: " + place);
	}

	public Set<String> takeChangedProperties() {
		if (changedProperties.size() == 0) {
			return Collections.emptySet();
		}
		Set<String> res = new HashSet<>(changedProperties);
		changedProperties.clear();
		return res;
	}

	protected final void propertyChanged(String name) {
		changedProperties.add(name);
		this.updateTime = new Date();
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
				", lowestPoints=" + lowestPoints +
				", averagePoints=" + averagePoints +
				", highestPoints=" + highestPoints +
				'}';
	}
}
