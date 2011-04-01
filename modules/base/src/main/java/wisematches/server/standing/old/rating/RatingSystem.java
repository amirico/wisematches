package wisematches.server.standing.old.rating;


import wisematches.server.personality.Personality;

/**
 * Rating System is implementation of rating system that allows calculates rating of players
 * when game was finished.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface RatingSystem {
	/**
	 * Calculates rating of specified players. This method returns array of new ratings for each
	 * player in specified {@code players} array.
	 *
	 * @param players the players who ratings should be recalculated.
	 * @param points  the players points for played game.
	 * @return the array of new ratings for each specified player.
	 * @throws NullPointerException	 if {@code players } or {@code points} arrays are {@code null}
	 *                                  or any of {@code players} element is {@code null}
	 * @throws IllegalArgumentException if length of {@code points} array is not equals with
	 *                                  {@code players} array length or any of {@code points} element is less than zero.
	 */
	int[] calculateRatings(Personality[] players, int[] points);
}
