package wisematches.tracking.statistic;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface MovesStatistic {
	int getTurnsCount();

	int getWordsCount();

	int getPassesCount();

	int getExchangesCount();


	int getAverageTurnTime();


	int getMinPoints();

	int getAvgPoints();

	int getMaxPoints();


	Date getLastMoveTime();
}
