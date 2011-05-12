package wisematches.server.standing.stats;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerStatistic {
	long getPlayerId();

	Date getUpdateTime();


	int getWins();

	int getLoses();

	int getDraws();

	int getTimeouts();

	int getActiveGames();

	int getUnratedGames();

	int getFinishedGames();


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

	int getWordsCount();

	int getPassesCount();

	int getExchangesCount();

	int getLowestPoints();

	int getAveragePoints();

	int getHighestPoints();
}
