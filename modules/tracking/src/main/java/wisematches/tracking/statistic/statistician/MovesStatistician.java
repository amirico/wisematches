package wisematches.tracking.statistic.statistician;

import wisematches.server.playground.board.GameBoard;
import wisematches.server.playground.board.GameMove;
import wisematches.server.playground.board.GamePlayerHand;
import wisematches.server.playground.board.GameSettings;
import wisematches.tracking.statistic.PlayerStatistic;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface MovesStatistician<S extends GameSettings, P extends GamePlayerHand, B extends GameBoard<S, P>> extends PlayerStatistician {
	void updateMovesStatistic(B board, GameMove move, PlayerStatistic statistic, MovesStatisticEditor editor);
}
