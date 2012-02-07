package wisematches.playground.tournament.upcoming.impl;

import wisematches.personality.Language;
import wisematches.personality.player.Player;
import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.upcoming.*;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AbstractTournamentSubscriptionManager implements TournamentSubscriptionManager {
	protected final Lock lock = new ReentrantLock();

	private final Set<TournamentRequestListener> requestListeners = new CopyOnWriteArraySet<TournamentRequestListener>();
	private final Set<TournamentAnnouncementListener> announcementListeners = new CopyOnWriteArraySet<TournamentAnnouncementListener>();

	public AbstractTournamentSubscriptionManager() {
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

	@Override
	public TournamentAnnouncement getTournamentAnnouncement() {
		lock.lock();
		try {
			throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
		} finally {
			lock.unlock();
		}
	}

	@Override
	public TournamentRequest subscribe(int announcement, Player player, Language language, TournamentSection section) throws WrongSectionException, WrongAnnouncementException {
		lock.lock();
		try {
			throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
		} finally {
			lock.unlock();
		}
	}

	@Override
	public TournamentRequest unsubscribe(int announcement, Player player, Language language) throws WrongAnnouncementException {
		lock.lock();
		try {
			throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
		} finally {
			lock.unlock();
		}
	}

	@Override
	public TournamentRequest getTournamentRequest(int announcement, Player player, Language language) throws WrongAnnouncementException {
		lock.lock();
		try {
			throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Collection<TournamentRequest> getTournamentRequests(int announcement, Player player) throws WrongAnnouncementException {
		lock.lock();
		try {
			throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
		} finally {
			lock.unlock();
		}
	}
}
