package wisematches.playground.tourney;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneyWinner {
	/**
	 * Returns player id.
	 *
	 * @return the player id.
	 */
	long getPlayer();

	/**
	 * Returns player's place
	 *
	 * @return the player's place
	 */
	TourneyPlace getPlace();
}
