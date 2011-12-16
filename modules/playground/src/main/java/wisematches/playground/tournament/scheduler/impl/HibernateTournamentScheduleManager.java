package wisematches.playground.tournament.scheduler.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.personality.Language;
import wisematches.personality.player.Player;
import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.scheduler.TournamentPoster;
import wisematches.playground.tournament.scheduler.TournamentScheduleManager;
import wisematches.playground.tournament.scheduler.TournamentTicket;
import wisematches.playground.tournament.scheduler.TournamentTicketListener;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTournamentScheduleManager implements TournamentScheduleManager {
	private TournamentPoster poster;

	private SessionFactory sessionFactory;
	private TransactionTemplate transactionTemplate;

	private final Lock lock = new ReentrantLock();
	private final Collection<TournamentTicketListener> listeners = new CopyOnWriteArraySet<TournamentTicketListener>();

	public HibernateTournamentScheduleManager() {
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
	public TournamentPoster getTournamentPoster() {
		lock.lock();
		try {
			return poster;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void buyTicket(Player player, Language language, TournamentSection section) {
		lock.lock();
		try {
			final Session session = sessionFactory.getCurrentSession();
			HibernateTournamentTicket ticket = (HibernateTournamentTicket) session.get(
					HibernateTournamentTicket.class, new HibernateTournamentTicket.PK(poster, player, language));
			if (ticket == null) {
				ticket = new HibernateTournamentTicket(poster, player, language, section);
				session.save(ticket);
			} else {
				ticket.setSection(section);
				session.update(ticket);
			}

			for (TournamentTicketListener listener : listeners) {
				listener.tournamentTicketBought(poster, player, ticket);
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void sellTicket(Player player, Language language) {
		lock.lock();
		try {
			final Session session = sessionFactory.getCurrentSession();
			HibernateTournamentTicket ticket = (HibernateTournamentTicket) session.get(
					HibernateTournamentTicket.class, new HibernateTournamentTicket.PK(poster, player, language));
			if (ticket != null) {
				session.delete(ticket);
			}

			for (TournamentTicketListener listener : listeners) {
				listener.tournamentTicketSold(poster, player, ticket);
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<TournamentTicket> getPlayerTickets(Player player) {
		lock.lock();
		try {
			final Session session = sessionFactory.getCurrentSession();
			final Query query = session.createQuery(
					"from wisematches.playground.tournament.scheduler.impl.HibernateTournamentTicket t where " +
							"t.pk.poster = ? and t.pk.player = ?");
			query.setParameter(0, poster.getNumber());
			query.setParameter(1, player.getId());
			return query.list();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public TournamentTicket getPlayerTicket(Player player, Language language) {
		lock.lock();
		try {
			final Session session = sessionFactory.getCurrentSession();
			return (TournamentTicket) session.get(HibernateTournamentTicket.class,
					new HibernateTournamentTicket.PK(poster, player, language));
		} finally {
			lock.unlock();
		}
	}

	private void initTournamentPoster() {
		if (sessionFactory == null || transactionTemplate == null) {
			return;
		}

		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				lock.lock();
				try {
					final Session session = sessionFactory.getCurrentSession();
					final Query query = session.createQuery(
							"from wisematches.playground.tournament.scheduler.impl.HibernateTournamentPoster " +
									"order by number desc");
					query.setMaxResults(1);

					poster = (HibernateTournamentPoster) query.uniqueResult();
					if (poster == null) {
						poster = new HibernateTournamentPoster(1, new Date());
						session.save(poster);
					}
				} finally {
					lock.unlock();
				}
			}
		});
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		initTournamentPoster();
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
		initTournamentPoster();
	}
}
