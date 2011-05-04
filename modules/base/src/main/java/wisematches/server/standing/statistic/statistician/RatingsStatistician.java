package wisematches.server.standing.statistic.statistician;

import wisematches.server.playground.board.GameBoard;
import wisematches.server.playground.board.GamePlayerHand;
import wisematches.server.playground.board.GameResolution;
import wisematches.server.playground.board.GameSettings;
import wisematches.server.standing.statistic.PlayerStatistic;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RatingsStatistician<S extends GameSettings, P extends GamePlayerHand, B extends GameBoard<S, P>> extends PlayerStatistician {
	void updateRatingsStatistic(B board, GameResolution resolution, Collection<P> wonPlayers, PlayerStatistic statistic, RatingsStatisticEditor editor);
}
