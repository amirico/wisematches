package wisematches.playground;

import org.apache.commons.logging.Log;
import wisematches.server.personality.Personality;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
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
public abstract class AbstractBoardManager<S extends GameSettings, B extends GameBoard<S, ?>> implements BoardManager<S, B> {
	private final Log log;

	private final BoardsMap<B> boardsMap;

	private final Lock openBoardLock = new ReentrantLock();

	private final GameBoardListener gameBoardListener = new TheBoardListener();
	private final Collection<BoardStateListener> boardStateListeners = new CopyOnWriteArraySet<BoardStateListener>();

	/**
	 * Creates new room manager for specified room.
	 *
	 * @param log logger for this room.
	 */
	protected AbstractBoardManager(Log log) {
		this.log = log;
		boardsMap = new BoardsMap<B>(log);
	}

	@Override
	public void addBoardStateListener(BoardStateListener l) {
		if (l != null) {
			boardStateListeners.add(l);
		}
	}

	@Override
	public void removeBoardStateListener(BoardStateListener l) {
		boardStateListeners.remove(l);
	}

	@Override
	public B createBoard(S gameSettings, Collection<? extends Personality> players) throws BoardCreationException {
		if (log.isDebugEnabled()) {
			log.debug("Creating new board: settings - " + gameSettings + ", players - " + players);
		}
		final B board = createBoardImpl(gameSettings, players);

		openBoardLock.lock();
		try {
			saveBoardImpl(board);
			board.addGameBoardListener(gameBoardListener);
			boardsMap.addBoard(board);

			for (BoardStateListener listener : boardStateListeners) {
				listener.gameStarted((GameBoard) board);
			}
		} finally {
			openBoardLock.unlock();
		}
		return board;
	}

	@Override
	public B openBoard(long gameId) throws BoardLoadingException {
		if (log.isInfoEnabled()) {
			log.info("Opening board: " + gameId);
		}
		B board = boardsMap.getBoard(gameId);
		if (board != null) {
			if (log.isDebugEnabled()) {
				log.debug("Board found in memory cache");
			}
			return board;
		}

		openBoardLock.lock();
		try {
			board = boardsMap.getBoard(gameId);  //Double check for optimization.
			if (board != null) {
				return board;
			}

			final B loaded = loadBoardImpl(gameId);
			if (loaded == null) {
				if (log.isDebugEnabled()) {
					log.debug("Board is inknown");
				}
				return null;
			}
			if (log.isDebugEnabled()) {
				log.debug("Board loaded from storage");
			}
			loaded.addGameBoardListener(gameBoardListener);
			boardsMap.addBoard(loaded);
			return loaded;
		} finally {
			openBoardLock.unlock();
		}
	}

	@Override
	public Collection<B> getOpenedBoards() {
		openBoardLock.lock();
		try {
			return boardsMap.values();
		} finally {
			openBoardLock.unlock();
		}
	}

	@Override
	public Collection<B> getActiveBoards(Personality player) {
		if (log.isDebugEnabled()) {
			log.debug("get active boards for player: " + player);
		}

		final Collection<Long> longs = loadActivePlayerBoards(player);
		final Collection<B> res = new ArrayList<B>(longs.size());
		for (Long boardId : longs) {
			try {
				final B b = openBoard(boardId);
				if (b != null) {
					res.add(b);
				}
			} catch (BoardLoadingException ex) {
				log.error("Board can't be loaded", ex);
			}
		}
		return res;
	}

	/**
	 * Creates new board with specified settings.
	 *
	 * @param gameSettings the game settings.
	 * @param players	  the list of board players.
	 * @return the created game board.
	 * @throws BoardCreationException if board can't be created by some reasones.
	 */
	protected abstract B createBoardImpl(S gameSettings, Collection<? extends Personality> players) throws BoardCreationException;

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
	 * Saves specified board to the storage.
	 *
	 * @param board the board to be saved.
	 */
	protected abstract void saveBoardImpl(B board);

	/**
	 * Loads ids of active boards for specified player.
	 *
	 * @param player the player whos boards should be loaded.
	 * @return the collection of board's ids for specified player or empty collection.
	 */
	protected abstract Collection<Long> loadActivePlayerBoards(Personality player);

	/* ======================== Inner classes defenitions. ================ */

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
		private final ReferenceQueue<B> boardsQueue = new ReferenceQueue<B>();
		private final Map<Long, BoardWeakReference<B>> boardsReferences = new HashMap<Long, BoardWeakReference<B>>();

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
			final BoardWeakReference<B> value = new BoardWeakReference<B>(board, boardsQueue);
			final long id = value.getBoardId();
			if (id == 0) {
				throw new IllegalArgumentException("Board id can't be zero");
			}
			if (log.isDebugEnabled()) {
				log.debug("Add board to boards map: " + id);
			}
			boardsReferences.put(id, value);
		}

		Collection<B> values() {
			final Collection<BoardWeakReference<B>> referenceCollection = boardsReferences.values();
			final Collection<B> res = new ArrayList<B>(referenceCollection.size());
			for (BoardWeakReference<B> reference : referenceCollection) {
				final B b = reference.get();
				if (b != null) {
					res.add(b);
				}
			}
			return res;
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
				if (log.isInfoEnabled()) {
					log.info("Board is expired and removed from map: " + id);
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

	private class TheBoardListener implements GameBoardListener {
		TheBoardListener() {
		}

		@Override
		@SuppressWarnings("unchecked")
		public void gameMoveDone(GameBoard board, GameMove move) {
			saveBoardImpl((B) board);

			for (GameBoardListener statesListener : boardStateListeners) {
				statesListener.gameMoveDone(board, move);
			}
		}

		@Override
		@SuppressWarnings("unchecked")
		public <S extends GameSettings, P extends GamePlayerHand> void gameFinished(GameBoard<S, P> board, GameResolution resolution, Collection<P> wonPlayers) {
			saveBoardImpl((B) board);

			for (GameBoardListener statesListener : boardStateListeners) {
				statesListener.gameFinished(board, resolution, wonPlayers);
			}
		}
	}
}
