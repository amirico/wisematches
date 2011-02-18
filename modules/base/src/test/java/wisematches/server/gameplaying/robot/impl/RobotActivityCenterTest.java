package wisematches.server.gameplaying.robot.impl;

import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import wisematches.server.gameplaying.board.*;
import wisematches.server.gameplaying.robot.RobotBrain;
import wisematches.server.gameplaying.robot.RobotBrainManager;
import wisematches.server.gameplaying.room.*;
import wisematches.server.player.computer.robot.RobotPlayer;
import wisematches.server.player.computer.robot.RobotType;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.Executor;

import static org.easymock.EasyMock.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RobotActivityCenterTest {
	private RoomBoardsListener boardsListener;
	private GameMoveListener gameMoveListener;
	private GameStateListener gameStateListener;

	private RoomManager roomManager;
	private RobotBrainManager brainManager;
	private RobotActivityCenter activityManager;

	private static final Room ROOM = Room.valueOf("mock");

	@Before
	public void setUp() {
		activityManager = new RobotActivityCenter();

		brainManager = createStrictMock(RobotBrainManager.class);
		roomManager = createStrictMock(RoomManager.class);

		final RoomsManager roomsManager = createStrictMock(RoomsManager.class);
		expect(roomsManager.getRoomManagers()).andReturn(Arrays.asList(roomManager)).anyTimes();
		expect(roomsManager.getRoomManager(ROOM)).andReturn(roomManager).anyTimes();
		replay(roomsManager);

		activityManager.setRoomsManager(roomsManager);
		activityManager.setRobotBrainManager(brainManager);

		final TransactionStatus status = createNiceMock(TransactionStatus.class);

		final PlatformTransactionManager transaction = createStrictMock(PlatformTransactionManager.class);
		expect(transaction.getTransaction(isA(TransactionDefinition.class))).andReturn(status);
		transaction.commit(status);
		replay(transaction);

		activityManager.setTransactionManager(transaction);
	}

	@Test
	public void makeTurnTask() throws BoardLoadingException, GameMoveException {
		final GameBoard gameBoard = createStrictMock(GameBoard.class);
		expect(gameBoard.getPlayerTrun()).andReturn(createPlayerHand(1L));
		replay(gameBoard);

		final RobotPlayer player = createRobotPlayer(1L, RobotType.TRAINEE);

		final RobotBrain robotBrain = createStrictMock(RobotBrain.class);
		robotBrain.putInAction(gameBoard, RobotType.TRAINEE);
		replay(robotBrain);

		expect(brainManager.getRobotBrain(ROOM, RobotType.TRAINEE)).andReturn(robotBrain);
		replay(brainManager);

		replay(roomManager);

		final TransactionStatus status = createNiceMock(TransactionStatus.class);

		final PlatformTransactionManager transaction = createStrictMock(PlatformTransactionManager.class);
		expect(transaction.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW))).andReturn(status);
		transaction.commit(status);
		replay(transaction);

		activityManager.setTransactionManager(transaction);

		final RobotActivityCenter.MakeTurnTask task = activityManager.new MakeTurnTask(ROOM, gameBoard);
		task.run();

		verify(brainManager, roomManager, robotBrain, transaction);
	}

	@Test
	public void initializeManager() throws BoardLoadingException {
		final RobotPlayer p1 = createRobotPlayer(1L, RobotType.TRAINEE);
		final RobotPlayer p2 = createRobotPlayer(2L, RobotType.DULL);

		final GameBoard gameBoard = createStrictMock(GameBoard.class);
		expectGameMoveListener(gameBoard);
		expect(gameBoard.getPlayerTrun()).andReturn(createPlayerHand(1L));
		expectGameMoveListener(gameBoard);
		expect(gameBoard.getPlayerTrun()).andReturn(createPlayerHand(2L));
		replay(gameBoard);

		expect(brainManager.getRobotPlayers()).andReturn(Arrays.asList(p1, p2));
		replay(brainManager);

		expect(roomManager.getRoomType()).andReturn(ROOM);
		expectRoomBoardsListener();
		expect(roomManager.getActiveBoards(p1)).andReturn(Arrays.asList(gameBoard));
		expect(roomManager.getActiveBoards(p2)).andReturn(Arrays.asList(gameBoard));
		replay(roomManager);

		// Where is two robots and two tasks must be executed.
		final Executor executor = createStrictMock(Executor.class);
		executor.execute(isA(RobotActivityCenter.MakeTurnTask.class));
		executor.execute(isA(RobotActivityCenter.MakeTurnTask.class));
		replay(executor);

		activityManager.setMovesExecutor(executor);

		verify(gameBoard, brainManager, roomManager, executor);
	}

	@Test
	public void robotMove() throws BoardLoadingException {
		expect(brainManager.getRobotPlayers()).andReturn(Collections.<RobotPlayer>emptyList());
		replay(brainManager);

		expect(roomManager.getRoomType()).andReturn(ROOM);
		expectRoomBoardsListener();
		replay(roomManager);

		final Executor executor = createStrictMock(Executor.class);
		replay(executor);

		activityManager.setMovesExecutor(executor);
		verify(brainManager, roomManager, executor);

		//Test room game opened
		reset(brainManager, roomManager, executor);
		final GameBoard gameBoard = createStrictMock(GameBoard.class);

		expectGameMoveListener(gameBoard);
		expect(gameBoard.getPlayerTrun()).andReturn(createPlayerHand(10023L));
		replay(gameBoard);

		expect(roomManager.openBoard(1L)).andReturn(gameBoard);
		replay(roomManager);

		replay(executor);

		boardsListener.boardOpened(ROOM, 1L);
		verify(gameBoard, brainManager, roomManager, executor);

		// Test move transfered
		reset(gameBoard, brainManager, roomManager, executor);
		executor.execute(isA(RobotActivityCenter.MakeTurnTask.class));
		replay(executor, brainManager, gameBoard, roomManager);

		gameMoveListener.playerMoved(new GameMoveEvent(gameBoard, createPlayerHand(13L), new GameMove(new PassTurnMove(13L), 0, 0, new Date()), createPlayerHand(1L)));

		verify(gameBoard, brainManager, roomManager, executor);
	}

	private void expectRoomBoardsListener() {
		roomManager.addRoomBoardsListener(isA(RoomBoardsListener.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				boardsListener = (RoomBoardsListener) getCurrentArguments()[0];
				return null;
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void expectGameMoveListener(GameBoard gameBoard) {
		gameBoard.addGameMoveListener(isA(GameMoveListener.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				gameMoveListener = (GameMoveListener) getCurrentArguments()[0];
				return null;
			}
		});
		gameBoard.addGameStateListener(isA(GameStateListener.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				gameStateListener = (GameStateListener) getCurrentArguments()[0];
				return null;
			}
		});
	}

	private RobotPlayer createRobotPlayer(long id, RobotType type) {
		RobotPlayer mock = createMock("Robot" + id, RobotPlayer.class);
		expect(mock.getId()).andReturn(id).anyTimes();
		expect(mock.getRobotType()).andReturn(type).anyTimes();
		replay(mock);
		return mock;
	}

	private GamePlayerHand createPlayerHand(long id) {
		GamePlayerHand mock = createMock("PlayerHand" + id, GamePlayerHand.class);
		expect(mock.getPlayerId()).andReturn(id).anyTimes();
		replay(mock);
		return mock;
	}
}
