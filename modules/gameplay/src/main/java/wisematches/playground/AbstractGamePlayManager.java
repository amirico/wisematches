package wisematches.playground;

import org.apache.commons.logging.Log;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import wisematches.core.Personality;
import wisematches.core.Player;
import wisematches.core.Robot;
import wisematches.core.RobotType;
import wisematches.playground.tracking.StatisticManager;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class implements base methods of <code>BoardManager</code> class, like works with boardListeners and so on.
 * <p/>
 * This implementation of <code>BoardManager</code> contains map of all opened boards with attached boardListeners with
 * attached boardListeners. This map is weak map and boards are removed from it automatical when board doesn't required
 * any more.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public abstract class AbstractGamePlayManager<S extends GameSettings, B extends AbstractGameBoard<S, ?>>
		implements GamePlayManager<S, B> {
	private RatingSystem ratingSystem;

	private TaskExecutor taskExecutor;
	private StatisticManager statisticManager;

	private final Log log;
	private final Lock openBoardLock = new ReentrantLock();

	private final BoardsMap<B> boardsCache;
	private final Set<RobotType> robotTypes = new HashSet<>();

	private final BoardListener gameBoardListener = new TheBoardListener();
	private final Collection<BoardListener> listeners = new CopyOnWriteArraySet<>();

	/**
	 * Creates new room manager for specified room.
	 *
	 * @param log logger for this room.
	 */
	protected AbstractGamePlayManager(Log log, Set<RobotType> robotTypes) {
		this.log = log;
		boardsCache = new BoardsMap<>(log);

		if (robotTypes != null) {
			this.robotTypes.addAll(robotTypes);
		}
	}

	@Override
	public void addBoardListener(BoardListener l) {
		if (l != null) {
			listeners.add(l);
		}
	}

	@Override
	public void removeBoardListener(BoardListener l) {
		listeners.remove(l);
	}

	@Override
	public Set<RobotType> getSupportedRobots() {
		return robotTypes;
	}

	@Override
	public B createBoard(S settings, Player player, RobotType robotType) throws BoardCreationException {
		if (!robotTypes.contains(robotType)) {
			throw new BoardCreationException("Unsupported robot type: " + robotType);
		}
		final B gameBoard = createGameBoard(settings, Arrays.asList(player, robotType.getPlayer()), null);
		processRobotMove(gameBoard, 1);
		return gameBoard;
	}

	@Override
	public B createBoard(S settings, Collection<Player> players, GameRelationship relationship) throws BoardCreationException {
		if (log.isDebugEnabled()) {
			log.debug("Creating new board: settings - " + settings + ", players - " + players);
		}
		return createGameBoard(settings, new ArrayList<Personality>(players), relationship);
	}

	@Override
	public B openBoard(long boardId) throws BoardLoadingException {
		B board = boardsCache.getBoard(boardId);
		if (board != null) {
			if (log.isDebugEnabled()) {
				log.debug("Board found in memory cache");
			}
			return board;
		}

		openBoardLock.lock();
		try {
			board = boardsCache.getBoard(boardId);  //Double check for optimization.
			if (board != null) {
				return board;
			}

			if (log.isInfoEnabled()) {
				log.info("Loading board from DB: " + boardId);
			}
			final B loaded = loadBoardImpl(boardId);
			if (loaded == null) {
				if (log.isDebugEnabled()) {
					log.debug("Board is unknown");
				}
				return null;
			}
			loaded.setBoardListener(gameBoardListener);
			boardsCache.addBoard(loaded);
			return loaded;
		} finally {
			openBoardLock.unlock();
		}
	}


	/**
	 * Saves specified board to the storage.
	 *
	 * @param board the board to be saved.
	 */
	protected abstract void saveBoardImpl(B board);

	/**
	 * Loads game board frome storage by specified game id.
	 * <p/>
	 * This method is used from {@link #openBoard(long)}. You MUST NOT use this method directly because in
	 * this case board will not be stored in boards map and will not be tracked for changes.
	 *
	 * @param gameId the id of game that must be loaded.
	 * @return the loaded game board or <code>null</code> if no game with specified id.
	 * @throws BoardLoadingException if board can't be loaded by some reasones.
	 */
	protected abstract B loadBoardImpl(long gameId) throws BoardLoadingException;

	/**
	 * Creates new board with specified settings.
	 *
	 * @param settings     the game settings.
	 * @param players      the list of board players.  @return the created game board.
	 * @param relationship game relationship.
	 * @throws BoardCreationException if board can't be created by some reasones.
	 */
	protected abstract B createBoardImpl(S settings, Collection<Personality> players, GameRelationship relationship) throws BoardCreationException;


	/**
	 * Returns collection of all robot games where it's robot's turn.
	 *
	 * @return the collection of all robot games where it's robot's turn.
	 */
	protected abstract Collection<Long> loadActiveRobotGames();

	/**
	 * Processes robot's move for specified board.
	 *
	 * @param board  the board there robot must make a move.
	 * @param player the robot player.
	 */
	protected abstract void processRobotMove(B board, Robot player);


	private B createGameBoard(S settings, Collection<Personality> players, GameRelationship relationship) throws BoardCreationException {
		final B board = createBoardImpl(settings, players, relationship);

		openBoardLock.lock();
		try {
			saveBoardImpl(board);
			board.setBoardListener(gameBoardListener);
			boardsCache.addBoard(board);

			for (BoardListener listener : listeners) {
				listener.gameStarted(board);
			}
		} finally {
			openBoardLock.unlock();
		}
		return board;
	}

	private void recalculatePlayerRatings(GameBoard<?, ? extends AbstractPlayerHand> board) {
		final List<Personality> players = board.getPlayers();

		final AbstractPlayerHand[] hands = new AbstractPlayerHand[players.size()];
		final short[] points = new short[players.size()];
		final short[] oldRatings = new short[players.size()];
		for (int i = 0; i < players.size(); i++) {
			final Personality player = players.get(i);
			final AbstractPlayerHand hand = board.getPlayerHand(player);

			hands[i] = hand;
			points[i] = hand.getPoints();

			if (player instanceof Robot) {
				oldRatings[i] = ((Robot) player).getRating();
			} else {
				oldRatings[i] = statisticManager.getRating((Player) player);
			}
		}

		short[] newRatings = oldRatings;
		if (board.isRated()) {
			newRatings = ratingSystem.calculateRatings(oldRatings, points);
		}

		for (int i = 0; i < hands.length; i++) {
			hands[i].updateRating(oldRatings[i], newRatings[i]);
		}
	}

	private void processRobotMove(final GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, final int attempt) {
		final Personality player = board.getPlayerTurn();
		if (player instanceof Robot) {
			final long boardId = board.getBoardId();
			log.info("Initialize robot activity [attempt=" + attempt + "] for board: " + boardId);

			try {
				if (TransactionSynchronizationManager.isSynchronizationActive()) {
					TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
						@Override
						public void afterCompletion(int status) {
							taskExecutor.execute(new MakeRobotTurnTask(boardId, attempt));
						}
					});
				} else {
					taskExecutor.execute(new MakeRobotTurnTask(boardId, attempt));
				}
			} catch (Throwable th) {
				log.error("", th);
				taskExecutor.execute(new MakeRobotTurnTask(boardId, attempt));
			}
		}
	}


	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void setRatingSystem(RatingSystem ratingSystem) {
		this.ratingSystem = ratingSystem;
	}

	public void setStatisticManager(StatisticManager statisticManager) {
		this.statisticManager = statisticManager;
	}

	/* ======================== Inner classes definition ================ */
	class MakeRobotTurnTask implements Runnable {
		private final int attempt;
		private final long boardId;

		MakeRobotTurnTask(long boardId, int attempt) {
			this.boardId = boardId;
			this.attempt = attempt;
		}

		public void run() {
			try {
				log.info("Start robot action for board " + boardId);
				final B board = openBoard(boardId);
				final Personality playerTurn = board.getPlayerTurn();
				if (playerTurn instanceof Robot) {
					try {
						AbstractGamePlayManager.this.processRobotMove(board, (Robot) playerTurn);
						log.info("Robot made a turn for board " + boardId + ": " + playerTurn);
					} catch (Throwable th) {
						log.error("Robot can't make a turn [attempt=" + attempt + "] for board " + boardId, th);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException ignore) {
						}
						processRobotMove(board, attempt + 1);
					}
				} else {
					log.info("It's not robot turn for board " + boardId);
				}
			} catch (BoardLoadingException ex) {
				log.error("Board for robot's move can't be loaded: " + boardId, ex);
			}
		}
	}

	/**
	 * Weak boards map. It contains weak references to boards and automatical cleared when board doesn't required
	 * any more.
	 * <p/>
	 * We are not using <code>Map</code> interface because we don't required so many functionality and we don't want
	 * implement a lot of redundant methods.
	 * <p/>
	 * We can't use <code>WeakHashMap</code> because it use weak reference to key but we need weak reference to value.
	 * <p/>
	 * This class is package visible because we wrote unit test but we don't want export it.
	 *
	 * @param <B>
	 */
	static class BoardsMap<B extends GameBoard> {
		private final ReferenceQueue<B> boardsQueue = new ReferenceQueue<>();
		private final Map<Long, BoardWeakReference<B>> boardsReferences = new HashMap<>();

		private final Log log;

		BoardsMap(Log log) {
			this.log = log;
		}

		/**
		 * Adds specified board to this map.
		 *
		 * @param board board to be added.
		 * @throws IllegalArgumentException if board id is zero.
		 */
		void addBoard(B board) {
			final BoardWeakReference<B> value = new BoardWeakReference<>(board, boardsQueue);
			final long id = value.getBoardId();
			if (id == 0) {
				throw new IllegalArgumentException("Board id can't be zero");
			}
			if (log.isDebugEnabled()) {
				log.debug("Add board to boards map: " + id);
			}
			boardsReferences.put(id, value);
		}

		/**
		 * Returns board with specified board id. If no board with specified id <code>null</code> will be returned.
		 *
		 * @param boardId the board id
		 * @return the board with specified id or <code>null</code>.
		 */
		B getBoard(long boardId) {
			clearUnnecessaryBoards();

			final WeakReference<B> weakReference = boardsReferences.get(boardId);
			if (weakReference == null) {
				return null;
			}
			return weakReference.get();
		}

		int size() {
			clearUnnecessaryBoards();
			return boardsReferences.size();
		}

		private void clearUnnecessaryBoards() {
			Reference<? extends B> reference = boardsQueue.poll();
			while (reference != null) {
				final BoardWeakReference ref = (BoardWeakReference) reference;
				final long id = ref.getBoardId();
				if (log.isDebugEnabled()) {
					log.debug("Board is expired and removed from map: " + id);
				}
				boardsReferences.remove(id);
				reference = boardsQueue.poll();
			}
		}
	}

	/**
	 * This class contains weak reference to a board and id of this board. We must keep id of board because
	 * we are using this reference after board has been deleted and reference returns <code>null</code> as
	 * result of <code>get</code> operation.
	 *
	 * @param <B> the board that this reference is referenced to
	 */
	static class BoardWeakReference<B extends GameBoard> extends WeakReference<B> {
		private final long boardId;

		public BoardWeakReference(B referent, ReferenceQueue<B> q) {
			super(referent, q);
			boardId = referent.getBoardId();
		}

		public long getBoardId() {
			return boardId;
		}
	}

	private class TheBoardListener implements BoardListener {
		TheBoardListener() {
		}

		@Override
		public void gameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
			processRobotMove(board, 1);
		}

		@Override
		@SuppressWarnings("unchecked")
		public void gameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move, GameMoveScore moveScore) {
			saveBoardImpl((B) board);

			for (BoardListener statesListener : listeners) {
				statesListener.gameMoveDone(board, move, moveScore);
			}

			processRobotMove(board, 1);
		}

		@Override
		@SuppressWarnings("unchecked")
		public void gameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameResolution resolution, Collection<Personality> winners) {
			recalculatePlayerRatings((GameBoard<?, ? extends AbstractPlayerHand>) board);
			saveBoardImpl((B) board);

			for (BoardListener statesListener : listeners) {
				statesListener.gameFinished(board, resolution, winners);
			}
		}
	}
}
