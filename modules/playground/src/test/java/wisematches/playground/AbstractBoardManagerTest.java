package wisematches.playground;

import org.junit.Test;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@SuppressWarnings("unchecked")
public class AbstractBoardManagerTest {
	@Test
	public void commented() {
		throw new UnsupportedOperationException("Commented");
	}
/*
	private static final Log log = LogFactory.getLog("test.wisematches.room.abstract");

	private GamePlayListener gamePlayListener;

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
	public void testCreateBoard() throws BoardCreationException {
		final GameSettings settings = new MockGameSettings("test", 3);

		final Collection<Personality> players = Arrays.<Personality>asList(Personality.person(1), Personality.person(2));

		final AbstractGameBoard<GameSettings, GamePlayerHand> board = createStrictMock(AbstractGameBoard.class);
		expectListeners(board);
		expect(board.getBoardId()).andReturn(1L);
		replay(board);

		final GameBoardDao dao = createStrictMock(GameBoardDao.class);
		expect(dao.createBoard(settings, players)).andReturn(board);
		dao.saveBoard(board);
		replay(dao);

		final MockGamePlayManager mock = new MockGamePlayManager(dao);

		final GameBoard<?, ?> newBoard = mock.createBoard(settings, players);
		assertSame(board, newBoard);

		verify(board);
		verify(dao);
	}

	@Test
	public void testOpenBoard() throws BoardLoadingException {
		final AbstractGameBoard<GameSettings, GamePlayerHand> board = createStrictMock(AbstractGameBoard.class);
		expectListeners(board);
		expect(board.getBoardId()).andReturn(1L);
		replay(board);

		final GameBoardDao dao = createStrictMock(GameBoardDao.class);
		expect(dao.loadBoard(1L)).andReturn(board);
		replay(dao);

		final MockGamePlayManager mock = new MockGamePlayManager(dao);

		final GameBoard<?, ?> newBoard = mock.openBoard(1L);
		assertSame(board, newBoard);

		verify(board);
		verify(dao);

		//Test that board is not opened twice
		reset(board);
		replay(board);

		final GameBoard<?, ?> newBoard2 = mock.openBoard(1L);
		assertSame(board, newBoard2);
		assertSame(newBoard, newBoard2);

		verify(board);
	}

	@Test
	public void testGetActiveBoards() throws BoardLoadingException {
		final Personality player = Personality.person(1);

		final AbstractGameBoard<GameSettings, GamePlayerHand> board1 = createStrictMock(AbstractGameBoard.class);
		expectListeners(board1);
		expect(board1.getBoardId()).andReturn(1L);
		replay(board1);

		final AbstractGameBoard<GameSettings, GamePlayerHand> board2 = createStrictMock(AbstractGameBoard.class);
		expectListeners(board2);
		expect(board2.getBoardId()).andReturn(2L);
		replay(board2);

		final GameBoardDao dao = createStrictMock(GameBoardDao.class);
		expect(dao.loadPlayerBoards(player, GameState.ACTIVE, null, null, null)).andReturn(Arrays.asList(1L, 2L));
		expect(dao.loadBoard(1L)).andReturn(board1);
		expect(dao.loadBoard(2L)).andReturn(board2);
		replay(dao);

		final MockGamePlayManager mock = new MockGamePlayManager(dao);

		final Collection<AbstractGameBoard<GameSettings, GamePlayerHand>> waitingBoards = mock.searchEntities(player, GameState.ACTIVE, null, null, null);
		assertEquals(2, waitingBoards.size());
		assertTrue(waitingBoards.contains(board1));
		assertTrue(waitingBoards.contains(board2));

		verify(board1);
		verify(board2);
		verify(dao);
	}

	@Test
	public void testSaveListeners() throws BoardLoadingException {
		final GamePlayerHand h1 = new GamePlayerHand(1, (short) 100);
		final GamePlayerHand h2 = new GamePlayerHand(2, (short) 200);

		final GameRatingChange c1 = new GameRatingChange(1, (short) 100, (short) 123, (short) 120);
		final GameRatingChange c2 = new GameRatingChange(2, (short) 200, (short) 127, (short) 145);

		final RatingManager ratingManager = createStrictMock(RatingManager.class);
		expect(ratingManager.calculateRatings(Arrays.asList(h1, h2))).andReturn(Arrays.asList(c1, c2)).times(2);
		replay(ratingManager);

		final AbstractGameBoard<GameSettings, GamePlayerHand> board = createStrictMock(AbstractGameBoard.class);
		expectListeners(board);
		expect(board.getBoardId()).andReturn(1L);
		expect(board.getPlayers()).andReturn(Arrays.asList(h1, h2));
		expect(board.isRated()).andReturn(true);
		expect(board.getPlayers()).andReturn(Arrays.asList(h1, h2));
		expect(board.isRated()).andReturn(true);
		replay(board);

		final GameBoardDao dao = createStrictMock(GameBoardDao.class);
		expect(dao.loadBoard(1L)).andReturn(board);
		dao.saveBoard(board);
		expectLastCall().times(2);
		replay(dao);

		final GameMove move = new GameMove(new PassTurn(13L), 0, 0, new Date());

		final GamePlayListener boardListener = createStrictMock(GamePlayListener.class);
		boardListener.gameMoveDone(board, move, null);
		boardListener.gameFinished(board, GameResolution.FINISHED, null);
		boardListener.gameFinished(board, GameResolution.INTERRUPTED, null);
		replay(boardListener);

		final MockGamePlayManager mock = new MockGamePlayManager(dao);
		mock.addGamePlayListener(boardListener);
		mock.setRatingManager(ratingManager);

		mock.openBoard(1L);

		boardListener.gameMoveDone(board, move, null);

		gamePlayListener.gameFinished(board, GameResolution.FINISHED, null);
		gamePlayListener.gameFinished(board, GameResolution.INTERRUPTED, null);

		verify(board, dao, boardListener, ratingManager);
	}

	private void expectListeners(AbstractGameBoard<?, ?> board) {
		board.setGamePlayListener(isA(GamePlayListener.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				gamePlayListener = (GamePlayListener) getCurrentArguments()[0];
				return null;
			}
		});
	}

	private interface GameBoardDao {
		AbstractGameBoard loadBoard(long gameId) throws BoardLoadingException;

		AbstractGameBoard createBoard(GameSettings settings, Collection<? extends Personality> players) throws BoardCreationException;

		void saveBoard(AbstractGameBoard board);

		Collection<Long> loadPlayerBoards(Personality player, GameState state, SearchFilter filter, Orders orders, Range range);

		int loadPlayerBoardsCount(Personality player, GameState state, SearchFilter filter);
	}

	@SuppressWarnings("unchecked")
	private static class MockGamePlayManager extends AbstractGamePlayManager<GameSettings, AbstractGameBoard<GameSettings, GamePlayerHand>> {
		private final GameBoardDao gameBoardDao;

		private MockGamePlayManager(GameBoardDao gameBoardDao) {
			super(log);
			this.gameBoardDao = gameBoardDao;
		}

		@Override
		protected AbstractGameBoard loadBoardImpl(long gameId) throws BoardLoadingException {
			return gameBoardDao.loadBoard(gameId);
		}

		@Override
		protected int loadPlayerBoardsCount(Personality player, GameState state, SearchFilter filter) {
			return gameBoardDao.loadPlayerBoardsCount(player, state, filter);
		}

		@Override
		protected AbstractGameBoard<GameSettings, GamePlayerHand> createBoardImpl(GameSettings settings, GameRelationship relationship, Collection<? extends Personality> players) throws BoardCreationException {
			return gameBoardDao.createBoard(settings, players);
		}

		@Override
		protected void saveBoardImpl(AbstractGameBoard board) {
			gameBoardDao.saveBoard(board);
		}

		@Override
		protected Collection<Long> loadPlayerBoards(Personality player, GameState state, SearchFilter filter, Orders orders, Range range) {
			return gameBoardDao.loadPlayerBoards(player, state, filter, orders, range);
		}
	}
*/
}
