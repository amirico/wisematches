package wisematches.playground.tourney.regular;

import wisematches.personality.Language;
import wisematches.playground.timer.BreakingDayListener;
import wisematches.playground.tourney.TourneyEntityManager;

import java.util.Date;

/**
 * TODO: remove  BreakingDayListener from this interface
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RegularTourneyManager extends TourneyEntityManager<RegularTourneyEntity>, BreakingDayListener {
	void addRegularTourneyListener(RegularTourneyListener l);

	void removeRegularTourneyListener(RegularTourneyListener l);


	void addTourneySubscriptionListener(TourneySubscriptionListener l);

	void removeTourneySubscriptionListener(TourneySubscriptionListener l);


	TourneySubscription subscribe(int tourney, long player, Language language, TourneySection section) throws TourneySubscriptionException;

	TourneySubscription unsubscribe(int tourney, long player, Language language, TourneySection section) throws TourneySubscriptionException;


	TourneySubscription getSubscription(int tourney, long player);

	TourneySubscriptions getSubscriptionStatus(int tourney);


	/**
	 * TODO: for testing only!
	 */
	RegularTourney startRegularTourney(Date scheduledDate);
}
