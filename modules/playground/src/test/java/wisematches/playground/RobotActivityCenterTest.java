package wisematches.playground;

import org.junit.Test;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RobotActivityCenterTest {
	@Test
	public void commented() {
		throw new UnsupportedOperationException("Commented");
	}
/*
	private GamePlayManager gamePlayManager;
	private RobotBrain<GameBoard> robotBrain;
	private RobotActivityCenter activityManager;

	private Capture<GamePlayListener> listener = new Capture<>();

	public RobotActivityCenterTest() {
	}

	@Before
	@SuppressWarnings("unchecked")
	public void setUp() {
		activityManager = new RobotActivityCenter();

		robotBrain = createStrictMock(RobotBrain.class);
		gamePlayManager = createStrictMock(GamePlayManager.class);

		expect(robotBrain.getRobotPlayers()).andReturn(new RobotPlayer[]{RobotPlayer.DULL, RobotPlayer.TRAINEE, RobotPlayer.EXPERT});
		replay(robotBrain);

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
		activityManager.setGamePlayManager(gamePlayManager);
		activityManager.setTaskExecutor(taskExecutor);
	}

	@Test
	public void makeTurnTask() throws BoardLoadingException, GameMoveException {
		final RobotPlayer player = RobotPlayer.TRAINEE;

		final GameBoard gameBoard = createStrictMock(GameBoard.class);
		expect(gameBoard.getPlayerTurn()).andReturn(createPlayerHand(player.getId()));
		replay(gameBoard);

		expect(gamePlayManager.openBoard(1L)).andReturn(gameBoard);
		replay(gamePlayManager);

		reset(robotBrain);
		expect(robotBrain.isRobotTurn(gameBoard)).andReturn(true);
		expect(robotBrain.performRobotMove(gameBoard)).andReturn(null);
		replay(robotBrain);

		final RobotActivityCenter.MakeTurnTask task = activityManager.new MakeTurnTask(1L, 1);
		task.run();

		verify(robotBrain, gamePlayManager);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void initializeManager() throws BoardLoadingException {
		final RobotPlayer p1 = RobotPlayer.DULL;
		final RobotPlayer p2 = RobotPlayer.TRAINEE;
		final RobotPlayer p3 = RobotPlayer.EXPERT;

		final GameBoard gameBoard = createStrictMock(GameBoard.class);
		expect(gameBoard.getPlayerTurn()).andReturn(createPlayerHand(p1.getId()));
		expect(gameBoard.getBoardId()).andReturn(1L);
		expect(gameBoard.getPlayerTurn()).andReturn(createPlayerHand(p2.getId()));
		expect(gameBoard.getPlayerTurn()).andReturn(createPlayerHand(p1.getId()));
		expect(gameBoard.getBoardId()).andReturn(1L);
		expect(gameBoard.getPlayerTurn()).andReturn(createPlayerHand(p2.getId()));
		replay(gameBoard);

		reset(robotBrain);
		expect(robotBrain.getRobotPlayers()).andReturn(new RobotPlayer[]{p1, p2, p3});
		expect(robotBrain.isRobotTurn(gameBoard)).andReturn(true);
		expect(robotBrain.performRobotMove(gameBoard)).andReturn(null);
		expect(robotBrain.isRobotTurn(gameBoard)).andReturn(true);
		expect(robotBrain.performRobotMove(gameBoard)).andReturn(null);
		expect(robotBrain.isRobotTurn(gameBoard)).andReturn(false);
		replay(robotBrain);

		gamePlayManager.addGamePlayListener(capture(listener));
		expect(gamePlayManager.searchEntities(p1, GameState.ACTIVE, null, null, null)).andReturn(Arrays.asList(gameBoard));
		expect(gamePlayManager.openBoard(1L)).andReturn(gameBoard);
		expect(gamePlayManager.searchEntities(p2, GameState.ACTIVE, null, null, null)).andReturn(Arrays.asList(gameBoard));
		expect(gamePlayManager.openBoard(1L)).andReturn(gameBoard);
		expect(gamePlayManager.searchEntities(p3, GameState.ACTIVE, null, null, null)).andReturn(Collections.emptyList());
		replay(gamePlayManager);

		activityManager.afterPropertiesSet();

		verify(gameBoard, gamePlayManager);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void robotMove() throws BoardLoadingException {
		final RobotPlayer p1 = RobotPlayer.TRAINEE;

		gamePlayManager.addGamePlayListener(capture(listener));
		expect(gamePlayManager.searchEntities(RobotPlayer.DULL, GameState.ACTIVE, null, null, null)).andReturn(Collections.emptyList());
		expect(gamePlayManager.searchEntities(RobotPlayer.TRAINEE, GameState.ACTIVE, null, null, null)).andReturn(Collections.emptyList());
		expect(gamePlayManager.searchEntities(RobotPlayer.EXPERT, GameState.ACTIVE, null, null, null)).andReturn(Collections.emptyList());
		replay(gamePlayManager);

		activityManager.afterPropertiesSet();

		reset(gamePlayManager);

		//Test room game started
		final GameBoard board = createStrictMock(GameBoard.class);
		replay(board, gamePlayManager);

		reset(robotBrain);
		expect(robotBrain.isRobotTurn(board)).andReturn(false);
		replay(robotBrain);

		listener.getValue().gameStarted(board);
		verify(board, gamePlayManager);

		// Test move transferred
		reset(robotBrain);
		expect(robotBrain.isRobotTurn(board)).andReturn(true).times(2);
		expect(robotBrain.performRobotMove(board)).andReturn(null);
		replay(robotBrain);

		reset(board);
		expect(board.getBoardId()).andReturn(1L);
		replay(board);

		reset(gamePlayManager);
		expect(gamePlayManager.openBoard(1L)).andReturn(board);
		replay(gamePlayManager);

		listener.getValue().gameMoveDone(board, new GameMove(new PassTurn(13L), 0, 0, new Date()), null);

		verify(board, gamePlayManager);
	}

	private GamePlayerHand createPlayerHand(long id) {
		GamePlayerHand mock = createMock("PlayerHand" + id, GamePlayerHand.class);
		expect(mock.getPlayerId()).andReturn(id).anyTimes();
		replay(mock);
		return mock;
	}
*/
}
