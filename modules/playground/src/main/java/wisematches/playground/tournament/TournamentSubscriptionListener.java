package wisematches.playground.tournament;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentSubscriptionListener {
	/**
	 * Indicates that new player subscribed to a tournament.
	 *
	 * @param subscription original player's subscription.
	 */
	void playerSubscribed(TournamentSubscription subscription);

	/**
	 * Indicates that new a unsubscribed from a tournament.
	 *
	 * @param subscription original player's subscription.
	 */
	void playerUnsubscribed(TournamentSubscription subscription);
}
