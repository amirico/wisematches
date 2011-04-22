package wisematches.server.gameplaying.expiration.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.server.gameplaying.board.*;
import wisematches.server.gameplaying.expiration.GameExpirationListener;
import wisematches.server.gameplaying.expiration.GameExpirationManager;
import wisematches.server.gameplaying.expiration.GameExpirationType;
import wisematches.server.gameplaying.room.Room;
import wisematches.server.gameplaying.room.RoomManager;
import wisematches.server.gameplaying.room.RoomsManager;
import wisematches.server.gameplaying.board.BoardLoadingException;
import wisematches.server.gameplaying.board.BoardManager;
import wisematches.server.gameplaying.board.BoardStateListener;
import wisematches.server.gameplaying.search.BoardLastMoveInfo;
import wisematches.server.gameplaying.search.BoardsSearchEngine;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ExpiredGamesTerminator implements GameExpirationManager {
	private RoomsManager roomsManager;
	private TaskScheduler taskScheduler;
	private TransactionTemplate transactionTemplate;

	private final Lock lock = new ReentrantLock();

	private final Map<Long, ScheduledFuture> scheduledExpirations = new HashMap<Long, ScheduledFuture>();
	private final Map<Room, TheBoardStateListener> boardStateListeners = new HashMap<Room, TheBoardStateListener>();

	private final Collection<GameExpirationListener> listeners = new CopyOnWriteArraySet<GameExpirationListener>();

	private static final int MILLIS_IN_DAY = 24 * 60 * 60 * 1000;
	private static final Log log = LogFactory.getLog("wisematches.server.gameplaying.terminator");

	public ExpiredGamesTerminator() {
	}

	@Override
	public void addGameExpirationListener(GameExpirationListener l) {
		if (l != null) {
			listeners.add(l);
		}
	}

	@Override
	public void removeGameExpirationListener(GameExpirationListener l) {
		listeners.remove(l);
	}

	protected void scheduleBoardTermination(final Room room, final long boardId, final Date expiringDate) {
		lock.lock();
		try {
			ScheduledFuture scheduledFuture = scheduledExpirations.get(boardId);
			if (scheduledFuture != null) {
				scheduledFuture.cancel(false);
			}

			final ScheduledFuture schedule;
			final GameExpirationType type = GameExpirationType.nextExpiringPoint(expiringDate);
			final GameExpirationTask task = new GameExpirationTask(room, boardId, expiringDate, type);
			if (type == null) { // expired
				log.info("Board is expired: " + boardId + "@" + room + " and will be terminated");
				schedule = taskScheduler.schedule(task, expiringDate);
			} else {
				final Date triggerTime = type.getExpirationTriggerTime(expiringDate);
				log.info("Start expiration scheduler: " + boardId + "@" + room + " to " + triggerTime + "(" + type + ")");
				schedule = taskScheduler.schedule(task, triggerTime);
			}
			scheduledExpirations.put(boardId, schedule);
		} finally {
			lock.unlock();
		}
	}

	protected void cancelBoardTermination(final Room room, final long boardId) {
		lock.lock();
		try {
			log.info("Cancel board termination " + boardId + "@" + room);

			ScheduledFuture scheduledFuture = scheduledExpirations.get(boardId);
			if (scheduledFuture != null) {
				scheduledFuture.cancel(false);
			}
		} finally {
			lock.unlock();
		}
	}

	protected boolean executeBoardTermination(final Room room, final long boardId) {
		lock.lock();
		try {
			@SuppressWarnings("unchecked")
			final GameBoard board = roomsManager.getBoardManager(room).openBoard(boardId);
			if (board != null) {
				if (!board.isGameActive()) {
					log.info("Terminate game " + boardId + "@" + room);
					board.terminate();
					return true;
				} else {
					log.info("Looks like the game still active " + boardId + "@" + room);
				}
			} else {
				return true; // no board - nothing to do
			}
		} catch (BoardLoadingException e) {
			log.error("Board " + boardId + "@" + room + " can't be loaded for termination", e);
		} catch (GameMoveException e) {
			log.error("Board " + boardId + "@" + room + " can't be terminated", e);
		} finally {
			lock.unlock();
		}
		return false;
	}

	protected void processGameExpiration(final Room room, final long boardId, final GameExpirationTask task) {
		lock.lock();
		try {
			log.info("Process game expiration: " + boardId + "@" + room + ": " + task.getExpirationType());
			final ScheduledFuture scheduledFuture = scheduledExpirations.get(boardId);
			if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
				boolean reschedulingRequired = true;
				if (task.getExpirationType() == null) {
					reschedulingRequired = transactionTemplate.execute(new TransactionCallback<Boolean>() {
						@Override
						public Boolean doInTransaction(TransactionStatus status) {
							return !executeBoardTermination(room, boardId);
						}
					});
				} else {
					for (GameExpirationListener listener : listeners) {
						listener.gameExpiring(boardId, room, task.getExpirationType());
					}
				}

				if (reschedulingRequired) {
					scheduleBoardTermination(room, boardId, task.getExpiringDate());
				}
			}
		} finally {
			lock.unlock();
		}
	}

	protected Date getExpiringDate(int daysPerMove, Date lastMoveTime) {
		return new Date(lastMoveTime.getTime() + daysPerMove * MILLIS_IN_DAY);
	}

	@SuppressWarnings("unchecked")
	public void setRoomsManager(RoomsManager roomsManager) {
		lock.lock();
		try {
			if (this.roomsManager != null) {
				for (Map.Entry<Room, TheBoardStateListener> entry : boardStateListeners.entrySet()) {
					BoardManager boardManager = this.roomsManager.getBoardManager(entry.getKey());
					if (boardManager != null) {
						boardManager.removeBoardStateListener(entry.getValue());
					}
				}
				boardStateListeners.clear();
			}

			this.roomsManager = roomsManager;

			initTerminator();
		} finally {
			lock.unlock();
		}
	}

	public void setTaskScheduler(TaskScheduler taskScheduler) {
		lock.lock();
		try {
			this.taskScheduler = taskScheduler;
			initTerminator();
		} finally {
			lock.unlock();
		}
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		lock.lock();
		try {
			this.transactionTemplate = transactionTemplate;
			initTerminator();
		} finally {
			lock.unlock();
		}
	}

	private void initTerminator() {
		if (roomsManager == null || taskScheduler == null || transactionTemplate == null) {
			return;
		}

		if (boardStateListeners.isEmpty()) {
			final Collection<RoomManager> roomManagers = roomsManager.getRoomManagers();
			for (RoomManager roomManager : roomManagers) {
				final Room room = roomManager.getRoomType();

				final BoardsSearchEngine searchesEngine = roomManager.getSearchesEngine();
				final Collection<BoardLastMoveInfo> expiringBoards = searchesEngine.findExpiringBoards();
				for (BoardLastMoveInfo board : expiringBoards) {
					scheduleBoardTermination(room, board.getBoardId(), getExpiringDate(board.getDaysPerMove(), board.getLastMoveTime()));
				}

				final TheBoardStateListener l = new TheBoardStateListener(room);
				roomManager.getBoardManager().addBoardStateListener(l);
				boardStateListeners.put(room, l);
			}
		}
	}

	public void destroy() {
		lock.lock();
		try {
			for (ScheduledFuture scheduledFuture : scheduledExpirations.values()) {
				scheduledFuture.cancel(false);
			}
		} finally {
			lock.unlock();
		}
	}

	private class GameExpirationTask implements Runnable {
		private final Room room;
		private final long boardId;
		private final Date expiringDate;
		private final GameExpirationType expirationType;

		private GameExpirationTask(Room room, long boardId, Date expiringDate, GameExpirationType expirationType) {
			this.room = room;
			this.boardId = boardId;
			this.expiringDate = expiringDate;
			this.expirationType = expirationType;
		}

		@Override
		public void run() {
			processGameExpiration(room, boardId, this);
		}

		public long getBoardId() {
			return boardId;
		}

		public Date getExpiringDate() {
			return expiringDate;
		}

		public GameExpirationType getExpirationType() {
			return expirationType;
		}
	}

	private class TheBoardStateListener implements BoardStateListener {
		private final Room room;

		private TheBoardStateListener(Room room) {
			this.room = room;
		}

		@Override
		public void gameStarted(GameBoard board) {
			gameMoveDone(board, null);
		}

		@Override
		public void gameMoveDone(GameBoard board, GameMove move) {
			scheduleBoardTermination(room, board.getBoardId(), getExpiringDate(board.getGameSettings().getDaysPerMove(), board.getLastMoveTime()));
		}

		@Override
		public <S extends GameSettings, P extends GamePlayerHand> void gameFinished(GameBoard<S, P> board, GameResolution gameResolution, Collection<P> wonPlayers) {
			cancelBoardTermination(room, board.getBoardId());
		}
	}
}
