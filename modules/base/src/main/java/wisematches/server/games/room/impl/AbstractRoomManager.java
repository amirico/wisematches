package wisematches.server.games.room.impl;

import org.apache.commons.logging.Log;
import wisematches.kernel.player.Player;
import wisematches.server.games.board.*;
import wisematches.server.games.room.*;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class implements base methods of <code>RoomManager</code> class, like works with listeners and so on.
 * <p/>
 * This implementation of <code>RoomManager</code> contains map of all opened boards with attached listeners with
 * attached listeners. This map is weak map and boards are removed from it automatical when board doesn't required
 * any more.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public abstract class AbstractRoomManager<B extends GameBoard<S, ?>, S extends GameSettings> implements RoomManager<B, S>, RoomManagerFacade<B, S> {
	private final Log log;
	private final Room room;

	private final BoardsMap<B> boardsMap;
	private final Collection<B> waitingGameBoards = Collections.synchronizedCollection(new ArrayList<B>());

	private final Lock openBoardLock = new ReentrantLock();
	private final Lock initialazingWaitingsLock = new ReentrantLock();

	private volatile boolean waitingGameInitialized = false;

	private final Collection<RoomSeatesListener> seatesListeners = new CopyOnWriteArraySet<RoomSeatesListener>();
	private final Collection<RoomBoardsListener> boardsListeners = new CopyOnWriteArraySet<RoomBoardsListener>();
	private final Collection<GamePlayersListener> playersListeners = new CopyOnWriteArraySet<GamePlayersListener>();
	private final Collection<GameMoveListener> movesListeners = new CopyOnWriteArraySet<GameMoveListener>();
	private final Collection<GameStateListener> statesListeners = new CopyOnWriteArraySet<GameStateListener>();

	/**
	 * Creates new room manager for specified room.
	 *
	 * @param room the room that this manager is implemented.
	 * @param log  logger for this room.
	 */
	protected AbstractRoomManager(Room room, Log log) {
		if (room == null) {
			throw new NullPointerException("Room is null");
		}
		this.log = log;
		this.room = room;

		boardsMap = new BoardsMap<B>(this);
	}

	public void addRoomSeatesListener(RoomSeatesListener roomListener) {
		seatesListeners.add(roomListener);
	}

	public void removeRoomSeatesListener(RoomSeatesListener roomListener) {
		seatesListeners.remove(roomListener);
	}

	public void addRoomBoardsListener(RoomBoardsListener roomBoardsListener) {
		boardsListeners.add(roomBoardsListener);
	}

	public void removeRoomBoardsListener(RoomBoardsListener roomBoardsListener) {
		boardsListeners.remove(roomBoardsListener);
	}

	public void addGamePlayersListener(GamePlayersListener listener) {
		playersListeners.add(listener);
	}

	public void removeGamePlayersListener(GamePlayersListener listener) {
		playersListeners.remove(listener);
	}

	public void addGameMoveListener(GameMoveListener listener) {
		movesListeners.add(listener);
	}

	public void removeGameMoveListener(GameMoveListener listener) {
		movesListeners.remove(listener);
	}

	public void addGameStateListener(GameStateListener listener) {
		statesListeners.add(listener);
	}

	public void removeGameStateListener(GameStateListener listener) {
		statesListeners.remove(listener);
	}

	public Room getRoomType() {
		return room;
	}

	public RoomManager<B, S> getRoomManager() {
		return this;
	}

	public B createBoard(Player owner, S gameSettings) throws BoardCreationException {
		if (log.isInfoEnabled()) {
			log.info("Creating new board: settings - " + gameSettings + ", owner - " + owner);
		}
		final B board = createBoard(gameSettings);
		board.addPlayer(owner);

		openBoardLock.lock();
		try {
			saveBoard(board);
			initializeBoard(board);
			processPlayerAdded(board, owner);
		} finally {
			openBoardLock.unlock();
		}

		return board;
	}

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

			final B loaded = loadBoard(gameId);
			if (loaded == null) {
				if (log.isDebugEnabled()) {
					log.debug("Board is inknown");
				}
				return null;
			}
			if (log.isDebugEnabled()) {
				log.debug("Board loaded from storage");
			}
			initializeBoard(loaded);
			return loaded;
		} finally {
			openBoardLock.unlock();
		}
	}

	public Collection<B> getOpenedBoards() {
		openBoardLock.lock();
		try {
			return boardsMap.values();
		} finally {
			openBoardLock.unlock();
		}
	}

	public void updateBoard(B board) {
		saveBoard(board);
	}

	/**
	 * Returns waiting boards. When this method is invoked first time it calls <code>loadWaitingBoards</code>
	 * to load waiting boards from storage. And save its in memory. Any later invokations just returns memory
	 * copy of these boards.
	 *
	 * @return unmodifiabled collection of waiting boards.
	 */
	public Collection<B> getWaitingBoards() {
		if (!waitingGameInitialized) {
			initialazingWaitingsLock.lock();
			try {
				if (!waitingGameInitialized) {
					final Collection<Long> longCollection = loadWaitingBoards();
					if (log.isInfoEnabled()) {
						log.info("Loading waiting games from storage for ids: " + longCollection);
					}
					for (Long boardId : longCollection) {
						try {
							final B b = openBoard(boardId);
							if (b != null) {
								waitingGameBoards.add(b);
							} else {
								log.warn("Id of waiting board was loaded but board is unknown.");
							}
						} catch (BoardLoadingException ex) {
							log.error("Board can't be loadded", ex);
						}
					}
					waitingGameInitialized = true;
				}
			} finally {
				initialazingWaitingsLock.unlock();
			}
		}
		return Collections.unmodifiableCollection(waitingGameBoards);
	}

	/**
	 * {@inheritDoc}
	 */
	public Collection<B> getActiveBoards(Player player) {
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
	 * Attaches a board listener to specified board and adds it to boards map.
	 * <p/>
	 * This is not synchronized method and should be synchronized externally.
	 *
	 * @param board the board to be initializing.
	 */
	private void initializeBoard(B board) {
		TheBoardListener boardListener = new TheBoardListener(board);

		board.addGameMoveListener(boardListener);
		board.addGamePlayersListener(boardListener);
		board.addGameStateListener(boardListener);

		boardsMap.addBoard(board);
	}

	/**
	 * Helper method that is called when board seats are appeared. This method adds
	 * board to waiting collection and invokes <code>boardSeatsAppeared</code> method for each listener.
	 *
	 * @param board  the board that has a seates.
	 * @param player the player who enter a game
	 */
	private void processPlayerAdded(B board, Player player) {
		final int maxPlayers = board.getGameSettings().getMaxPlayers();
		final int playersCount = board.getPlayersHands().size();

		if (playersCount == 1) {
			waitingGameBoards.add(board);
		} else if (playersCount == maxPlayers) {
			waitingGameBoards.remove(board);
		}

		final RoomSeatesEvent event = new RoomSeatesEvent(room, board, player);
		for (RoomSeatesListener listener : seatesListeners) {
			listener.playerSitDown(event);
		}

		for (GamePlayersListener playersListener : playersListeners) {
			playersListener.playerAdded(board, player);
		}
	}

	/**
	 * Helper method that is called when board seats are ended. This method removes
	 * board from waiting collection and invokes <code>boardSeatsEnded</code> method for each listener.
	 *
	 * @param board  the board that does not have seats any more.
	 * @param player the player who leave a game.
	 */
	private void processPlayerRemoved(B board, Player player) {
		if (board.getPlayersHands().size() == 0) {
			waitingGameBoards.remove(board);
		} else {
			waitingGameBoards.add(board);
		}

		final RoomSeatesEvent event = new RoomSeatesEvent(room, board, player);
		for (RoomSeatesListener listener : seatesListeners) {
			listener.playerStandUp(event);
		}

		for (GamePlayersListener playersListener : playersListeners) {
			playersListener.playerRemoved(board, player);
		}
	}

	private void fireBoardOpened(long boardId) {
		for (RoomBoardsListener boardsListener : boardsListeners) {
			boardsListener.boardOpened(room, boardId);
		}
	}

	private void fireBoardClosed(long boardId) {
		for (RoomBoardsListener boardsListener : boardsListeners) {
			boardsListener.boardClosed(room, boardId);
		}
	}

	/** ========== Definitions of abstract methods ================= */

	/**
	 * Loads game board frome storage by specified game id.
	 * <p/>
	 * This method is used from {@link #openBoard(long)}. You MUST NOT use this method directly because in
	 * this case board will not be stored in boards map and will not be tracked for changes.
	 *
	 * @param gameId the id of game that must be loaded.
	 * @return the loaded game board or <code>null</code> if no game with specified id.
	 * @throws wisematches.server.games.room.BoardLoadingException
	 *          if board can't be loaded by some reasones.
	 */
	protected abstract B loadBoard(long gameId) throws BoardLoadingException;

	/**
	 * Creates new board with specified settings.
	 *
	 * @param gameSettings the game settings.
	 * @return the created game board.
	 * @throws BoardCreationException if board can't be created by some reasones.
	 */
	protected abstract B createBoard(S gameSettings) throws BoardCreationException;

	/**
	 * Saves specified board to the storage.
	 *
	 * @param board the board to be saved.
	 */
	protected abstract void saveBoard(B board);

	/**
	 * Loads ids of active boards for specified player.
	 *
	 * @param player the player whos boards should be loaded.
	 * @return the collection of board's ids for specified player or empty collection.
	 */
	protected abstract Collection<Long> loadActivePlayerBoards(Player player);

	/**
	 * Loads ids of waiting boards.
	 *
	 * @return the collection of board's ids that waiting players or empty collection.
	 */
	protected abstract Collection<Long> loadWaitingBoards();

	/* ======================== Inner classes defenitions. ================ */

	private class TheBoardListener implements GameMoveListener, GameStateListener, GamePlayersListener {
		private final B gameBoard;

		TheBoardListener(B gameBoard) {
			this.gameBoard = gameBoard;
		}

		public void playerMoved(GameMoveEvent event) {
			saveBoard(gameBoard);

			for (GameMoveListener movesListener : movesListeners) {
				movesListener.playerMoved(event);
			}
		}

		public void gameStarted(GameBoard board, GamePlayerHand playerTurn) {
			saveBoard(gameBoard);

			for (GameStateListener statesListener : statesListeners) {
				statesListener.gameStarted(board, playerTurn);
			}
		}

		public void gameFinished(GameBoard board, GamePlayerHand wonPlayer) {
			saveBoard(gameBoard);


			for (GameStateListener statesListener : statesListeners) {
				statesListener.gameFinished(board, wonPlayer);
			}
		}

		public void gameDraw(GameBoard board) {
			saveBoard(gameBoard);

			for (GameStateListener statesListener : statesListeners) {
				statesListener.gameDraw(board);
			}
		}

		public void gameInterrupted(GameBoard board, GamePlayerHand interrupterPlayer, boolean byTimeout) {
			saveBoard(gameBoard);


			for (GameStateListener statesListener : statesListeners) {
				statesListener.gameInterrupted(board, interrupterPlayer, byTimeout);
			}
		}

		public void playerAdded(GameBoard board, Player player) {
			saveBoard(gameBoard);

			processPlayerAdded(gameBoard, player);
		}

		public void playerRemoved(GameBoard board, Player player) {
			saveBoard(gameBoard);

			processPlayerRemoved(gameBoard, player);
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
		private final ReferenceQueue<B> boardsQueue = new ReferenceQueue<B>();
		private final Map<Long, BoardWeakReference<B>> boardsReferences = new HashMap<Long, BoardWeakReference<B>>();

		private AbstractRoomManager roomManager;
		private final Log log;

		BoardsMap(AbstractRoomManager roomManager) {
			this.roomManager = roomManager;
			this.log = roomManager.log;
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
			roomManager.fireBoardOpened(id);
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
				roomManager.fireBoardClosed(id);

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
}
