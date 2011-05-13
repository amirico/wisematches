package wisematches.tracking.statistic.impl;

import wisematches.server.personality.Personality;
import wisematches.tracking.statistic.statistician.GamesStatistician;
import wisematches.tracking.statistic.statistician.MovesStatistician;
import wisematches.tracking.statistic.statistician.PlayerStatisticEditor;
import wisematches.tracking.statistic.statistician.RatingsStatistician;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerStatisticFactory<T extends PlayerStatisticEditor> {
	Class<? extends T> getStatisticType();

	T createPlayerStatistic(Personality personality);

	GamesStatistician getGamesStatistician();

	MovesStatistician getMovesStatistician();

	RatingsStatistician getRatingsStatistician();
}
