package wisematches.playground.scribble.tracking;

import wisematches.personality.Personality;
import wisematches.playground.*;
import wisematches.playground.scribble.ExchangeTilesMove;
import wisematches.playground.scribble.MakeWordMove;
import wisematches.playground.scribble.Word;
import wisematches.playground.tracking.StatisticsTrapper;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleStatisticsTrapper extends StatisticsTrapper<ScribbleStatisticsEditor> {
	public ScribbleStatisticsTrapper() {
		super(ScribbleStatisticsEditor.class);
	}

	@Override
	public ScribbleStatisticsEditor createStatisticsEditor(Personality person) {
		return new ScribbleStatisticsEditor(person);
	}

	@Override
	public void trapGameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move, ScribbleStatisticsEditor editor) {
		super.trapGameMoveDone(board, move, editor);

		final PlayerMove playerMove = move.getPlayerMove();
		if (playerMove instanceof MakeWordMove) {
			final MakeWordMove wordMove = (MakeWordMove) playerMove;
			editor.setWordsCount(editor.getWordsCount() + 1);

			final Word word = wordMove.getWord();
			final String text = word.getText();
			editor.setAverageWordLength(average(editor.getAverageWordLength(), text.length(), editor.getWordsCount()));

			if (editor.getHighestPoints() <= move.getPoints()) {
				editor.setLastValuableWord(word);
			}

			if (editor.getLastLongestWord() == null || editor.getLastLongestWord().length() <= text.length()) {
				editor.setLastLongestWord(word);
			}
		} else if (playerMove instanceof ExchangeTilesMove) {
			editor.setExchangesCount(editor.getExchangesCount() + 1);
		}
	}
}
