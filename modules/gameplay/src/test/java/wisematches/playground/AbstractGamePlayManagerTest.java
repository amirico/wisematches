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
import wisematches.core.cache.ReferenceMapCacheManager;
import wisematches.core.cache.ReferenceType;
import wisematches.core.personality.DefaultMember;
import wisematches.playground.tracking.StatisticsManager;

import java.util.*;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@SuppressWarnings("unchecked")
public class AbstractGamePlayManagerTest {
	private static final Logger log = LoggerFactory.getLogger(AbstractGamePlayManagerTest.class);

	private BoardListener gamePlayListener;

	private static final Player player1 = new DefaultMember(901, null, null, null, null, null);
	private static final Player player2 = new DefaultMember(902, null, null, null, null, null);

	public AbstractGamePlayManagerTest() {
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
		mock.setCacheManager(new ReferenceMapCacheManager(ReferenceType.SOFT, "board"));

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
		expect(board.getSettings()).andReturn(new MockGameSettings("asd", 10));
		expect(board.getPlayers()).andReturn(Arrays.<Personality>asList(player1, player2));
		expect(board.getPlayerHand(player1)).andReturn(h1);
		expect(board.getPlayerHand(player2)).andReturn(h2);
		expect(board.isRated()).andReturn(true);
		expect(board.getSettings()).andReturn(new MockGameSettings("asd", 10, true, true));
		replay(board);

		final GameBoardDao dao = createStrictMock(GameBoardDao.class);
		expect(dao.loadBoardImpl(1L)).andReturn(board);
		dao.saveBoardImpl(board);
		dao.deleteBoardImpl(board);
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

		final StatisticsManager statisticManager = createMock(StatisticsManager.class);
		expect(statisticManager.getRating(player1)).andReturn((short) 1000).anyTimes();
		expect(statisticManager.getRating(player2)).andReturn((short) 2000).anyTimes();
		replay(statisticManager);

		final MockGamePlayManager mock = new MockGamePlayManager(dao);
		mock.setRatingSystem(ratingSystem);
		mock.setStatisticManager(statisticManager);
		mock.addBoardListener(boardListener);

		mock.openBoard(1L);

		boardListener.gameMoveDone(board, move, null);

		// not scratch
		gamePlayListener.gameFinished(board, GameResolution.FINISHED, null);
		assertEquals(1010, h1.getNewRating());
		assertEquals(1990, h2.getNewRating());

		// scratch
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

		void deleteBoardImpl(AbstractGameBoard<GameSettings, AbstractPlayerHand, GameMove> board);

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
		protected void deleteBoardImpl(AbstractGameBoard<GameSettings, AbstractPlayerHand, GameMove> board) {
			boardDao.deleteBoardImpl(board);
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
