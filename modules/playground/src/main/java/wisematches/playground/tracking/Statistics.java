package wisematches.playground.tracking;

import wisematches.core.personality.proprietary.robot.RobotType;
import wisematches.playground.tourney.TourneyPlace;

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
	 * Returns number of resigned games.
	 *
	 * @return number of resigned games.
	 */
	int getResigned();

	/**
	 * Returns number of games interrupted by timeout.
	 *
	 * @return number of games interrupted by timeout.
	 */
	int getTimeouts();

	/**
	 * Returns number of stalemates games.
	 *
	 * @return number of stalemates games.
	 */
	int getStalemates();

	/**
	 * Returns number of all rated games. It's just sum of wins, loses and draws.
	 *
	 * @return number of all rated games.
	 */
	int getRatedGames();

	/**
	 * Returns number of finished games which were not rated. It's number of all finished games minus rated games.
	 *
	 * @return number of finished games which were not rated.
	 */
	int getUnratedGames();


	float getAverageRating();

	short getHighestRating();

	short getLowestRating();

	float getAverageOpponentRating();

	short getHighestWonOpponentRating();

	long getHighestWonOpponentId();

	short getLowestLostOpponentRating();

	long getLowestLostOpponentId();


	Date getLastMoveTime();

	float getAverageMoveTime();

	float getAverageMovesPerGame();

	int getTurnsCount();

	int getPassesCount();

	int getLowestPoints();

	float getAveragePoints();

	int getHighestPoints();


	/**
	 * Returns number of games played with specified robot types.
	 *
	 * @param type the robot type. If {@code null} number of all wins will be returned.
	 * @return games count played (finished) with robot of specified type.
	 */
	int getRobotWins(RobotType type);

	/**
	 * Returns number of wins in a tourney with taken place.
	 *
	 * @param place taken tourney place. If {@code null} number of all wins will be returned.
	 * @return number of wins in tourneys.
	 */
	int getTourneyWins(TourneyPlace place);
}
