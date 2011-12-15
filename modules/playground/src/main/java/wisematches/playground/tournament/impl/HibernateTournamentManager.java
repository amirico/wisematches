package wisematches.playground.tournament.impl;

import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.tournament.*;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTournamentManager implements TournamentManager {
	public HibernateTournamentManager() {
	}

	@Override
	public void addTournamentListener(TournamentListener l) {
	}

	@Override
	public void removeTournamentListener(TournamentListener l) {
	}

	@Override
	public void addTournamentSubscriptionListener(TournamentSubscriptionListener l) {
	}

	@Override
	public void removeTournamentSubscriptionListener(TournamentSubscriptionListener l) {
	}

	@Override
	public Tournament getAnnouncedTournament() {
		return null;
	}

	@Override
	public void subscribe(Personality player, Language language, TournamentSection tournamentSection) throws TournamentSubscriptionException {
	}

	@Override
	public void unsubscribe(Personality player, Language language) throws TournamentSubscriptionException {
	}

	@Override
	public TournamentSection getSubscribedSection(Personality player, Language language) {
		return null;
	}

	@Override
	public Collection<TournamentSubscription> getSubscriptions(Personality player) {
		return null;
	}
}
