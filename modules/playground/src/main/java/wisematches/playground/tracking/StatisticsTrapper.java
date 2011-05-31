package wisematches.playground.tracking;

import wisematches.personality.Personality;
import wisematches.playground.GameBoard;
import wisematches.playground.GameMove;
import wisematches.playground.GamePlayerHand;
import wisematches.playground.GameSettings;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface StatisticsTrapper {
	Class<? extends StatisticsEditor> getStatisticType();

	StatisticsEditor createStatisticsEditor(Personality person);


	void trapGameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, StatisticsEditor editor);

	void trapGameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move, StatisticsEditor editor);

	void trapGameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, RatingChanges changes, StatisticsEditor editor);
}
