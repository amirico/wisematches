package wisematches.server.playground.scribble.statistic;

import wisematches.server.playground.board.GameMove;
import wisematches.server.playground.board.PlayerMove;
import wisematches.server.playground.scribble.board.*;
import wisematches.server.standing.statistic.PlayerStatistic;
import wisematches.server.standing.statistic.impl.statistician.DefaultMovesStatistician;
import wisematches.server.standing.statistic.statistician.MovesStatisticEditor;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleMovesStatistician extends DefaultMovesStatistician<ScribbleSettings, ScribblePlayerHand, ScribbleBoard> {
	public ScribbleMovesStatistician() {
	}

	@Override
	protected void processMakeTurn(GameMove move, PlayerStatistic statistic, MovesStatisticEditor editor) {
		super.processMakeTurn(move, statistic, editor);

		final PlayerMove playerMove = move.getPlayerMove();
		if (playerMove instanceof MakeWordMove) {
			final MakeWordMove wordMove = (MakeWordMove) playerMove;
			editor.setWordsCount(editor.getWordsCount() + 1);

			final String text = wordMove.getWord().getText();
			editor.setAverageWordLength(average(editor.getAverageWordLength(), text.length(), editor.getWordsCount()));

			if (editor.getMaxPoints() == move.getPoints()) {
				editor.setLastValuableWord(text);
			}

			if (editor.getLastLongestWord() == null || editor.getLastLongestWord().length() < text.length()) {
				editor.setLastLongestWord(text);
			}
		} else if (playerMove instanceof ExchangeTilesMove) {
			editor.setExchangesCount(editor.getExchangesCount() + 1);
		}
	}
}
