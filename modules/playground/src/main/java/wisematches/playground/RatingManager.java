package wisematches.playground;

import wisematches.personality.Personality;

import java.util.List;

/**
 * Ratings manager is a interface that allows you get current player's rating and calculate
 * new rating (or possible new rating) for players.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RatingManager {
	/**
	 * Returns current player's rating.
	 *
	 * @param person the player who's rating must be returned.
	 * @return the player's rating.
	 * @throws IllegalArgumentException if specified personality out of this manager.
	 */
	short getRating(Personality person);

	/**
	 * Calculates rating based on specified hands.
	 *
	 * @param hands player hands which should be used for rating calculation.
	 * @return list of rating changes for each hand.
	 */
	List<GameRatingChange> calculateRatings(List<? extends GamePlayerHand> hands);
}
