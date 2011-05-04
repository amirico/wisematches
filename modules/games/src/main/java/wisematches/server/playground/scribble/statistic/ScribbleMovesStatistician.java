package wisematches.server.playground.scribble.statistic;

import wisematches.server.playground.board.*;
import wisematches.server.playground.scribble.board.*;
import wisematches.server.standing.statistic.statistician.MovesStatisticEditor;
import wisematches.server.standing.statistic.statistician.MovesStatistician;
import wisematches.server.standing.statistic.PlayerStatistic;

import java.util.Date;
import java.util.List;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleMovesStatistician implements MovesStatistician<ScribbleSettings, ScribblePlayerHand, ScribbleBoard> {
	public ScribbleMovesStatistician() {
	}

	@Override
	public void updateMovesStatistic(ScribbleBoard board, GameMove move, PlayerStatistic statistic, MovesStatisticEditor editor) {
		final int turnsCount = editor.getTurnsCount() + 1;

		editor.setTurnsCount(turnsCount);
		editor.setLastMoveTime(move.getMoveTime());

		boolean valuableMove = false;
		final int points = move.getPoints();
		if (turnsCount == 1 || points > editor.getMaxPoints()) {
			valuableMove = true;
			editor.setMaxPoints(points);
		}
		if (turnsCount == 1 || points < editor.getMinPoints()) {
			editor.setMinPoints(points);
		}
		editor.setAvgPoints(average(editor.getAvgPoints(), points, turnsCount));

		final PlayerMove playerMove = move.getPlayerMove();
		if (playerMove instanceof MakeWordMove) {
			final MakeWordMove wordMove = (MakeWordMove) playerMove;
			editor.setWordsCount(editor.getWordsCount() + 1);

			final String text = wordMove.getWord().getText();
			editor.setAverageWordLength(average(editor.getAverageWordLength(), text.length(), editor.getWordsCount()));

			if (valuableMove) {
				editor.setLastValuableWord(text);
			}

			if (editor.getLastLongestWord() == null || editor.getLastLongestWord().length() < text.length()) {
				editor.setLastLongestWord(text);
			}
		} else if (playerMove instanceof ExchangeTilesMove) {
			editor.setExchangesCount(editor.getExchangesCount() + 1);
		} else if (playerMove instanceof PassTurnMove) {
			editor.setPassesCount(editor.getPassesCount() + 1);
		}

		final int turnTime = (int) (move.getMoveTime().getTime() - previousMoveTime(board).getTime());
		editor.setAverageTurnTime(average(editor.getAverageTurnTime(), turnTime, turnsCount));
	}

	protected Date previousMoveTime(ScribbleBoard board) {
		final List<GameMove> list = board.getGameMoves();
		if (list.size() <= 1) {
			return board.getStartedTime();
		}
		return list.get(list.size() - 2).getMoveTime(); // previous move
	}

	protected int average(final int previousAverage, final int newValue, final int newCount) {
		return (previousAverage * (newCount - 1) + newValue) / newCount;
	}
}
