package wisematches.playground.tournament.refactoring.impl;

import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.refactoring.Tournament;
import wisematches.playground.tournament.refactoring.TournamentListener;
import wisematches.playground.tournament.refactoring.TournamentManager;
import wisematches.playground.tournament.scheduler.TournamentTicket;
import wisematches.playground.tournament.scheduler.TournamentTicketListener;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTournamentManager implements TournamentManager {
/*
	private SessionFactory sessionFactory;
	private TransactionTemplate transactionTemplate;

	private Tournament announcedTournament;

	private final Lock subscriptionLock = new ReentrantLock();

	private final Collection<TournamentListener> tournamentListeners = new CopyOnWriteArraySet<TournamentListener>();
	private final Collection<TournamentTicketListener> ticketListeners = new CopyOnWriteArraySet<TournamentTicketListener>();

	public HibernateTournamentManager() {
	}

	@Override
	public void addTournamentListener(TournamentListener l) {
		if (l != null) {
			tournamentListeners.add(l);
		}
	}

	@Override
	public void removeTournamentListener(TournamentListener l) {
		tournamentListeners.remove(l);
	}

	@Override
	public void addTournamentSubscriptionListener(TournamentTicketListener l) {
		if (l != null) {
			ticketListeners.add(l);
		}
	}

	@Override
	public void removeTournamentSubscriptionListener(TournamentTicketListener l) {
		ticketListeners.remove(l);
	}

	@Override
	public Tournament getAnnouncedTournament() {
		return announcedTournament;
	}

	@Override
	public void subscribe(Personality player, Language language, TournamentSection section) {
		subscriptionLock.lock();
		try {
			final Session session = sessionFactory.getCurrentSession();

			final HibernateTournamentTicket.PK pk = new HibernateTournamentTicket.PK(announcedTournament, player, language);
			HibernateTournamentTicket subscription = (HibernateTournamentTicket) session.get(HibernateTournamentTicket.class, pk);
			if (subscription == null) {
				subscription = new HibernateTournamentTicket(announcedTournament, player, language, section);
			} else {
				subscription.setSection(section);
			}
			session.saveOrUpdate(subscription);
		} finally {
			subscriptionLock.unlock();
		}
	}

	@Override
	public void unsubscribe(Personality player, Language language) {
		subscriptionLock.lock();
		try {
			final Session session = sessionFactory.getCurrentSession();
			final HibernateTournamentTicket.PK pk = new HibernateTournamentTicket.PK(announcedTournament, player, language);
			session.delete(session.get(HibernateTournamentTicket.class, pk));
		} finally {
			subscriptionLock.unlock();
		}
	}

	@Override
	public TournamentSection getSubscribedSection(Personality player, Language language) {
		subscriptionLock.lock();
		try {
			final Session session = sessionFactory.getCurrentSession();
			final HibernateTournamentTicket.PK pk = new HibernateTournamentTicket.PK(announcedTournament, player, language);
			return (TournamentSection) session.get(HibernateTournamentTicket.class, pk);
		} finally {
			subscriptionLock.unlock();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<TournamentTicket> getSubscriptions(Personality player) {
		subscriptionLock.lock();
		try {
			final Session session = sessionFactory.getCurrentSession();
			final Query query = session.createQuery("select from wisematches.playground.tournament.scheduled.impl.HibernateTournamentTicket as s where s.tournamentId=? and s.player = ?");
			query.setParameter(0, announcedTournament.getNumber());
			query.setParameter(1, player.getId());
			return query.list();
		} finally {
			subscriptionLock.unlock();
		}
	}

	private void initAnnouncedTournament() {
		if (transactionTemplate == null || sessionFactory == null) {
			return;
		}

		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				final Session session = sessionFactory.getCurrentSession();

				announcedTournament = new HibernateTournament(1, null, null, TournamentState.ANNOUNCED);
			}
		});
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
		initAnnouncedTournament();
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
		initAnnouncedTournament();
	}
*/

	@Override
	public void addTournamentListener(TournamentListener l) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void removeTournamentListener(TournamentListener l) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void addTournamentSubscriptionListener(TournamentTicketListener l) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void removeTournamentSubscriptionListener(TournamentTicketListener l) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Tournament getAnnouncedTournament() {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void subscribe(Personality player, Language language, TournamentSection section) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void unsubscribe(Personality player, Language language) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public TournamentSection getSubscribedSection(Personality player, Language language) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Collection<TournamentTicket> getSubscriptions(Personality player) {
		throw new UnsupportedOperationException("Not implemented"); //To change body of implemented methods use File | Settings | File Templates.
	}
}
