package wisematches.playground.tourney;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TourneySubscriptionListener<T extends TourneySubscription> {
	/**
	 * Indicates that new player subscribed to a tournament.
	 *
	 * @param subscription original player's subscription.
	 */
	void playerSubscribed(T subscription);

	/**
	 * Indicates that new a unsubscribed from a tournament.
	 *
	 * @param subscription original player's subscription.
	 */
	void playerUnsubscribed(T subscription);
}
