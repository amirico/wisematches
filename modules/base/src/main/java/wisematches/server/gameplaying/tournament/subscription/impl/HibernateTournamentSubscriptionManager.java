/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.gameplaying.tournament.subscription.impl;

import wisematches.server.gameplaying.tournament.Tournament;
import wisematches.server.gameplaying.tournament.TournamentSection;
import wisematches.server.gameplaying.tournament.subscription.TournamentSubscription;
import wisematches.server.gameplaying.tournament.subscription.TournamentSubscriptionException;
import wisematches.server.gameplaying.tournament.subscription.TournamentSubscriptionManager;
import wisematches.server.player.Language;
import wisematches.server.player.Player;

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
