/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.tournament.subscription.impl;

import wisematches.kernel.player.Player;
import wisematches.server.player.Language;
import wisematches.server.tournament.Tournament;
import wisematches.server.tournament.TournamentSection;
import wisematches.server.tournament.subscription.TournamentSubscription;
import wisematches.server.tournament.subscription.TournamentSubscriptionException;
import wisematches.server.tournament.subscription.TournamentSubscriptionManager;

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
	public void subscribe(Player player, TournamentSection tournamentSection, Language language) throws TournamentSubscriptionException {
	}

	@Override
	public void unsubscribe(Player player) throws TournamentSubscriptionException {
	}

	@Override
	public boolean isSubscribed(Player player) {
		return false;
	}

	@Override
	public TournamentSubscription getSubscription(Player player) {
		return null;
	}
}
