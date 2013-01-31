package wisematches.playground.tourney;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneyWinner {
	/**
	 * Returns player.
	 *
	 * @return the player.
	 */
	Long getPlayer();

	/**
	 * Returns player's place
	 *
	 * @return the player's place
	 */
	TourneyPlace getPlace();
}
