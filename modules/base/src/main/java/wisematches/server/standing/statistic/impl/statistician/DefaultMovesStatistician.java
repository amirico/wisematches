package wisematches.server.standing.statistic.impl.statistician;

import wisematches.server.playground.board.*;
import wisematches.server.standing.statistic.PlayerStatistic;
import wisematches.server.standing.statistic.statistician.MovesStatisticEditor;
import wisematches.server.standing.statistic.statistician.MovesStatistician;

import java.util.Date;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultMovesStatistician<S extends GameSettings, P extends GamePlayerHand, B extends GameBoard<S, P>>
		extends AbstractStatistician implements MovesStatistician<S, P, B> {
	public DefaultMovesStatistician() {
	}

	@Override
	public void updateMovesStatistic(B board, GameMove move, PlayerStatistic statistic, MovesStatisticEditor editor) {
		final int turnsCount = editor.getTurnsCount() + 1;

		editor.setTurnsCount(turnsCount);
		editor.setLastMoveTime(move.getMoveTime());

		final int points = move.getPoints();
		if (turnsCount == 1 || points > editor.getMaxPoints()) {
			editor.setMaxPoints(points);
		}
		if (turnsCount == 1 || points < editor.getMinPoints()) {
			editor.setMinPoints(points);
		}
		editor.setAvgPoints(average(editor.getAvgPoints(), points, turnsCount));

		final int turnTime = (int) (move.getMoveTime().getTime() - previousMoveTime(board).getTime());
		editor.setAverageTurnTime(average(editor.getAverageTurnTime(), turnTime, turnsCount));

		final PlayerMove playerMove = move.getPlayerMove();
		if (playerMove instanceof MakeTurnMove) {
			processMakeTurn(move, statistic, editor);
		} else if (playerMove instanceof PassTurnMove) {
			processPassTurn(move, statistic, editor);
		}
	}

	protected void processMakeTurn(GameMove move, PlayerStatistic statistic, MovesStatisticEditor editor) {
	}

	protected void processPassTurn(GameMove move, PlayerStatistic statistic, MovesStatisticEditor editor) {
		editor.setPassesCount(editor.getPassesCount() + 1);
	}

	protected Date previousMoveTime(B board) {
		final List<GameMove> list = board.getGameMoves();
		if (list.size() <= 1) {
			return board.getStartedTime();
		}
		return list.get(list.size() - 2).getMoveTime(); // previous move
	}
}
