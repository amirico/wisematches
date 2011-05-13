package wisematches.tracking.statistic.statistician;

import wisematches.tracking.statistic.MovesStatistic;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@MappedSuperclass
public class MovesStatisticEditor implements MovesStatistic, Serializable {
	@Column(name = "mTurns")
	private int turnsCount;

	@Column(name = "mWords")
	private int wordsCount;

	@Column(name = "mPasses")
	private int passesCount;

	@Column(name = "mExchanges")
	private int exchangesCount;

	@Column(name = "mAvgTime")
	private int averageTurnTime;

	@Column(name = "mMinPoints")
	private int minPoints;

	@Column(name = "mAvgPoints")
	private int avgPoints;

	@Column(name = "mMaxPoints")
	private int maxPoints;

	@Column(name = "mLastTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastMoveTime;

	private static final long serialVersionUID = -8120586301278760742L;

	public MovesStatisticEditor() {
	}

	@Override
	public int getTurnsCount() {
		return turnsCount;
	}

	@Override
	public int getWordsCount() {
		return wordsCount;
	}

	@Override
	public int getPassesCount() {
		return passesCount;
	}

	@Override
	public int getExchangesCount() {
		return exchangesCount;
	}

	@Override
	public int getAverageTurnTime() {
		return averageTurnTime;
	}

	@Override
	public int getMinPoints() {
		return minPoints;
	}

	@Override
	public int getAvgPoints() {
		return avgPoints;
	}

	@Override
	public int getMaxPoints() {
		return maxPoints;
	}

	@Override
	public Date getLastMoveTime() {
		return lastMoveTime;
	}

	public void setTurnsCount(int turnsCount) {
		this.turnsCount = turnsCount;
	}

	public void setWordsCount(int wordsCount) {
		this.wordsCount = wordsCount;
	}

	public void setPassesCount(int passesCount) {
		this.passesCount = passesCount;
	}

	public void setExchangesCount(int exchangesCount) {
		this.exchangesCount = exchangesCount;
	}

	public void setAverageTurnTime(int averageTurnTime) {
		this.averageTurnTime = averageTurnTime;
	}

	public void setMinPoints(int minPoints) {
		this.minPoints = minPoints;
	}

	public void setAvgPoints(int avgPoints) {
		this.avgPoints = avgPoints;
	}

	public void setMaxPoints(int maxPoints) {
		this.maxPoints = maxPoints;
	}

	public void setLastMoveTime(Date lastMoveTime) {
		this.lastMoveTime = lastMoveTime;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("MovesStatisticEditor");
		sb.append("{turnsCount=").append(turnsCount);
		sb.append(", wordsCount=").append(wordsCount);
		sb.append(", passesCount=").append(passesCount);
		sb.append(", exchangesCount=").append(exchangesCount);
		sb.append(", averageTurnTime=").append(averageTurnTime);
		sb.append(", minPoints=").append(minPoints);
		sb.append(", avgPoints=").append(avgPoints);
		sb.append(", maxPoints=").append(maxPoints);
		sb.append(", lastMoveTime=").append(lastMoveTime);
		sb.append('}');
		return sb.toString();
	}
}

