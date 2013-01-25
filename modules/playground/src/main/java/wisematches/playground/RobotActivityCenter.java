package wisematches.playground;

import org.springframework.beans.factory.InitializingBean;

/**
 * This manager listen all games and when turn is transferred to robot it start process for perfomed that move.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Deprecated
public class RobotActivityCenter implements InitializingBean {
	@Override
	public void afterPropertiesSet() throws Exception {
		throw new UnsupportedOperationException("TODO: Not implemented");
	}
/*	private GamePlayManager gamePlayManager;
	private RobotBrain<GameBoard> robotBrain;

	private TransactionalExecutor taskExecutor;

	private final TheGamePlayListener boardStateListener = new TheGamePlayListener();

	private static final Log log = LogFactory.getLog("wisematches.server.robot.activity");

	public RobotActivityCenter() {
	}

	@Override
	public void afterPropertiesSet() {
		gamePlayManager.addGamePlayListener(boardStateListener);

		for (final RobotPlayer player : robotBrain.getRobotPlayers()) {
			taskExecutor.execute(new Runnable() {
				@Override
				@SuppressWarnings("unchecked")
				public void run() {
					final Collection<GameBoard> activeBoards = gamePlayManager.searchEntities(player, GameState.ACTIVE, null, null, null);
					for (GameBoard activeBoard : activeBoards) {
						processRobotMove(activeBoard, 1);
					}
				}
			});
		}
	}

	private boolean processRobotMove(final GameBoard gameBoard, final int attempt) {
		if (robotBrain.isRobotTurn(gameBoard)) {
			final long boardId = gameBoard.getBoardId();
			log.info("Initialize robot activity [attempt=" + attempt + "] for board: " + boardId);

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
		return false;
	}


	public void setGamePlayManager(GamePlayManager gamePlayManager) {
		this.gamePlayManager = gamePlayManager;
	}

	public void setRobotBrain(RobotBrain<GameBoard> robotBrain) {
		this.robotBrain = robotBrain;
	}

	public void setTaskExecutor(TransactionalExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
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
				final GameBoard gameBoard = gamePlayManager.openBoard(boardId);
				if (robotBrain.isRobotTurn(gameBoard)) {
					try {
						final PlayerMove playerMove = robotBrain.performRobotMove(gameBoard);
						log.info("Robot made a turn for board " + boardId + ": " + playerMove);
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
			} catch (BoardLoadingException ex) {
				log.error("Board for robot's move can't be loaded: " + boardId, ex);
			}
		}
	}

	private final class TheGamePlayListener implements GamePlayListener {
		private TheGamePlayListener() {
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
	}*/
}