package wisematches.playground.tournament.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.personality.Language;
import wisematches.personality.player.Player;
import wisematches.playground.RatingManager;
import wisematches.playground.tournament.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTournamentTicketManager implements TournamentTicketManager {
	private HibernateTournamentPoster poster;

	private RatingManager ratingManager;
	private SessionFactory sessionFactory;
	private TransactionTemplate transactionTemplate;

	private final Lock lock = new ReentrantLock();
	private final Collection<TournamentTicketListener> listeners = new CopyOnWriteArraySet<TournamentTicketListener>();

	public HibernateTournamentTicketManager() {
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
	@Transactional(propagation = Propagation.MANDATORY)
	public void buyTicket(Player player, Language language, TournamentSection section) {
		lock.lock();
		try {
			if (poster == null) {
				throw new IllegalStateException("No active poster");
			}

			final short rating = ratingManager.getRating(player);
			if (rating > section.getRatingThreshold()) {
				throw new IllegalArgumentException("Player's rating our of the section: " + rating + " > " + section.getRatingThreshold());
			}
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
	@Transactional(propagation = Propagation.MANDATORY)
	public void sellTicket(Player player, Language language) {
		lock.lock();
		try {
			if (poster == null) {
				throw new IllegalStateException("No active poster");
			}

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
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public TournamentTickets getTournamentTickets(Language language) {
		lock.lock();
		try {
			if (poster == null) {
				throw new IllegalStateException("No active poster");
			}

			final Session session = sessionFactory.getCurrentSession();
			final Query q = session.createQuery("" +
					"select t.section, count(t.pk.poster) " +
					"from wisematches.playground.tournament.impl.HibernateTournamentTicket t " +
					"where t.pk.poster=? and t.pk.language=? group by t.section");
			q.setParameter(0, poster.getNumber());
			q.setParameter(1, language);

			final List list = q.list();
			final Map<TournamentSection, Long> res = new HashMap<TournamentSection, Long>(list.size());
			for (Object o : list) {
				final Object[] val = (Object[]) o;
				res.put((TournamentSection) val[0], (Long) val[1]);
			}
			return new TournamentTickets(res);
		} finally {
			lock.unlock();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Collection<TournamentTicket> getPlayerTickets(Player player) {
		lock.lock();
		try {
			if (poster == null) {
				throw new IllegalStateException("No active poster");
			}
			final Session session = sessionFactory.getCurrentSession();
			final Query query = session.createQuery("" +
					"from wisematches.playground.tournament.impl.HibernateTournamentTicket t " +
					"where t.pk.poster = ? and t.pk.player = ?");
			query.setParameter(0, poster.getNumber());
			query.setParameter(1, player.getId());
			return query.list();
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public TournamentTicket getPlayerTicket(Player player, Language language) {
		lock.lock();
		try {
			if (poster == null) {
				throw new IllegalStateException("No active poster");
			}
			final Session session = sessionFactory.getCurrentSession();
			return (TournamentTicket) session.get(HibernateTournamentTicket.class,
					new HibernateTournamentTicket.PK(poster, player, language));
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void announceTournament(final Date scheduledDate) {
		if (sessionFactory == null || transactionTemplate == null) {
			return;
		}
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				lock.lock();
				try {
					final Session session = sessionFactory.getCurrentSession();
					if (poster == null) {
						poster = new HibernateTournamentPoster(1, scheduledDate);
					} else {
						poster.setStarted(true);
						session.update(session.merge(poster));
						poster = new HibernateTournamentPoster(poster.getNumber() + 1, scheduledDate);
					}
					session.save(poster);
				} finally {
					lock.unlock();
				}
			}
		});
	}

	private void initPoster() {
		if (sessionFactory == null || transactionTemplate == null) {
			return;
		}
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				lock.lock();
				try {
					final Session session = sessionFactory.getCurrentSession();
					final Query query = session.createQuery("" +
							"from wisematches.playground.tournament.impl.HibernateTournamentPoster p " +
							"where p.started=false");
					query.setMaxResults(1);
					poster = (HibernateTournamentPoster) query.uniqueResult();
					if (poster != null) {
						session.evict(poster);
					}
				} finally {
					lock.unlock();
				}
			}
		});
	}

	public void setRatingManager(RatingManager ratingManager) {
		this.ratingManager = ratingManager;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		initPoster();
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
		initPoster();
	}
}
