package wisematches.playground.tournament;

import wisematches.personality.player.Player;

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
	 * @param player	   player who subscribed to the tournament
	 * @param subscription subscription information
	 */
	void tournamentSubscribed(Tournament tournament, Player player, TournamentSubscription subscription);

	/**
	 * Indicates that tournament subscription has been canceled.
	 *
	 * @param tournament   the tournament
	 * @param player	   player who unsubscribed from the tournament
	 * @param subscription the subscription information.
	 */
	void tournamentUnsubscribed(Tournament tournament, Player player, TournamentSubscription subscription);
}
