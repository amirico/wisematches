package wisematches.playground.tourney.regular;

import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.search.SearchFilter;
import wisematches.playground.search.SearchManager;
import wisematches.playground.tourney.TourneyEntityManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RegularTourneyManager extends TourneyEntityManager<RegularTourneyEntity> {
	void addRegularTourneyListener(RegularTourneyListener l);

	void removeRegularTourneyListener(RegularTourneyListener l);


	void addTourneySubscriptionListener(TourneySubscriptionListener l);

	void removeTourneySubscriptionListener(TourneySubscriptionListener l);


	TourneySubscription subscribe(Tourney tourney, Personality player, Language language, TourneySection section) throws TourneySubscriptionException;

	TourneySubscription unsubscribe(Tourney tourney, Personality player, Language language, TourneySection section) throws TourneySubscriptionException;


	TourneySubscription getSubscription(Tourney tourney, Personality player);

	TourneySubscriptions getSubscriptions(Tourney tourney);


	/**
	 * Returns search manager that allows search players who is not registered to a tourney.
	 *
	 * @return the search manager that allows search players who is not registered to a tourney.
	 */
	SearchManager<Long, Tourney.Id, SearchFilter> getUnregisteredPlayersSearch();
}
