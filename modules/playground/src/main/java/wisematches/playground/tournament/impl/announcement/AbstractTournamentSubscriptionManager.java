package wisematches.playground.tournament.impl.announcement;

import wisematches.playground.tournament.Announcement;
import wisematches.playground.tournament.TournamentSubscriptionListener;
import wisematches.playground.tournament.TournamentSubscriptionManager;
import wisematches.playground.tournament.TournamentSubscription;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractTournamentSubscriptionManager implements TournamentSubscriptionManager {
	protected final Lock lock = new ReentrantLock();

	private final Set<TournamentSubscriptionListener> tournamentSubscriptionListeners = new CopyOnWriteArraySet<TournamentSubscriptionListener>();

	protected AbstractTournamentSubscriptionManager() {
	}

	@Override
	public void addAnnouncementListener(TournamentSubscriptionListener l) {
		if (l != null) {
			tournamentSubscriptionListeners.add(l);
		}
	}

	@Override
	public void removeAnnouncementListener(TournamentSubscriptionListener l) {
		tournamentSubscriptionListeners.remove(l);
	}

	protected void firePlayerSubscribed(TournamentSubscription request) {
		for (TournamentSubscriptionListener tournamentSubscriptionListener : tournamentSubscriptionListeners) {
			tournamentSubscriptionListener.playerSubscribed(request);
		}
	}

	protected void firePlayerUnsubscribed(TournamentSubscription request) {
		for (TournamentSubscriptionListener tournamentSubscriptionListener : tournamentSubscriptionListeners) {
			tournamentSubscriptionListener.playerUnsubscribed(request);
		}
	}

	protected void fireTournamentAnnounced(Announcement closedAnnouncement, Announcement newAnnouncement) {
		for (TournamentSubscriptionListener tournamentSubscriptionListener : tournamentSubscriptionListeners) {
			tournamentSubscriptionListener.tournamentAnnounced(closedAnnouncement, newAnnouncement);
		}
	}
}
