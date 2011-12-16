package wisematches.playground.tournament.scheduler;

import wisematches.personality.Language;
import wisematches.personality.player.Player;
import wisematches.playground.tournament.TournamentSection;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentScheduleManager {
	void addTournamentTicketListener(TournamentTicketListener l);

	void removeTournamentTicketListener(TournamentTicketListener l);

	/**
	 * Returns information about next tournament.
	 *
	 * @return the information about next tournament: number and scheduled date.
	 */
	TournamentPoster getTournamentPoster();

	/**
	 * Subscribes to next tournament for specified language and section.
	 * <p/>
	 * If the player already subscribed to the tournament with a language, he's section
	 * will be changed otherwise new subscription will be created.
	 *
	 * @param player   the player who wants subscribe to the tournament.
	 * @param language the language of the tournament
	 * @param section  the subscribing section
	 */
	void buyTicket(Player player, Language language, TournamentSection section);

	/**
	 * Unsubscribes a player from the tournament.
	 *
	 * @param player   the player who should be unsubscribed
	 * @param language the language of the tournament
	 */
	void sellTicket(Player player, Language language);

	/**
	 * Returns all tickets for specified player.
	 *
	 * @param player the player who's tickets should be returned.
	 * @return the collection of all tickets or empty collection of player is not subscribed to any.
	 */
	Collection<TournamentTicket> getPlayerTickets(Player player);

	/**
	 * Returns player's ticket the specified section of tournament with specified language.
	 *
	 * @param player   the player who's ticket should be returned
	 * @param language the language of the tournament
	 * @return the player's ticket or {@code null} if player is not subscribed to the tournament.
	 */
	TournamentTicket getPlayerTicket(Player player, Language language);
}
