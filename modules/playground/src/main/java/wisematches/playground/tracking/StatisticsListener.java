package wisematches.playground.tracking;

import wisematches.personality.Personality;

import java.util.Set;

/**
 * {@code PlayerStatisticListener} notifies clients that player statistic was changed.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface StatisticsListener {
	/**
	 * Indicates that player statistic was updated.
	 * <p/>
	 * This method does not use {@code Player} object because {@code StatisticManager} operates only
	 * with {@code Personality} and it's not optimal to load {@code Player} object.
	 *
	 * @param personality the id of player who's statistic was updated.
	 * @param changes     set of changed properties.
	 * @param statistic   the statistic that contains updated data.
	 */
	void playerStatisticUpdated(Personality personality, Set<String> changes, Statistics statistic);
}
