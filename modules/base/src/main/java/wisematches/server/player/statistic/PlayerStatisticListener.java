package wisematches.server.player.statistic;

/**
 * {@code PlayerStatisticListener} notifies clients that player statistic was changed.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PlayerStatisticListener {
	/**
	 * Indicates that player statistic was updated.
	 * <p/>
	 * This method does not user {@code Player} object because {@code StatisticManager} operates only
	 * with playerId and it's not oprimale to load {@code Player} object.
	 *
	 * @param playerId  the id of player whos statistic was updated.
	 * @param statistic the statistic that contains updated data.
	 */
	void playerStatisticUpdated(long playerId, PlayerStatistic statistic);
}
