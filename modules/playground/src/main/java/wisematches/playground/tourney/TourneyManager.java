package wisematches.playground.tourney;

import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Personality;
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
public interface TourneyManager<S extends TourneySubscription> extends SearchManager<TourneyEntity, TourneyEntity.Id, SearchFilter> {
	void addTourneyItemListener(TourneyItemListener l);

	void removeTourneyItemListener(TourneyItemListener l);

	void addTourneySubscriptionListener(TourneySubscriptionListener<S> l);

	void removeTourneySubscriptionListener(TourneySubscriptionListener<S> l);


	boolean subscribe(S subscription) throws TourneySubscriptionException;

	boolean unsubscribe(S subscription) throws TourneySubscriptionException;


	<T extends TourneyEntity<T, I, ?>, I extends TourneyEntity.Id<T, I>> T getTournamentEntity(I id);

	<T extends TourneyEntity<T, I, C>, I extends TourneyEntity.Id<T, I>, C extends TourneyEntity.Context<T, C>> List<T> searchEntities(Personality person, C context, SearchFilter filter, Orders orders, Range range);


//	void addTournamentSubscriptionListener(TournamentSubscriptionListener l);
//
//	void removeTournamentSubscriptionListener(TournamentSubscriptionListener l);
//
//
//	/**
//	 * Subscribes specified player to the announcement.
//	 * <p/>
//	 * If player is not subscribed nothing will happen.
//	 *
//	 * @param announcement the number of a announcement. The number is used for user actions synchronization with
//	 *                     current state of manager. If they don't equals {@code BadAnnouncementException} will be thrown.
//	 * @param player       the player who would like take part in the announcement.
//	 * @param language     the language of the announcement.
//	 * @param section      the section to be subscribed to
//	 * @return original request or {@code null} if player wasn't subscribed.
//	 * @throws WrongTournamentException if specified announcement doesn't equals to specified.
//	 */
//	TournamentSubscription subscribe(int tournament, Player player, Language language, TournamentSection section) throws WrongTournamentException, WrongSectionException;
//
//	/**
//	 * Unsubscribes specified player from the announcement.
//	 * <p/>
//	 * If player is not subscribed nothing will happen.
//	 *
//	 * @param announcement the number of a announcement. The number is used for user actions synchronization with
//	 *                     current state of manager. If they don't equals {@code BadAnnouncementException} will be thrown.
//	 * @param player       the player who would like take part in the announcement.
//	 * @param language     the language of the announcement.
//	 * @param section      the section to be unsubscribed from
//	 * @return original request or {@code null} if player wasn't subscribed.
//	 * @throws WrongTournamentException if specified announcement doesn't equals to specified.
//	 */
//	TournamentSubscription unsubscribe(int tournament, Player player, Language language, TournamentSection section) throws WrongTournamentException;
//
//	@Override
//	int getTotalCount(Personality person, TournamentEntity.Id context);
//
//	@Override
//	int getFilteredCount(Personality person, TournamentEntity.Id context, SearchFilter filter);
//
//	@Override
//	List<TournamentEntity> searchEntities(Personality person, TournamentEntity.Id context, SearchFilter filter, Orders orders, Range range);
//
//
	//	<T extends TournamentEntity<T, ?>> T getTournamentEntity(TournamentEntity.Id entityId);
//
//	<T extends TournamentEntity<T, C>, C extends TournamentEntity.Context<T, C>> List<T> searchTournamentEntities(Personality person, C context, SearchFilter filter, Orders orders, Range range);
}
