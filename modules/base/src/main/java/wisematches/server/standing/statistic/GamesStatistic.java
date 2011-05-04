package wisematches.server.standing.statistic;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GamesStatistic {
	int getActive();

	int getWins();

	int getLoses();

	int getDraws();

	int getTimeouts();

	int getFinished();

	int getUnrated();

	int getAverageMovesPerGame();
}
