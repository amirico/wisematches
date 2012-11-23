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
	private RobotBrain<GameBoard> robotBrain;
	private BoardManager boardManager;

	private TaskExecutor taskExecutor;

	private final TheBoardStateListener boardStateListener = new TheBoardStateListener();

	private static final Log log = LogFactory.getLog("wisematches.server.robot.activity");

	public RobotActivityCenter() {
	}

	private void initializeGames() {
		boardManager.addBoardStateListener(boardStateListener);

		final Collection<RobotPlayer> robotPlayers = RobotPlayer.getRobotPlayers();
		for (RobotPlayer player : robotPlayers) {
			@SuppressWarnings("unchecked")
			final Collection<GameBoard> activeBoards = boardManager.searchEntities(player, GameState.ACTIVE, null, null, null);
			for (GameBoard activeBoard : activeBoards) {
				processRobotMove(activeBoard, 1);
			}
		}
	}

	@Override
	public void afterPropertiesSet() {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				initializeGames();
			}
		});
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
				log.info("Initialize robot activity [attempt=" + attempt + "] for board: " + gameBoard.getBoardId() + robot.getRobotType());
				taskExecutor.execute(new MakeTurnTask(gameBoard, attempt));
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
		private final GameBoard gameBoard;

		MakeTurnTask(GameBoard gameBoard, int attempt) {
			this.gameBoard = gameBoard;
			this.attempt = attempt;
		}

		public void run() {
			final GamePlayerHand hand = gameBoard.getPlayerTurn();
			if (hand != null) {
				final RobotPlayer robot = RobotPlayer.getComputerPlayer(hand.getPlayerId(), RobotPlayer.class);
				if (robot != null) {
					final RobotType robotType = robot.getRobotType();
					taskExecutor.execute(new Runnable() {
						@Override
						public void run() {
							try {
								robotBrain.putInAction(gameBoard, robotType);
							} catch (Throwable th) {
								log.error("Robot can't make a turn [attempt=" + attempt + "]", th);
								try {
									Thread.sleep(1000);
								} catch (InterruptedException ignore) {
								}
								processRobotMove(gameBoard, attempt + 1);
							}
						}
					});
				}
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