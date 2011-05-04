package wisematches.server.standing.statistic.impl.statistician;

import wisematches.server.playground.board.GameBoard;
import wisematches.server.playground.board.GamePlayerHand;
import wisematches.server.playground.board.GameResolution;
import wisematches.server.playground.board.GameSettings;
import wisematches.server.standing.statistic.PlayerStatistic;
import wisematches.server.standing.statistic.statistician.GamesStatisticEditor;
import wisematches.server.standing.statistic.statistician.GamesStatistician;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultGamesStatistician<S extends GameSettings, P extends GamePlayerHand, B extends GameBoard<S, P>> implements GamesStatistician<S, P, B> {
	public DefaultGamesStatistician() {
	}

	@Override
	public void updateGamesStatistic(B board, PlayerStatistic statistic, GamesStatisticEditor editor) {
		editor.setActive(editor.getActive() + 1);
	}

	@Override
	public void updateGamesStatistic(B board, GameResolution resolution, Collection<P> wonPlayers, PlayerStatistic statistic, GamesStatisticEditor editor) {
		editor.setActive(editor.getActive() - 1);
		editor.setFinished(editor.getFinished() + 1);

/*
		editor.setAverageMovesPerGame();

		final Date previousMoveTime = previousMoveTime(board);

		statistic.incrementTurnsCount();
		statistic.setAverageTurnTime(
				average(statistic.getAverageTurnTime(),
						(int) (currentMoveTime.getTime() - previousMoveTime.getTime()),
						statistic.getTurnsCount()
				)
		);

*/
		if (board.isRatedGame()) { // If game is not rated just ignore it
			if (resolution == GameResolution.TIMEOUT) {
				editor.setTimeouts(editor.getTimeouts() + 1);
			}

			if (wonPlayers.isEmpty()) { // draw
				editor.setDraws(editor.getDraws() + 1);
			} else {
				boolean winner = false;
				for (GamePlayerHand hand : wonPlayers) {
					if (hand.getPlayerId() == statistic.getPlayerId()) {
						winner = true;
						break;
					}
				}

				if (winner) {
					editor.setWins(editor.getWins() + 1);
				} else {
					editor.setLoses(editor.getLoses() + 1);
				}
			}
		} else {
			editor.setUnrated(editor.getUnrated() + 1);
		}
	}
}
