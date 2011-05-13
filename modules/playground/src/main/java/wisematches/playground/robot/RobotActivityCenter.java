package wisematches.playground.robot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.personality.player.computer.robot.RobotType;
import wisematches.playground.*;

import java.util.Collection;
import java.util.concurrent.Executor;

/**
 * This manager listen all games and when turn is transferred to robot it start process for perfomed that move.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RobotActivityCenter {
	private Executor movesExecutor;

	private RobotBrain robotBrain;
	private BoardManager boardManager;
	private TransactionTemplate transactionTemplate;

	private final TheBoardStateListener boardStateListener = new TheBoardStateListener();

	private static final Log log = LogFactory.getLog("wisematches.server.robot.activity");

	public RobotActivityCenter() {
	}

	private void initializeGames() {
		boardManager.addBoardStateListener(boardStateListener);

		final Collection<RobotPlayer> robotPlayers = RobotPlayer.getRobotPlayers();
		for (RobotPlayer player : robotPlayers) {
			@SuppressWarnings("unchecked")
			final Collection<GameBoard> activeBoards = boardManager.getActiveBoards(player);
			for (GameBoard activeBoard : activeBoards) {
				processRobotMove(activeBoard);
			}
		}
	}

	private void afterPropertiesSet() {
		if (this.robotBrain != null && boardManager != null && this.movesExecutor != null && this.transactionTemplate != null) {
			transactionTemplate.execute(new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(TransactionStatus status) {
					initializeGames();
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
	private boolean processRobotMove(GameBoard gameBoard) {
		final GamePlayerHand hand = gameBoard.getPlayerTurn();
		if (hand != null) {
			final RobotPlayer robot = RobotPlayer.getComputerPlayer(hand.getPlayerId(), RobotPlayer.class);
			if (robot != null) {
				log.info("Initialize robot activity: " + robot);
				movesExecutor.execute(new MakeTurnTask(gameBoard));
				return true;
			}
		}
		return false;
	}


	public void setRobotBrain(RobotBrain robotBrain) {
		this.robotBrain = robotBrain;
		afterPropertiesSet();
	}

	public void setBoardManager(BoardManager boardManager) {
		this.boardManager = boardManager;
		afterPropertiesSet();
	}

	public void setMovesExecutor(Executor movesExecutor) {
		this.movesExecutor = movesExecutor;
		afterPropertiesSet();
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		afterPropertiesSet();
	}


	final class MakeTurnTask implements Runnable {
		private final GameBoard gameBoard;

		MakeTurnTask(GameBoard gameBoard) {
			this.gameBoard = gameBoard;
		}

		public void run() {
			final GamePlayerHand hand = gameBoard.getPlayerTurn();
			if (hand != null) {
				final RobotPlayer robot = RobotPlayer.getComputerPlayer(hand.getPlayerId(), RobotPlayer.class);
				if (robot != null) {
					final RobotType robotType = robot.getRobotType();
					transactionTemplate.execute(new TransactionCallbackWithoutResult() {
						@Override
						@SuppressWarnings("unchecked")
						protected void doInTransactionWithoutResult(TransactionStatus status) {
							robotBrain.putInAction(gameBoard, robotType);
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
		public void gameStarted(GameBoard board) {
			processRobotMove(board);
		}

		@Override
		public void gameMoveDone(GameBoard board, GameMove move) {
			processRobotMove(board);
		}

		@Override
		public <S extends GameSettings, P extends GamePlayerHand> void gameFinished(GameBoard<S, P> board, GameResolution resolution, Collection<P> wonPlayers) {
		}
	}
}