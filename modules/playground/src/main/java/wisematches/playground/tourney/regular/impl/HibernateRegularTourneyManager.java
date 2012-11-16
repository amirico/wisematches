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
import wisematches.database.Orders;
import wisematches.database.Range;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.*;
import wisematches.playground.scheduling.BreakingDayListener;
import wisematches.playground.search.SearchFilter;
import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.regular.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateRegularTourneyManager<S extends GameSettings> implements InitializingBean, RegularTourneyManager, BreakingDayListener {
	private SessionFactory sessionFactory;

	private TaskExecutor taskExecutor;
	private CronExpression cronExpression;

	private BoardManager<S, ?> boardManager;
	private GameSettingsProvider<S, TourneyGroup> settingsProvider;
	private DefaultTourneyProcessor tourneyProcessor = new DefaultTourneyProcessor();
	private final BoardStateListener boardStateListener = new TheBoardStateListener();

	private final Lock lock = new ReentrantLock();

	private final Collection<RegularTourneyListener> tourneyListeners = new CopyOnWriteArraySet<RegularTourneyListener>();
	private final Collection<TourneySubscriptionListener> subscriptionListeners = new CopyOnWriteArraySet<TourneySubscriptionListener>();

	private static final Log log = LogFactory.getLog("wisematches.server.playground.tourney.regular");

	public HibernateRegularTourneyManager() {
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
	@Transactional(propagation = Propagation.MANDATORY, readOnly = false)
	public TourneySubscription subscribe(Tourney tourney, Personality player, Language language, TourneySection section) throws TourneySubscriptionException {
		lock.lock();
		try {
			if (tourney == null) {
				throw new TourneySubscriptionException("Tourney can't be null: " + tourney);
			}

			final Tourney t = getTourneyEntity(new Tourney.Id(tourney.getNumber()));
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

			final HibernateTourneySubscription s = new HibernateTourneySubscription(player.getId(), tourney.getNumber(), 1, language, section);
			sessionFactory.getCurrentSession().save(s);

			for (TourneySubscriptionListener subscriptionListener : subscriptionListeners) {
				subscriptionListener.subscribed(s, null);
			}
			return s;
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY, readOnly = false)
	public TourneySubscription unsubscribe(Tourney tourney, Personality player, Language language, TourneySection section) {
		lock.lock();
		try {
			final TourneySubscription subscription = getSubscription(tourney, player);
			if (subscription != null && subscription.getLanguage().equals(language) && subscription.getSection().equals(section)) {
				sessionFactory.getCurrentSession().delete(subscription);

				for (TourneySubscriptionListener subscriptionListener : subscriptionListeners) {
					subscriptionListener.unsubscribed(subscription, null);
				}
			}
			return subscription;
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public TourneySubscriptions getSubscriptions(Tourney tourney) {
		lock.lock();
		try {
			return tourneyProcessor.getTourneySubscriptions(sessionFactory.getCurrentSession(), tourney.getNumber(), 1);
		} finally {
			lock.unlock();
		}
	}

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public TourneySubscription getSubscription(Tourney tourney, Personality player) {
		lock.lock();
		try {
			return (TourneySubscription) sessionFactory.getCurrentSession().get(HibernateTourneySubscription.class,
					new HibernateTourneySubscription.Id(player.getId(), tourney.getNumber(), 1));
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
				final Tourney.Id id1 = Tourney.Id.class.cast(id);
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
	@SuppressWarnings("unchecked")
	public <Ctx extends TourneyEntity.Context<? extends RegularTourneyEntity, ?>> int getTotalCount(Personality person, Ctx context) {
		lock.lock();
		try {
			final Query query = createEntityQuery(context, true);
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
			final Query query1 = createEntityQuery(context, false);
			if (range != null) {
				range.apply(query1);
			}
			return query1.list();
		} finally {
			lock.unlock();
		}
	}

	private Query createEntityQuery(TourneyEntity.Context<?, ?> context, boolean count) {
		final Session session = sessionFactory.getCurrentSession();

		Query query = null;
		if (Tourney.Context.class.isAssignableFrom(context.getClass())) {
			final Tourney.Context ctx = Tourney.Context.class.cast(context);
			query = session.createQuery((count ? "select count(*) " : "") + "from HibernateTourney t " + convertStateToQuery(ctx.getStates(), "t", "where"));
		} else if (TourneyDivision.Context.class.isAssignableFrom(context.getClass())) {
			final TourneyDivision.Context ctx = TourneyDivision.Context.class.cast(context);
			query = session.createQuery((count ? "select count(*) " : "") +
					"from HibernateTourneyDivision d " +
					"where d.tourney.number=:tourney " +
					(ctx.getLanguage() != null ? " and d.language=:language " : "") +
					(ctx.getSection() != null ? " and d.section=:section " : "") +
					convertStateToQuery(ctx.getStates(), "d", "and"));
			query.setParameter("tourney", ctx.getTourneyId().getNumber());
			if (ctx.getLanguage() != null) {
				query.setParameter("language", ctx.getLanguage());
			}
			if (ctx.getSection() != null) {
				query.setParameter("section", ctx.getSection());
			}
		} else if (TourneyRound.Context.class.isAssignableFrom(context.getClass())) {
			return createRoundQuery(session, TourneyRound.Context.class.cast(context), count);
		} else if (TourneyGroup.Context.class.isAssignableFrom(context.getClass())) {
			return createGroupQuery(session, TourneyGroup.Context.class.cast(context), count);
		}
		return query;
	}

	private Query createRoundQuery(Session session, TourneyRound.Context context, boolean count) {
		if (context.getDivisionId() != null) {
			Query query = session.createQuery((count ? "select count(*) " : "") +
					"from HibernateTourneyRound r " +
					"where r.division.tourney.number=:tourney and " +
					"r.division.language=:language and r.division.section=:section " +
					convertStateToQuery(context.getStates(), "r", "and"));
			query.setParameter("tourney", context.getDivisionId().getTourneyId().getNumber());
			query.setParameter("language", context.getDivisionId().getLanguage());
			query.setParameter("section", context.getDivisionId().getSection());
			return query;
		} else if (context.getTourneyId() != null) {
			Query query = session.createQuery((count ? "select count(*) " : "") +
					"from HibernateTourneyRound r " +
					"where r.division.tourney.number=:tourney " +
					convertStateToQuery(context.getStates(), "r", "and"));
			query.setParameter("tourney", context.getTourneyId().getNumber());
			return query;
		} else {
			throw new IllegalArgumentException("Invalid group context: " + context);
		}
	}

	private Query createGroupQuery(Session session, TourneyGroup.Context context, boolean count) {
		if (context.getRoundId() != null) {
			Query query = session.createQuery((count ? "select count(*) " : "") +
					"from HibernateTourneyGroup g " +
					"where g.round.division.tourney.number=:tourney and " +
					"g.round.division.language=:language and g.round.division.section=:section " +
					"and g.round.round=:round " +
					convertStateToQuery(context.getStates(), "g", "and"));
			query.setInteger("tourney", context.getRoundId().getDivisionId().getTourneyId().getNumber());
			query.setParameter("language", context.getRoundId().getDivisionId().getLanguage());
			query.setParameter("section", context.getRoundId().getDivisionId().getSection());
			query.setInteger("round", context.getRoundId().getRound());
			return query;
		} else if (context.getPersonality() != null) {
			Query query = session.createQuery((count ? "select count(*) " : "") +
					"from HibernateTourneyGroup g " +
					"where (g.player1=:pid or g.player2=:pid or g.player3=:pid or g.player4=:pid) " +
					convertStateToQuery(context.getStates(), "g", "and"));
			query.setLong("pid", context.getPersonality().getId());
			return query;
		} else {
			throw new IllegalArgumentException("Invalid group context: " + context);
		}
	}

	@Override
	public <Ctx extends TourneyEntity.Context<? extends RegularTourneyEntity, ?>, Fl extends SearchFilter> int getFilteredCount(Personality person, Ctx context, Fl filter) {
		return getTotalCount(person, context);
	}

	@Override
	public <Ctx extends TourneyEntity.Context<? extends RegularTourneyEntity, ?>, Fl extends SearchFilter> List<RegularTourneyEntity> searchEntities(Personality person, Ctx context, Fl filter, Orders orders, Range range) {
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

	private String convertStateToQuery(EnumSet<TourneyEntity.State> states, String name, String prefix) {
		if (states == null) {
			return "";
		}

		final StringBuilder res = new StringBuilder();
		if (states.contains(TourneyEntity.State.SCHEDULED)) {
			res.append("(").append(name).append(".startedDate is null and ").append(name).append(".finishedDate is null)");
		}
		if (states.contains(TourneyEntity.State.ACTIVE)) {
			if (res.length() != 0) {
				res.append(" or ");
			}
			res.append("(").append(name).append(".startedDate is not null and ").append(name).append(".finishedDate is null)");
		}
		if (states.contains(TourneyEntity.State.FINISHED)) {
			if (res.length() != 0) {
				res.append(" or ");
			}
			res.append("(").append(name).append(".finishedDate is not null)");
		}
		if (res.length() != 0) {
			res.insert(0, " (");
			res.insert(0, prefix);
			res.append(")");
		}
		return res.toString();
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
					tourneyProcessor.initiateDivisions(sessionFactory.getCurrentSession(), boardManager, settingsProvider);
				} catch (Exception ex) {
					log.error("Divisions can't be initiated by internal error", ex);
					throw new TourneyProcessingException("Divisions can't be initiated by internal error", ex);
				} finally {
					lock.unlock();
				}
			}
		});
	}

	private void finalizeTourneyEntities(final GameBoard<?, ?> board) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				lock.lock();
				try {
					tourneyProcessor.finalizeDivisions(sessionFactory.getCurrentSession(), board, subscriptionListeners);
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
					tourneyProcessor.finalizeTourneys(sessionFactory.getCurrentSession(), tourneyListeners);
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
		final Query query = session.createQuery("select 1 from HibernateTourney t where t.scheduledDate = :scheduledDate");
		query.setParameter("scheduledDate", scheduledDate);
		if (query.uniqueResult() != null) {
			return null;
		}

		int number = 1;
		final Number n = (Number) session.createQuery("select max(number) from HibernateTourney ").uniqueResult();
		if (n != null) {
			number = n.intValue() + 1;
		}

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
	}

	public void setCronExpression(CronExpression cronExpression) {
		this.cronExpression = cronExpression;
		if (this.cronExpression != null) {
			this.cronExpression.setTimeZone(TimeZone.getTimeZone("GMT"));
		}
	}

	public void setBoardManager(BoardManager<S, ?> boardManager) {
		if (this.boardManager != null) {
			this.boardManager.removeBoardStateListener(boardStateListener);
		}

		this.boardManager = boardManager;

		if (this.boardManager != null) {
			this.boardManager.addBoardStateListener(boardStateListener);
		}
	}

	public void setSettingsProvider(GameSettingsProvider<S, TourneyGroup> settingsProvider) {
		this.settingsProvider = settingsProvider;
	}

	private class TheBoardStateListener implements BoardStateListener {
		private TheBoardStateListener() {
		}

		@Override
		public void gameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
		}

		@Override
		public void gameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move, GameMoveScore moveScore) {
		}

		@Override
		public void gameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameResolution resolution, Collection<? extends GamePlayerHand> winners) {
			finalizeTourneyEntities(board);
		}
	}
}
