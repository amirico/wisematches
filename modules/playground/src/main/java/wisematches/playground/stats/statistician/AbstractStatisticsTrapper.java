package wisematches.playground.stats.statistician;

import wisematches.playground.GameBoard;
import wisematches.playground.GameMove;
import wisematches.playground.GamePlayerHand;
import wisematches.playground.GameSettings;
import wisematches.playground.tracking.RatingChanges;
import wisematches.playground.tracking.StatisticsEditor;
import wisematches.playground.tracking.StatisticsTrapper;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractStatisticsTrapper implements StatisticsTrapper {
	public AbstractStatisticsTrapper() {
	}

	@Override
	public void trapGameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, StatisticsEditor editor) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void trapGameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move, StatisticsEditor editor) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void trapGameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, RatingChanges changes, StatisticsEditor editor) {
		throw new UnsupportedOperationException("Not implemented");
	}

	protected int average(final int previousAverage, final int newValue, final int newCount) {
		return (previousAverage * (newCount - 1) + newValue) / newCount;
	}
}
