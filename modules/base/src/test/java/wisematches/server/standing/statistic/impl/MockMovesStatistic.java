package wisematches.server.standing.statistic.impl;

import wisematches.server.standing.statistic.MovesStatistic;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface MockMovesStatistic extends MovesStatistic {
	int getAverageWordLength();

	String getLastLongestWord();

	String getLastValuableWord();
}
