package wisematches.playground.tournament.impl.hibernate;

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
import wisematches.playground.tournament.TournamentPoster;
import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.TournamentTicket;
import wisematches.playground.tournament.TournamentTicketBatch;
import wisematches.playground.tournament.impl.AbstractTournamentTicketManager;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTournamentTicketManager extends AbstractTournamentTicketManager<HibernateTournamentPoster> {
	private RatingManager ratingManager;
	private SessionFactory sessionFactory;
	private TransactionTemplate transactionTemplate;

	public HibernateTournamentTicketManager() {
	}

	@Override
	protected HibernateTournamentPoster createNewPoster() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	protected HibernateTournamentPoster loadActivePoster() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	protected void saveActivePoster(HibernateTournamentPoster poster) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}


	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void buyTicket(Player player, Language language, TournamentSection section) {
		lock.lock();
		try {
			HibernateTournamentPoster poster = getTournamentPoster();
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

			fireTicketBought(poster, player, ticket);
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY)
	public void sellTicket(Player player, Language language) {
		lock.lock();
		try {
			HibernateTournamentPoster poster = getTournamentPoster();
			if (poster == null) {
				throw new IllegalStateException("No active poster");
			}

			final Session session = sessionFactory.getCurrentSession();
			HibernateTournamentTicket ticket = (HibernateTournamentTicket) session.get(
					HibernateTournamentTicket.class, new HibernateTournamentTicket.PK(poster, player, language));
			if (ticket != null) {
				session.delete(ticket);
			}

			fireTicketSold(poster, player, ticket);
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public TournamentTicketBatch getTournamentTicketBatch(Language language) {
		lock.lock();
		try {
			HibernateTournamentPoster poster = getTournamentPoster();
			if (poster == null) {
				throw new IllegalStateException("No active poster");
			}

			final Session session = sessionFactory.getCurrentSession();
			final Query q = session.createQuery("" +
					"select t.section, count(t.pk.poster) " +
					"from wisematches.playground.tournament.impl.hibernate.HibernateTournamentTicket t " +
					"where t.pk.poster=? and t.pk.language=? group by t.section");
			q.setParameter(0, poster.getNumber());
			q.setParameter(1, language);

			final List list = q.list();
			final Map<TournamentSection, Long> res = new HashMap<TournamentSection, Long>(list.size());
			for (Object o : list) {
				final Object[] val = (Object[]) o;
				res.put((TournamentSection) val[0], (Long) val[1]);
			}
			return new TournamentTicketBatch(res);
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
			HibernateTournamentPoster poster = getTournamentPoster();
			if (poster == null) {
				throw new IllegalStateException("No active poster");
			}
			final Session session = sessionFactory.getCurrentSession();
			final Query query = session.createQuery("" +
					"from wisematches.playground.tournament.impl.hibernate.HibernateTournamentTicket t " +
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
			HibernateTournamentPoster poster = getTournamentPoster();
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

	@Transactional(propagation = Propagation.MANDATORY)
	public TournamentPoster announceTournament(final Date scheduledDate) {
		if (sessionFactory == null || transactionTemplate == null) {
			return null;
		}
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				lock.lock();
				try {
					HibernateTournamentPoster poster = getTournamentPoster();
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
		return null;
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
					HibernateTournamentPoster poster = getTournamentPoster();
					final Session session = sessionFactory.getCurrentSession();
					final Query query = session.createQuery("" +
							"from wisematches.playground.tournament.impl.hibernate.HibernateTournamentPoster p " +
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
