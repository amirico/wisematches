package wisematches.playground.scribble.tracking;

import wisematches.playground.scribble.Word;
import wisematches.playground.tracking.Statistics;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface ScribbleStatistics extends Statistics {
	int getWordsCount();


	int getPassesCount();

	int getExchangesCount();


	Word getLastLongestWord();

	Word getLastValuableWord();


	int getAllHandTilesBonuses();

	float getAverageWordLength();
}
