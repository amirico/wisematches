package wisematches.playground;


import org.easymock.Capture;
import org.easymock.IAnswer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wisematches.core.Personality;
import wisematches.core.Player;
import wisematches.core.Robot;
import wisematches.core.RobotType;
import wisematches.core.personality.DefaultMember;
import wisematches.playground.tracking.StatisticManager;

import java.util.*;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@SuppressWarnings("unchecked")
public class AbstractBoardManagerTest {
	private static final Logger log = LoggerFactory.getLogger(AbstractBoardManagerTest.class);

	private BoardListener gamePlayListener;

	private static final Player player1 = new DefaultMember(901, null, null, null, null, null);
	private static final Player player2 = new DefaultMember(902, null, null, null, null, null);

	public AbstractBoardManagerTest() {
	}

	@Test
	public void testBoardsMap() throws InterruptedException {
		GameBoard board1 = createMock("Board1", GameBoard.class);
		expect(board1.getBoardId()).andReturn(1L).anyTimes();
		replay(board1);

		GameBoard board2 = createMock("Board1", GameBoard.class);
		expect(board2.getBoardId()).andReturn(2L).anyTimes();
		replay(board2);

		AbstractGamePlayManager.BoardsMap<GameBoard> board = new AbstractGamePlayManager.BoardsMap<>(log);
		assertEquals(0, board.size());

		board.addBoard(board1);
		assertEquals(1, board.size());

		board.addBoard(board2);
		assertEquals(2, board.size());

		// Start GC. We still have references to boards.
		System.gc();
		Thread.sleep(300);
		assertEquals(2, board.size());

		assertSame("Check that boards are returned", board1, board.getBoard(1L));
		assertSame("Check that boards are returned", board2, board.getBoard(2L));
		assertNull("Checks that no exist board return null", board.getBoard(3L));

		board1 = null; //Remove board1
		System.gc();
		Thread.sleep(300);
		assertEquals("If board has no reference it must be removed", 1, board.size());
		assertNull("Check that removed board return null", board.getBoard(1L));
		assertSame("Check that boards are returned", board2, board.getBoard(2L));

		board2 = null; //Remove board1
		System.gc();
		Thread.sleep(300);
		assertNull("Check that removed board return null", board.getBoard(1L));
		assertNull("Check that removed board return null", board.getBoard(2L));
		assertEquals(0, board.size());
	}

	@Test
	public void testCreatePlayersGame() throws BoardCreationException {
		final GameSettings settings = new MockGameSettings("test", 3);

		final Collection<Personality> players = Arrays.<Personality>asList(player1, player2);

		final AbstractGameBoard<GameSettings, AbstractPlayerHand, GameMove> board = createStrictMock(AbstractGameBoard.class);
		expectListeners(board);
		expect(board.getBoardId()).andReturn(1L);
		replay(board);

		final Capture<Collection<Personality>> personalityCapture = new Capture<>();

		final GameBoardDao dao = createStrictMock(GameBoardDao.class);
		expect(dao.createBoardImpl(same(settings), capture(personalityCapture), isNull(GameRelationship.class))).andReturn(board);
		dao.saveBoardImpl(board);
		replay(dao);

		final MockGamePlayManager mock = new MockGamePlayManager(dao);

		final GameBoard<?, ?, ?> newBoard = mock.createBoard(settings, players, null);
		assertSame(board, newBoard);

		assertEquals(personalityCapture.getValue(), new ArrayList<Personality>(players));

		verify(board);
		verify(dao);
	}

/*
	@Test
	public void testCreateRobotsGame() throws BoardCreationException {
		final GameSettings settings = new MockGameSettings("test", 3);

		final AbstractGameBoard<GameSettings, AbstractPlayerHand> board = createStrictMock(AbstractGameBoard.class);
		expectListeners(board);
		expect(board.getBoardId()).andReturn(1L);
		expect(board.getPlayerTurn()).andReturn(RobotType.DULL.getMember());
		expect(board.getBoardId()).andReturn(1L);
		replay(board);

		final Capture<Collection<Personality>> personalityCapture = new Capture<>();

		final GameBoardDao dao = createStrictMock(GameBoardDao.class);
		expect(dao.createBoardImpl(same(settings), capture(personalityCapture), isNull(GameRelationship.class))).andReturn(board);
		dao.saveBoardImpl(board);
		replay(dao);

		final TaskExecutor taskExecutor = createNiceMock(TaskExecutor.class);
		replay(taskExecutor);

		final MockGamePlayManager mock = new MockGamePlayManager(dao);
		mock.setTaskExecutor(taskExecutor);

		final MockPlayer player = new MockPlayer(1);
		try {
			mock.createBoard(settings, player, RobotType.EXPERT);
			fail("EXPERT is not supported");
		} catch (BoardCreationException ex) {
		}

		final GameBoard<?, ?> newBoard = mock.createBoard(settings, player, RobotType.DULL);
		assertSame(board, newBoard);

		assertEquals(Arrays.asList(player, RobotType.DULL.getMember()), personalityCapture.getValue());

		verify(board);
		verify(dao);
	}
*/

	@Test
	public void testOpenBoard() throws BoardLoadingException {
		final AbstractGameBoard<GameSettings, AbstractPlayerHand, GameMove> board = createStrictMock(AbstractGameBoard.class);
		expectListeners(board);
		expect(board.getBoardId()).andReturn(1L);
		replay(board);

		final GameBoardDao dao = createStrictMock(GameBoardDao.class);
		expect(dao.loadBoardImpl(1L)).andReturn(board);
		replay(dao);

		final MockGamePlayManager mock = new MockGamePlayManager(dao);

		final GameBoard<?, ?, ?> newBoard = mock.openBoard(1L);
		assertSame(board, newBoard);

		verify(board);
		verify(dao);

		//Test that board is not opened twice
		reset(board);
		replay(board);

		final GameBoard<?, ?, ?> newBoard2 = mock.openBoard(1L);
		assertSame(board, newBoard2);
		assertSame(newBoard, newBoard2);

		verify(board);
	}

/*
	TODO: move to board search manager
	@Test
	public void testGetActiveBoards() throws BoardLoadingException {
		final Personality player = Personality.person(1);

		final AbstractGameBoard<GameSettings, AbstractPlayerHand> board1 = createStrictMock(AbstractGameBoard.class);
		expectListeners(board1);
		expect(board1.getBoardId()).andReturn(1L);
		replay(board1);

		final AbstractGameBoard<GameSettings, AbstractPlayerHand> board2 = createStrictMock(AbstractGameBoard.class);
		expectListeners(board2);
		expect(board2.getBoardId()).andReturn(2L);
		replay(board2);

		final GameBoardDao dao = createStrictMock(GameBoardDao.class);
		expect(dao.loadPlayerBoards(player, GameState.ACTIVE, null, null, null)).andReturn(Arrays.asList(1L, 2L));
		expect(dao.loadBoard(1L)).andReturn(board1);
		expect(dao.loadBoard(2L)).andReturn(board2);
		replay(dao);

		final MockGamePlayManager mock = new MockGamePlayManager(dao);

		final Collection<AbstractGameBoard<GameSettings, AbstractPlayerHand>> waitingBoards = mock.searchEntities(player, GameState.ACTIVE, null, null, null);
		assertEquals(2, waitingBoards.size());
		assertTrue(waitingBoards.contains(board1));
		assertTrue(waitingBoards.contains(board2));

		verify(board1);
		verify(board2);
		verify(dao);
	}
*/

	@Test
	public void testSaveListeners() throws BoardLoadingException {
		final AbstractPlayerHand h1 = new AbstractPlayerHand(player1, (short) 100);
		final AbstractPlayerHand h2 = new AbstractPlayerHand(player2, (short) 200);

		final AbstractGameBoard<GameSettings, AbstractPlayerHand, GameMove> board = createStrictMock(AbstractGameBoard.class);
		expectListeners(board);
		expect(board.getBoardId()).andReturn(1L);
		expect(board.getPlayers()).andReturn(Arrays.<Personality>asList(player1, player2));
		expect(board.getPlayerHand(player1)).andReturn(h1);
		expect(board.getPlayerHand(player2)).andReturn(h2);
		expect(board.isRated()).andReturn(true);
		expect(board.getPlayers()).andReturn(Arrays.<Personality>asList(player1, player2));
		expect(board.getPlayerHand(player1)).andReturn(h1);
		expect(board.getPlayerHand(player2)).andReturn(h2);
		expect(board.isRated()).andReturn(true);
		replay(board);

		final GameBoardDao dao = createStrictMock(GameBoardDao.class);
		expect(dao.loadBoardImpl(1L)).andReturn(board);
		dao.saveBoardImpl(board);
		expectLastCall().times(2);
		replay(dao);

		final GameMove move = createStrictMock(GameMove.class);
		replay(move);

		final BoardListener boardListener = createStrictMock(BoardListener.class);
		boardListener.gameMoveDone(board, move, null);
		boardListener.gameFinished(board, GameResolution.FINISHED, null);
		boardListener.gameFinished(board, GameResolution.INTERRUPTED, null);
		replay(boardListener);

		final RatingSystem ratingSystem = createStrictMock(RatingSystem.class);
		expect(ratingSystem.calculateRatings(aryEq(new short[]{1000, 2000}), aryEq(new short[]{100, 200}))).andReturn(new short[]{1010, 1990});
		expect(ratingSystem.calculateRatings(aryEq(new short[]{1000, 2000}), aryEq(new short[]{100, 200}))).andReturn(new short[]{990, 2010});
		replay(ratingSystem);

		final StatisticManager statisticManager = createMock(StatisticManager.class);
		expect(statisticManager.getRating(player1)).andReturn((short) 1000).anyTimes();
		expect(statisticManager.getRating(player2)).andReturn((short) 2000).anyTimes();
		replay(statisticManager);

		final MockGamePlayManager mock = new MockGamePlayManager(dao);
		mock.setRatingSystem(ratingSystem);
		mock.setStatisticManager(statisticManager);
		mock.addBoardListener(boardListener);

		mock.openBoard(1L);

		boardListener.gameMoveDone(board, move, null);

		gamePlayListener.gameFinished(board, GameResolution.FINISHED, null);
		assertEquals(1010, h1.getNewRating());
		assertEquals(1990, h2.getNewRating());

		gamePlayListener.gameFinished(board, GameResolution.INTERRUPTED, null);
		assertEquals(990, h1.getNewRating());
		assertEquals(2010, h2.getNewRating());

		verify(board, dao, boardListener, ratingSystem, move, statisticManager);
	}

	private void expectListeners(AbstractGameBoard<?, ?, ?> board) {
		board.setBoardListener(isA(BoardListener.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				gamePlayListener = (BoardListener) getCurrentArguments()[0];
				return null;
			}
		});
	}

	private interface GameBoardDao {
		void saveBoardImpl(AbstractGameBoard<GameSettings, AbstractPlayerHand, GameMove> board);

		AbstractGameBoard<GameSettings, AbstractPlayerHand, GameMove> loadBoardImpl(long gameId);

		AbstractGameBoard<GameSettings, AbstractPlayerHand, GameMove> createBoardImpl(GameSettings settings, Collection<Personality> players, GameRelationship relationship);

		void processGameMove(AbstractGameBoard<GameSettings, AbstractPlayerHand, GameMove> board, Robot player);
	}

	@SuppressWarnings("unchecked")
	private static class MockGamePlayManager extends AbstractGamePlayManager<GameSettings, AbstractGameBoard<GameSettings, AbstractPlayerHand, GameMove>> {
		private final GameBoardDao boardDao;

		protected MockGamePlayManager(GameBoardDao boardDao) {
			super(EnumSet.of(RobotType.DULL), log);
			this.boardDao = boardDao;
		}

		@Override
		protected void saveBoardImpl(AbstractGameBoard<GameSettings, AbstractPlayerHand, GameMove> board) {
			boardDao.saveBoardImpl(board);
		}

		@Override
		protected AbstractGameBoard<GameSettings, AbstractPlayerHand, GameMove> loadBoardImpl(long gameId) throws BoardLoadingException {
			return boardDao.loadBoardImpl(gameId);
		}

		@Override
		protected AbstractGameBoard<GameSettings, AbstractPlayerHand, GameMove> createBoardImpl(GameSettings settings, Collection<Personality> players, GameRelationship relationship) throws BoardCreationException {
			return boardDao.createBoardImpl(settings, players, relationship);
		}

		@Override
		protected Collection<Long> loadActiveRobotGames() {
			return Collections.emptyList();
		}

		@Override
		protected void processRobotMove(AbstractGameBoard<GameSettings, AbstractPlayerHand, GameMove> board, Robot player) {
			boardDao.processGameMove(board, player);
		}
	}
}
