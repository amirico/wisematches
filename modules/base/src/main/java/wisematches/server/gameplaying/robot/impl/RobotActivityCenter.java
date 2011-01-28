package wisematches.server.gameplaying.robot.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.server.gameplaying.board.*;
import wisematches.server.gameplaying.robot.RobotBrain;
import wisematches.server.gameplaying.robot.RobotBrainManager;
import wisematches.server.gameplaying.room.*;
import wisematches.server.player.computer.robot.RobotPlayer;
import wisematches.server.player.computer.robot.RobotType;

import java.util.Collection;
import java.util.concurrent.Executor;

/**
 * This manager listen all games and when turn is transfered to robot it start process for perfome that move.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RobotActivityCenter {
	private Executor movesExecutor;

	private RoomsManager roomsManager;
	private RobotBrainManager robotBrainManager;
	private TransactionTemplate transactionTemplate;

	private final RoomBoardsListener roomBoardsListener = new TheRoomBoardsListener();

	private static final Log log = LogFactory.getLog("wisematches.server.robot.activity");

	public RobotActivityCenter() {
	}

	private void initializeGames() {
		final Collection<RobotPlayer> collection = robotBrainManager.getRobotPlayers();
		final Collection<RoomManager> roomManagerCollection = roomsManager.getRoomManagers();

		for (RoomManager roomManager : roomManagerCollection) {
			final Room room = roomManager.getRoomType();
			roomManager.addRoomBoardsListener(roomBoardsListener);

			for (RobotPlayer player : collection) {
				@SuppressWarnings("unchecked")
				final Collection<GameBoard<GameSettings, GamePlayerHand>> boards = roomManager.getActiveBoards(player);
				for (GameBoard<GameSettings, GamePlayerHand> board : boards) {
					final TheGameMoveListener listener = new TheGameMoveListener(room, board);
					board.addGameMoveListener(listener);
					board.addGameStateListener(listener);

					processRobotMove(room, board);
				}
			}
		}
	}

	private void afterPropertiesSet() {
		if (this.robotBrainManager != null && this.roomsManager != null && this.movesExecutor != null && this.transactionTemplate != null) {
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
	 * @param room	  the room of specified game board.
	 * @param gameBoard the bame board to check and make a turn.
	 * @return {@code true} if move was maden; {@code false} - otherwise.
	 */
	private boolean processRobotMove(Room room, GameBoard gameBoard) {
		final GamePlayerHand hand = gameBoard.getPlayerTrun();
		if (hand != null) {
			final RobotPlayer robot = RobotPlayer.getComputerPlayer(hand.getPlayerId(), RobotPlayer.class);
			if (robot != null) {
				movesExecutor.execute(new MakeTurnTask(room, gameBoard));
				return true;
			}
		}
		return false;
	}


	public void setRoomsManager(RoomsManager roomsManager) {
		this.roomsManager = roomsManager;
		afterPropertiesSet();
	}

	public void setRobotBrainManager(RobotBrainManager robotBrainManager) {
		this.robotBrainManager = robotBrainManager;
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

	private final class TheRoomBoardsListener implements RoomBoardsListener {
		@SuppressWarnings("unchecked")
		public void boardOpened(Room room, long boardId) {
			final RoomManager roomManager = roomsManager.getRoomManager(room);
			try {
				final GameBoard board = roomManager.openBoard(boardId);
				final TheGameMoveListener listener = new TheGameMoveListener(room, board);

				board.addGameMoveListener(listener);
				board.addGameStateListener(listener);

				processRobotMove(room, board);
			} catch (BoardLoadingException ex) {
				log.fatal("Board can't be opened in boardOpened event!!!", ex);
			}
		}

		public void boardClosed(Room room, long boardId) {
		}
	}

	private final class TheGameMoveListener implements GameMoveListener, GameStateListener {
		private final Room roomType;
		private final GameBoard gameBoard;

		private TheGameMoveListener(Room roomType, GameBoard gameBoard) {
			this.roomType = roomType;
			this.gameBoard = gameBoard;
		}

		public void gameStarted(GameBoard board, GamePlayerHand playerTurn) {
			final RobotPlayer robot = RobotPlayer.getComputerPlayer(playerTurn.getPlayerId(), RobotPlayer.class);
			if (robot != null) {
				movesExecutor.execute(new MakeTurnTask(roomType, gameBoard));
			}
		}

		public void playerMoved(GameMoveEvent event) {
			playerTurnTransmitted(event.getGameBoard(), event.getNextPlayer());
		}

		public void playerTurnTransmitted(GameBoard board, GamePlayerHand activePlayer) {
			if (activePlayer != null) {
				final RobotPlayer robot = RobotPlayer.getComputerPlayer(activePlayer.getPlayerId(), RobotPlayer.class);
				if (robot != null) {
					movesExecutor.execute(new MakeTurnTask(roomType, board));
				}
			}
		}

		public void gameFinished(GameBoard board, GamePlayerHand wonPlayer) {
		}

		public void gameDraw(GameBoard board) {
		}

		public void gameInterrupted(GameBoard board, GamePlayerHand interrupterPlayer, boolean byTimeout) {
		}
	}

	final class MakeTurnTask implements Runnable {
		private final Room roomType;
		private final GameBoard gameBoard;

		MakeTurnTask(Room roomType, GameBoard gameBoard) {
			this.roomType = roomType;
			this.gameBoard = gameBoard;
		}

		public void run() {
			final GamePlayerHand hand = gameBoard.getPlayerTrun();

			final RobotPlayer robot = RobotPlayer.getComputerPlayer(hand.getPlayerId(), RobotPlayer.class);
			if (robot != null) {
				final RobotType robotType = robot.getRobotType();
				transactionTemplate.execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status) {
						@SuppressWarnings("unchecked")
						final RobotBrain<GameBoard> brain = robotBrainManager.getRobotBrain(roomType, robotType);
						brain.putInAction(gameBoard, robotType);
					}
				});
			}
		}
	}
}