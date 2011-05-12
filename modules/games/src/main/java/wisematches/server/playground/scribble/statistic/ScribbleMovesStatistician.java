package wisematches.server.playground.scribble.statistic;

import wisematches.server.playground.board.GameMove;
import wisematches.server.playground.board.PlayerMove;
import wisematches.server.playground.scribble.Word;
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

		if (editor instanceof ScribbleMovesStatisticEditor) {
			final ScribbleMovesStatisticEditor sme = (ScribbleMovesStatisticEditor) editor;

			final PlayerMove playerMove = move.getPlayerMove();
			if (playerMove instanceof MakeWordMove) {
				final MakeWordMove wordMove = (MakeWordMove) playerMove;
				editor.setWordsCount(editor.getWordsCount() + 1);

				final Word word = wordMove.getWord();
				final String text = word.getText();
				sme.setAverageWordLength(average(sme.getAverageWordLength(), text.length(), editor.getWordsCount()));

				if (editor.getMaxPoints() == move.getPoints()) {
					sme.setLastValuableWord(word);
				}

				if (sme.getLastLongestWord() == null || sme.getLastLongestWord().length() < text.length()) {
					sme.setLastLongestWord(word);
				}
			} else if (playerMove instanceof ExchangeTilesMove) {
				editor.setExchangesCount(editor.getExchangesCount() + 1);
			}
		}
	}
}
