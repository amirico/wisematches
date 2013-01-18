package wisematches.playground.robot;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;
import wisematches.core.personality.proprietary.robot.RobotPlayer;
import wisematches.core.personality.proprietary.robot.RobotType;
import wisematches.core.task.TransactionalExecutor;
import wisematches.playground.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.easymock.EasyMock.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RobotActivityCenterTest {
	private RobotBrain<GameBoard> robotBrain;
	private BoardManager boardManager;
	private RobotActivityCenter activityManager;

	private Capture<BoardStateListener> listener = new Capture<>();

	public RobotActivityCenterTest() {
	}

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() {
		activityManager = new RobotActivityCenter();

		robotBrain = createStrictMock(RobotBrain.class);
		boardManager = createStrictMock(BoardManager.class);

		TransactionalExecutor taskExecutor = createMock(TransactionalExecutor.class);
		taskExecutor.execute(EasyMock.<Runnable>anyObject());
		expectLastCall().andAnswer(new IAnswer<Object>() {
			@Override
			public Object answer() throws Throwable {
				((Runnable) getCurrentArguments()[0]).run();
				return null;
			}
		}).anyTimes();
		taskExecutor.executeStraight(EasyMock.<Runnable>anyObject());
		expectLastCall().andAnswer(new IAnswer<Object>() {
			@Override
			public Object answer() throws Throwable {
				((Runnable) getCurrentArguments()[0]).run();
				return null;
			}
		}).anyTimes();
		replay(taskExecutor);

		activityManager.setRobotBrain(robotBrain);
		activityManager.setBoardManager(boardManager);
		activityManager.setTaskExecutor(taskExecutor);
	}

	@Test
	public void makeTurnTask() throws BoardLoadingException, GameMoveException {
		final RobotPlayer player = RobotPlayer.valueOf(RobotType.TRAINEE);

		final GameBoard gameBoard = createStrictMock(GameBoard.class);
		expect(gameBoard.getPlayerTurn()).andReturn(createPlayerHand(player.getId()));
		replay(gameBoard);

		expect(boardManager.openBoard(1L)).andReturn(gameBoard);
		replay(boardManager);

		robotBrain.putInAction(gameBoard, RobotType.TRAINEE);
		replay(robotBrain);

		final RobotActivityCenter.MakeTurnTask task = activityManager.new MakeTurnTask(1L, 1);
		task.run();

		verify(robotBrain, boardManager);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void initializeManager() throws BoardLoadingException {
		final RobotPlayer p1 = RobotPlayer.valueOf(RobotType.DULL);
		final RobotPlayer p2 = RobotPlayer.valueOf(RobotType.TRAINEE);
		final RobotPlayer p3 = RobotPlayer.valueOf(RobotType.EXPERT);

		final GameBoard gameBoard = createStrictMock(GameBoard.class);
		expect(gameBoard.getPlayerTurn()).andReturn(createPlayerHand(p1.getId()));
		expect(gameBoard.getBoardId()).andReturn(1L);
		expect(gameBoard.getPlayerTurn()).andReturn(createPlayerHand(p2.getId()));
		expect(gameBoard.getPlayerTurn()).andReturn(createPlayerHand(p1.getId()));
		expect(gameBoard.getBoardId()).andReturn(1L);
		expect(gameBoard.getPlayerTurn()).andReturn(createPlayerHand(p2.getId()));
		replay(gameBoard);

		boardManager.addBoardStateListener(capture(listener));
		expect(boardManager.searchEntities(p1, GameState.ACTIVE, null, null, null)).andReturn(Arrays.asList(gameBoard));
		expect(boardManager.openBoard(1L)).andReturn(gameBoard);
		expect(boardManager.searchEntities(p2, GameState.ACTIVE, null, null, null)).andReturn(Arrays.asList(gameBoard));
		expect(boardManager.openBoard(1L)).andReturn(gameBoard);
		expect(boardManager.searchEntities(p3, GameState.ACTIVE, null, null, null)).andReturn(Collections.emptyList());
		replay(boardManager);

		activityManager.afterPropertiesSet();

		verify(gameBoard, boardManager);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void robotMove() throws BoardLoadingException {
		final RobotPlayer p1 = RobotPlayer.valueOf(RobotType.TRAINEE);

		boardManager.addBoardStateListener(capture(listener));
		expect(boardManager.searchEntities(RobotPlayer.valueOf(RobotType.DULL), GameState.ACTIVE, null, null, null)).andReturn(Collections.emptyList());
		expect(boardManager.searchEntities(RobotPlayer.valueOf(RobotType.TRAINEE), GameState.ACTIVE, null, null, null)).andReturn(Collections.emptyList());
		expect(boardManager.searchEntities(RobotPlayer.valueOf(RobotType.EXPERT), GameState.ACTIVE, null, null, null)).andReturn(Collections.emptyList());
		replay(boardManager);

		activityManager.afterPropertiesSet();

		reset(boardManager);

		//Test room game started
		final GameBoard board = createStrictMock(GameBoard.class);
		expect(board.getPlayerTurn()).andReturn(null);
		replay(board, boardManager);

		listener.getValue().gameStarted(board);
		verify(board, boardManager);

		// Test move transferred
		reset(board);
		expect(board.getPlayerTurn()).andReturn(createPlayerHand(p1.getId()));
		expect(board.getBoardId()).andReturn(1L);
		expect(board.getPlayerTurn()).andReturn(createPlayerHand(p1.getId()));
		replay(board);

		reset(boardManager);
		expect(boardManager.openBoard(1L)).andReturn(board);
		replay(boardManager);

		listener.getValue().gameMoveDone(board, new GameMove(new PassTurnMove(13L), 0, 0, new Date()), null);

		verify(board, boardManager);
	}

	private GamePlayerHand createPlayerHand(long id) {
		GamePlayerHand mock = createMock("PlayerHand" + id, GamePlayerHand.class);
		expect(mock.getPlayerId()).andReturn(id).anyTimes();
		replay(mock);
		return mock;
	}
}
