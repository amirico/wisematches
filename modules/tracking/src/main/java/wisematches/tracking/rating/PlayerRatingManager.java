package wisematches.tracking.rating;

import wisematches.server.personality.Personality;
import wisematches.server.playground.board.GameBoard;

import java.util.Collection;
import java.util.Date;

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
	 * @throws IllegalArgumentException if specified personality out of this manager.
	 */
	short getRating(Personality person);

	/**
	 * Returns position of player in ratings table that is sorted by {@code SortType.DESC}. To get position
	 * of player in table sorted by {@code SortType.ASC} user following construction:
	 * {@code getPlayersCount() - getPlayerPosition(long)}.
	 *
	 * @param player the player id how position should be returned.
	 * @return the player's position in ratings table starting from 1 or {@code 0} if player is unknown.
	 * @throws IllegalArgumentException if specified personality out of this manager.
	 */
	long getPosition(Personality player);

	/**
	 * Returns collection of all changes for specified board.
	 *
	 * @param board the board
	 * @return collection of rating changes or null if board doesn't exist or not finished yet.
	 */
	Collection<RatingChange> getRatingChanges(GameBoard board);

	/**
	 * Returns rating curve container that contains information about all rating changes for specified player
	 * for specified dates with specified resolution.
	 *
	 * @param player	 the player who's curve should be returned.
	 * @param resolution the curve resolution. Indicates how many days must be grouped for one point. It's not possible
	 *                   to get curve with resolution less that one day at this moment.
	 * @param startDate  startDate date. If null all range will be used.
	 * @param endDate	endDate date. If null today will be used
	 * @return the rating curve.
	 * @throws IllegalArgumentException if resolution if zero or negative.
	 * @throws NullPointerException	 if {@code player} is null
	 */
	RatingCurve getRatingCurve(Personality player, int resolution, Date startDate, Date endDate);
}
