package wisematches.playground.tournament;

/**
 * {@code TournamentSubscriptionListener} allows get notification about new subscriptions
 * for announced tournament.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentSubscriptionListener {
	/**
	 * Indicates that new subscription has been received.
	 *
	 * @param tournament   subscribed tournament
	 * @param subscription subscription information
	 */
	void tournamentSubscribed(Tournament tournament, TournamentSubscription subscription);

	/**
	 * Indicates that tournament subscription has been canceled.
	 *
	 * @param tournament   the tournament
	 * @param subscription the subscription information.
	 */
	void tournamentUnsubscribed(Tournament tournament, TournamentSubscription subscription);
}
