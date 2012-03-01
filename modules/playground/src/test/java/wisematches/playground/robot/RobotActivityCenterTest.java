package wisematches.playground.robot;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.personality.player.computer.robot.RobotType;
import wisematches.playground.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.Executor;

import static org.easymock.EasyMock.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RobotActivityCenterTest {
	private RobotBrain<GameBoard> robotBrain;
	private BoardManager boardManager;
	private RobotActivityCenter activityManager;

	private Capture<BoardStateListener> listener = new Capture<BoardStateListener>();

	private final TransactionTemplate template = new TransactionTemplate() {
		@Override
		public <T> T execute(TransactionCallback<T> action) throws TransactionException {
			return action.doInTransaction(null);
		}
	};

	public RobotActivityCenterTest() {
	}

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() {
		activityManager = new RobotActivityCenter();

		robotBrain = createStrictMock(RobotBrain.class);
		boardManager = createStrictMock(BoardManager.class);

		activityManager.setRobotBrain(robotBrain);
		activityManager.setBoardManager(boardManager);
		activityManager.setTransactionTemplate(template);
	}

	@Test
	public void makeTurnTask() throws BoardLoadingException, GameMoveException {
		final RobotPlayer player = RobotPlayer.valueOf(RobotType.TRAINEE);

		final GameBoard gameBoard = createStrictMock(GameBoard.class);
		expect(gameBoard.getPlayerTurn()).andReturn(createPlayerHand(player.getId()));
		replay(gameBoard);

		robotBrain.putInAction(gameBoard, RobotType.TRAINEE);
		replay(robotBrain);

		activityManager.setTransactionTemplate(template);

		final RobotActivityCenter.MakeTurnTask task = activityManager.new MakeTurnTask(gameBoard);
		task.run();

		verify(robotBrain);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void initializeManager() throws BoardLoadingException {
		final RobotPlayer p1 = RobotPlayer.valueOf(RobotType.DULL);
		final RobotPlayer p2 = RobotPlayer.valueOf(RobotType.TRAINEE);
		final RobotPlayer p3 = RobotPlayer.valueOf(RobotType.EXPERT);

		final GameBoard gameBoard = createStrictMock(GameBoard.class);
		expect(gameBoard.getPlayerTurn()).andReturn(createPlayerHand(p1.getId()));
		expect(gameBoard.getPlayerTurn()).andReturn(createPlayerHand(p2.getId()));
		replay(gameBoard);

		boardManager.addBoardStateListener(capture(listener));
		expect(boardManager.searchEntities(p1, GameState.ACTIVE, null, null, null)).andReturn(Arrays.asList(gameBoard));
		expect(boardManager.searchEntities(p2, GameState.ACTIVE, null, null, null)).andReturn(Arrays.asList(gameBoard));
		expect(boardManager.searchEntities(p3, GameState.ACTIVE, null, null, null)).andReturn(Collections.emptyList());
		replay(boardManager);

		// Where is two robots and two tasks must be executed.
		final Executor executor = createStrictMock(Executor.class);
		executor.execute(isA(RobotActivityCenter.MakeTurnTask.class));
		executor.execute(isA(RobotActivityCenter.MakeTurnTask.class));
		replay(executor);

		activityManager.setMovesExecutor(executor);

		verify(gameBoard, boardManager, executor);
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

		final Executor executor = createStrictMock(Executor.class);
		replay(executor);

		activityManager.setMovesExecutor(executor);
		verify(boardManager, executor);

		//Test room game started
		reset(boardManager, executor);

		final GameBoard board = createStrictMock(GameBoard.class);
		expect(board.getPlayerTurn()).andReturn(null);

		replay(board, boardManager, executor);

		listener.getValue().gameStarted(board);
		verify(board, boardManager, executor);

		// Test move transferred
		reset(board, executor);
		expect(board.getPlayerTurn()).andReturn(createPlayerHand(p1.getId()));
		executor.execute(isA(RobotActivityCenter.MakeTurnTask.class));
		replay(executor, board);

		listener.getValue().gameMoveDone(board, new GameMove(new PassTurnMove(13L), 0, 0, new Date()), null);

		verify(board, executor);
	}

	private GamePlayerHand createPlayerHand(long id) {
		GamePlayerHand mock = createMock("PlayerHand" + id, GamePlayerHand.class);
		expect(mock.getPlayerId()).andReturn(id).anyTimes();
		replay(mock);
		return mock;
	}
}
