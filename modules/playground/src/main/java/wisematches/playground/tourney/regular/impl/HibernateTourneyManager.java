package wisematches.playground.tourney.regular.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.LongType;
import org.quartz.CronExpression;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.search.SearchFilter;
import wisematches.playground.timer.BreakingDayListener;
import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.TourneyEntityListener;
import wisematches.playground.tourney.regular.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTourneyManager implements InitializingBean, RegularTourneyManager, BreakingDayListener {
	private SessionFactory sessionFactory;

	private TaskExecutor taskExecutor;
	private CronExpression cronExpression;

	private final Lock lock = new ReentrantLock();

	private final Collection<RegularTourneyListener> tourneyListeners = new CopyOnWriteArraySet<RegularTourneyListener>();
	private final Collection<TourneySubscriptionListener> subscriptionListeners = new CopyOnWriteArraySet<TourneySubscriptionListener>();
	private final Collection<TourneyEntityListener> entityListeners = new CopyOnWriteArraySet<TourneyEntityListener>();

	private static final Log log = LogFactory.getLog("wisematches.server.playground.tourney.regular");

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
	public void addTourneyEntityListener(TourneyEntityListener l) {
		if (l != null) {
			entityListeners.add(l);
		}
	}

	@Override
	public void removeTourneyEntityListener(TourneyEntityListener l) {
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
						"where r.division.tourney.number=? and r.division.section = ? and r.division.language = ? and r.round=?")
						.setInteger(0, id1.getDivisionId().getTourneyId().getNumber())
						.setParameter(1, id1.getDivisionId().getSection())
						.setParameter(2, id1.getDivisionId().getLanguage())
						.setInteger(3, id1.getRound())
						.uniqueResult();
			} else if (TourneyGroup.Id.class.isAssignableFrom(id.getClass())) {
				final TourneyGroup.Id id1 = TourneyGroup.Id.class.cast(id);
				return (T) session.createQuery("from HibernateTourneyGroup g " +
						"where g.round.division.tourney.number=? and g.round.division.section = ? and g.round.division.language = ? and g.round.round=? and g.group = ?")
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
//						"where t.number=?").setInteger(0, id1.getRound()).uniqueResult()).intValue();
//			} else if (TourneyDivision.Id.class.isAssignableFrom(id.getClass())) {
//				final TourneyDivision.Id id1 = TourneyDivision.Id.class.cast(id);
//				return (T) session.createQuery("from HibernateTourneyDivision d " +
//						"where d.tourney.number=? and d.section = ? and d.language = ?")
//						.setInteger(0, id1.getTourneyId().getRound())
//						.setParameter(1, id1.getSection())
//						.setParameter(2, id1.getLanguage())
//						.uniqueResult();
//			} else if (TourneyRound.Id.class.isAssignableFrom(id.getClass())) {
//				final TourneyRound.Id id1 = TourneyRound.Id.class.cast(id);
//				return (T) session.createQuery("from HibernateTourneyRound r " +
//						"where r.division.tourney.number=? and r.division.section = ? and r.division.language = ? and r.number=?")
//						.setInteger(0, id1.getDivisionId().getTourneyId().getRound())
//						.setParameter(1, id1.getDivisionId().getSection())
//						.setParameter(2, id1.getDivisionId().getLanguage())
//						.setInteger(3, id1.getRound())
//						.uniqueResult();
//			} else if (TourneyGroup.Id.class.isAssignableFrom(id.getClass())) {
//				final TourneyGroup.Id id1 = TourneyGroup.Id.class.cast(id);
//				return (T) session.createQuery("from HibernateTourneyGroup g " +
//						"where g.round.division.tourney.number=? and g.round.division.section = ? and g.round.division.language = ? and g.round.number=? and g.number = ?")
//						.setInteger(0, id1.getRoundId().getDivisionId().getTourneyId().getRound())
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
	public void breakingDayTime(Date midnight) {
		processTourneyEntities();
	}

	private void processTourneyEntities() {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					initiateScheduledTourneys();
				} catch (Exception ex) {
					log.error("Scheduled Tourneys can't be initialized", ex);
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void initiateScheduledTourneys() {
		lock.lock();
		try {
			final Session session = sessionFactory.getCurrentSession();
//			final List list = session.createQuery("from HibernateTourney where scheduledDate<=? and startedDate is null").setParameter(0, getMidnight()).list();
			final List list = session.createQuery("from HibernateTourney").list();
			for (Object aList : list) {
				final HibernateTourney tourney = (HibernateTourney) aList;
				tourney.startTourney();

				for (TourneyEntityListener entityListener : entityListeners) {
					entityListener.entityStarted(tourney);
				}

				final Map<Long, TourneySection> originalSection = new HashMap<Long, TourneySection>();
				final Map<Long, TourneySection> resubscribedSection = new HashMap<Long, TourneySection>();

				final DefaultTourneySubscriptions status = (DefaultTourneySubscriptions) getSubscriptionStatus(tourney.getNumber());
				for (Language language : Language.values()) {
					for (TourneySection section : TourneySection.values()) {
						// nothing to do if no subscription
						if (status.getPlayers(language, section) == 0) {
							continue;
						}

						final Set<Long> invalidPlayers = new HashSet<Long>();
						if (status.getPlayers(language, section) != 1) {
							final Query namedQuery = session.getNamedQuery("tourney.outOfSection");
							namedQuery.setParameter("round", 1);
							namedQuery.setParameter("rating", section.getTopRating());
							namedQuery.setParameter("section", section.ordinal());
							namedQuery.setParameter("language", language.ordinal());
							namedQuery.setParameter("tourney", tourney.getNumber());

							// select all
							for (Object res : namedQuery.list()) { // result is BigInteger instead of long
								invalidPlayers.add(((Number) res).longValue());
							}
						}

						// if only one - move to next
						if (status.getPlayers(language, section) - invalidPlayers.size() == 1) {
							Query query = session.createQuery("select id.player " +
									"from HibernateTourneySubscription " +
									"where id.tourney=:tourney and id.round=1 and language=:language and section=:section");
							query.setParameter("section", section);
							query.setParameter("language", language);
							query.setParameter("tourney", tourney.getNumber());
							invalidPlayers.addAll((Collection<Long>) query.list());
						}

						if (invalidPlayers.size() != 0) {
							// remove from stats
							status.setPlayers(language, section, status.getPlayers(language, section) - invalidPlayers.size());

							Query query;
							// search next not empty section
							TourneySection nextSection = section.getHigherSection();
							while (nextSection != null && invalidPlayers.size() < 2 && status.getPlayers(language, nextSection) == 0) {
								nextSection = nextSection.getHigherSection();
							}

							if (nextSection != null) { // and move to next group
								query = session.createQuery("update from HibernateTourneySubscription set section=:section " +
										"where id.tourney=:tourney and id.round=1 and language=:language and id.player in (:players)");
								query.setParameter("section", nextSection);
							} else { // or cancel subscription
								query = session.createQuery("delete from HibernateTourneySubscription " +
										"where id.tourney=:tourney and id.round=1 and language=:language and id.player in (:players)");
							}
							query.setParameter("language", language);
							query.setParameter("tourney", tourney.getNumber());
							query.setParameterList("players", invalidPlayers, LongType.INSTANCE);

							final int count = query.executeUpdate();
							if (nextSection != null) {
								status.setPlayers(language, nextSection, status.getPlayers(language, nextSection) + count);
							}

							for (Long pid : invalidPlayers) {
								// only if new. Otherwise - reuse old
								if (!originalSection.containsKey(pid)) {
									originalSection.put(pid, section);
								}
								resubscribedSection.put(pid, nextSection);
							}
						}
					}
				}

				for (TourneySubscriptionListener subscriptionListener : subscriptionListeners) {
					for (Long resubscribedPlayer : originalSection.keySet()) {
						final TourneySection oldSection = originalSection.get(resubscribedPlayer);
						final TourneySection newSection = resubscribedSection.get(resubscribedPlayer);
						subscriptionListener.resubscribed(resubscribedPlayer, tourney.getNumber(), oldSection, newSection);
					}
				}

				// save tourney
				session.save(tourney);

				// create divisions
				for (Language language : Language.values()) {
					for (TourneySection section : TourneySection.values()) {
						if (status.getPlayers(language, section) != 0) {
							final HibernateTourneyDivision division = new HibernateTourneyDivision(tourney, language, section);
							session.save(division);

							for (TourneyEntityListener entityListener : entityListeners) {
								entityListener.entityStarted(division);
							}
						}
					}
				}
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * TODO: for test cases only!
	 */
	public RegularTourney startRegularTourney(Date scheduledDate) {
		int number = 1;
		final Session session = sessionFactory.getCurrentSession();

		final Number n = (Number) session.createQuery("select max(number) from HibernateTourney ").uniqueResult();
		if (n != null) {
			number = n.intValue() + 1;
		}

		final HibernateTourney t = new HibernateTourney(number, scheduledDate);
		session.save(t);

		for (RegularTourneyListener tourneyListener : tourneyListeners) {
			tourneyListener.tourneyAnnounced(t);
		}
		return t;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setCronExpression(CronExpression cronExpression) {
		this.cronExpression = cronExpression;
		if (this.cronExpression != null) {
			this.cronExpression.setTimeZone(TimeZone.getTimeZone("GMT"));
		}
	}

	static Date getMidnight() {
		return new Date((System.currentTimeMillis() / 86400000L) * 86400000L);
	}
}
