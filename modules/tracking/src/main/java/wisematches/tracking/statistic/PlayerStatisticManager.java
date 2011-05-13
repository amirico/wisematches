package wisematches.tracking.statistic;

import wisematches.server.personality.Personality;

/**
 * Statistics manager allows get statistic for specified player. This manager can use database or any
 * other manager for collecting data.
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
	PlayerStatistic getPlayerStatistic(Personality personality);
}
