package wisematches.playground.tournament.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.tournament.*;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTournamentManager implements TournamentManager {
	private SessionFactory sessionFactory;
	private TransactionTemplate transactionTemplate;

	private Tournament announcedTournament;

	private final Lock subscriptionLock = new ReentrantLock();

	private final Collection<TournamentListener> tournamentListeners = new CopyOnWriteArraySet<TournamentListener>();
	private final Collection<TournamentSubscriptionListener> subscriptionListeners = new CopyOnWriteArraySet<TournamentSubscriptionListener>();

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
	public void addTournamentSubscriptionListener(TournamentSubscriptionListener l) {
		if (l != null) {
			subscriptionListeners.add(l);
		}
	}

	@Override
	public void removeTournamentSubscriptionListener(TournamentSubscriptionListener l) {
		subscriptionListeners.remove(l);
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

			final HibernateTournamentSubscription.PK pk = new HibernateTournamentSubscription.PK(announcedTournament, player, language);
			HibernateTournamentSubscription subscription = (HibernateTournamentSubscription) session.get(HibernateTournamentSubscription.class, pk);
			if (subscription == null) {
				subscription = new HibernateTournamentSubscription(announcedTournament, player, language, section);
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
			final HibernateTournamentSubscription.PK pk = new HibernateTournamentSubscription.PK(announcedTournament, player, language);
			session.delete(session.get(HibernateTournamentSubscription.class, pk));
		} finally {
			subscriptionLock.unlock();
		}
	}

	@Override
	public TournamentSection getSubscribedSection(Personality player, Language language) {
		subscriptionLock.lock();
		try {
			final Session session = sessionFactory.getCurrentSession();
			final HibernateTournamentSubscription.PK pk = new HibernateTournamentSubscription.PK(announcedTournament, player, language);
			return (TournamentSection) session.get(HibernateTournamentSubscription.class, pk);
		} finally {
			subscriptionLock.unlock();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Collection<TournamentSubscription> getSubscriptions(Personality player) {
		subscriptionLock.lock();
		try {
			final Session session = sessionFactory.getCurrentSession();
			final Query query = session.createQuery("select from wisematches.playground.tournament.impl.HibernateTournamentSubscription as s where s.tournamentId=? and s.player = ?");
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
}
