package wisematches.server.playground.scribble.statistic;

import wisematches.server.playground.scribble.Word;
import wisematches.server.standing.statistic.MovesStatistic;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface ScribbleMovesStatistic extends MovesStatistic {
	int getAverageWordLength();

	Word getLastLongestWord();

	Word getLastValuableWord();
}
