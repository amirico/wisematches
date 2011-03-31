package wisematches.server.standing.old.rating;

import wisematches.server.player.Player;

import java.util.List;

/**
 * {@code RatingsManager} allows get ratings of players: top rating, full rating and so on.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface PlayerRatingsManager {
	/**
	 * Adds ratings listener.
	 *
	 * @param l the ratings listener to be added.
	 */
	void addRatingsChangeListener(PlayerRatingListener l);

	/**
	 * Removes ratings listener.
	 *
	 * @param l the ratings listener to be removed.
	 */
	void removeRatingsChangeListener(PlayerRatingListener l);

	/**
	 * Adds top players listener.
	 *
	 * @param l the top players listener to be added.
	 */
	void addTopPlayersListener(TopPlayersListener l);

	/**
	 * Removes top players listener.
	 *
	 * @param l the top players listener to be removed.
	 */
	void removeTopPlayersListener(TopPlayersListener l);


	/**
	 * Returns number of all players that is included into ratings table.
	 *
	 * @return the number of players in rating table.
	 */
	long getPlayersCount();

	/**
	 * Returns position of player in ratings table that is sorted by {@code SortType.DESC}. To get position
	 * of player in table sorted by {@code SortType.ASC} user following construction:
	 * {@code getPlayersCount() - getPlayerPosition(long)}.
	 *
	 * @param playerId the player id how position should be returned.
	 * @return the player's position in ratings table starting from 1 or {@code 0} if player is unknown.
	 * @see SortType#DESC
	 */
	long getPlayerPosition(long playerId);


	/**
	 * Returns unmodifiable collection of top rated players. Number of top rated player predefined
	 * by ratings manager realization.
	 *
	 * @return unmodifiable collection of top rated players.
	 */
	List<Player> getTopRatedPlayers();

	/**
	 * Returns ratings of players by specified parameters.
	 * <p/>
	 * This method should not be used to get top players because it's not optimazed method and can be very hardly.
	 * To get top players use {@link #getTopRatedPlayers()}  instead.
	 *
	 * @param fromPosition the start position in rating table
	 * @param playersCount the number of players that should be returned.
	 * @param sortType	 the type of sort operation.
	 * @return the collection of players sorted by it's rating from specified position and not
	 *         more that specified {@code playersCount}. The returned collection will be sorted by
	 *         specified sort type.
	 * @see #getTopRatedPlayers()
	 */
	List<Player> getPlayersRating(long fromPosition, int playersCount, SortType sortType);

	/**
	 * Type of sort
	 */
	public enum SortType {
		ASC("asc"),
		DESC("desc");

		private final String sqlStatement;

		SortType(String sqlStatement) {
			this.sqlStatement = sqlStatement;
		}

		public String sqlStatement() {
			return sqlStatement;
		}
	}
}
