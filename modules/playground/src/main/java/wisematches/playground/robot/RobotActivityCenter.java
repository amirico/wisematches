package wisematches.playground.robot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import wisematches.core.personality.proprietary.robot.RobotPlayer;
import wisematches.core.personality.proprietary.robot.RobotType;
import wisematches.core.task.TransactionalExecutor;
import wisematches.playground.*;

import java.util.Collection;

/**
 * This manager listen all games and when turn is transferred to robot it start process for perfomed that move.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RobotActivityCenter implements InitializingBean {
	private BoardManager boardManager;
	private RobotBrain<GameBoard> robotBrain;

	private TransactionalExecutor taskExecutor;

	private final TheBoardStateListener boardStateListener = new TheBoardStateListener();

	private static final Log log = LogFactory.getLog("wisematches.server.robot.activity");

	public RobotActivityCenter() {
	}

	@Override
	public void afterPropertiesSet() {
		boardManager.addBoardStateListener(boardStateListener);

		final Collection<RobotPlayer> robotPlayers = RobotPlayer.getRobotPlayers();
		for (final RobotPlayer player : robotPlayers) {
			taskExecutor.execute(new Runnable() {
				@Override
				@SuppressWarnings("unchecked")
				public void run() {
					final Collection<GameBoard> activeBoards = boardManager.searchEntities(player, GameState.ACTIVE, null, null, null);
					for (GameBoard activeBoard : activeBoards) {
						processRobotMove(activeBoard, 1);
					}
				}
			});
		}
	}

	private boolean processRobotMove(final GameBoard gameBoard, final int attempt) {
		final GamePlayerHand hand = gameBoard.getPlayerTurn();
		if (hand != null) {
			final RobotPlayer robot = RobotPlayer.getComputerPlayer(hand.getPlayerId(), RobotPlayer.class);
			if (robot != null) {
				final long boardId = gameBoard.getBoardId();
				log.info("Initialize robot activity [attempt=" + attempt + "] for board: " + boardId + " [" + robot.getRobotType() + "]");

				try {
					if (TransactionSynchronizationManager.isSynchronizationActive()) {
						TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
							@Override
							public void afterCompletion(int status) {
								taskExecutor.execute(new MakeTurnTask(boardId, attempt));
							}
						});
					} else {
						taskExecutor.execute(new MakeTurnTask(boardId, attempt));
					}
				} catch (Throwable th) {
					log.error("", th);
					taskExecutor.execute(new MakeTurnTask(boardId, attempt));
				}
				return true;
			}
		}
		return false;
	}


	public void setTaskExecutor(TransactionalExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void setBoardManager(BoardManager boardManager) {
		this.boardManager = boardManager;
	}

	public void setRobotBrain(RobotBrain<GameBoard> robotBrain) {
		this.robotBrain = robotBrain;
	}

	final class MakeTurnTask implements Runnable {
		private final int attempt;
		private final long boardId;

		MakeTurnTask(long boardId, int attempt) {
			this.boardId = boardId;
			this.attempt = attempt;
		}

		public void run() {
			try {
				log.info("Start robot action for board " + boardId);
				final GameBoard gameBoard = boardManager.openBoard(boardId);
				final GamePlayerHand hand = gameBoard.getPlayerTurn();
				if (hand != null) {
					final RobotPlayer robot = RobotPlayer.getComputerPlayer(hand.getPlayerId(), RobotPlayer.class);
					if (robot != null) {
						final RobotType robotType = robot.getRobotType();
						try {
							robotBrain.putInAction(gameBoard, robotType);
							log.info("Robot made a turn for board " + boardId);
						} catch (Throwable th) {
							log.error("Robot can't make a turn [attempt=" + attempt + "] for board " + boardId, th);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException ignore) {
							}
							processRobotMove(gameBoard, attempt + 1);
						}
					} else {
						log.info("It's not robot turn for board " + boardId);
					}
				} else {
					log.info("Game is finished for board " + boardId);
				}
			} catch (BoardLoadingException ex) {
				log.error("Board for robot's move can't be loaded: " + boardId, ex);
			}
		}
	}

	private final class TheBoardStateListener implements BoardStateListener {
		private TheBoardStateListener() {
		}

		@Override
		public void gameStarted(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board) {
			processRobotMove(board, 1);
		}

		@Override
		public void gameMoveDone(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameMove move, GameMoveScore moveScore) {
			processRobotMove(board, 1);
		}

		@Override
		public void gameFinished(GameBoard<? extends GameSettings, ? extends GamePlayerHand> board, GameResolution resolution, Collection<? extends GamePlayerHand> winners) {
		}
	}
}