package wisematches.playground.tournament.upcoming;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentRequestListener {
	/**
	 * Indicates that new player subscribed to a tournament.
	 *
	 * @param request original player's request.
	 */
	void playerSubscribed(TournamentRequest request);

	/**
	 * Indicates that new a unsubscribed from a tournament.
	 *
	 * @param request original player's request.
	 */
	void playerUnsubscribed(TournamentRequest request);
}
