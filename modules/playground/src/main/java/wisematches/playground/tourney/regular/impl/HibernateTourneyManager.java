package wisematches.playground.tourney.regular.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.InitializingBean;
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
	public TourneySubscription subscribe(int tourney, long player, Language language, TourneySection section) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public TourneySubscription unsubscribe(int tourney, long player, Language language, TourneySection section) {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public TourneySubscriptionStatus getSubscriptionStatus() {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}

	@Override
	public TourneySubscription getSubscription(int tournament, long player) {
		lock.lock();
		try {
			return null;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public <T extends RegularTourneyEntity, K extends TourneyEntity.Id<? extends T, ?>> T getTournamentEntity(K id) {
		throw new UnsupportedOperationException("TODO: Not implemented");
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
			initiateScheduledTourneys();
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

	private Date getMidnight() {
		return new Date((System.currentTimeMillis() / 86400000L) * 86400000L);
	}
}
