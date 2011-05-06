package wisematches.server.standing.statistic.statistician;

import wisematches.server.standing.statistic.RatingsStatistic;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Embeddable
public class RatingsStatisticEditor implements RatingsStatistic, Serializable {
	@Column(name = "rAvg")
	private short average;

	@Column(name = "rHi")
	private short highest;

	@Column(name = "rLow")
	private short lowest;

	@Column(name = "roAvg")
	private short averageOpponentRating;

	@Column(name = "roWon")
	private short highestWonOpponentRating;

	@Column(name = "roWonId")
	private long highestWonOpponentId;

	@Column(name = "roLost")
	private short lowestLostOpponentRating;

	@Column(name = "roLostId")
	private long lowestLostOpponentId;

	private static final long serialVersionUID = 8962631678248095038L;

	public RatingsStatisticEditor() {
	}

	@Override
	public short getAverage() {
		return average;
	}

	@Override
	public short getHighest() {
		return highest;
	}

	@Override
	public short getLowest() {
		return lowest;
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

	public void setAverage(short averageRating) {
		this.average = averageRating;
	}

	public void setHighest(short highestRating) {
		this.highest = highestRating;
	}

	public void setLowest(short lowestRating) {
		this.lowest = lowestRating;
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

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("RatingsStatisticEditor");
		sb.append("{average=").append(average);
		sb.append(", highest=").append(highest);
		sb.append(", lowest=").append(lowest);
		sb.append(", averageOpponentRating=").append(averageOpponentRating);
		sb.append(", highestWonOpponentRating=").append(highestWonOpponentRating);
		sb.append(", highestWonOpponentId=").append(highestWonOpponentId);
		sb.append(", lowestLostOpponentRating=").append(lowestLostOpponentRating);
		sb.append(", lowestLostOpponentId=").append(lowestLostOpponentId);
		sb.append('}');
		return sb.toString();
	}
}
