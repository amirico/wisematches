package wisematches.tracking.statistic.statistician;

import wisematches.server.playground.board.GameBoard;
import wisematches.server.playground.board.GamePlayerHand;
import wisematches.server.playground.board.GameResolution;
import wisematches.server.playground.board.GameSettings;
import wisematches.tracking.statistic.PlayerStatistic;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GamesStatistician<S extends GameSettings, P extends GamePlayerHand, B extends GameBoard<S, P>> extends PlayerStatistician {
	void updateGamesStatistic(B board, PlayerStatistic statistic, GamesStatisticEditor editor);

	void updateGamesStatistic(B board, GameResolution resolution, Collection<P> wonPlayers, PlayerStatistic statistic, GamesStatisticEditor editor);
}
