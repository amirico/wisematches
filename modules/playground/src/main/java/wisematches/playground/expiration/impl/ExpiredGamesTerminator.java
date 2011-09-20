package wisematches.playground.expiration.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.playground.*;
import wisematches.playground.expiration.GameExpirationListener;
import wisematches.playground.expiration.GameExpirationManager;
import wisematches.playground.expiration.GameExpirationType;
import wisematches.playground.search.board.BoardsSearchEngine;
import wisematches.playground.search.board.LastMoveInfo;

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
	private final Lock lock = new ReentrantLock();

	private TaskScheduler taskScheduler;
	private TransactionTemplate transactionTemplate;

	private BoardsSearchEngine boardsSearchEngine;
	private BoardManager<GameSettings, GameBoard<GameSettings, GamePlayerHand>> boardManager;
	private final TheBoardStateListener boardStateListener = new TheBoardStateListener();

	private final Map<Long, ScheduledFuture> scheduledExpirations = new HashMap<Long, ScheduledFuture>();
	private final Collection<GameExpirationListener> listeners = new CopyOnWriteArraySet<GameExpirationListener>();

	private static final int MILLIS_IN_DAY = 24 * 60 * 60 * 1000;

	private static final Log log = LogFactory.getLog("wisematches.server.playground.terminator");

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

	protected void scheduleBoardTermination(final long boardId, final Date expiringDate) {
		lock.lock();
		try {
			ScheduledFuture scheduledFuture = scheduledExpirations.get(boardId);
			if (scheduledFuture != null) {
				scheduledFuture.cancel(false);
			}

			final ScheduledFuture schedule;
			final GameExpirationType type = GameExpirationType.nextExpiringPoint(expiringDate);
			final GameExpirationTask task = new GameExpirationTask(boardId, expiringDate, type);
			if (type == null) { // expired
				log.info("Board is expired: " + boardId + " and will be terminated");
				schedule = taskScheduler.schedule(task, expiringDate);
			} else {
				final Date triggerTime = type.getExpirationTriggerTime(expiringDate);
				log.info("Start expiration scheduler: " + boardId + " to " + triggerTime + "(" + type + ")");
				schedule = taskScheduler.schedule(task, triggerTime);
			}
			scheduledExpirations.put(boardId, schedule);
		} finally {
			lock.unlock();
		}
	}

	protected void cancelBoardTermination(final long boardId) {
		lock.lock();
		try {
			log.info("Cancel board termination " + boardId);

			ScheduledFuture scheduledFuture = scheduledExpirations.get(boardId);
			if (scheduledFuture != null) {
				scheduledFuture.cancel(false);
			}
		} finally {
			lock.unlock();
		}
	}

	protected boolean executeBoardTermination(final long boardId) {
		lock.lock();
		try {
			final GameBoard board = boardManager.openBoard(boardId);
			if (board != null) {
				if (!board.isGameActive()) {
					log.info("Terminate game " + boardId);
					board.terminate();
					return true;
				} else {
					log.info("Looks like the game still active " + boardId);
				}
			} else {
				return true; // no board - nothing to do
			}
		} catch (BoardLoadingException e) {
			log.error("Board " + boardId + " can't be loaded for termination", e);
		} catch (GameMoveException e) {
			log.error("Board " + boardId + " can't be terminated", e);
		} finally {
			lock.unlock();
		}
		return false;
	}

	protected void processGameExpiration(final long boardId, final GameExpirationTask task) {
		lock.lock();
		try {
			log.info("Process game expiration: " + boardId + ": " + task.getExpirationType());
			final ScheduledFuture scheduledFuture = scheduledExpirations.get(boardId);
			if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
				boolean reschedulingRequired = true;
				if (task.getExpirationType() == null) {
					reschedulingRequired = transactionTemplate.execute(new TransactionCallback<Boolean>() {
						@Override
						public Boolean doInTransaction(TransactionStatus status) {
							return !executeBoardTermination(boardId);
						}
					});
				} else {
					for (GameExpirationListener listener : listeners) {
						listener.gameExpiring(boardId, task.getExpirationType());
					}
				}

				if (reschedulingRequired) {
					scheduleBoardTermination(boardId, task.getExpiringDate());
				}
			}
		} finally {
			lock.unlock();
		}
	}

	protected Date getExpiringDate(int daysPerMove, Date lastMoveTime) {
		return new Date(lastMoveTime.getTime() + daysPerMove * MILLIS_IN_DAY);
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

	public void setBoardsSearchEngine(BoardsSearchEngine boardsSearchEngine) {
		this.boardsSearchEngine = boardsSearchEngine;
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

	public void setBoardManager(BoardManager<GameSettings, GameBoard<GameSettings, GamePlayerHand>> boardManager) {
		lock.lock();
		try {
			if (this.boardManager != null) {
				this.boardManager.removeBoardStateListener(boardStateListener);
			}

			this.boardManager = boardManager;

			if (this.boardManager != null) {
				this.boardManager.addBoardStateListener(boardStateListener);
			}

			initTerminator();
		} finally {
			lock.unlock();
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

	private void initTerminator() {
		if (boardsSearchEngine == null || boardManager == null || taskScheduler == null || transactionTemplate == null) {
			return;
		}

		final Collection<LastMoveInfo> expiring = boardsSearchEngine.findExpiringBoards();
		for (LastMoveInfo board : expiring) {
			scheduleBoardTermination(board.getBoardId(), getExpiringDate(board.getDaysPerMove(), board.getLastMoveTime()));
		}
	}

	private class GameExpirationTask implements Runnable {
		private final long boardId;
		private final Date expiringDate;
		private final GameExpirationType expirationType;

		private GameExpirationTask(long boardId, Date expiringDate, GameExpirationType expirationType) {
			this.boardId = boardId;
			this.expiringDate = expiringDate;
			this.expirationType = expirationType;
		}

		@Override
		public void run() {
			processGameExpiration(boardId, this);
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
		private TheBoardStateListener() {
		}

		@Override
		public void gameStarted(GameBoard board) {
			gameMoveDone(board, null);
		}

		@Override
		public void gameMoveDone(GameBoard board, GameMove move) {
			scheduleBoardTermination(board.getBoardId(), getExpiringDate(board.getGameSettings().getDaysPerMove(), board.getLastMoveTime()));
		}

		@Override
		public void gameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameResolution resolution, Collection<? extends GamePlayerHand> wonPlayers) {
			cancelBoardTermination(board.getBoardId());
		}
	}
}
