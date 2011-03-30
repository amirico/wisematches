package wisematches.server.gameplaying.cleaner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.server.gameplaying.board.*;
import wisematches.server.gameplaying.room.Room;
import wisematches.server.gameplaying.room.RoomManager;
import wisematches.server.gameplaying.room.RoomsManager;
import wisematches.server.gameplaying.room.board.BoardLoadingException;
import wisematches.server.gameplaying.room.board.BoardManager;
import wisematches.server.gameplaying.room.board.BoardStateListener;
import wisematches.server.gameplaying.room.search.BoardsSearchEngine;
import wisematches.server.gameplaying.room.search.ExpiringBoard;

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
	private final Map<Room, BoardStateListener> boardStateListeners = new HashMap<Room, BoardStateListener>();

	private final Timer terminatorTimer = new Timer("GameTimeoutTerminator");
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
			final BoardsSearchEngine<GameBoard<?, ?>> boardsSearchEngine = manager.getSearchesEngine();

			final Collection<ExpiringBoard> infoCollection = boardsSearchEngine.findExpiringBoards();
			for (ExpiringBoard info : infoCollection) {
				startBoardTerminationTask(room, info);
			}
		}
	}

	public void destroy() {
		terminatorTimer.cancel();
	}

	private void startBoardTerminationTask(Room room, ExpiringBoard expiringBoard) {
		terminatorLock.lock();
		try {
			if (log.isDebugEnabled()) {
				log.debug("Start new board termination task for board " + expiringBoard.getBoardId());
			}

			final GameTerminationTask interrupter = new GameTerminationTask(room, expiringBoard);
			final GameTerminationTask previousTask = scheduledBoards.put(expiringBoard.getBoardId(), interrupter);
			final long remainder = interrupter.getDelayToRemainder();
			if (previousTask != null) { // previous previousTask exist
				if (previousTask.cancel()) { // if it was canceled: schedule new one
					if (log.isDebugEnabled()) {
						log.debug("Previous task found and was canceled successfully for board " + expiringBoard.getBoardId() + " with delay " + remainder + "ms");
					}
					terminatorTimer.schedule(interrupter, remainder);
				} else {
					if (log.isDebugEnabled()) {
						log.debug("Previous task found but it can't be canceled for board " + expiringBoard.getBoardId());
					}
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("Schedule new deactivation task for board " + expiringBoard.getBoardId() + " with delay " + remainder + "ms");
				}
				terminatorTimer.schedule(interrupter, remainder);
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

			@SuppressWarnings("unchecked")
			final BoardManager boardManager = roomsManager.getBoardManager(room);
			final GameBoard board = boardManager.openBoard(boardId);
			if (board.isGameActive()) {
				if (log.isInfoEnabled()) {
					log.info("Board " + board + " terminated");
				}
				board.terminate();
				return true;
			}

			if (log.isInfoEnabled()) {
				log.info("Board can't be terminated because it's state is not ACTIVE");
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
				final BoardManager boardManager = manager.getBoardManager();
				if (boardManager != null) {
					boardManager.removeBoardStateListener(boardStateListeners.get(manager.getRoomType()));
				}
			}
		}

		this.roomsManager = roomsManager;

		if (this.roomsManager != null) {
			final Collection<RoomManager> managers = this.roomsManager.getRoomManagers();
			for (RoomManager manager : managers) {
				final Room type = manager.getRoomType();

				BoardStateListener listener = boardStateListeners.get(type);
				if (listener == null) {
					listener = new TheBoardStateListener(type);
					boardStateListeners.put(type, listener);
				}
				manager.getBoardManager().addBoardStateListener(listener);
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
		private final ExpiringBoard expiring;

		private GameTerminationTask(Room room, ExpiringBoard expiring) {
			this.room = room;
			this.expiring = expiring;
		}

		public void run() {
			terminatorLock.lock();
			try {
				final RemainderType nextReminderType = RemainderType.getNextReminderType(expiring.getDaysPerMove(), expiring.getLastMoveTime());
				if (nextReminderType == null) {
					final boolean terminated = transactionTemplate.execute(new TransactionCallback<Boolean>() {
						@Override
						public Boolean doInTransaction(TransactionStatus status) {
							return terminateGameBoard(room, expiring.getBoardId());
						}
					});
					if (terminated) {
						fireGameTimeoutEvent(room, expiring.getBoardId(), RemainderType.TIME_IS_UP);
					}
				} else { // schedule next termination point...
					if (log.isDebugEnabled()) {
						log.debug("Reschedule termination task for next period: " + nextReminderType);
					}
					final GameTerminationTask interrupter = new GameTerminationTask(room, expiring);
					scheduledBoards.put(expiring.getBoardId(), interrupter);
					terminatorTimer.schedule(interrupter, interrupter.getDelayToRemainder());

					fireGameTimeoutEvent(room, expiring.getBoardId(), nextReminderType.getPreviousRemainderType());
				}
			} finally {
				terminatorLock.unlock();
			}
		}

		private RemainderType getNextRemainderType() {
			return RemainderType.getNextReminderType(expiring.getDaysPerMove(), expiring.getLastMoveTime());
		}

		private long getDelayToRemainder() {
			final RemainderType type = getNextRemainderType();
			if (type == null) {
				return 0;
			} else {
				return type.getDelayToRemainder(expiring.getDaysPerMove(), expiring.getLastMoveTime());
			}
		}
	}

	private class TheBoardStateListener implements BoardStateListener {
		private final Room room;

		private TheBoardStateListener(Room room) {
			this.room = room;
		}

		@Override
		public void gameStarted(GameBoard board) {
			startBoardTerminationTask(room, new ExpiringBoard(board));
		}

		@Override
		public void gameMoveDone(GameBoard board, GameMove move) {
			startBoardTerminationTask(room, new ExpiringBoard(board));
		}

		@Override
		public <S extends GameSettings, P extends GamePlayerHand> void gameFinished(GameBoard<S, P> board, GameResolution gameResolution, Collection<P> wonPlayers) {
			cancelBoardTermination(board);
		}
	}
}
