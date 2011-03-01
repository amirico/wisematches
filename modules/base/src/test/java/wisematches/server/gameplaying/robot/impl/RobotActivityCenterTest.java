package wisematches.server.gameplaying.robot.impl;

import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import wisematches.server.gameplaying.board.GameBoard;
import wisematches.server.gameplaying.board.GameMoveException;
import wisematches.server.gameplaying.board.GamePlayerHand;
import wisematches.server.gameplaying.robot.RobotBrain;
import wisematches.server.gameplaying.robot.RobotBrainManager;
import wisematches.server.gameplaying.room.MockRoom;
import wisematches.server.gameplaying.room.RoomManager;
import wisematches.server.gameplaying.room.RoomsManager;
import wisematches.server.gameplaying.room.board.BoardLoadingException;
import wisematches.server.gameplaying.room.board.BoardManager;
import wisematches.server.gameplaying.room.board.BoardStateListener;
import wisematches.server.player.computer.robot.RobotPlayer;
import wisematches.server.player.computer.robot.RobotType;

import java.util.Arrays;

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

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() {
		activityManager = new RobotActivityCenter();

		roomManager = createStrictMock(RoomManager.class);
		brainManager = createStrictMock(RobotBrainManager.class);
		boardManager = createStrictMock(BoardManager.class);

		final RoomsManager roomsManager = createStrictMock(RoomsManager.class);
		expect(roomsManager.getRoomManagers()).andReturn(Arrays.asList(roomManager)).anyTimes();
		expect(roomsManager.getRoomManager(MockRoom.room)).andReturn(roomManager).anyTimes();
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

		expect(brainManager.getRobotBrain(MockRoom.room, RobotType.TRAINEE)).andReturn(robotBrain);
		replay(brainManager);

		replay(roomManager);

		final TransactionStatus status = createNiceMock(TransactionStatus.class);

		final PlatformTransactionManager transaction = createStrictMock(PlatformTransactionManager.class);
		expect(transaction.getTransaction(new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW))).andReturn(status);
		transaction.commit(status);
		replay(transaction);

		activityManager.setTransactionManager(transaction);

		final RobotActivityCenter.MakeTurnTask task = activityManager.new MakeTurnTask(MockRoom.room, gameBoard);
		task.run();

		verify(brainManager, roomManager, robotBrain, transaction);
	}

	@Test
	public void commented() {
		throw new UnsupportedOperationException("Test has been commented");
	}
/*
	@Test
	public void initializeManager() throws BoardLoadingException {
		final RobotPlayer p1 = RobotPlayer.valueOf(RobotType.TRAINEE);
		final RobotPlayer p2 = RobotPlayer.valueOf(RobotType.DULL);

		final GameBoard gameBoard = createStrictMock(GameBoard.class);
		expectGameMoveListener(gameBoard);
		expect(gameBoard.getPlayerTurn()).andReturn(createPlayerHand(p1.getId()));
		expectGameMoveListener(gameBoard);
		expect(gameBoard.getPlayerTurn()).andReturn(createPlayerHand(p2.getId()));
		replay(gameBoard);

		expect(brainManager.getRobotPlayers()).andReturn(Arrays.asList(p1, p2));
		replay(brainManager);

		expect(roomManager.getRoomType()).andReturn(MockRoom.room);
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
	public void robotMove() throws BoardLoadingException {
		final RobotPlayer p1 = RobotPlayer.valueOf(RobotType.TRAINEE);

		expect(brainManager.getRobotPlayers()).andReturn(Collections.<RobotPlayer>emptyList());
		replay(brainManager);

		expect(roomManager.getRoomType()).andReturn(MockRoom.room);
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
		replay(gameBoard);

		expect(roomManager.openBoard(1L)).andReturn(gameBoard);
		replay(roomManager);

		replay(brainManager, executor);

		listener.boardOpened(MockRoom.room, 1L);
		verify(gameBoard, brainManager, roomManager, executor);

		// Test move transfered
		reset(gameBoard, brainManager, roomManager, executor);
		expect(gameBoard.getPlayerTurn()).andReturn(createPlayerHand(p1.getId()));
		executor.execute(isA(RobotActivityCenter.MakeTurnTask.class));
		replay(executor, brainManager, gameBoard, roomManager);

		boardStateListener.gameMoveMade(new GameMoveEvent(gameBoard, createPlayerHand(13L), new GameMove(new PassTurnMove(13L), 0, 0, new Date()), createPlayerHand(p1.getId())));

		verify(gameBoard, brainManager, roomManager, executor);
	}
*/

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
