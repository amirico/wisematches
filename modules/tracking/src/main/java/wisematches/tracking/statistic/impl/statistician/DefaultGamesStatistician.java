package wisematches.tracking.statistic.impl.statistician;

import wisematches.tracking.statistic.PlayerStatistic;
import wisematches.tracking.statistic.statistician.GamesStatisticEditor;
import wisematches.tracking.statistic.statistician.GamesStatistician;

import java.util.Collection;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultGamesStatistician<S extends GameSettings, P extends GamePlayerHand, B extends GameBoard<S, P>> extends AbstractStatistician implements GamesStatistician<S, P, B> {
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

		// Update average moves per game
		int movesCount = 0;
		final List<GameMove> list = board.getGameMoves();
		for (GameMove gameMove : list) {
			if (gameMove.getPlayerMove().getPlayerId() == statistic.getPlayerId()) {
				movesCount++;
			}
		}
		final int gamesCount = editor.getFinished();
		editor.setAverageMovesPerGame(average(editor.getAverageMovesPerGame(), movesCount, gamesCount));

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
