package wisematches.server.gameplaying.board;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.IAnswer;
import org.junit.Test;
import wisematches.server.gameplaying.board.*;
import wisematches.server.personality.Personality;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@SuppressWarnings("unchecked")
public class AbstractBoardManagerTest {
	private static final Log log = LogFactory.getLog("test.wisematches.room.abstract");

	private GameBoardListener gameBoardListener;

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

		AbstractBoardManager.BoardsMap<GameBoard> board = new AbstractBoardManager.BoardsMap<GameBoard>(log);
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
		final GameSettings gameSettings = new MockGameSettings("test", 3);

		final Collection<Personality> players = Arrays.<Personality>asList(Personality.person(1), Personality.person(2));

		final GameBoard<GameSettings, GamePlayerHand> board = createStrictMock(GameBoard.class);
		expectListeners(board);
		expect(board.getBoardId()).andReturn(1L);
		replay(board);

		final GameBoardDao dao = createStrictMock(GameBoardDao.class);
		expect(dao.createBoard(gameSettings, players)).andReturn(board);
		dao.saveBoard(board);
		replay(dao);

		final MockBoardManager mock = new MockBoardManager(dao);

		final GameBoard<?, ?> newBoard = mock.createBoard(gameSettings, players);
		assertSame(board, newBoard);

		verify(board);
		verify(dao);
	}

	@Test
	public void testOpenBoard() throws BoardLoadingException {
		final GameBoard<GameSettings, GamePlayerHand> board = createStrictMock(GameBoard.class);
		expectListeners(board);
		expect(board.getBoardId()).andReturn(1L);
		replay(board);

		final GameBoardDao dao = createStrictMock(GameBoardDao.class);
		expect(dao.loadBoard(1L)).andReturn(board);
		replay(dao);

		final MockBoardManager mock = new MockBoardManager(dao);

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

		final GameBoard<GameSettings, GamePlayerHand> board1 = createStrictMock(GameBoard.class);
		expectListeners(board1);
		expect(board1.getBoardId()).andReturn(1L);
		replay(board1);

		final GameBoard<GameSettings, GamePlayerHand> board2 = createStrictMock(GameBoard.class);
		expectListeners(board2);
		expect(board2.getBoardId()).andReturn(2L);
		replay(board2);

		final GameBoardDao dao = createStrictMock(GameBoardDao.class);
		expect(dao.loadActivePlayerBoards(player)).andReturn(Arrays.asList(1L, 2L));
		expect(dao.loadBoard(1L)).andReturn(board1);
		expect(dao.loadBoard(2L)).andReturn(board2);
		replay(dao);

		final MockBoardManager mock = new MockBoardManager(dao);

		final Collection<GameBoard<GameSettings, GamePlayerHand>> waitingBoards = mock.getActiveBoards(player);
		assertEquals(2, waitingBoards.size());
		assertTrue(waitingBoards.contains(board1));
		assertTrue(waitingBoards.contains(board2));

		verify(board1);
		verify(board2);
		verify(dao);
	}

	@Test
	public void testSaveListeners() throws BoardLoadingException {
		final GameBoard<GameSettings, GamePlayerHand> board = createStrictMock(GameBoard.class);
		expectListeners(board);
		expect(board.getBoardId()).andReturn(1L);
		replay(board);

		final GameBoardDao dao = createStrictMock(GameBoardDao.class);
		expect(dao.loadBoard(1L)).andReturn(board);
		dao.saveBoard(board);
		expectLastCall().times(2);
		replay(dao);

		final GameMove move = new GameMove(new PassTurnMove(13L), 0, 0, new Date());

		final BoardStateListener boardListener = createStrictMock(BoardStateListener.class);
		boardListener.gameMoveDone(board, move);
		boardListener.gameFinished(board, GameResolution.FINISHED, null);
		boardListener.gameFinished(board, GameResolution.TIMEOUT, null);
		replay(boardListener);

		final MockBoardManager mock = new MockBoardManager(dao);
		mock.addBoardStateListener(boardListener);

		mock.openBoard(1L);

		boardListener.gameMoveDone(board, move);

		gameBoardListener.gameFinished(board, GameResolution.FINISHED, null);
		gameBoardListener.gameFinished(board, GameResolution.TIMEOUT, null);

		verify(board, dao, boardListener);
	}

	private void expectListeners(GameBoard<?, ?> board) {
		board.addGameBoardListener(isA(GameBoardListener.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				gameBoardListener = (GameBoardListener) getCurrentArguments()[0];
				return null;
			}
		});
	}

	private interface GameBoardDao {
		GameBoard loadBoard(long gameId) throws BoardLoadingException;

		GameBoard createBoard(GameSettings gameSettings, Collection<? extends Personality> players) throws BoardCreationException;

		void saveBoard(GameBoard board);

		Collection<Long> loadActivePlayerBoards(Personality player);
	}

	@SuppressWarnings("unchecked")
	private static class MockBoardManager extends AbstractBoardManager<GameSettings, GameBoard<GameSettings, GamePlayerHand>> {
		private final GameBoardDao gameBoardDao;

		private MockBoardManager(GameBoardDao gameBoardDao) {
			super(log);
			this.gameBoardDao = gameBoardDao;
		}

		@Override
		protected GameBoard loadBoardImpl(long gameId) throws BoardLoadingException {
			return gameBoardDao.loadBoard(gameId);
		}

		@Override
		protected GameBoard<GameSettings, GamePlayerHand> createBoardImpl(GameSettings gameSettings, Collection<? extends Personality> players) throws BoardCreationException {
			return gameBoardDao.createBoard(gameSettings, players);
		}

		@Override
		protected void saveBoardImpl(GameBoard board) {
			gameBoardDao.saveBoard(board);
		}

		@Override
		protected Collection<Long> loadActivePlayerBoards(Personality player) {
			return gameBoardDao.loadActivePlayerBoards(player);
		}
	}
}
