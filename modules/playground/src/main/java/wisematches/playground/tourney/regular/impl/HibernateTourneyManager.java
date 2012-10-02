package wisematches.playground.tourney.regular.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
			final RegularTourney t = getTournamentEntity(new RegularTourney.Id(tourney));
			if (t == null) {
				throw new TourneySubscriptionException("Unknown tourney: " + tourney);
			}
			if (t.getStartedDate() != null) {
				throw new TourneySubscriptionException("Tourney already started: " + tourney);
			}

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
			if (subscription != null && subscription.getLanguage().equals(language) && subscription.getSection().equals(section)) {
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
	public TourneySubscriptions getSubscriptionStatus(int tourney) {
		lock.lock();
		try {
			final Session session = sessionFactory.getCurrentSession();
			final Query query = session.createQuery("select language, section, count(id.player) from HibernateTourneySubscription where id.tourney=:tid and id.round=1 group by language, section");
			query.setParameter("tid", tourney);
			final DefaultTourneySubscriptions res = new DefaultTourneySubscriptions(tourney);
			final List list = query.list();
			for (Object o : list) {
				Object[] cols = (Object[]) o;
				res.setPlayers((Language) cols[0], (TourneySection) cols[1], ((Number) cols[2]).intValue());
			}
			return res;
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
				return (T) session.createQuery("from HibernateTourney t " +
						"where t.number=?").setInteger(0, id1.getNumber()).uniqueResult();
			} else if (TourneyDivision.Id.class.isAssignableFrom(id.getClass())) {
				final TourneyDivision.Id id1 = TourneyDivision.Id.class.cast(id);
				return (T) session.createQuery("from HibernateTourneyDivision d " +
						"where d.tourney.number=? and d.section = ? and d.language = ?")
						.setInteger(0, id1.getTourneyId().getNumber())
						.setParameter(1, id1.getSection())
						.setParameter(2, id1.getLanguage())
						.uniqueResult();
			} else if (TourneyRound.Id.class.isAssignableFrom(id.getClass())) {
				final TourneyRound.Id id1 = TourneyRound.Id.class.cast(id);
				return (T) session.createQuery("from HibernateTourneyRound r " +
						"where r.division.tourney.number=? and r.division.section = ? and r.division.language = ? and r.number=?")
						.setInteger(0, id1.getDivisionId().getTourneyId().getNumber())
						.setParameter(1, id1.getDivisionId().getSection())
						.setParameter(2, id1.getDivisionId().getLanguage())
						.setInteger(3, id1.getRound())
						.uniqueResult();
			} else if (TourneyGroup.Id.class.isAssignableFrom(id.getClass())) {
				final TourneyGroup.Id id1 = TourneyGroup.Id.class.cast(id);
				return (T) session.createQuery("from HibernateTourneyGroup g " +
						"where g.round.division.tourney.number=? and g.round.division.section = ? and g.round.division.language = ? and g.round.number=? and g.number = ?")
						.setInteger(0, id1.getRoundId().getDivisionId().getTourneyId().getNumber())
						.setParameter(1, id1.getRoundId().getDivisionId().getSection())
						.setParameter(2, id1.getRoundId().getDivisionId().getLanguage())
						.setInteger(3, id1.getRoundId().getRound())
						.setInteger(4, id1.getGroup())
						.uniqueResult();
			}
			throw new IllegalArgumentException("Unsupported entity type: " + id);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public <Ctx extends TourneyEntity.Context<? extends RegularTourneyEntity, ?>> int getTotalCount(Personality person, Ctx context) {
		lock.lock();
		try {
			final Session session = sessionFactory.getCurrentSession();
			if (RegularTourney.Context.class.isAssignableFrom(context.getClass())) {
//				final RegularTourney.Context ctx = RegularTourney.Context.class.cast(context);
//				return ((Number) session.createQuery("select count(*) from HibernateTourney t " +
//						"where t.number=?").setInteger(0, id1.getNumber()).uniqueResult()).intValue();
//			} else if (TourneyDivision.Id.class.isAssignableFrom(id.getClass())) {
//				final TourneyDivision.Id id1 = TourneyDivision.Id.class.cast(id);
//				return (T) session.createQuery("from HibernateTourneyDivision d " +
//						"where d.tourney.number=? and d.section = ? and d.language = ?")
//						.setInteger(0, id1.getTourneyId().getNumber())
//						.setParameter(1, id1.getSection())
//						.setParameter(2, id1.getLanguage())
//						.uniqueResult();
//			} else if (TourneyRound.Id.class.isAssignableFrom(id.getClass())) {
//				final TourneyRound.Id id1 = TourneyRound.Id.class.cast(id);
//				return (T) session.createQuery("from HibernateTourneyRound r " +
//						"where r.division.tourney.number=? and r.division.section = ? and r.division.language = ? and r.number=?")
//						.setInteger(0, id1.getDivisionId().getTourneyId().getNumber())
//						.setParameter(1, id1.getDivisionId().getSection())
//						.setParameter(2, id1.getDivisionId().getLanguage())
//						.setInteger(3, id1.getRound())
//						.uniqueResult();
//			} else if (TourneyGroup.Id.class.isAssignableFrom(id.getClass())) {
//				final TourneyGroup.Id id1 = TourneyGroup.Id.class.cast(id);
//				return (T) session.createQuery("from HibernateTourneyGroup g " +
//						"where g.round.division.tourney.number=? and g.round.division.section = ? and g.round.division.language = ? and g.round.number=? and g.number = ?")
//						.setInteger(0, id1.getRoundId().getDivisionId().getTourneyId().getNumber())
//						.setParameter(1, id1.getRoundId().getDivisionId().getSection())
//						.setParameter(2, id1.getRoundId().getDivisionId().getLanguage())
//						.setInteger(3, id1.getRoundId().getRound())
//						.setInteger(4, id1.getGroup())
//						.uniqueResult();
			}
			throw new IllegalArgumentException("Unsupported entity context: " + context);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public <T extends RegularTourneyEntity, C extends TourneyEntity.Context<? extends T, ?>> List<T> searchTournamentEntities(Personality person, C context, SearchFilter filter, Orders orders, Range range) {
		return null;
	}

	@Override
	public <Ctx extends TourneyEntity.Context<? extends RegularTourneyEntity, ?>, Fl extends SearchFilter> int getFilteredCount(Personality person, Ctx context, Fl filter) {
		return getTotalCount(person, context);
	}

	@Override
	public <Ctx extends TourneyEntity.Context<? extends RegularTourneyEntity, ?>, Fl extends SearchFilter> List<RegularTourneyEntity> searchEntities(Personality person, Ctx context, Fl filter, Orders orders, Range range) {
		return searchTournamentEntities(person, context, filter, orders, range);
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
		final List list = session.createQuery("from HibernateTourney where scheduledDate=? and startedDate is not null").setParameter(0, getMidnight()).list();
		for (Object aList : list) {
			final HibernateTourney tourney = (HibernateTourney) aList;
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
