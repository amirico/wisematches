package wisematches.playground.tourney.regular;

import wisematches.personality.Language;
import wisematches.playground.tourney.TourneyEntityManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RegularTourneyManager extends TourneyEntityManager<RegularTourneyEntity> {
	void addRegularTourneyListener(RegularTourneyListener l);

	void removeRegularTourneyListener(RegularTourneyListener l);


	void addTourneySubscriptionListener(TourneySubscriptionListener l);

	void removeTourneySubscriptionListener(TourneySubscriptionListener l);


	TourneySubscription subscribe(int tourney, long player, Language language, TourneySection section) throws TourneySubscriptionException;

	TourneySubscription unsubscribe(int tourney, long player, Language language, TourneySection section) throws TourneySubscriptionException;


	TourneySubscriptionStatus getSubscriptionStatus();

	TourneySubscription getSubscription(int tourney, long player);
}
