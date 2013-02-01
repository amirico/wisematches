package wisematches.playground.tourney.regular.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.quartz.CronExpression;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Language;
import wisematches.core.Personality;
import wisematches.core.PersonalityManager;
import wisematches.core.Player;
import wisematches.core.search.Orders;
import wisematches.core.search.Range;
import wisematches.core.search.SearchFilter;
import wisematches.core.task.BreakingDayListener;
import wisematches.playground.*;
import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.regular.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTourneyManager<S extends GameSettings>
		implements RegularTourneyManager, InitializingBean, BreakingDayListener {
	private SessionFactory sessionFactory;

	private TaskExecutor taskExecutor;
	private CronExpression cronExpression;

	private PersonalityManager playerManager;
	private GamePlayManager<S, ?> gamePlayManager;
	private GameSettingsProvider<S, TourneyGroup> settingsProvider;
	private HibernateTourneyProcessor tourneyProcessor = new HibernateTourneyProcessor();
	private final BoardListener gamePlayListener = new TheBoardListener();

	private final Lock lock = new ReentrantLock();
	private final HibernateRegistrationSearchManager registrationSearchManager = new HibernateRegistrationSearchManager();

	private final Collection<RegularTourneyListener> tourneyListeners = new CopyOnWriteArraySet<>();
	private final Collection<RegistrationListener> subscriptionListeners = new CopyOnWriteArraySet<>();

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
	public void addTourneySubscriptionListener(RegistrationListener l) {
		if (l != null) {
			subscriptionListeners.add(l);
		}
	}

	@Override
	public void removeTourneySubscriptionListener(RegistrationListener l) {
		subscriptionListeners.remove(l);
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY, readOnly = false)
	public RegistrationRecord register(Personality player, Tourney tourney, Language language, TourneySection section) throws RegistrationException {
		lock.lock();
		try {
			if (tourney == null) {
				throw new RegistrationException("Tourney can't be null: " + tourney);
			}

			final Tourney t = getTourneyEntity(new Tourney.Id(tourney.getNumber()));
			if (t == null) {
				throw new RegistrationException("Unknown tourney: " + tourney);
			}
			if (t.getStartedDate() != null) {
				throw new RegistrationException("Tourney already started: " + tourney);
			}

			final RegistrationRecord subscription = getRegistration(player, tourney);
			if (subscription != null) {
				throw new RegistrationException("Already registered");
			}

			final HibernateRegistrationRecord s = new HibernateRegistrationRecord(tourney.getNumber(), player.getId(), language, section);
			sessionFactory.getCurrentSession().save(s);

			for (RegistrationListener subscriptionListener : subscriptionListeners) {
				subscriptionListener.registered(s, null);
			}
			return s;
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY, readOnly = false)
	public RegistrationRecord unregister(Personality player, Tourney tourney, Language language, TourneySection section) {
		lock.lock();
		try {
			final RegistrationRecord subscription = getRegistration(player, tourney);
			if (subscription != null && subscription.getLanguage().equals(language) && subscription.getSection().equals(section)) {
				sessionFactory.getCurrentSession().delete(subscription);

				for (RegistrationListener subscriptionListener : subscriptionListeners) {
					subscriptionListener.unregistered(subscription, null);
				}
			}
			return subscription;
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public RegistrationsSummary getRegistrationsSummary(Tourney tourney) {
		lock.lock();
		try {
			return HibernateQueryHelper.getRegistrationsSummary(sessionFactory.getCurrentSession(), tourney.getNumber(), 1);
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public RegistrationRecord getRegistration(Personality player, Tourney tourney) {
		lock.lock();
		try {
			return (RegistrationRecord) sessionFactory.getCurrentSession().get(HibernateRegistrationRecord.class,
					new HibernateRegistrationRecord.Id(tourney.getNumber(), player.getId(), 1));
		} finally {
			lock.unlock();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public <T extends RegularTourneyEntity, K extends TourneyEntity.Id<? extends T, ?>> T getTourneyEntity(K id) {
		lock.lock();
		try {
			final Session session = sessionFactory.getCurrentSession();
			if (Tourney.Id.class.isAssignableFrom(id.getClass())) {
				return (T) HibernateQueryHelper.getTourney(session, Tourney.Id.class.cast(id));
			} else if (TourneyDivision.Id.class.isAssignableFrom(id.getClass())) {
				return (T) HibernateQueryHelper.getDivision(session, TourneyDivision.Id.class.cast(id));
			} else if (TourneyRound.Id.class.isAssignableFrom(id.getClass())) {
				return (T) HibernateQueryHelper.getRound(session, TourneyRound.Id.class.cast(id));
			} else if (TourneyGroup.Id.class.isAssignableFrom(id.getClass())) {
				return (T) HibernateQueryHelper.getGroup(session, TourneyGroup.Id.class.cast(id));
			}
			throw new IllegalArgumentException("Unsupported entity type: " + id);
		} finally {
			lock.unlock();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <Ctx extends TourneyEntity.Context<? extends RegularTourneyEntity, ?>> int getTotalCount(Player person, Ctx context) {
		lock.lock();
		try {
			final Query query = createEntityQuery(person, context, true);
			return ((Number) query.uniqueResult()).intValue();
		} finally {
			lock.unlock();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends RegularTourneyEntity, C extends TourneyEntity.Context<? extends T, ?>> List<T> searchTourneyEntities(Personality person, C context, SearchFilter filter, Orders orders, Range range) {
		lock.lock();
		try {
			final Query query1 = createEntityQuery(person, context, false);
			if (range != null) {
				range.apply(query1);
			}
			return query1.list();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public RegistrationSearchManager getRegistrationSearchManager() {
		return registrationSearchManager;
	}

	private Query createEntityQuery(Personality personality, TourneyEntity.Context<?, ?> context, boolean count) {
		final Session session = sessionFactory.getCurrentSession();
		final Class<? extends TourneyEntity.Context> contextType = context.getClass();

		if (Tourney.Context.class.isAssignableFrom(contextType)) {
			return HibernateQueryHelper.searchTourneys(session, personality, Tourney.Context.class.cast(context), count);
		}

		if (TourneyDivision.Context.class.isAssignableFrom(contextType)) {
			return HibernateQueryHelper.searchDivisions(session, personality, TourneyDivision.Context.class.cast(context), count);
		}

		if (TourneyRound.Context.class.isAssignableFrom(contextType)) {
			return HibernateQueryHelper.searchRounds(session, personality, TourneyRound.Context.class.cast(context), count);
		}

		if (TourneyGroup.Context.class.isAssignableFrom(contextType)) {
			return HibernateQueryHelper.searchGroups(session, personality, TourneyGroup.Context.class.cast(context), count);
		}
		throw new IllegalArgumentException("Unsupported context type: " + contextType);
	}

	@Override
	public <Ctx extends TourneyEntity.Context<? extends RegularTourneyEntity, ?>, Fl extends SearchFilter> int getFilteredCount(Player person, Ctx context, Fl filter) {
		return getTotalCount(person, context);
	}

	@Override
	public <Ctx extends TourneyEntity.Context<? extends RegularTourneyEntity, ?>, Fl extends SearchFilter> List<RegularTourneyEntity> searchEntities(Player person, Ctx context, Fl filter, Orders orders, Range range) {
		return searchTourneyEntities(person, context, filter, orders, range);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initiateTourneyEntities();
	}

	@Override
	public void breakingDayTime(Date midnight) {
		initiateTourneyEntities();
	}

	private void initiateTourneyEntities() {
		// initiate new tourneys
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				startRegularTourney();
			}
		});

		// initiate scheduled tourneys
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				lock.lock();
				try {
					tourneyProcessor.initiateTourneys(sessionFactory.getCurrentSession(), tourneyListeners, subscriptionListeners);
				} catch (Exception ex) {
					log.error("Scheduled tourneys can't be initiated by internal error", ex);
					throw new TourneyProcessingException("Scheduled tourneys can't be initiated by internal error", ex);
				} finally {
					lock.unlock();
				}
			}
		});

		// initiate scheduled divisions
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				lock.lock();
				try {
					tourneyProcessor.initiateDivisions(sessionFactory.getCurrentSession(), gamePlayManager, settingsProvider, playerManager);
				} catch (Exception ex) {
					log.error("Divisions can't be initiated by internal error", ex);
					throw new TourneyProcessingException("Divisions can't be initiated by internal error", ex);
				} finally {
					lock.unlock();
				}
			}
		});

		// clear all subscriptions
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				lock.lock();
				try {
					tourneyProcessor.clearRegistrations(sessionFactory.getCurrentSession());
				} catch (Exception ex) {
					log.error("Subscriptions can't be cleaned", ex);
					throw new TourneyProcessingException("Subscriptions can't be cleaned", ex);
				} finally {
					lock.unlock();
				}
			}
		});
	}

	public void finalizeTourneyEntities(final GameBoard<?, ?> board) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				lock.lock();
				try {
					tourneyProcessor.finalizeDivisions(sessionFactory.getCurrentSession(), board, subscriptionListeners, tourneyListeners);
				} catch (Exception ex) {
					log.error("Board can't be finalized by internal error: " + board.getBoardId(), ex);
					throw new TourneyProcessingException("Board can't be finalized by internal error", ex);
				} finally {
					lock.unlock();
				}
			}
		});

		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				lock.lock();
				try {
					tourneyProcessor.finalizeTourneys(sessionFactory.getCurrentSession());
				} catch (Exception ex) {
					log.error("Tourneys can't be finalized by internal error", ex);
					throw new TourneyProcessingException("Tourneys can't be finalized by internal error", ex);
				} finally {
					lock.unlock();
				}
			}
		});
	}

	protected Tourney startRegularTourney() {
		final Session session = sessionFactory.getCurrentSession();

		final Date scheduledDate = cronExpression.getNextValidTimeAfter(new Date());
		if (HibernateQueryHelper.hasScheduledTourneys(session, scheduledDate)) {
			return null;
		}

		int number = HibernateQueryHelper.getNextTourneyNumber(session);
		log.info("It's time for new tourney: number - " + number + ", scheduled date - " + scheduledDate);

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
		registrationSearchManager.setSessionFactory(sessionFactory);
	}

	public void setCronExpression(CronExpression cronExpression) {
		this.cronExpression = cronExpression;
		if (this.cronExpression != null) {
			this.cronExpression.setTimeZone(TimeZone.getTimeZone("GMT"));
		}
	}

	public void setPlayerManager(PersonalityManager playerManager) {
		this.playerManager = playerManager;
	}

	public void setGamePlayManager(GamePlayManager<S, ?> gamePlayManager) {
		if (this.gamePlayManager != null) {
			this.gamePlayManager.removeBoardListener(gamePlayListener);
		}

		this.gamePlayManager = gamePlayManager;

		if (this.gamePlayManager != null) {
			this.gamePlayManager.addBoardListener(gamePlayListener);
		}
	}

	public void setSettingsProvider(GameSettingsProvider<S, TourneyGroup> settingsProvider) {
		this.settingsProvider = settingsProvider;
	}

	private class TheBoardListener implements BoardListener {
		private TheBoardListener() {
		}

		@Override
		public void gameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
		}

		@Override
		public void gameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move, GameMoveScore moveScore) {
		}

		@Override
		public void gameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameResolution resolution, Collection<Personality> winners) {
			finalizeTourneyEntities(board);
		}
	}
}
