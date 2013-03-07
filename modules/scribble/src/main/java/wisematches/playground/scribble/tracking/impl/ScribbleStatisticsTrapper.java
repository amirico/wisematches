package wisematches.playground.scribble.tracking.impl;

import wisematches.core.Player;
import wisematches.playground.*;
import wisematches.playground.scribble.*;
import wisematches.playground.tracking.impl.StatisticsTrapper;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleStatisticsTrapper extends StatisticsTrapper<ScribbleStatisticsEditor> {
	public ScribbleStatisticsTrapper() {
	}

	@Override
	public void trapGameMoveDone(Player player, ScribbleStatisticsEditor editor, GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move, GameMoveScore score) {
		super.trapGameMoveDone(player, editor, board, move, score);

		if (move instanceof PassTurn) {
			editor.setPassesCount(editor.getPassesCount() + 1);
		}

		if (move instanceof MakeTurn) {
			final MakeTurn wordMove = (MakeTurn) move;
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

			if (score != null && score instanceof ScribbleMoveScore) {
				final ScribbleMoveScore sms = (ScribbleMoveScore) score;
				if (sms.isAllFromHand()) {
					editor.setAllHandTilesBonuses(editor.getAllHandTilesBonuses() + 1);
				}
			}
		} else if (move instanceof ExchangeTiles) {
			editor.setExchangesCount(editor.getExchangesCount() + 1);
		}
	}
}
