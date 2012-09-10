package wisematches.playground.tourney;

/**
 * The tournament request is player's application for upcoming tournament. It contains
 * player, section and language.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneySubscription<
		T extends TourneySubscription<T, I, C>,
		I extends TourneyEntity.Id<T, I>,
		C extends TourneyEntity.Context<T, C>> extends TourneyEntity<T, I, C> {
	/**
	 * Returns player id who sent the request
	 *
	 * @return the player id who sent the request
	 */
	long getPlayer();

	/**
	 * Returns number of a tournament.
	 *
	 * @return the number of a tournament.
	 */
	int getTournament();
}
