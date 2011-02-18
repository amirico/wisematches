package wisematches.server.gameplaying.room.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easymock.IAnswer;
import org.easymock.internal.matchers.Equals;
import org.junit.Test;
import wisematches.server.gameplaying.board.*;
import wisematches.server.gameplaying.room.*;
import wisematches.server.player.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class AbstractRoomManagerTest {
	private static final Log log = LogFactory.getLog("test.wisematches.room.abstract");
	private static final Room ROOM = Room.valueOf("mock");

	private GamePlayersListener gamePlayersListener;
	private GameMoveListener gameMoveListener;
	private GameStateListener gameStateListener;

	private static final GamePlayerHand GAME_PLAYER_HAND = new GamePlayerHand(1L);

	@Test
	public void testBoardsMap() throws InterruptedException {
		GameBoard board1 = createMock("Board1", GameBoard.class);
		expect(board1.getBoardId()).andReturn(1L).anyTimes();
		replay(board1);

		GameBoard board2 = createMock("Board1", GameBoard.class);
		expect(board2.getBoardId()).andReturn(2L).anyTimes();
		replay(board2);

		AbstractRoomManager roomManager = new MockRoomManager(createNiceMock(GameBoardDao.class));

		AbstractRoomManager.BoardsMap<GameBoard> board = new AbstractRoomManager.BoardsMap<GameBoard>(roomManager);
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
		final GameSettings gameSettings = new MockGameSettings("test", new Date(), 3);

		final Player player = createMock(Player.class);

		final GameBoard<GameSettings, GamePlayerHand> board = createStrictMock(GameBoard.class);
		expect(board.addPlayer(player)).andReturn(GAME_PLAYER_HAND);
		expectListeners(board);
		expect(board.getBoardId()).andReturn(1L);
		expect(board.getGameSettings()).andReturn(gameSettings);
		expect(board.getPlayersHands()).andReturn(Arrays.asList(GAME_PLAYER_HAND));
		replay(board);

		final GameBoardDao dao = createStrictMock(GameBoardDao.class);
		expect(dao.createBoard(gameSettings)).andReturn(board);
		dao.saveBoard(board);
		replay(dao);

		final RoomSeatesListener listener = createStrictMock(RoomSeatesListener.class);
		listener.playerSitDown(EventEquals.eq(ROOM, board, player));
		replay(listener);

		final MockRoomManager mock = new MockRoomManager(dao);
		mock.addRoomSeatesListener(listener);

		final GameBoard<?, ?> newBoard = mock.createBoard(player, gameSettings);
		assertSame(board, newBoard);
//        gamePlayersListener.playerAdded(board, player);

		verify(board);
		verify(dao);
		verify(listener);
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

		final RoomSeatesListener listener = createStrictMock(RoomSeatesListener.class);
		replay(listener);

		final MockRoomManager mock = new MockRoomManager(dao);
		mock.addRoomSeatesListener(listener);

		final GameBoard<?, ?> newBoard = mock.openBoard(1L);
		assertSame(board, newBoard);

		verify(board);
		verify(dao);
		verify(listener);

		//Test that board is not opened twice
		reset(board);
		replay(board);

		final GameBoard<?, ?> newBoard2 = mock.openBoard(1L);
		assertSame(board, newBoard2);
		assertSame(newBoard, newBoard2);

		verify(board);
	}

	@Test
	public void testLoadingWaitingBoards() throws BoardLoadingException {
		final GameBoard<GameSettings, GamePlayerHand> board1 = createStrictMock(GameBoard.class);
		expectListeners(board1);
		expect(board1.getBoardId()).andReturn(1L);
		replay(board1);

		final GameBoard<GameSettings, GamePlayerHand> board2 = createStrictMock(GameBoard.class);
		expectListeners(board2);
		expect(board2.getBoardId()).andReturn(2L);
		replay(board2);

		final GameBoardDao dao = createStrictMock(GameBoardDao.class);
		expect(dao.loadWaitingBoards()).andReturn(Arrays.asList(1L, 2L));
		expect(dao.loadBoard(1L)).andReturn(board1);
		expect(dao.loadBoard(2L)).andReturn(board2);
		replay(dao);

		final MockRoomManager mock = new MockRoomManager(dao);

		final Collection<GameBoard<GameSettings, GamePlayerHand>> waitingBoards = mock.getWaitingBoards();
		assertEquals("First loading is call DAO storage", 2, waitingBoards.size());
		assertTrue(waitingBoards.contains(board1));
		assertTrue(waitingBoards.contains(board2));

		final Collection<GameBoard<GameSettings, GamePlayerHand>> waitingBoards2 = mock.getWaitingBoards();
		assertEquals("Second call doesn't call anything", 2, waitingBoards2.size());

		verify(board1);
		verify(board2);
		verify(dao);
	}

	@Test
	public void testGetActiveBoards() throws BoardLoadingException {
		final Player player = createMock(Player.class);

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

		final MockRoomManager mock = new MockRoomManager(dao);

		final Collection<GameBoard<GameSettings, GamePlayerHand>> waitingBoards = mock.getActiveBoards(player);
		assertEquals(2, waitingBoards.size());
		assertTrue(waitingBoards.contains(board1));
		assertTrue(waitingBoards.contains(board2));

		verify(board1);
		verify(board2);
		verify(dao);
	}

	@Test
	public void boardSeatsChanging() throws BoardLoadingException, BoardCreationException {
		final GameSettings gameSettings = new MockGameSettings("test", new Date(), 3);

		final Player player1 = createMock(Player.class);
		final Player player2 = createMock(Player.class);
		final Player player3 = createMock(Player.class);

		final GameBoard<GameSettings, GamePlayerHand> waitingBoard = createStrictMock(GameBoard.class);
		expectListeners(waitingBoard);
		expect(waitingBoard.getBoardId()).andReturn(1L);
		replay(waitingBoard);

		final GameBoard<GameSettings, GamePlayerHand> createdBoard = createStrictMock(GameBoard.class);
		expect(createdBoard.addPlayer(player1)).andReturn(GAME_PLAYER_HAND);
		expectListeners(createdBoard);
		expect(createdBoard.getBoardId()).andReturn(2L);
		expect(createdBoard.getGameSettings()).andReturn(gameSettings);
		expect(createdBoard.getPlayersHands()).andReturn(Arrays.asList(GAME_PLAYER_HAND));
		expect(createdBoard.getGameSettings()).andReturn(gameSettings);
		expect(createdBoard.getPlayersHands()).andReturn(Arrays.asList(GAME_PLAYER_HAND, GAME_PLAYER_HAND));
		expect(createdBoard.getGameSettings()).andReturn(gameSettings);
		expect(createdBoard.getPlayersHands()).andReturn(Arrays.asList(GAME_PLAYER_HAND, GAME_PLAYER_HAND, GAME_PLAYER_HAND));
		expect(createdBoard.getPlayersHands()).andReturn(Arrays.asList(GAME_PLAYER_HAND, GAME_PLAYER_HAND));
		expect(createdBoard.getGameSettings()).andReturn(gameSettings);
		expect(createdBoard.getPlayersHands()).andReturn(Arrays.asList(GAME_PLAYER_HAND, GAME_PLAYER_HAND, GAME_PLAYER_HAND));
		replay(createdBoard);

		final GameBoardDao dao = createStrictMock(GameBoardDao.class);
		expect(dao.loadWaitingBoards()).andReturn(Arrays.asList(1L));
		expect(dao.loadBoard(1L)).andReturn(waitingBoard);
		expect(dao.createBoard(gameSettings)).andReturn(createdBoard);
		dao.saveBoard(createdBoard); //after creating and adding first player
		dao.saveBoard(createdBoard); //after adding second player
		dao.saveBoard(createdBoard); //after adding third player
		dao.saveBoard(createdBoard); //after removing second player
		dao.saveBoard(createdBoard); //after adding second player
		replay(dao);

		final RoomSeatesListener roomListener = createStrictMock(RoomSeatesListener.class);
		roomListener.playerSitDown(EventEquals.eq(ROOM, createdBoard, player1));
		roomListener.playerSitDown(EventEquals.eq(ROOM, createdBoard, player2));
		roomListener.playerSitDown(EventEquals.eq(ROOM, createdBoard, player3));
		roomListener.playerStandUp(EventEquals.eq(ROOM, createdBoard, player2));
		roomListener.playerSitDown(EventEquals.eq(ROOM, createdBoard, player2));
		replay(roomListener);

		final MockRoomManager mock = new MockRoomManager(dao);
		mock.addRoomSeatesListener(roomListener);

		assertEquals(1, mock.getWaitingBoards().size());

		final GameBoard<GameSettings, GamePlayerHand> board = mock.createBoard(player1, gameSettings);
		assertEquals(2, mock.getWaitingBoards().size());

		gamePlayersListener.playerAdded(board, player2);
		assertEquals(2, mock.getWaitingBoards().size());

		gamePlayersListener.playerAdded(board, player3);
		assertEquals(1, mock.getWaitingBoards().size());
		assertTrue(mock.getWaitingBoards().contains(waitingBoard));
		assertFalse(mock.getWaitingBoards().contains(createdBoard));

		gamePlayersListener.playerRemoved(board, player2);
		assertEquals(2, mock.getWaitingBoards().size());
		assertTrue(mock.getWaitingBoards().contains(waitingBoard));
		assertTrue(mock.getWaitingBoards().contains(createdBoard));

		gamePlayersListener.playerAdded(board, player2);
		assertEquals(1, mock.getWaitingBoards().size());
		assertTrue(mock.getWaitingBoards().contains(waitingBoard));
		assertFalse(mock.getWaitingBoards().contains(createdBoard));

		verify(waitingBoard);
		verify(createdBoard);
		verify(dao);
		verify(roomListener);
	}

	@Test
	public void testSaveListeners() throws BoardLoadingException {
		final GameSettings gameSettings = new MockGameSettings("test", new Date(), 2);

		final Player player = createMock(Player.class);

		final GameBoard<GameSettings, GamePlayerHand> board = createStrictMock(GameBoard.class);
		expectListeners(board);
		expect(board.getBoardId()).andReturn(1L);
		expect(board.getGameSettings()).andReturn(gameSettings);
		expect(board.getPlayersHands()).andReturn(Arrays.asList(GAME_PLAYER_HAND, GAME_PLAYER_HAND));
		expect(board.getPlayersHands()).andReturn(Arrays.asList(GAME_PLAYER_HAND));
		replay(board);

		final GameMoveEvent moveEvent = new GameMoveEvent(board, new GamePlayerHand(13L), new GameMove(new PassTurnMove(13L), 0, 0, new Date()), new GamePlayerHand(14L));

		final GameBoardDao dao = createStrictMock(GameBoardDao.class);
		expect(dao.loadBoard(1L)).andReturn(board);
		dao.saveBoard(board);
		expectLastCall().times(7);
		replay(dao);

		final RoomSeatesListener listener = createMock(RoomSeatesListener.class);
		listener.playerSitDown(EventEquals.eq(ROOM, board, player));
		listener.playerStandUp(EventEquals.eq(ROOM, board, player));
		replay(listener);

		final GameMoveListener moveListener = createStrictMock(GameMoveListener.class);
		moveListener.playerMoved(moveEvent);
		replay(moveListener);

		final GamePlayersListener playersListener = createStrictMock(GamePlayersListener.class);
		playersListener.playerAdded(board, player);
		playersListener.playerRemoved(board, player);
		replay(playersListener);

		final GameStateListener stateListener = createStrictMock(GameStateListener.class);
		stateListener.gameDraw(board);
		stateListener.gameFinished(board, null);
		stateListener.gameInterrupted(board, null, false);
		stateListener.gameStarted(board, null);
		replay(stateListener);

		final MockRoomManager mock = new MockRoomManager(dao);
		mock.addRoomSeatesListener(listener);
		mock.addGameMoveListener(moveListener);
		mock.addGamePlayersListener(playersListener);
		mock.addGameStateListener(stateListener);

		mock.openBoard(1L);
		gamePlayersListener.playerAdded(board, player);
		gamePlayersListener.playerRemoved(board, player);

		gameMoveListener.playerMoved(moveEvent);

		gameStateListener.gameDraw(board);
		gameStateListener.gameFinished(board, null);
		gameStateListener.gameInterrupted(board, null, false);
		gameStateListener.gameStarted(board, null);

		verify(board, dao, listener, moveListener, playersListener, stateListener);
	}

	private void expectListeners(GameBoard<?, ?> board) {
		board.addGameMoveListener(isA(GameMoveListener.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				gameMoveListener = (GameMoveListener) getCurrentArguments()[0];
				return null;
			}
		});

		board.addGamePlayersListener(isA(GamePlayersListener.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				gamePlayersListener = (GamePlayersListener) getCurrentArguments()[0];
				return null;
			}
		});


		board.addGameStateListener(isA(GameStateListener.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			public Object answer() throws Throwable {
				gameStateListener = (GameStateListener) getCurrentArguments()[0];
				return null;
			}
		});
	}

	private interface GameBoardDao {
		GameBoard loadBoard(long gameId) throws BoardLoadingException;

		GameBoard createBoard(GameSettings gameSettings) throws BoardCreationException;

		void saveBoard(GameBoard board);

		Collection<Long> loadActivePlayerBoards(Player player);

		Collection<Long> loadWaitingBoards();
	}

	private static class MockGameSettings extends GameSettings {
		public MockGameSettings(String title, Date startDate, int maxPlayers) {
			super(title, startDate, maxPlayers);
		}
	}

	private static class MockRoomManager extends AbstractRoomManager<GameBoard<GameSettings, GamePlayerHand>, GameSettings> {
		private final GameBoardDao gameBoardDao;

		private MockRoomManager(GameBoardDao gameBoardDao) {
			super(ROOM, log);
			this.gameBoardDao = gameBoardDao;
		}

		@Override
		protected GameBoard loadBoard(long gameId) throws BoardLoadingException {
			return gameBoardDao.loadBoard(gameId);
		}

		@Override
		protected GameBoard createBoard(GameSettings gameSettings) throws BoardCreationException {
			return gameBoardDao.createBoard(gameSettings);
		}

		@Override
		protected void saveBoard(GameBoard board) {
			gameBoardDao.saveBoard(board);
		}

		@Override
		protected Collection<Long> loadActivePlayerBoards(Player player) {
			return gameBoardDao.loadActivePlayerBoards(player);
		}

		@Override
		protected Collection<Long> loadWaitingBoards() {
			return gameBoardDao.loadWaitingBoards();
		}

		public SearchesEngine<GameBoard<GameSettings, GamePlayerHand>> getSearchesEngine() {
			return null;
		}
	}

	private static class EventEquals extends Equals {
		private EventEquals(RoomSeatesEvent event) {
			super(event);
		}

		@Override
		public boolean matches(Object actual) {
			final RoomSeatesEvent expected = (RoomSeatesEvent) getExpected();
			final RoomSeatesEvent original = (RoomSeatesEvent) actual;

			if (expected == null) {
				return actual == null;
			}
			return expected.getRoom().equals(original.getRoom()) && expected.getGameBoard() == original.getGameBoard()
					&& expected.getPlayer() == original.getPlayer();
		}

		public static RoomSeatesEvent eq(Room room, GameBoard gameBoard, Player player) {
			reportMatcher(new EventEquals(new RoomSeatesEvent(room, gameBoard, player)));
			return null;
		}
	}
}
