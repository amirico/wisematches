package wisematches.playground.scribble.tracking;

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
