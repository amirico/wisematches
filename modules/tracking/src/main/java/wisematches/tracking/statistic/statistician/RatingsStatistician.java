package wisematches.tracking.statistic.statistician;

import wisematches.playground.GameBoard;
import wisematches.playground.GamePlayerHand;
import wisematches.playground.GameResolution;
import wisematches.playground.GameSettings;
import wisematches.tracking.statistic.PlayerStatistic;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RatingsStatistician<S extends GameSettings, P extends GamePlayerHand, B extends GameBoard<S, P>> extends PlayerStatistician {
	void updateRatingsStatistic(B board, GameResolution resolution, Collection<P> wonPlayers, PlayerStatistic statistic, RatingsStatisticEditor editor);
}
