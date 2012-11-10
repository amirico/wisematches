package wisematches.playground.tourney.regular;

import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.scheduling.BreakingDayListener;
import wisematches.playground.tourney.TourneyEntityManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface RegularTourneyManager extends TourneyEntityManager<RegularTourneyEntity>, BreakingDayListener {
    void addRegularTourneyListener(RegularTourneyListener l);

    void removeRegularTourneyListener(RegularTourneyListener l);


    void addTourneySubscriptionListener(TourneySubscriptionListener l);

    void removeTourneySubscriptionListener(TourneySubscriptionListener l);


    TourneySubscription subscribe(Tourney tourney, Personality player, Language language, TourneySection section) throws TourneySubscriptionException;

    TourneySubscription unsubscribe(Tourney tourney, Personality player, Language language, TourneySection section) throws TourneySubscriptionException;


    TourneySubscription getSubscription(Tourney tourney, Personality player);

    TourneySubscriptions getSubscriptions(Tourney tourney);
}
