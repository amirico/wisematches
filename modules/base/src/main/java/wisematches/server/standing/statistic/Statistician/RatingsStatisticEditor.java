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
	private short averageRating;

	@Column(name = "rHi")
	private short highestRating;

	@Column(name = "rLow")
	private short lowestRating;

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
		return averageRating;
	}

	@Override
	public short getHighest() {
		return highestRating;
	}

	@Override
	public short getLowest() {
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

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("RatingsStatisticEditor");
		sb.append("{averageRating=").append(averageRating);
		sb.append(", highestRating=").append(highestRating);
		sb.append(", lowestRating=").append(lowestRating);
		sb.append(", averageOpponentRating=").append(averageOpponentRating);
		sb.append(", highestWonOpponentRating=").append(highestWonOpponentRating);
		sb.append(", highestWonOpponentId=").append(highestWonOpponentId);
		sb.append(", lowestLostOpponentRating=").append(lowestLostOpponentRating);
		sb.append(", lowestLostOpponentId=").append(lowestLostOpponentId);
		sb.append('}');
		return sb.toString();
	}
}
