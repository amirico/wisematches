package wisematches.server.standing.statistic;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Embeddable
public class PlayerStatisticRating implements Serializable, Cloneable {
	/**
	 * Average player's rating
	 */
	private int averageRating;
	/**
	 * Highest player's rating
	 */
	private int highestRating;
	/**
	 * Lowest player's rating
	 */
	private int lowestRating;
	/**
	 * Average rating of opponents
	 */
	private int averageOpponentRating;
	/**
	 * Highest rating won against
	 */
	private int highestWonOpponentRating;
	/**
	 * Player id of highest won against
	 */
	private long highestWonOpponentId;
	/**
	 * Lowest rating lost against
	 */
	private int lowestLostOpponentRating;
	/**
	 * Player id of lost against
	 */
	private long lowestLostOpponentId;
	/**
	 * Average number of moves in one game
	 */
	private int averageMovesPerGame;

	public PlayerStatisticRating() {
	}

	public int getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(int averageRating) {
		this.averageRating = averageRating;
	}

	public int getHighestRating() {
		return highestRating;
	}

	public void setHighestRating(int highestRating) {
		this.highestRating = highestRating;
	}

	public int getLowestRating() {
		return lowestRating;
	}

	public void setLowestRating(int lowestRating) {
		this.lowestRating = lowestRating;
	}

	public int getAverageOpponentRating() {
		return averageOpponentRating;
	}

	public void setAverageOpponentRating(int averageOpponentRating) {
		this.averageOpponentRating = averageOpponentRating;
	}

	public int getHighestWonOpponentRating() {
		return highestWonOpponentRating;
	}

	public void setHighestWonOpponentRating(int highestWonOpponentRating) {
		this.highestWonOpponentRating = highestWonOpponentRating;
	}

	public int getLowestLostOpponentRating() {
		return lowestLostOpponentRating;
	}

	public void setLowestLostOpponentRating(int lowestLostOpponentRating) {
		this.lowestLostOpponentRating = lowestLostOpponentRating;
	}

	public int getAverageMovesPerGame() {
		return averageMovesPerGame;
	}

	public void setAverageMovesPerGame(int averageMovesPerGame) {
		this.averageMovesPerGame = averageMovesPerGame;
	}

	public long getHighestWonOpponentId() {
		return highestWonOpponentId;
	}

	public void setHighestWonOpponentId(long highestWonOpponentId) {
		this.highestWonOpponentId = highestWonOpponentId;
	}

	public long getLowestLostOpponentId() {
		return lowestLostOpponentId;
	}

	public void setLowestLostOpponentId(long lowestLostOpponentId) {
		this.lowestLostOpponentId = lowestLostOpponentId;
	}

	@Override
	public PlayerStatisticRating clone() {
		try {
			return (PlayerStatisticRating) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("PlayerStatisticRating");
		sb.append("{averageMovesPerGame=").append(averageMovesPerGame);
		sb.append(", averageRating=").append(averageRating);
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
