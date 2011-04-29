/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.playground.tournament.subscription.impl;

import wisematches.server.playground.tournament.Tournament;
import wisematches.server.playground.tournament.TournamentSection;
import wisematches.server.playground.tournament.subscription.TournamentSubscription;
import wisematches.server.playground.tournament.subscription.TournamentSubscriptionException;
import wisematches.server.playground.tournament.subscription.TournamentSubscriptionManager;
import wisematches.server.personality.Personality;
import wisematches.server.personality.account.Language;

/**
 * TODO: write description here
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 * @version $Id:  $
 */
public class HibernateTournamentSubscriptionManager implements TournamentSubscriptionManager {
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
