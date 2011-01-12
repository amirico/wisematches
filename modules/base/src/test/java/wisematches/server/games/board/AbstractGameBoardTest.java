package wisematches.server.games.board;

import org.easymock.LogicalOperator;
import org.junit.Before;
import org.junit.Test;
import wisematches.kernel.player.Player;
import wisematches.server.core.MockPlayer;

import java.util.*;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@SuppressWarnings("unchecked")
public class AbstractGameBoardTest {
	private GameSettings gameSettings;

	private GamePlayerHand h1;
	private GamePlayerHand h2;
	private GamePlayerHand h3;

	private MockGameBoard board;

	private Comparator<GameMoveEvent> GMEC = new Comparator<GameMoveEvent>() {
		public int compare(GameMoveEvent o1, GameMoveEvent o2) {
			final GameMove move1 = o1.getGameMove();
			final GameMove move2 = o2.getGameMove();
			if (o1.getPlayer() == o1.getPlayer() && o1.getGameBoard() == o2.getGameBoard() &&
					move1.getPlayerMove() == move2.getPlayerMove() && o1.getNextPlayer() == o2.getNextPlayer()) {
				return 0;
			}
			return -1;
		}
	};

	@Before
	public void setUp() throws Exception {
		class MockGameSettingsBuilder extends GameSettings.Builder {
			@Override
			public GameSettings build() {
				return new GameSettings("Mock", new Date(), 3, 1, 1300, 1000) {
				};
			}
		}
		;

		gameSettings = new MockGameSettingsBuilder().build();

		board = new MockGameBoard(gameSettings);
		h1 = board.addPlayer(new MockPlayer(1, 1000));
		h2 = board.addPlayer(new MockPlayer(2, 1000));
		h3 = board.addPlayer(new MockPlayer(3, 1000));
	}

	@Test
	public void test_incoorectCreateGameBoard() {
		//settings is null
		try {
			new MockGameBoard(null);
			fail("Exception must be here");
		} catch (NullPointerException ex) {
			;
		}
	}

	@Test
	public void test_defaultValues() {
		assertNotNull(board.getPlayerTrun());
		assertSame(gameSettings, board.getGameSettings());
		assertSame(GameState.IN_PROGRESS, board.getGameState());
		assertEquals(0, board.getGameMoves().size());
		assertSame(h1, board.getPlayerHand(1));
		assertSame(h2, board.getPlayerHand(2));
		assertSame(h3, board.getPlayerHand(3));
		assertNull(board.getPlayerHand(4));
	}

	@Test
	public void test_addPlayers() throws TooManyPlayersException {
		board = new MockGameBoard(gameSettings);
		assertEquals(GameState.WAITING, board.getGameState());

		assertEquals(0, board.getPlayersHands().size());
		assertNull(board.getPlayerTrun());

		h1 = board.addPlayer(new MockPlayer(1, 1000));
		assertNotNull(h1);
		assertEquals(1, h1.getPlayerId());

		try {
			board.addPlayer(new MockPlayer(1, 1000));
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
			;
		}

		board.addPlayer(new MockPlayer(2, 1000));
		assertEquals(2, board.getPlayersHands().size());
		assertEquals(GameState.WAITING, board.getGameState());
		assertTrue(board.getLastMoveTime() == 0);

		try {
			board.removePlayer(new MockPlayer(3, 1000));
			fail("Exception must be here: removing unknown player");
		} catch (IllegalArgumentException ex) {
			;
		}

		board.removePlayer(new MockPlayer(2, 1000));
		assertEquals(1, board.getPlayersHands().size());

		board.addPlayer(new MockPlayer(2, 1000));
		board.addPlayer(new MockPlayer(3, 1000));
		assertEquals(GameState.IN_PROGRESS, board.getGameState());
		assertTrue(board.getLastMoveTime() != 0);

		try {
			board.addPlayer(new MockPlayer(4, 1000));
			fail("Exception must be here: game already started");
		} catch (TooManyPlayersException ex) {
			;
		}
	}

	@Test
	public void test_addPlayers_checkRatings() throws TooManyPlayersException {
		board = new MockGameBoard(gameSettings);

		try {
			board.addPlayer(new MockPlayer(1, 100));
			fail("Exception must be here: IllegalArgumentException. To low rating.");
		} catch (IllegalArgumentException ex) {
			;
		}
		try {
			board.addPlayer(new MockPlayer(1, 1500));
			fail("Exception must be here: IllegalArgumentException. To high rating.");
		} catch (IllegalArgumentException ex) {
			;
		}

		assertNotNull(board.addPlayer(new MockPlayer(1, 1200)));
	}

	@Test
	public void test_playerListeners() throws TooManyPlayersException {
		Player p1 = new MockPlayer(1, 1000);
		Player p2 = new MockPlayer(2, 1000);
		Player p3 = new MockPlayer(3, 1000);

		board = new MockGameBoard(gameSettings);

		GamePlayersListener l = createStrictMock(GamePlayersListener.class);
		l.playerAdded(board, p1);
		l.playerAdded(board, p2);
		l.playerRemoved(board, p2);
		l.playerAdded(board, p2);
		l.playerAdded(board, p3);
		replay(l);

		GameStateListener sl = createStrictMock(GameStateListener.class);
		sl.gameStarted(same(board), isA(GamePlayerHand.class));
		replay(sl);

		board.addGamePlayersListener(l);
		board.addGameStateListener(sl);

		board.addPlayer(p1);
		board.addPlayer(p2);
		board.removePlayer(p2);
		board.addPlayer(p2);
		board.addPlayer(p3);

		verify(l);
		verify(sl);
	}

	@Test
	public void test_movesListeners() {
		final GameMove gm = new GameMove(createMock(PlayerMove.class), 10, 1, 1);

		final GameMoveListener l = createStrictMock(GameMoveListener.class);
		l.playerMoved(cmp(new GameMoveEvent(board, h1, gm, h3), GMEC, LogicalOperator.EQUAL));
		replay(l);

		board.addGameMoveListener(l);
		board.addGameMoveListener(l);

		board.firePlayerMoved(h1, gm, h3);

		//no any calles after removing must be
		board.removeGameMoveListener(l);
		board.firePlayerMoved(h1, gm, h3);

		verify(l);
	}

	@Test
	public void test_stateListeners() {
		GameStateListener l = createStrictMock(GameStateListener.class);
		l.gameDraw(board);
		l.gameFinished(board, h1);
		l.gameInterrupted(board, h2, false);
		replay(l);

		board.addGameStateListener(l);
		board.addGameStateListener(l);

		board.fireGameDraw();
		board.fireGameFinished(h1);
		board.fireGameInterrupted(h2, false);

		//no any calles after removing must be
		board.removeGameStateListener(l);
		board.fireGameDraw();

		verify(l);
	}

	@Test
	public void test_selectFirstPlayer() {
		Collection c = Arrays.asList(h1, h2, h3);
		assertTrue("First player selected", c.contains(board.getPlayerTrun()));
	}

	@Test
	public void test_playerHand() {
		assertSame(h1, board.getPlayerHand(1));
		assertSame(h2, board.getPlayerHand(2));
		assertSame(h3, board.getPlayerHand(3));
		assertNull(board.getPlayerHand(4));
	}

	@Test
	public void test_illegalMoves() throws GameMoveException {
		//unknown player
		try {
			board.makeMove(new MakeTurnMove(13));
			fail("Exception must be here");
		} catch (UnsuitablePlayerException e) {
			;
		}

		//unsuitable player
		try {
			PlayersIterator p = new PlayersIterator(Arrays.asList(h1, h2, h3), board.getPlayerTrun());
			board.makeMove(new MakeTurnMove(p.next().getPlayerId()));
			fail("Exception must be here");
		} catch (UnsuitablePlayerException e) {
			;
		}

		//illegal move
		board.setAllowNextMove(false);
		try {
			board.makeMove(new MakeTurnMove(board.getPlayerTrun().getPlayerId()));
		} catch (IncorrectMoveException ex) {
			;
		}
		board.setAllowNextMove(true);

		//game was finished
		board.setGameFinished(true);
		board.setFinishScore(new int[]{0, 0, 0});
		board.makeMove(new MakeTurnMove(board.getPlayerTrun().getPlayerId()));
		try {
			board.makeMove(new PassTurnMove(13));
			fail("Exception must be here: GameFinishedException");
		} catch (GameFinishedException ex) {
			;
		}
		board.setGameFinished(false);

		//game was passed
		board.setGamePassed(true);
		try {
			board.makeMove(new MakeTurnMove(board.getPlayerTrun().getPlayerId()));
			fail("Exception must be here: GameFinishedException");
		} catch (GameFinishedException ex) {
			;
		}
		try {
			board.makeMove(new PassTurnMove(board.getPlayerTrun().getPlayerId()));
			fail("Exception must be here: GameFinishedException");
		} catch (GameFinishedException ex) {
			;
		}
		board.setGamePassed(false);
	}

	@Test
	public void test_gameMoves() throws GameMoveException {
		PlayersIterator p = new PlayersIterator(Arrays.asList(h1, h2, h3), board.getPlayerTrun());
		PlayersIterator p2 = new PlayersIterator(Arrays.asList(h1, h2, h3), board.getPlayerTrun());


		final GameMoveListener l = createStrictMock(GameMoveListener.class);
		//move maden
		final PlayerMove m1 = new MakeTurnMove(board.getPlayerTrun().getPlayerId());
		final GameMove gm1 = new GameMove(m1, 10, 1, System.currentTimeMillis());
		l.playerMoved(cmp(new GameMoveEvent(board, board.getPlayerTrun(), gm1, p.next()), GMEC, LogicalOperator.EQUAL));
		replay(l);

		board.setPoints(10);
		board.setMoveFinished(false);
		board.addGameMoveListener(l);

		board.makeMove(m1);
		assertEquals(1, board.getGameMoves().size());
		assertSame(m1, board.getGameMoves().get(0).getPlayerMove());
		assertSame(10, board.getGameMoves().get(0).getPoints());
		assertSame(0, board.getGameMoves().get(0).getMoveNumber());
		assertEquals(10, p2.getPlayerTurn().getPoints());
		assertSame(p2.next(), board.getPlayerTrun());
		assertTrue(board.isMoveFinished());
		verify(l);

		//move passed
		reset(l);
		PlayerMove m2 = new PassTurnMove(board.getPlayerTrun().getPlayerId());
		GameMove gm2 = new GameMove(m2, 2, 2, System.currentTimeMillis());
		l.playerMoved(cmp(new GameMoveEvent(board, board.getPlayerTrun(), gm2, p.next()), GMEC, LogicalOperator.EQUAL));
		replay(l);

		board.setPoints(2);
		board.setMoveFinished(false);
		board.makeMove(m2);
		assertEquals(2, board.getGameMoves().size());
		assertSame(p2.next(), board.getPlayerTrun());
		assertSame(m2, board.getGameMoves().get(1).getPlayerMove());
		assertSame(2, board.getGameMoves().get(1).getPoints());
		assertSame(1, board.getGameMoves().get(1).getMoveNumber());
		assertTrue(board.isMoveFinished());
		verify(l);
	}

	@Test
	public void test_getWonPlayer() {
		final List<GamePlayerHand> gamePlayerHandList = Arrays.asList(h1, h2, h3);

		assertNull(board.getWonPlayer(gamePlayerHandList));

		h1.increasePoints(1);
		assertSame(h1, board.getWonPlayer(gamePlayerHandList));

		h2.increasePoints(2);
		assertSame(h2, board.getWonPlayer(gamePlayerHandList));

		h3.increasePoints(2);
		assertNull(board.getWonPlayer(gamePlayerHandList));
	}

	@Test
	public void test_finishByWin() throws GameMoveException {
		PlayersIterator p = new PlayersIterator(Arrays.asList(h1, h2, h3), board.getPlayerTrun());
		h1.increasePoints(1);

		GameStateListener l = createStrictMock(GameStateListener.class);
		l.gameFinished(board, h1);
		replay(l);

		board.addGameStateListener(l);

		board.makeMove(new MakeTurnMove(p.getPlayerTurn().getPlayerId()));
		board.setGameFinished(true);
		board.setFinishScore(new int[]{10, 3, 4});
		board.makeMove(new MakeTurnMove(p.next().getPlayerId()));

		assertEquals(GameState.FINISHED, board.getGameState());
		assertFalse(board.getFinishedTime() == 0);

		verify(l);
		assertNull(board.getFinishScore());
		assertNull(board.getPlayerTrun());
		assertEquals(11, h1.getPoints());
		assertEquals(3, h2.getPoints());
		assertEquals(4, h3.getPoints());
	}

	@Test
	public void test_finishByDraw_NoWins() throws GameMoveException {
		PlayersIterator p = new PlayersIterator(Arrays.asList(h1, h2, h3), board.getPlayerTrun());

		GameStateListener l = createStrictMock(GameStateListener.class);
		l.gameDraw(board);
		replay(l);

		board.addGameStateListener(l);

		board.makeMove(new MakeTurnMove(p.getPlayerTurn().getPlayerId()));
		board.setGameFinished(true);
		board.setFinishScore(new int[]{0, 0, 0});
		board.makeMove(new MakeTurnMove(p.next().getPlayerId()));

		assertEquals(GameState.DRAW, board.getGameState());
		assertNull(board.getPlayerTrun());
		assertFalse(board.getFinishedTime() == 0);

		verify(l);
	}

	@Test
	public void test_finishByDraw_NoMoves() throws GameMoveException {
		GameStateListener l = createStrictMock(GameStateListener.class);
		l.gameDraw(board);
		replay(l);

		board.addGameStateListener(l);

		board.makeMove(new PassTurnMove(board.getPlayerTrun().getPlayerId()));
		board.setGamePassed(true);
		board.setFinishScore(new int[]{0, 0, 0});
		board.makeMove(new PassTurnMove(board.getPlayerTrun().getPlayerId()));
		board.setGamePassed(false);

		assertEquals(GameState.DRAW, board.getGameState());
		assertNull(board.getPlayerTrun());
		assertFalse(board.getFinishedTime() == 0);

		verify(l);
	}

	@Test
	public void test_finishByClose() throws GameMoveException {
		GameStateListener l = createStrictMock(GameStateListener.class);
		l.gameInterrupted(board, h1, false);
		replay(l);

		board.addGameStateListener(l);
		board.setFinishScore(new int[]{0, 0, 0});

		try {
			board.close(new GamePlayerHand(13L));
			fail("Exception must be here");
		} catch (UnsuitablePlayerException ex) {
			;
		}

		board.close(h1);
		verify(l);

		assertEquals(GameState.INTERRUPTED, board.getGameState());
		assertSame(h1, board.getPlayerTrun());
		assertFalse(board.getFinishedTime() == 0);
	}

	@Test
	public void test_finishByTermination() throws GameMoveException {
		GameStateListener l = createStrictMock(GameStateListener.class);
		final GamePlayerHand playerTurn = board.getPlayerTrun();
		l.gameInterrupted(board, playerTurn, true);
		replay(l);

		board.addGameStateListener(l);
		board.setFinishScore(new int[]{0, 0, 0});
		board.terminate();
		verify(l);

		assertEquals(GameState.INTERRUPTED, board.getGameState());
		assertSame(playerTurn, board.getPlayerTrun());
		assertFalse(board.getFinishedTime() == 0);
	}

	@Test
	public void test_isRated_NoRequiredMoves() throws GameMoveException {
		board.setFinishScore(new int[]{0, 0, 0});
		assertTrue(board.isRatedGame());

		board.terminate();

		assertFalse(board.isRatedGame());
		assertEquals(GameState.INTERRUPTED, board.getGameState());
		assertFalse(board.getFinishedTime() == 0);
	}

	@Test
	public void test_isRated_hasRequiredMoves() throws GameMoveException {
		board.setFinishScore(new int[]{0, 0, 0});
		assertTrue(board.isRatedGame());

		final List<GamePlayerHand> playersHands = board.getPlayersHands();
		for (GamePlayerHand playersHand : playersHands) { //make turns for each player
			board.setPoints(10);
			board.setMoveFinished(false);
			board.makeMove(new MakeTurnMove(board.getPlayerTrun().getPlayerId()));
		}

		for (GamePlayerHand playersHand : playersHands) { //pass turns for each player
			board.makeMove(new PassTurnMove(board.getPlayerTrun().getPlayerId()));
		}

		board.terminate();

		assertTrue(board.isRatedGame());
		assertEquals(GameState.INTERRUPTED, board.getGameState());
		assertFalse(board.getFinishedTime() == 0);
	}

	public static void increasePlayerPoints(GamePlayerHand h, int points) {
		h.increasePoints(points);
	}
}
