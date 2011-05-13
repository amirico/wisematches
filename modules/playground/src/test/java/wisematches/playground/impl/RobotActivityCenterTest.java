package wisematches.playground.impl;

import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.personality.player.computer.robot.RobotType;
import wisematches.playground.room.MockRoom;
import wisematches.server.playground.board.BoardLoadingException;
import wisematches.server.playground.board.BoardManager;
import wisematches.server.playground.board.BoardStateListener;
import wisematches.server.playground.robot.RobotBrain;
import wisematches.server.playground.robot.RobotBrainManager;
import wisematches.server.playground.room.RoomManager;
import wisematches.server.playground.room.RoomsManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.Executor;

import static org.easymock.EasyMock.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RobotActivityCenterTest {
	private RoomManager roomManager;
	private BoardManager boardManager;
	private RobotBrainManager brainManager;
	private RobotActivityCenter activityManager;

	private BoardStateListener listener;

	public RobotActivityCenterTest() {
	}

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() {
		activityManager = new RobotActivityCenter();

		roomManager = createStrictMock(RoomManager.class);
		brainManager = createStrictMock(RobotBrainManager.class);
		boardManager = createStrictMock(BoardManager.class);

		final RoomsManager roomsManager = createStrictMock(RoomsManager.class);
		expect(roomsManager.getRoomManagers()).andReturn(Arrays.asList(roomManager)).anyTimes();
		expect(roomsManager.getRoomManager(MockRoom.type)).andReturn(roomManager).anyTimes();
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
		final RobotPlayer player = RobotPlayer.valueOf(RobotType.TRAINEE);

		final GameBoard gameBoard = createStrictMock(GameBoard.class);
		expect(gameBoard.getPlayerTurn()).andReturn(createPlayerHand(player.getId()));
		replay(gameBoard);

		@SuppressWarnings("unchecked")
		final RobotBrain<GameBoard> robotBrain = createStrictMock(RobotBrain.class);
		robotBrain.putInAction(gameBoard, RobotType.TRAINEE);
		replay(robotBrain);

		expect(brainManager.getRobotBrain(MockRoom.type, RobotType.TRAINEE)).andReturn(robotBrain);
		replay(brainManager);

		replay(roomManager);

		final TransactionStatus status = createNiceMock(TransactionStatus.class);

		final PlatformTransactionManager transaction = createStrictMock(PlatformTransactionManager.class);
		expect(transaction.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW))).andReturn(status);
		transaction.commit(status);
		replay(transaction);

		activityManager.setTransactionManager(transaction);

		final RobotActivityCenter.MakeTurnTask task = activityManager.new MakeTurnTask(MockRoom.type, gameBoard);
		task.run();

		verify(brainManager, roomManager, robotBrain, transaction);
	}

	@Test
	public void initializeManager() throws BoardLoadingException {
		final RobotPlayer p1 = RobotPlayer.valueOf(RobotType.TRAINEE);
		final RobotPlayer p2 = RobotPlayer.valueOf(RobotType.DULL);

		final GameBoard gameBoard = createStrictMock(GameBoard.class);
		expect(gameBoard.getPlayerTurn()).andReturn(createPlayerHand(p1.getId()));
		expect(gameBoard.getPlayerTurn()).andReturn(createPlayerHand(p2.getId()));
		replay(gameBoard);

		expect(brainManager.getRobotPlayers()).andReturn(Arrays.asList(p1, p2));
		replay(brainManager);

		expect(roomManager.getRoomType()).andReturn(MockRoom.type);
		expect(roomManager.getBoardManager()).andReturn(boardManager);
		expectRoomBoardsListener();
		expect(boardManager.getActiveBoards(p1)).andReturn(Arrays.asList(gameBoard));
		expect(boardManager.getActiveBoards(p2)).andReturn(Arrays.asList(gameBoard));
		replay(roomManager, boardManager);

		// Where is two robots and two tasks must be executed.
		final Executor executor = createStrictMock(Executor.class);
		executor.execute(isA(RobotActivityCenter.MakeTurnTask.class));
		executor.execute(isA(RobotActivityCenter.MakeTurnTask.class));
		replay(executor);

		activityManager.setMovesExecutor(executor);

		verify(gameBoard, brainManager, roomManager, boardManager, executor);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void robotMove() throws BoardLoadingException {
		final RobotPlayer p1 = RobotPlayer.valueOf(RobotType.TRAINEE);

		expect(brainManager.getRobotPlayers()).andReturn(Collections.<RobotPlayer>emptyList());
		replay(brainManager);

		expectRoomBoardsListener();
		replay(boardManager);

		expect(roomManager.getRoomType()).andReturn(MockRoom.type);
		expect(roomManager.getBoardManager()).andReturn(boardManager);
		replay(roomManager);

		final Executor executor = createStrictMock(Executor.class);
		replay(executor);

		activityManager.setMovesExecutor(executor);
		verify(brainManager, roomManager, boardManager, executor);

		//Test room game started
		reset(brainManager, roomManager, boardManager, executor);

		final GameBoard board = createStrictMock(GameBoard.class);
		expect(board.getPlayerTurn()).andReturn(null);

		replay(board, roomManager, boardManager, brainManager, executor);

		listener.gameStarted(board);
		verify(board, brainManager, boardManager, roomManager, executor);

		// Test move transfered
		reset(board, brainManager, roomManager, executor);
		expect(board.getPlayerTurn()).andReturn(createPlayerHand(p1.getId()));
		executor.execute(isA(RobotActivityCenter.MakeTurnTask.class));
		replay(executor, brainManager, board, roomManager);

		listener.gameMoveDone(board, new GameMove(new PassTurnMove(13L), 0, 0, new Date()));

		verify(board, brainManager, roomManager, executor);
	}

	private void expectRoomBoardsListener() {
		boardManager.addBoardStateListener(isA(BoardStateListener.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				listener = (BoardStateListener) getCurrentArguments()[0];
				return null;
			}
		});
	}

	private GamePlayerHand createPlayerHand(long id) {
		GamePlayerHand mock = createMock("PlayerHand" + id, GamePlayerHand.class);
		expect(mock.getPlayerId()).andReturn(id).anyTimes();
		replay(mock);
		return mock;
	}
}
