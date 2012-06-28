package wisematches.playground.tournament.impl.fsm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.personality.Personality;
import wisematches.playground.*;
import wisematches.playground.task.AssuredTaskExecutor;
import wisematches.playground.tournament.*;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TournamentStateMachine<S extends GameSettings, B extends GameBoard<S, ?>> {
	private volatile boolean started;

	private RatingManager ratingManager;
	private BoardManager<S, B> boardManager;
	private TournamentGameSettingsProvider<S> gameSettingsProvider;

	private TSMDataProcessor dataProcessor;
	private TSMEventProcessor eventProcessor;
	private AssuredTaskExecutor<TSMActivity, TSMActivityContext> assuredTaskExecutor;

	private final Lock lock = new ReentrantLock();
	private final TheBoardStateListener boardStateListener = new TheBoardStateListener();

	private static final Log log = LogFactory.getLog("wisematches.server.tournament.initializer");

	public TournamentStateMachine() {
	}

	/**
	 * Initializes the state machine and start internal work.
	 *
	 * @param dataProcessor       the data process that will be used to load or store required data
	 * @param eventProcessor      will be notified about any internal events.
	 * @param assuredTaskExecutor will be used to process all internal tasks.
	 */
	public void startEngine(TSMDataProcessor dataProcessor, TSMEventProcessor eventProcessor, AssuredTaskExecutor<TSMActivity, TSMActivityContext> assuredTaskExecutor) {
		if (started) {
			throw new IllegalStateException("Already started. Please create new one.");
		}
		this.dataProcessor = dataProcessor;
		this.eventProcessor = eventProcessor;
		this.assuredTaskExecutor = assuredTaskExecutor;

		assuredTaskExecutor.registerProcessor(TSMActivity.createTaskProcessor(this));

		started = true;
	}

	/**
	 * Initiate internal checks and start appropriate activities is required.
	 * <p/>
	 * This method usually should be executed once a day.
	 */
	public void startMoving() {
		lock.lock();
		try {
			final Collection<Tournament> tournaments = dataProcessor.getReadyTournaments();
			for (Tournament tournament : tournaments) {
				assuredTaskExecutor.execute(TSMActivity.INITIATE_TOURNAMENT, new InitializingTournament(tournament.getNumber()));
			}

			final Collection<TournamentGroup> groups = dataProcessor.getReadyGroups();
			for (TournamentGroup group : groups) {
				assuredTaskExecutor.execute(TSMActivity.FINALIZE_GROUP, new FinalizingGroup(new TournamentGroupCtx(group)));
			}
		} finally {
			lock.unlock();
		}
	}

	void processInitiateTournament(InitializingTournament ctx) {
		lock.lock();
		try {
			final Tournament tournament = dataProcessor.startTournament(ctx.getNumber());
			assuredTaskExecutor.execute(TSMActivity.INITIATE_ROUND, new InitializingRound(ctx.getNumber(), 0));
			eventProcessor.fireTournamentStarted(tournament);
		} finally {
			lock.unlock();
		}
	}

	void processInitiateRound(InitializingRound ctx) {
		lock.lock();
		try {
			final Collection<TournamentSubscription> subscriptions = dataProcessor.getUnprocessedPlayers(ctx.getTournament(), ctx.getRound());
			final Collection<InitializingGroup> tournamentGroups = createTournamentGroups(ctx, subscriptions);
			for (InitializingGroup tournamentGroup : tournamentGroups) {
				assuredTaskExecutor.execute(TSMActivity.INITIATE_GROUP, tournamentGroup);
			}
		} finally {
			lock.unlock();
		}
	}

	void processInitiateGroup(InitializingGroup ctx) {
		lock.lock();
		try {
			final TournamentGroupCtx context = ctx.getGroupContext();
			final TournamentGroup group = dataProcessor.startGroup(context, ctx.getPlayers());
			if (group != null) {
				final S settings = gameSettingsProvider.createTournamentSettings(context);

				int index = 0;
				final int length = ctx.getPlayers().length;
				final long[] games = new long[(length * (length - 1)) / 2];
				for (int i = 0; i < length; i++) {
					final Personality p1 = ctx.getPlayers()[i];
					for (int j = i + 1; j < length; j++) {
						final Personality p2 = ctx.getPlayers()[j];
						final List<Personality> players = Arrays.asList(p1, p2);
						try {
							games[index++] = boardManager.createBoard(settings, players).getBoardId();
						} catch (BoardCreationException e) {
							log.error("Tournament game can't be created: " + settings + ", " + players, e);
							throw new IllegalStateException("Tournament game can't be created: " + settings + ", " + players, e);
						}
					}
				}
				dataProcessor.startGroupGame(context, games);
			}
		} finally {
			lock.unlock();
		}
	}

	void processFinalizeGame(FinalizingGame ctx) {
		lock.lock();
		try {
			TournamentGroup group = dataProcessor.getGameGroup(ctx.getBoardId());
			if (group != null && !group.isFinished()) {
				final B board = boardManager.openBoard(ctx.getBoardId());
				final List<? extends GamePlayerHand> hands = board.getPlayersHands();

				int index = 0;
				final short[] points = new short[hands.size()];
				for (GamePlayerHand hand : hands) {
					points[index++] = hand.getPoints();
				}

				final TournamentGroupCtx groupCtx = new TournamentGroupCtx(group);
				dataProcessor.finishGroupGame(groupCtx, board.getBoardId(), points).isFinished();
			}
		} catch (BoardLoadingException ex) {
			log.error("Tournament game can't be finalized: " + ctx.getBoardId(), ex);
			throw new IllegalStateException("Tournament game can't be finalized: " + ctx.getBoardId(), ex);
		} finally {
			lock.unlock();
		}
	}

	void processFinalizeGroup(FinalizingGroup ctx) {
		lock.lock();
		try {
			final TournamentGroup group = dataProcessor.getGameGroup(ctx.getGroupCtx());
			if (group != null && !group.isFinished()) {
				dataProcessor.finishGroup(ctx.getGroupCtx());
				eventProcessor.fireGroupFinished(group);
			}
		} finally {
			lock.unlock();
		}
	}

	void processFinalizeRound(FinalizingRound ctx) {
		lock.lock();
		try {

		} finally {
			lock.unlock();
		}
	}

	void processFinalizeTournament(FinalizingTournament ctx) {
		lock.lock();
		try {

		} finally {
			lock.unlock();
		}
	}

	protected Collection<InitializingGroup> createTournamentGroups(InitializingRound round, Collection<TournamentSubscription> subscriptions) {
		log.info("Found " + subscriptions.size() + " subscriptions");

		final Map<TournamentSection, Collection<Long>> groups = new HashMap<TournamentSection, Collection<Long>>();
		for (TournamentSubscription subscription : subscriptions) {
			final short rating = ratingManager.getRating(Personality.person(subscription.getPlayer()));
			final TournamentSection section = getPlayerSection(subscription, rating);

			Collection<Long> longs = groups.get(section);
			if (longs == null) {
				longs = new ArrayList<Long>();
				groups.put(section, longs);
			}
		}
		log.info("Found " + groups.size() + " registered groups");

		final Collection<Long> movingPlayers = new ArrayList<Long>();
		for (TournamentSection section : TournamentSection.values()) {
			final Collection<Long> longs = groups.get(section);
			if (longs == null) {
				continue;
			}

			longs.addAll(movingPlayers); // add from previous group
			movingPlayers.clear();

			if (longs.size() == 1) {
				movingPlayers.addAll(groups.remove(section));
			}
		}

		final Collection<TournamentGroupCtx> res = new ArrayList<TournamentGroupCtx>();
		for (Map.Entry<TournamentSection, Collection<Long>> entry : groups.entrySet()) {
//			res.add(new TournamentGroupCtx(tournament, round, entry.getKey(), ));
//			TournamentGroup group = new HibernateTournamentGroup(tournament, round, );
		}
		throw new UnsupportedOperationException("Not implemented");
//		return null;
	}

	protected TournamentSection getPlayerSection(TournamentSubscription subscription, short rating) {
		TournamentSection section = subscription.getSection(); // current or next
		while (!section.isRatingAllowed(rating)) {
			section = section.getHigherSection();
		}
		return section;
	}

	private final class TheBoardStateListener implements BoardStateListener {
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
			assuredTaskExecutor.execute(TSMActivity.FINALIZE_GAME, new FinalizingGame(board.getBoardId()));
		}
	}
}

