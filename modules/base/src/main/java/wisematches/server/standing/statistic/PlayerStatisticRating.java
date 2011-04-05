package wisematches.server.standing.statistic;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerStatisticRating {
	int getAverageRating();

	int getHighestRating();

	int getLowestRating();

	int getAverageOpponentRating();

	int getHighestWonOpponentRating();

	int getLowestLostOpponentRating();

	int getAverageMovesPerGame();

	long getHighestWonOpponentId();

	long getLowestLostOpponentId();
}
