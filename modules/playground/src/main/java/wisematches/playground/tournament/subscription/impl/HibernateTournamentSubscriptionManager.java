/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.playground.tournament.subscription.impl;

import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.tournament.Tournament;
import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.subscription.TournamentSubscription;
import wisematches.playground.tournament.subscription.TournamentSubscriptionException;
import wisematches.playground.tournament.subscription.TournamentSubscriptionManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTournamentSubscriptionManager implements TournamentSubscriptionManager {
	public HibernateTournamentSubscriptionManager() {
	}

	@Override
	public Tournament getAnnouncedTournament() {
		return null;
	}

	@Override
	public void subscribe(Personality player, TournamentSection tournamentSection, Language language) throws TournamentSubscriptionException {
	}

	@Override
	public void unsubscribe(Personality player) throws TournamentSubscriptionException {
	}

	@Override
	public boolean isSubscribed(Personality player) {
		return false;
	}

	@Override
	public TournamentSubscription getSubscription(Personality player) {
		return null;
	}
}
