package wisematches.playground.scribble.tracking;

import wisematches.core.Personality;
import wisematches.playground.*;
import wisematches.playground.scribble.ExchangeMove;
import wisematches.playground.scribble.MakeTurn;
import wisematches.playground.scribble.ScribbleMoveScore;
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
	public void trapGameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move, GameMoveScore moveScore, ScribbleStatisticsEditor editor) {
		super.trapGameMoveDone(board, move, moveScore, editor);

		final PlayerMove playerMove = move.getPlayerMove();
		if (playerMove instanceof MakeTurn) {
			final MakeTurn wordMove = (MakeTurn) playerMove;
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

			if (moveScore != null && moveScore instanceof ScribbleMoveScore) {
				final ScribbleMoveScore sms = (ScribbleMoveScore) moveScore;
				if (sms.isAllFromHand()) {
					editor.setAllHandTilesBonuses(editor.getAllHandTilesBonuses() + 1);
				}
			}
		} else if (playerMove instanceof ExchangeMove) {
			editor.setExchangesCount(editor.getExchangesCount() + 1);
		}
	}
}
