package wisematches.server.testimonial.statistic;

/**
 * Statistics manager allows get statistic for specified player. This manager can use database or any
 * other manager for collectiong data.
 * <p/>
 * TODO: this is not correct interface. It blocks {@code PlayerStatistic} for each call of {@code getPlayerStatistic}
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PlayerStatisticsManager {
	void addPlayerStatisticListener(PlayerStatisticListener l);

	void removePlayerStatisticListener(PlayerStatisticListener l);

	/**
	 * Returns statistic for specified player.
	 *
	 * @param playerId the player id.
	 * @return the player statistic.
	 */
	PlayerStatistic getPlayerStatistic(long playerId);

	/**
	 * Updates previously loaded statistic.
	 *
	 * @param statistic the statistic to be updated.
	 * @throws NullPointerException	 if statistic is null
	 * @throws IllegalArgumentException if specified statistic is not belongs to this manager.
	 */
	void updatePlayerStatistic(PlayerStatistic statistic);

	/**
	 * Locks player statistic for specified player. If statistic is locked when
	 * only locker can call {@code getPlayerStatistic}. Other threads will blocked while
	 * statistic is locked.
	 *
	 * @param playerId the player id which statistic should be locked.
	 * @see #getPlayerStatistic(long)
	 */
	void lockPlayerStatistic(long playerId);

	void unlockPlayerStatistic(long playerId);
}
