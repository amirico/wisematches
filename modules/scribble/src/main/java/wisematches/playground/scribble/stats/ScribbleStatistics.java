package wisematches.playground.scribble.stats;

import wisematches.playground.scribble.Word;
import wisematches.playground.tracking.Statistics;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface ScribbleStatistics extends Statistics {
	int getWordsCount();

	int getExchangesCount();

	int getAverageWordLength();

	Word getLastLongestWord();

	Word getLastValuableWord();
}
