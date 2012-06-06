package wisematches.playground.tournament.upcoming;

import wisematches.personality.Language;
import wisematches.personality.player.Player;
import wisematches.playground.search.SearchManager;
import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.TournamentSectionId;

import java.util.Collection;

/**
 * {@code TournamentSubscriptionManager} provides information about upcoming tournament and ability for subscription.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentSubscriptionManager extends SearchManager<TournamentRequest, TournamentSectionId> {
	void addTournamentRequestListener(TournamentRequestListener l);

	void removeTournamentRequestListener(TournamentRequestListener l);

	void addTournamentAnnouncementListener(TournamentAnnouncementListener l);

	void removeTournamentAnnouncementListener(TournamentAnnouncementListener l);

	/**
	 * Returns announced tournament.
	 *
	 * @return the announced tournament.
	 */
	TournamentAnnouncement getTournamentAnnouncement();

	/**
	 * Returns announcement by it's number.
	 *
	 * @param number the announcement's number
	 * @return announcement by specified number or {@code null} if there is announcement.
	 */
	TournamentAnnouncement getTournamentAnnouncement(int number);

	/**
	 * Subscribes specified player to announced tournament with specified parameters.
	 * <p/>
	 * If player already subscribed when he's section will be changed.
	 *
	 * @param announcement the number of the announcement. The number is used for user actions synchronization with
	 *                     current state of manager. If they don't equals {@code BadAnnouncementException} will be thrown.
	 * @param player       the player who would like take part in the tournament.
	 * @param language     the language of the tournament.
	 * @param section      the section of the tournament.
	 * @return created tournament request.
	 * @throws WrongAnnouncementException if specified announcement doesn't equals to specified.
	 * @throws WrongSectionException      if player can't be subscribed to specified section because has higher rating.
	 */
	TournamentRequest subscribe(int announcement, Player player, Language language, TournamentSection section) throws WrongAnnouncementException, WrongSectionException;

	/**
	 * Unsubscribes specified player from the tournament.
	 * <p/>
	 * If player is not subscribed nothing will happen.
	 *
	 * @param announcement the number of the announcement. The number is used for user actions synchronization with
	 *                     current state of manager. If they don't equals {@code BadAnnouncementException} will be thrown.
	 * @param player       the player who would like take part in the tournament.
	 * @param language     the language of the tournament.
	 * @return original request or {@code null} if player wasn't subscribed.
	 * @throws WrongAnnouncementException if specified announcement doesn't equals to specified.
	 */
	TournamentRequest unsubscribe(int announcement, Player player, Language language) throws WrongAnnouncementException;

	/**
	 * Returns current request for the player.
	 *
	 * @param announcement the number of the announcement. The number is used for user actions synchronization with
	 *                     current state of manager. If they don't equals {@code BadAnnouncementException} will be thrown.
	 * @param player       the player who's request should be returned.
	 * @param language     the language of the tournament.
	 * @return the player's request or {@code null} if plyaer is not subscribed to the tournament.
	 * @throws WrongAnnouncementException if specified announcement doesn't equals to specified.
	 */
	TournamentRequest getTournamentRequest(int announcement, Player player, Language language) throws WrongAnnouncementException;

	/**
	 * Returns all requests for the player. If there are no requests empty collection will be returned.
	 *
	 * @param announcement the number of the announcement. The number is used for user actions synchronization with
	 *                     current state of manager. If they don't equals {@code BadAnnouncementException} will be thrown.
	 * @param player       the player who's requests should be returned.
	 * @return collection of all active requests.
	 * @throws WrongAnnouncementException if specified announcement doesn't equals to specified.
	 */
	Collection<TournamentRequest> getTournamentRequests(int announcement, Player player) throws WrongAnnouncementException;
}