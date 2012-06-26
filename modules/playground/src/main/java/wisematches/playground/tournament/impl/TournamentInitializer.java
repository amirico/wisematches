package wisematches.playground.tournament.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.personality.Personality;
import wisematches.playground.RatingManager;
import wisematches.playground.tournament.TournamentGroup;
import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.TournamentSubscription;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
final class TournamentInitializer {
	private static final Log log = LogFactory.getLog("wisematches.server.tournament.initializer");

	private TournamentInitializer() {
	}


	public static Collection<TournamentGroup> createTournamentGroups(int tournament, int round, Collection<TournamentSubscription> subscriptions, RatingManager ratingManager) {
		log.info("Found " + subscriptions.size() + " subscriptions");

		final Map<TournamentSection, Collection<Long>> groups = new HashMap<TournamentSection, Collection<Long>>();
		for (TournamentSubscription subscription : subscriptions) {
			final short rating = ratingManager.getRating(Personality.person(subscription.getPlayer()));
			final TournamentSection section = getPlayerSection(subscription, rating);

			Collection<Long> longs = groups.get(section);
			if (longs == null) {
				longs = new ArrayList<Long>();
				groups.put(section, longs);
			}
		}
		log.info("Found " + groups.size() + " registered groups");

		final Collection<Long> movingPlayers = new ArrayList<Long>();
		for (TournamentSection section : TournamentSection.values()) {
			final Collection<Long> longs = groups.get(section);
			if (longs == null) {
				continue;
			}

			longs.addAll(movingPlayers); // add from previous group
			movingPlayers.clear();

			if (longs.size() == 1) {
				movingPlayers.addAll(groups.remove(section));
			}
		}

		final Collection<TournamentGroup> res = new ArrayList<TournamentGroup>();
		for (Map.Entry<TournamentSection, Collection<Long>> entry : groups.entrySet()) {
//			TournamentGroup group = new HibernateTournamentGroup(tournament, round, );
		}
		return res;
	}

	private static TournamentSection getPlayerSection(TournamentSubscription subscription, short rating) {
		TournamentSection section = subscription.getSection(); // current or next
		while (!section.isRatingAllowed(rating)) {
			section = section.getHigherSection();
		}
		return section;
	}
}
