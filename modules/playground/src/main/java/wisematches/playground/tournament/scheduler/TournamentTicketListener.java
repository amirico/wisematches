package wisematches.playground.tournament.scheduler;

import wisematches.personality.player.Player;

/**
 * {@code TournamentTicketListener} allows get notification about new subscriptions
 * for announced tournament.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentTicketListener {
	/**
	 * Indicates that new ticket has been received.
	 *
	 * @param poster subscribed tournament
	 * @param player player who subscribed to the tournament
	 * @param ticket ticket information
	 */
	void tournamentTicketBought(TournamentPoster poster, Player player, TournamentTicket ticket);

	/**
	 * Indicates that tournament ticket has been canceled.
	 *
	 * @param poster the tournament
	 * @param player player who unsubscribed from the tournament
	 * @param ticket the ticket information.
	 */
	void tournamentTicketSold(TournamentPoster poster, Player player, TournamentTicket ticket);
}
