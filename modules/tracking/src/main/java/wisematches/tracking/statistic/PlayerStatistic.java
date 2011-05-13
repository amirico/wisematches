package wisematches.tracking.statistic;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerStatistic {
	long getPlayerId();

	Date getUpdateTime();

	MovesStatistic getMovesStatistic();

	GamesStatistic getGamesStatistic();

	RatingsStatistic getRatingsStatistic();
}