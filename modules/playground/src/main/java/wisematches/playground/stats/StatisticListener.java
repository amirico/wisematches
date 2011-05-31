package wisematches.playground.stats;

import wisematches.personality.Personality;

/**
 * {@code PlayerStatisticListener} notifies clients that player statistic was changed.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface StatisticListener {
	/**
	 * Indicates that player statistic was updated.
	 * <p/>
	 * This method does not use {@code Player} object because {@code StatisticManager} operates only
	 * with {@code Personality} and it's not optimal to load {@code Player} object.
	 *
	 * @param personality the id of player who's statistic was updated.
	 * @param statistic   the statistic that contains updated data.
	 */
	void playerStatisticUpdated(Personality personality, PlayerStatistic statistic);
}
