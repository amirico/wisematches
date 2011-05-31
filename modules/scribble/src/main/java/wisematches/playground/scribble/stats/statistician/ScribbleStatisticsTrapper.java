package wisematches.playground.scribble.stats.statistician;

import wisematches.personality.Personality;
import wisematches.playground.*;
import wisematches.playground.scribble.ExchangeTilesMove;
import wisematches.playground.scribble.MakeWordMove;
import wisematches.playground.scribble.Word;
import wisematches.playground.stats.statistician.AbstractStatisticsTrapper;
import wisematches.playground.tracking.StatisticsEditor;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleStatisticsTrapper extends AbstractStatisticsTrapper {
	public ScribbleStatisticsTrapper() {
	}

	@Override
	public Class<? extends StatisticsEditor> getStatisticType() {
		return ScribbleStatisticsEditor.class;
	}

	@Override
	public StatisticsEditor createStatisticsEditor(Personality person) {
		return new ScribbleStatisticsEditor(person);
	}

	@Override
	public void trapGameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move, StatisticsEditor editor) {
		super.trapGameMoveDone(board, move, editor);

		final PlayerMove playerMove = move.getPlayerMove();
		final ScribbleStatisticsEditor sse = (ScribbleStatisticsEditor) editor;
		if (playerMove instanceof MakeWordMove) {
			final MakeWordMove wordMove = (MakeWordMove) playerMove;
			sse.setWordsCount(sse.getWordsCount() + 1);

			final Word word = wordMove.getWord();
			final String text = word.getText();
			sse.setAverageWordLength(average(sse.getAverageWordLength(), text.length(), sse.getWordsCount()));

			if (sse.getHighestPoints() <= move.getPoints()) {
				sse.setLastValuableWord(word);
			}

			if (sse.getLastLongestWord() == null || sse.getLastLongestWord().length() <= text.length()) {
				sse.setLastLongestWord(word);
			}
		} else if (playerMove instanceof ExchangeTilesMove) {
			sse.setExchangesCount(sse.getExchangesCount() + 1);
		}
	}
}
