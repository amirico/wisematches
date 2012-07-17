package wisematches.playground.tournament;

import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.personality.player.Player;
import wisematches.playground.search.SearchFilter;
import wisematches.playground.search.SearchManager;

import java.util.List;

/**
 * {@code TournamentManager} provides access to active tournaments/round and group.
 * <p/>
 * You can access announcement and rounds directly via the manager but groups are available only through
 * the {@code TournamentSearchManager} because there are a lot of groups and games in one announcement.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentManager extends SearchManager<TournamentEntity, TournamentEntityContext, SearchFilter> {
	void addTournamentStateListener(TournamentStateListener l);

	void removeTournamentStateListener(TournamentStateListener l);


	void addTournamentSubscriptionListener(TournamentSubscriptionListener l);

	void removeTournamentSubscriptionListener(TournamentSubscriptionListener l);


	/**
	 * Returns next scheduled announcement or {@code null} if there is no scheduled tournaments.
	 *
	 * @return the next scheduled announcement or {@code null} if there is no scheduled tournaments.
	 */
	Announcement getAnnouncement();

	/**
	 * Subscribes specified player to the announcement.
	 * <p/>
	 * If player is not subscribed nothing will happen.
	 *
	 * @param announcement the number of a announcement. The number is used for user actions synchronization with
	 *                     current state of manager. If they don't equals {@code BadAnnouncementException} will be thrown.
	 * @param player       the player who would like take part in the announcement.
	 * @param language     the language of the announcement.
	 * @param section      the section to be subscribed to
	 * @return original request or {@code null} if player wasn't subscribed.
	 * @throws WrongTournamentException if specified announcement doesn't equals to specified.
	 */
	TournamentSubscription subscribe(int announcement, Player player, Language language, TournamentSection section) throws WrongTournamentException, WrongSectionException;

	/**
	 * Unsubscribes specified player from the announcement.
	 * <p/>
	 * If player is not subscribed nothing will happen.
	 *
	 * @param announcement the number of a announcement. The number is used for user actions synchronization with
	 *                     current state of manager. If they don't equals {@code BadAnnouncementException} will be thrown.
	 * @param player       the player who would like take part in the announcement.
	 * @param language     the language of the announcement.
	 * @return original request or {@code null} if player wasn't subscribed.
	 * @throws WrongTournamentException if specified announcement doesn't equals to specified.
	 */
	TournamentSubscription unsubscribe(int announcement, Player player, Language language) throws WrongTournamentException;


	<T extends TournamentEntity> T getTournamentEntity(TournamentEntityId<T> id);

	<T extends TournamentEntity> List<T> searchTournamentEntities(Personality person, TournamentEntityContext<T> context, SearchFilter filter, Orders orders, Range range);
}
