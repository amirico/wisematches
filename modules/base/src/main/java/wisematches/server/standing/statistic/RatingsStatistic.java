package wisematches.server.standing.statistic;

/**
 * A player's statistic related to it's rating.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RatingsStatistic {
	/**
	 * Average player's rating.
	 *
	 * @return the average player's rating.
	 */
	short getAverage();

	/**
	 * Highest player's rating.
	 *
	 * @return highest player's rating.
	 */
	short getHighest();

	/**
	 * Lowest player's rating.
	 *
	 * @return lowest player's rating.
	 */
	short getLowest();


	/**
	 * Average opponent's rating.
	 *
	 * @return the average opponent's rating.
	 */
	short getAverageOpponentRating();


	/**
	 * Highest won opponent's rating.
	 *
	 * @return the highest won opponent's rating.
	 */
	short getHighestWonOpponentRating();

	/**
	 * Highest won opponent's id. If there is no one won opponent the method return zero.
	 *
	 * @return the highest won opponent's id or {@code zero} if there is no one won opponent.
	 */
	long getHighestWonOpponentId();


	/**
	 * Lowest won opponent's rating.
	 *
	 * @return the lowest won opponent's rating.
	 */
	short getLowestLostOpponentRating();

	/**
	 * Lowest won opponent's id. If there is no one lost opponent the method return zero.
	 *
	 * @return the lowest won opponent's id or {@code zero} if there is no one lost opponent.
	 */
	long getLowestLostOpponentId();
}
