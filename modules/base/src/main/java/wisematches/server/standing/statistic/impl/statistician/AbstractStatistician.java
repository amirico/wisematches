package wisematches.server.standing.statistic.impl.statistician;

import wisematches.server.standing.statistic.statistician.PlayerStatistician;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AbstractStatistician implements PlayerStatistician {
	public AbstractStatistician() {
	}

	protected int average(final int previousAverage, final int newValue, final int newCount) {
		return (previousAverage * (newCount - 1) + newValue) / newCount;
	}
}
