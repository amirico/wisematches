package wisematches.playground.scribble.tracking;

import wisematches.playground.scribble.Word;
import wisematches.playground.tracking.Statistics;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface ScribbleStatistics extends Statistics {
	int getWordsCount();

	int getExchangesCount();

	/**
	 * Returns number of 'all hand tiles' bonuses.
	 *
	 * @return number of 'all hand tiles' bonuses.
	 */
	int getAllHandTilesBonuses();

	int getAverageWordLength();

	Word getLastLongestWord();

	Word getLastValuableWord();
}
