package wisematches.server.standing.statistic;

import wisematches.server.personality.Personality;
import wisematches.server.standing.statistic.impl.HibernatePlayerStatistic;

/**
 * Statistics manager allows get statistic for specified player. This manager can use database or any
 * other manager for collectiong data.
 * <p/>
 * TODO: this is not correct interface. It blocks {@code PlayerStatistic} for each call of {@code getPlayerStatistic}
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PlayerStatisticManager {
	void addPlayerStatisticListener(PlayerStatisticListener l);

	void removePlayerStatisticListener(PlayerStatisticListener l);

	/**
	 * Returns statistic for specified player.
	 *
	 * @param personality the player id.
	 * @return the player statistic.
	 */
	HibernatePlayerStatistic getPlayerStatistic(Personality personality);
}
