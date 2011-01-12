package wisematches.server.robot.impl;

import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import wisematches.server.games.board.*;
import wisematches.server.games.room.*;
import wisematches.server.robot.RobotBrain;
import wisematches.server.robot.RobotPlayer;
import wisematches.server.robot.RobotType;
import wisematches.server.robot.RobotsBrainManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.Executor;

import static org.easymock.EasyMock.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RobotsActivityCenterTest {
	private RoomBoardsListener boardsListener;
	private GameMoveListener gameMoveListener;
	private GameStateListener gameStateListener;

	private RoomManager roomManager;
	private RobotsBrainManager brainManager;
	private RobotsActivityCenter activityManager;

	private static final Room ROOM = Room.valueOf("mock");

	@Before
	public void setUp() {
		activityManager = new RobotsActivityCenter();

		brainManager = createStrictMock(RobotsBrainManager.class);
		roomManager = createStrictMock(RoomManager.class);

		final RoomsManager roomsManager = createStrictMock(RoomsManager.class);
		expect(roomsManager.getRoomManagers()).andReturn(Arrays.asList(roomManager)).anyTimes();
		expect(roomsManager.getRoomManager(ROOM)).andReturn(roomManager).anyTimes();
		replay(roomsManager);

		activityManager.setRoomsManager(roomsManager);
		activityManager.setRobotsBrainManager(brainManager);

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

		final RobotPlayer player = createRobotPlayer(1L, RobotType.STAGER);

		final RobotBrain robotBrain = createStrictMock(RobotBrain.class);
		robotBrain.putInAction(gameBoard, RobotType.STAGER);
		replay(robotBrain);

		expect(brainManager.isRobotPlayer(1L)).andReturn(true);
		expect(brainManager.getPlayer(1L)).andReturn(player);
		expect(brainManager.getRobotBrain(ROOM, RobotType.STAGER)).andReturn(robotBrain);
		replay(brainManager);

		replay(roomManager);

		final TransactionStatus status = createNiceMock(TransactionStatus.class);

		final PlatformTransactionManager transaction = createStrictMock(PlatformTransactionManager.class);
		expect(transaction.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW))).andReturn(status);
		transaction.commit(status);
		replay(transaction);

		activityManager.setTransactionManager(transaction);

		final RobotsActivityCenter.MakeTurnTask task = activityManager.new MakeTurnTask(ROOM, gameBoard);
		task.run();

		verify(brainManager, roomManager, robotBrain, transaction);
	}

	@Test
	public void initializeManager() throws BoardLoadingException {
		final RobotPlayer p1 = createRobotPlayer(1L, RobotType.STAGER);
		final RobotPlayer p2 = createRobotPlayer(2L, RobotType.DULL);

		final GameBoard gameBoard = createStrictMock(GameBoard.class);
		expectGameMoveListener(gameBoard);
		expect(gameBoard.getPlayerTrun()).andReturn(createPlayerHand(1L));
		expectGameMoveListener(gameBoard);
		expect(gameBoard.getPlayerTrun()).andReturn(createPlayerHand(2L));
		replay(gameBoard);

		expect(brainManager.getRobotPlayers()).andReturn(Arrays.asList(p1, p2));
		expect(brainManager.isRobotPlayer(1L)).andReturn(true);
		expect(brainManager.isRobotPlayer(2L)).andReturn(true);
		replay(brainManager);

		expect(roomManager.getRoomType()).andReturn(ROOM);
		expectRoomBoardsListener();
		expect(roomManager.getActiveBoards(p1)).andReturn(Arrays.asList(gameBoard));
		expect(roomManager.getActiveBoards(p2)).andReturn(Arrays.asList(gameBoard));
		replay(roomManager);

		// Where is two robots and two tasks must be executed.
		final Executor executor = createStrictMock(Executor.class);
		executor.execute(isA(RobotsActivityCenter.MakeTurnTask.class));
		executor.execute(isA(RobotsActivityCenter.MakeTurnTask.class));
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
		expect(gameBoard.getPlayerTrun()).andReturn(createPlayerHand(3L));
		replay(gameBoard);

		expect(brainManager.isRobotPlayer(3L)).andReturn(false);
		replay(brainManager);

		expect(roomManager.openBoard(1L)).andReturn(gameBoard);
		replay(roomManager);

		replay(executor);

		boardsListener.boardOpened(ROOM, 1L);
		verify(gameBoard, brainManager, roomManager, executor);

		// Test move transfered
		reset(gameBoard, brainManager, roomManager, executor);
		executor.execute(isA(RobotsActivityCenter.MakeTurnTask.class));
		expect(brainManager.isRobotPlayer(1L)).andReturn(true);
		replay(executor, brainManager, gameBoard, roomManager);

		gameMoveListener.playerMoved(new GameMoveEvent(gameBoard, createPlayerHand(13L), new GameMove(new PassTurnMove(13L), 0, 0, 1), createPlayerHand(1L)));

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
