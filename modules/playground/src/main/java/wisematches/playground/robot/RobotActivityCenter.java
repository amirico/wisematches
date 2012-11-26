package wisematches.playground.robot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.TaskExecutor;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.personality.player.computer.robot.RobotType;
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

	private TaskExecutor taskExecutor;

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

	/**
	 * Checks that robot's has a turn on specified board and make a turn.
	 *
	 * @param gameBoard the bame board to check and make a turn.
	 * @return {@code true} if move was maden; {@code false} - otherwise.
	 */
	private boolean processRobotMove(GameBoard gameBoard, int attempt) {
		final GamePlayerHand hand = gameBoard.getPlayerTurn();
		if (hand != null) {
			final RobotPlayer robot = RobotPlayer.getComputerPlayer(hand.getPlayerId(), RobotPlayer.class);
			if (robot != null) {
				final long boardId = gameBoard.getBoardId();
				log.info("Initialize robot activity [attempt=" + attempt + "] for board: " + boardId + " [" + robot.getRobotType() + "]");
				taskExecutor.execute(new MakeTurnTask(boardId, attempt));
				return true;
			}
		}
		return false;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
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
				final GameBoard gameBoard = boardManager.openBoard(boardId);
				final GamePlayerHand hand = gameBoard.getPlayerTurn();
				if (hand != null) {
					final RobotPlayer robot = RobotPlayer.getComputerPlayer(hand.getPlayerId(), RobotPlayer.class);
					if (robot != null) {
						final RobotType robotType = robot.getRobotType();
						try {
							robotBrain.putInAction(gameBoard, robotType);
						} catch (Throwable th) {
							log.error("Robot can't make a turn [attempt=" + attempt + "] for board " + boardId, th);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException ignore) {
							}
							processRobotMove(gameBoard, attempt + 1);
						}
					}
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