package wisematches.playground.tournament.r1.impl;

import wisematches.personality.player.Player;
import wisematches.playground.tournament.r1.TournamentPoster;
import wisematches.playground.tournament.r1.TournamentTicket;
import wisematches.playground.tournament.r1.TournamentTicketListener;
import wisematches.playground.tournament.r1.TournamentTicketManager;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractTournamentTicketManager<P extends AbstractTournamentPoster> implements TournamentTicketManager {
	private P poster;

	protected final Lock lock = new ReentrantLock();
	private final Collection<TournamentTicketListener> listeners = new CopyOnWriteArraySet<TournamentTicketListener>();

	protected AbstractTournamentTicketManager() {
	}

	@Override
	public void addTournamentTicketListener(TournamentTicketListener l) {
		if (l != null) {
			listeners.add(l);
		}
	}

	@Override
	public void removeTournamentTicketListener(TournamentTicketListener l) {
		listeners.remove(l);
	}


	@Override
	public P getTournamentPoster() {
		lock.lock();
		try {
			return poster;
		} finally {
			lock.unlock();
		}
	}

	public TournamentPoster announceTournament(final Date scheduledDate) {
		lock.lock();
		try {
			final P newPoster = createNewPoster();
//			poster.setProcessed();
//			saveActivePoster();
			poster = newPoster;
			return poster;
		} finally {
			lock.unlock();
		}
	}

	protected void fireTicketSold(TournamentPoster poster, Player player, TournamentTicket ticket) {
		for (TournamentTicketListener listener : listeners) {
			listener.tournamentTicketSold(poster, player, ticket);
		}
	}

	protected void fireTicketBought(TournamentPoster poster, Player player, TournamentTicket ticket) {
		for (TournamentTicketListener listener : listeners) {
			listener.tournamentTicketBought(poster, player, ticket);
		}
	}


	protected abstract P createNewPoster();

	protected abstract P loadActivePoster();

	protected abstract void saveActivePoster(P poster);
}
