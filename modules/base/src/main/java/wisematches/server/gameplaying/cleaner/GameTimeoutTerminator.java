package wisematches.server.gameplaying.cleaner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.server.gameplaying.board.*;
import wisematches.server.gameplaying.room.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GameTimeoutTerminator {
	private RoomsManager roomsManager;
	private TransactionTemplate transactionTemplate;

	private final Lock terminatorLock = new ReentrantLock();

	private final Map<Long, GameTerminationTask> scheduledBoards = new HashMap<Long, GameTerminationTask>();

	private final Timer terminatorTimer = new Timer("GameTimeoutTerminator");
	private final TheRoomBoardsListener roomBoardsListener = new TheRoomBoardsListener();
	private final Collection<GameTimeoutListener> listeners = new CopyOnWriteArraySet<GameTimeoutListener>();

	private static final Log log = LogFactory.getLog("wisematches.server.terminator");

	public GameTimeoutTerminator() {
	}

	public void addGameTimeoutListener(GameTimeoutListener l) {
		listeners.add(l);
	}

	public void removeGameTimeoutListener(GameTimeoutListener l) {
		listeners.remove(l);
	}

	private void initializeTerminator() {
		if (roomsManager == null || transactionTemplate == null) {
			return;
		}

		if (log.isDebugEnabled()) {
			log.debug("Initialize game terminator");
		}

		final Collection<RoomManager> collection = roomsManager.getRoomManagers();
		for (RoomManager manager : collection) {
			final Room room = manager.getRoomType();
			if (log.isDebugEnabled()) {
				log.debug("Initialize termination for room " + room);
			}
			@SuppressWarnings("unchecked")
			final SearchesEngine<GameBoard<?, ?>> searchesEngine = manager.getSearchesEngine();

			final Collection<ExpiringBoardInfo> infoCollection = searchesEngine.findExpiringBoards();
			for (ExpiringBoardInfo info : infoCollection) {
				startBoardTerminationTask(room, info);
			}
		}
	}

	private void listenBoardChanges(Room room, GameBoard board) {
		final GameState state = board.getGameState();
		if (state == GameState.WAITING || state == GameState.IN_PROGRESS) {
			final TheBoardListener l = new TheBoardListener(room);
			board.addGameMoveListener(l);
			board.addGameStateListener(l);
		}
	}

	public void destroy() {
		terminatorTimer.cancel();
	}

	private void startBoardTerminationTask(Room room, ExpiringBoardInfo expiringBoardInfo) {
		terminatorLock.lock();
		try {
			if (log.isDebugEnabled()) {
				log.debug("Start new board termination task for board " + expiringBoardInfo.getBoardId());
			}

			final GameTerminationTask interruptor = new GameTerminationTask(room, expiringBoardInfo);
			final GameTerminationTask previousTask = scheduledBoards.put(expiringBoardInfo.getBoardId(), interruptor);
			final long remainder = interruptor.getDelayToRemainder();
			if (previousTask != null) { // previous previousTask exist
				if (previousTask.cancel()) { // if it was canceled: schedule new one
					if (log.isDebugEnabled()) {
						log.debug("Previous task found and was canceled successfully for board " + expiringBoardInfo.getBoardId() + " with delay " + remainder + "ms");
					}
					terminatorTimer.schedule(interruptor, remainder);
				} else {
					if (log.isDebugEnabled()) {
						log.debug("Previous task found but it can't be canceled for board " + expiringBoardInfo.getBoardId());
					}
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Schedule new deactivation task for board " + expiringBoardInfo.getBoardId() + " with delay " + remainder + "ms");
				}
				terminatorTimer.schedule(interruptor, remainder);
			}
		} finally {
			terminatorLock.unlock();
		}
	}

	private void cancelBoardTermination(GameBoard board) {
		terminatorLock.lock();
		try {
			final long boardId = board.getBoardId();
			if (log.isDebugEnabled()) {
				log.debug("Cancel deactivation task for board: " + boardId);
			}
			final GameTerminationTask task = scheduledBoards.remove(boardId);
			if (task != null && task.cancel()) {
				if (log.isDebugEnabled()) {
					log.debug("Termination task canceled for board " + boardId);
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Termination task can't be canceled for board " + boardId);
				}
			}
		} finally {
			terminatorLock.unlock();
		}
	}

	private boolean terminateGameBoard(Room room, long boardId) {
		try {
			if (log.isInfoEnabled()) {
				log.info("Terminating game board from room " + room + " with id " + boardId);
			}

			final GameTerminationTask task = scheduledBoards.remove(boardId);// remove task
			if (task != null) { // and cancel it
				task.cancel();
			}

			final RoomManager roomManager = roomsManager.getRoomManager(room);
			final GameBoard board = roomManager.openBoard(boardId);
			if (board.getGameState() == GameState.IN_PROGRESS) {
				if (log.isInfoEnabled()) {
					log.info("Board " + board + " terminated");
				}
				board.terminate();
				return true;
			}

			if (log.isInfoEnabled()) {
				log.info("Board can't be terminated because it's state is not IN_PROGRESS");
			}
		} catch (BoardLoadingException ex) {
			log.error("Terminator can't load board " + boardId + " for termination ", ex);
		} catch (GameMoveException ex) {
			log.error("Terminator can't load board " + boardId + " for termination ", ex);
		}
		return false;
	}


	private void fireGameTimeoutEvent(final Room room, final long board, final RemainderType type) {
		final GameTimeoutEvent event = new GameTimeoutEvent(room, board, type);
		if (log.isDebugEnabled()) {
			log.debug("Fire timeout event to listeners: " + event);
		}

		if (type == RemainderType.TIME_IS_UP) {
			for (GameTimeoutListener listener : listeners) {
				listener.timeIsUp(event);
			}
		} else {
			for (GameTimeoutListener listener : listeners) {
				listener.timeIsRunningOut(event);
			}
		}
	}

	public void setRoomsManager(RoomsManager roomsManager) {
		if (this.roomsManager != null) {
			final Collection<RoomManager> managers = this.roomsManager.getRoomManagers();
			for (RoomManager manager : managers) {
				manager.removeRoomBoardsListener(roomBoardsListener);
			}
		}

		this.roomsManager = roomsManager;

		if (this.roomsManager != null) {
			final Collection<RoomManager> managers = this.roomsManager.getRoomManagers();
			for (RoomManager manager : managers) {
				final Room type = manager.getRoomType();

				@SuppressWarnings("unchecked")
				final Collection<GameBoard> openedBoards = manager.getOpenedBoards();
				for (GameBoard openedBoard : openedBoards) {
					listenBoardChanges(type, openedBoard);
				}
				manager.addRoomBoardsListener(roomBoardsListener);
			}
		}
		initializeTerminator();
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
		initializeTerminator();
	}

	private class GameTerminationTask extends TimerTask {
		private final Room room;
		private final ExpiringBoardInfo expiringInfo;

		private GameTerminationTask(Room room, ExpiringBoardInfo expiringInfo) {
			this.room = room;
			this.expiringInfo = expiringInfo;
		}

		public void run() {
			terminatorLock.lock();
			try {
				final RemainderType nextReminderType = RemainderType.getNextReminderType(expiringInfo.getDaysPerMove(), expiringInfo.getLastMoveTime());
				if (nextReminderType == null) {
					final boolean terminated = (Boolean) transactionTemplate.execute(new TransactionCallback() {
						@Override
						public Object doInTransaction(TransactionStatus status) {
							return terminateGameBoard(room, expiringInfo.getBoardId());
						}
					});
					if (terminated) {
						fireGameTimeoutEvent(room, expiringInfo.getBoardId(), RemainderType.TIME_IS_UP);
					}
				} else { // schedule next termination point...
					if (log.isDebugEnabled()) {
						log.debug("Reschedule termination task for next period: " + nextReminderType);
					}
					final GameTerminationTask interruptor = new GameTerminationTask(room, expiringInfo);
					scheduledBoards.put(expiringInfo.getBoardId(), interruptor);
					terminatorTimer.schedule(interruptor, interruptor.getDelayToRemainder());

					fireGameTimeoutEvent(room, expiringInfo.getBoardId(), nextReminderType.getPreviousRemainderType());
				}
			} finally {
				terminatorLock.unlock();
			}
		}

		private RemainderType getNextRemainderType() {
			return RemainderType.getNextReminderType(expiringInfo.getDaysPerMove(), expiringInfo.getLastMoveTime());
		}

		private long getDelayToRemainder() {
			final RemainderType type = getNextRemainderType();
			if (type == null) {
				return 0;
			} else {
				return type.getDelayToRemainder(expiringInfo.getDaysPerMove(), expiringInfo.getLastMoveTime());
			}
		}
	}

	private class TheRoomBoardsListener implements RoomBoardsListener {
		public void boardOpened(Room room, long boardId) {
			final RoomManager roomManager = roomsManager.getRoomManager(room);

			try {
				final GameBoard board = roomManager.openBoard(boardId);
				listenBoardChanges(room, board);
			} catch (BoardLoadingException ex) {
				log.error("Board can't be loaded in boardOpened event", ex);
			}
		}

		public void boardClosed(Room room, long boardId) {
		}
	}

	private class TheBoardListener implements GameMoveListener, GameStateListener {
		private final Room room;

		private TheBoardListener(Room room) {
			this.room = room;
		}

		public void playerMoved(GameMoveEvent event) {
			startBoardTerminationTask(room, new ExpiringBoardInfo(event.getGameBoard()));
		}

		public void gameStarted(GameBoard board, GamePlayerHand playerTurn) {
			startBoardTerminationTask(room, new ExpiringBoardInfo(board));
		}

		public void gameFinished(GameBoard board, GamePlayerHand wonPlayer) {
			cancelBoardTermination(board);
		}

		public void gameDraw(GameBoard board) {
			cancelBoardTermination(board);
		}

		public void gameInterrupted(GameBoard board, GamePlayerHand interrupterPlayer, boolean byTimeout) {
			cancelBoardTermination(board);
		}
	}
}
