package wisematches.server.standing.rating;

import wisematches.server.gameplaying.board.GameBoard;
import wisematches.server.personality.Personality;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface PlayerRatingManager {
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
	 * Returns current player's rating.
	 *
	 * @param person the player who's rating must be returned.
	 * @return the player's rating.
	 */
	short getRating(Personality person);

	/**
	 * Returns position of player in ratings table that is sorted by {@code SortType.DESC}. To get position
	 * of player in table sorted by {@code SortType.ASC} user following construction:
	 * {@code getPlayersCount() - getPlayerPosition(long)}.
	 *
	 * @param player the player id how position should be returned.
	 * @return the player's position in ratings table starting from 1 or {@code 0} if player is unknown.
	 */
	long getPosition(Personality player);

	/**
	 * @param player
	 * @param board
	 * @return
	 */
	RatingChange getRatingChange(Personality player, GameBoard board);

	/**
	 * @param board
	 * @return
	 */
	RatingChange[] getBoardRatings(GameBoard board);

	/**
	 * Returns rating history for specified player.
	 *
	 * @param player the player who's rating history should be returned.
	 * @return the rating changes for specified player.
	 */
	RatingHistory getRatingHistory(Personality player);
}
