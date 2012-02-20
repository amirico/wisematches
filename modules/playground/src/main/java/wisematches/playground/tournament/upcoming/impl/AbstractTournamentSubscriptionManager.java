package wisematches.playground.tournament.upcoming.impl;

import wisematches.playground.tournament.upcoming.*;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractTournamentSubscriptionManager implements TournamentSubscriptionManager {
	protected final Lock lock = new ReentrantLock();

	private final Set<TournamentRequestListener> requestListeners = new CopyOnWriteArraySet<TournamentRequestListener>();
	private final Set<TournamentAnnouncementListener> announcementListeners = new CopyOnWriteArraySet<TournamentAnnouncementListener>();

	protected AbstractTournamentSubscriptionManager() {
	}

	@Override
	public void addTournamentRequestListener(TournamentRequestListener l) {
		if (l != null) {
			requestListeners.add(l);
		}
	}

	@Override
	public void removeTournamentRequestListener(TournamentRequestListener l) {
		requestListeners.remove(l);
	}

	@Override
	public void addTournamentAnnouncementListener(TournamentAnnouncementListener l) {
		if (l != null) {
			announcementListeners.add(l);
		}
	}

	@Override
	public void removeTournamentAnnouncementListener(TournamentAnnouncementListener l) {
		announcementListeners.remove(l);
	}

	protected void firePlayerSubscribed(TournamentRequest request) {
		for (TournamentRequestListener listener : requestListeners) {
			listener.playerSubscribed(request);
		}
	}

	protected void firePlayerUnsubscribed(TournamentRequest request) {
		for (TournamentRequestListener listener : requestListeners) {
			listener.playerUnsubscribed(request);
		}
	}

	protected void fireTournamentAnnounced(TournamentAnnouncement announcement) {
		for (TournamentAnnouncementListener announcementListener : announcementListeners) {
			announcementListener.tournamentAnnounced(announcement);
		}
	}
}
