package wisematches.playground.tourney.regular.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.search.SearchFilter;
import wisematches.playground.timer.BreakingDayListener;
import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.TourneyEntityListener;
import wisematches.playground.tourney.regular.*;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTourneyManager implements InitializingBean, RegularTourneyManager, BreakingDayListener {
	private SessionFactory sessionFactory;

	private final Lock lock = new ReentrantLock();

	private final Collection<RegularTourneyListener> tourneyListeners = new CopyOnWriteArraySet<RegularTourneyListener>();
	private final Collection<TourneySubscriptionListener> subscriptionListeners = new CopyOnWriteArraySet<TourneySubscriptionListener>();
	private final Collection<TourneyEntityListener<? super RegularTourneyEntity>> entityListeners = new CopyOnWriteArraySet<TourneyEntityListener<? super RegularTourneyEntity>>();
	private HibernateTransactionManager transactionManager;

	public HibernateTourneyManager() {
	}

	@Override
	public void addRegularTourneyListener(RegularTourneyListener l) {
		if (l != null) {
			tourneyListeners.add(l);
		}
	}

	@Override
	public void removeRegularTourneyListener(RegularTourneyListener l) {
		tourneyListeners.remove(l);
	}

	@Override
	public void addTourneySubscriptionListener(TourneySubscriptionListener l) {
		if (l != null) {
			subscriptionListeners.add(l);
		}
	}

	@Override
	public void removeTourneySubscriptionListener(TourneySubscriptionListener l) {
		subscriptionListeners.remove(l);
	}

	@Override
	public void addTourneyEntityListener(TourneyEntityListener<? super RegularTourneyEntity> l) {
		if (l != null) {
			entityListeners.add(l);
		}
	}

	@Override
	public void removeTourneyEntityListener(TourneyEntityListener<? super RegularTourneyEntity> l) {
		entityListeners.remove(l);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY, readOnly = false)
	public TourneySubscription subscribe(int tourney, long player, Language language, TourneySection section) throws TourneySubscriptionException {
		lock.lock();
		try {
			final TourneySubscription subscription = getSubscription(tourney, player);
			if (subscription != null) {
				throw new TourneySubscriptionException("Already subscribed");
			}
			final HibernateTourneySubscription s = new HibernateTourneySubscription(player, tourney, 1, language, section);
			sessionFactory.getCurrentSession().save(s);

			for (TourneySubscriptionListener subscriptionListener : subscriptionListeners) {
				subscriptionListener.subscribed(s);
			}
			return s;
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY, readOnly = false)
	public TourneySubscription unsubscribe(int tourney, long player, Language language, TourneySection section) {
		lock.lock();
		try {
			final TourneySubscription subscription = getSubscription(tourney, player);
			if (subscription != null) {
				sessionFactory.getCurrentSession().delete(subscription);

				for (TourneySubscriptionListener subscriptionListener : subscriptionListeners) {
					subscriptionListener.unsubscribed(subscription);
				}
			}
			return subscription;
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public TourneySubscriptionStatus getSubscriptionStatus() {
		lock.lock();
		try {
			return null;
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public TourneySubscription getSubscription(int tourney, long player) {
		lock.lock();
		try {
			return (TourneySubscription) sessionFactory.getCurrentSession().get(HibernateTourneySubscription.class,
					new HibernateTourneySubscription.Id(player, tourney, 1));
		} finally {
			lock.unlock();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T extends RegularTourneyEntity, K extends TourneyEntity.Id<? extends T, ?>> T getTournamentEntity(K id) {
		lock.lock();
		try {
			final Session session = sessionFactory.getCurrentSession();
			if (RegularTourney.Id.class.isAssignableFrom(id.getClass())) {
				final RegularTourney.Id id1 = RegularTourney.Id.class.cast(id);
				final Criteria criteria = session.createCriteria(HibernateTourney.class);
				criteria.add(Restrictions.eq("number", id1.getNumber()));
				return (T) criteria.uniqueResult();
			} else if (TourneyDivision.Id.class.isAssignableFrom(id.getClass())) {
				final TourneyDivision.Id id1 = TourneyDivision.Id.class.cast(id);
				final Criteria criteria = session.createCriteria(HibernateTourneyDivision.class);
				criteria.add(Restrictions.eq("section", id1.getSection()));
				criteria.add(Restrictions.eq("language", id1.getLanguage()));
				criteria.createAlias("tourney", "t").add(Restrictions.eq("t.number", id1.getTourneyId().getNumber()));
				return (T) criteria.uniqueResult();
			} else if (TourneyRound.Id.class.isAssignableFrom(id.getClass())) {
				final TourneyRound.Id id1 = TourneyRound.Id.class.cast(id);
				final Criteria criteria = session.createCriteria(HibernateTourneyRound.class);
				criteria.add(Restrictions.eq("number", id1.getRound()));
				criteria.createAlias("division", "d").add(Restrictions.eq("d.section", id1.getDivisionId().getSection())).add(Restrictions.eq("d.language", id1.getDivisionId().getLanguage()));
				criteria.createAlias("division.tourney", "t").add(Restrictions.eq("t.number", id1.getDivisionId().getTourneyId().getNumber()));
				return (T) criteria.uniqueResult();
			} else if (TourneyGroup.Id.class.isAssignableFrom(id.getClass())) {
				final TourneyGroup.Id id1 = TourneyGroup.Id.class.cast(id);
				final Criteria criteria = session.createCriteria(HibernateTourneyGroup.class);
				criteria.add(Restrictions.eq("number", id1.getGroup()));
				criteria.createAlias("round", "r").add(Restrictions.eq("r.number", id1.getRoundId().getRound()));
				criteria.createAlias("round.division", "d").add(Restrictions.eq("d.section", id1.getRoundId().getDivisionId().getSection())).add(Restrictions.eq("d.language", id1.getRoundId().getDivisionId().getLanguage()));
				criteria.createAlias("round.division.tourney", "t").add(Restrictions.eq("t.number", id1.getRoundId().getDivisionId().getTourneyId().getNumber()));
				return (T) criteria.uniqueResult();
			}
			throw new IllegalArgumentException("Unsupported entity type: " + id);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public <T extends RegularTourneyEntity, C extends TourneyEntity.Context<? extends T, ?>> List<T> searchTournamentEntities(Personality person, C context, SearchFilter filter, Orders orders, Range range) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public <Ctx extends TourneyEntity.Context<? extends RegularTourneyEntity, ?>> int getTotalCount(Personality person, Ctx context) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public <Ctx extends TourneyEntity.Context<? extends RegularTourneyEntity, ?>, Fl extends SearchFilter> int getFilteredCount(Personality person, Ctx context, Fl filter) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public <Ctx extends TourneyEntity.Context<? extends RegularTourneyEntity, ?>, Fl extends SearchFilter> List<RegularTourneyEntity> searchEntities(Personality person, Ctx context, Fl filter, Orders orders, Range range) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		processTourneyEntities();
	}

	@Override
	public void breakingDayTime(Date date) {
		processTourneyEntities();
	}

	private void processTourneyEntities() {
		lock.lock();
		try {
			final TransactionTemplate tt = new TransactionTemplate(transactionManager, new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
			tt.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					initiateScheduledTourneys();
				}
			});
		} finally {
			lock.unlock();
		}
	}

	private void initiateScheduledTourneys() {
		final Session session = sessionFactory.getCurrentSession();
		final Criteria criteria = session.createCriteria(HibernateTourney.class);
		criteria.add(Restrictions.eq("scheduledDate", getMidnight())).add(Restrictions.isNull("startedDate"));

		final List list = criteria.list();
		for (final Iterator iterator = list.iterator(); iterator.hasNext(); ) {
			final HibernateTourney tourney = (HibernateTourney) iterator.next();
			tourney.markStarted();
			session.save(tourney);
		}
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setTransactionManager(HibernateTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	private Date getMidnight() {
		return new Date((System.currentTimeMillis() / 86400000L) * 86400000L);
	}
}
