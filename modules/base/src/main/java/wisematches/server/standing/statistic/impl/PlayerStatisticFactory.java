package wisematches.server.standing.statistic.impl;

import wisematches.server.personality.Personality;
import wisematches.server.standing.statistic.statistician.GamesStatistician;
import wisematches.server.standing.statistic.statistician.MovesStatistician;
import wisematches.server.standing.statistic.statistician.PlayerStatisticEditor;
import wisematches.server.standing.statistic.statistician.RatingsStatistician;

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
