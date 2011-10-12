package wisematches.playground.tracking;

import java.util.Date;

/**
 * {@code Statistics} interface contains information about a player
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface Statistics {
	/**
	 * Returns id of a player who that statistic belongs to.
	 *
	 * @return the player id
	 */
	long getPlayerId();

	/**
	 * Returns date when the statistic was updated last time
	 *
	 * @return date when the statistic was updated last time
	 */
	Date getUpdateTime();

	/**
	 * Returns current player's rating
	 *
	 * @return current player's rating
	 */
	short getRating();

	/**
	 * Returns number of wins
	 *
	 * @return number of wins
	 */
	int getWins();

	/**
	 * Returns number of loses
	 *
	 * @return number of loses
	 */
	int getLoses();

	/**
	 * Returns number of draws
	 *
	 * @return number of draws
	 */
	int getDraws();

	/**
	 * Returns number of games interrupted by timeout.
	 *
	 * @return number of games interrupted by timeout.
	 */
	int getTimeouts();

	/**
	 * Returns number of active games.
	 *
	 * @return number of active games.
	 */
	int getActiveGames();

	/**
	 * Returns number of finished games.
	 *
	 * @return number of finished games.
	 */
	int getFinishedGames();

	/**
	 * Returns number of finished games which were not rated.
	 *
	 * @return number of finished games which were not rated.
	 */
	int getUnratedGames();


	short getAverageRating();

	short getHighestRating();

	short getLowestRating();

	short getAverageOpponentRating();

	short getHighestWonOpponentRating();

	long getHighestWonOpponentId();

	short getLowestLostOpponentRating();

	long getLowestLostOpponentId();


	Date getLastMoveTime();

	int getAverageMoveTime();

	int getAverageMovesPerGame();

	int getTurnsCount();

	int getPassesCount();

	int getLowestPoints();

	int getAveragePoints();

	int getHighestPoints();
}
