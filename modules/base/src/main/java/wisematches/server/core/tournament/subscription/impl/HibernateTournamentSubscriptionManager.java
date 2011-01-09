/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.core.tournament.subscription.impl;

import wisematches.kernel.player.Player;
import wisematches.kernel.util.Language;
import wisematches.server.core.tournament.Tournament;
import wisematches.server.core.tournament.TournamentSection;
import wisematches.server.core.tournament.subscription.TournamentSubscription;
import wisematches.server.core.tournament.subscription.TournamentSubscriptionException;
import wisematches.server.core.tournament.subscription.TournamentSubscriptionManager;

/**
 * TODO: write description here
 *
 * @author klimese
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
