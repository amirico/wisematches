package wisematches.server.gameplaying.board;

import org.easymock.EasyMock;
import org.easymock.LogicalOperator;
import org.junit.Before;
import org.junit.Test;
import wisematches.server.core.MockPlayer;
import wisematches.server.player.Player;

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
				return new GameSettings("Mock", 3) {
				};
			}
		}
		;

		gameSettings = new MockGameSettingsBuilder().build();

		board = new MockGameBoard(gameSettings,
				Arrays.<Player>asList(new MockPlayer(1, 1000), new MockPlayer(2, 1000), new MockPlayer(3, 1000)));
		h1 = board.getPlayerHand(1);
		h2 = board.getPlayerHand(2);
		h3 = board.getPlayerHand(3);
	}

	@Test
	public void test_incorrectCreateGameBoard() {
		//settings is null
		try {
			new MockGameBoard(null, null);
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
			;
		}

		//players is null
		try {
			new MockGameBoard(gameSettings, null);
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
			;
		}

		//players is null
		try {
			new MockGameBoard(gameSettings, Arrays.<Player>asList(new MockPlayer(1, 1000)));
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
			;
		}

		//players is null
		try {
			new MockGameBoard(gameSettings, Arrays.<Player>asList(new MockPlayer(1, 1000), null));
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
			;
		}
	}

	@Test
	public void test_defaultValues() {
		assertNotNull(board.getPlayerTurn());
		assertSame(gameSettings, board.getGameSettings());
		assertSame(GameState.ACTIVE, board.getGameState());
		assertEquals(0, board.getGameMoves().size());
		assertSame(h1, board.getPlayerHand(1));
		assertSame(h2, board.getPlayerHand(2));
		assertSame(h3, board.getPlayerHand(3));
		assertNull(board.getPlayerHand(4));
	}

	@Test
	public void test_stateListeners() {
		final GameMove gm = new GameMove(createMock(PlayerMove.class), 10, 1, new Date());

		final GameBoardListener l = createStrictMock(GameBoardListener.class);
		l.gameDraw(board);
		l.gameFinished(board, h1);
		l.gameInterrupted(board, h2, false);
		l.playerMoved(cmp(new GameMoveEvent(board, h1, gm, h3), GMEC, LogicalOperator.EQUAL));
		replay(l);

		board.addGameBoardListener(l);
		board.addGameBoardListener(l);

		board.fireGameDraw();
		board.fireGameFinished(h1);
		board.fireGameInterrupted(h2, false);
		board.firePlayerMoved(h1, gm, h3);

		//no any calles after removing must be
		board.removeGameBoardListener(l);
		board.fireGameDraw();
		board.firePlayerMoved(h1, gm, h3);

		verify(l);
	}

	@Test
	public void test_selectFirstPlayer() {
		Collection c = Arrays.asList(h1, h2, h3);
		assertTrue("First player selected", c.contains(board.getPlayerTurn()));
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
			PlayersIterator p = new PlayersIterator(Arrays.asList(h1, h2, h3), board.getPlayerTurn());
			board.makeMove(new MakeTurnMove(p.next().getPlayerId()));
			fail("Exception must be here");
		} catch (UnsuitablePlayerException e) {
			;
		}

		//illegal move
		board.setAllowNextMove(false);
		try {
			board.makeMove(new MakeTurnMove(board.getPlayerTurn().getPlayerId()));
		} catch (IncorrectMoveException ex) {
			;
		}
		board.setAllowNextMove(true);

		//game was finished
		board.setGameFinished(true);
		board.setFinishScore(new int[]{0, 0, 0});
		board.makeMove(new MakeTurnMove(board.getPlayerTurn().getPlayerId()));
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
			board.makeMove(new MakeTurnMove(board.getPlayerTurn().getPlayerId()));
			fail("Exception must be here: GameFinishedException");
		} catch (GameFinishedException ex) {
			;
		}
		try {
			board.makeMove(new PassTurnMove(board.getPlayerTurn().getPlayerId()));
			fail("Exception must be here: GameFinishedException");
		} catch (GameFinishedException ex) {
			;
		}
		board.setGamePassed(false);
	}

	@Test
	public void test_gameMoves() throws GameMoveException {
		PlayersIterator p = new PlayersIterator(Arrays.asList(h1, h2, h3), board.getPlayerTurn());
		PlayersIterator p2 = new PlayersIterator(Arrays.asList(h1, h2, h3), board.getPlayerTurn());

		final GameBoardListener l = createStrictMock(GameBoardListener.class);
		//move maden
		final PlayerMove m1 = new MakeTurnMove(board.getPlayerTurn().getPlayerId());
		final GameMove gm1 = new GameMove(m1, 10, 1, new Date());
		l.playerMoved(cmp(new GameMoveEvent(board, board.getPlayerTurn(), gm1, p.next()), GMEC, LogicalOperator.EQUAL));
		replay(l);

		board.setPoints(10);
		board.setMoveFinished(false);
		board.addGameBoardListener(l);

		board.makeMove(m1);
		assertEquals(1, board.getGameMoves().size());
		assertSame(m1, board.getGameMoves().get(0).getPlayerMove());
		assertSame(10, board.getGameMoves().get(0).getPoints());
		assertSame(0, board.getGameMoves().get(0).getMoveNumber());
		assertEquals(10, p2.getPlayerTurn().getPoints());
		assertSame(p2.next(), board.getPlayerTurn());
		assertTrue(board.isMoveFinished());
		verify(l);

		//move passed
		reset(l);
		PlayerMove m2 = new PassTurnMove(board.getPlayerTurn().getPlayerId());
		GameMove gm2 = new GameMove(m2, 2, 2, new Date());
		l.playerMoved(cmp(new GameMoveEvent(board, board.getPlayerTurn(), gm2, p.next()), GMEC, LogicalOperator.EQUAL));
		replay(l);

		board.setPoints(2);
		board.setMoveFinished(false);
		board.makeMove(m2);
		assertEquals(2, board.getGameMoves().size());
		assertSame(p2.next(), board.getPlayerTurn());
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
		PlayersIterator p = new PlayersIterator(Arrays.asList(h1, h2, h3), board.getPlayerTurn());
		h1.increasePoints(1);

		GameBoardListener l = createStrictMock(GameBoardListener.class);
		l.playerMoved(EasyMock.<GameMoveEvent>anyObject());
		l.playerMoved(EasyMock.<GameMoveEvent>anyObject());
		l.gameFinished(board, h1);
		replay(l);

		board.addGameBoardListener(l);

		board.makeMove(new MakeTurnMove(p.getPlayerTurn().getPlayerId()));
		board.setGameFinished(true);
		board.setFinishScore(new int[]{10, 3, 4});
		board.makeMove(new MakeTurnMove(p.next().getPlayerId()));

		assertEquals(GameState.FINISHED, board.getGameState());
		assertNotNull(board.getFinishedTime());

		verify(l);
		assertNull(board.getFinishScore());
		assertNull(board.getPlayerTurn());
		assertEquals(11, h1.getPoints());
		assertEquals(3, h2.getPoints());
		assertEquals(4, h3.getPoints());
	}

	@Test
	public void test_finishByDraw_NoWins() throws GameMoveException {
		PlayersIterator p = new PlayersIterator(Arrays.asList(h1, h2, h3), board.getPlayerTurn());

		GameBoardListener l = createStrictMock(GameBoardListener.class);
		l.playerMoved(EasyMock.<GameMoveEvent>anyObject());
		l.playerMoved(EasyMock.<GameMoveEvent>anyObject());
		l.gameDraw(board);
		replay(l);

		board.addGameBoardListener(l);

		board.makeMove(new MakeTurnMove(p.getPlayerTurn().getPlayerId()));
		board.setGameFinished(true);
		board.setFinishScore(new int[]{0, 0, 0});
		board.makeMove(new MakeTurnMove(p.next().getPlayerId()));

		assertEquals(GameState.DRAW, board.getGameState());
		assertNull(board.getPlayerTurn());
		assertNotNull(board.getFinishedTime());

		verify(l);
	}

	@Test
	public void test_finishByDraw_NoMoves() throws GameMoveException {
		GameBoardListener l = createStrictMock(GameBoardListener.class);
		l.playerMoved(EasyMock.<GameMoveEvent>anyObject());
		l.playerMoved(EasyMock.<GameMoveEvent>anyObject());
		l.gameDraw(board);
		replay(l);

		board.addGameBoardListener(l);

		board.makeMove(new PassTurnMove(board.getPlayerTurn().getPlayerId()));
		board.setGamePassed(true);
		board.setFinishScore(new int[]{0, 0, 0});
		board.makeMove(new PassTurnMove(board.getPlayerTurn().getPlayerId()));
		board.setGamePassed(false);

		assertEquals(GameState.DRAW, board.getGameState());
		assertNull(board.getPlayerTurn());
		assertNotNull(board.getFinishedTime());

		verify(l);
	}

	@Test
	public void test_finishByClose() throws GameMoveException {
		GameBoardListener l = createStrictMock(GameBoardListener.class);
		l.gameInterrupted(board, h1, false);
		replay(l);

		board.addGameBoardListener(l);
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
		assertSame(h1, board.getPlayerTurn());
		assertNotNull(board.getFinishedTime());
	}

	@Test
	public void test_finishByTermination() throws GameMoveException {
		GameBoardListener l = createStrictMock(GameBoardListener.class);
		final GamePlayerHand playerTurn = board.getPlayerTurn();
		l.gameInterrupted(board, playerTurn, true);
		replay(l);

		board.addGameBoardListener(l);
		board.setFinishScore(new int[]{0, 0, 0});
		board.terminate();
		verify(l);

		assertEquals(GameState.INTERRUPTED, board.getGameState());
		assertSame(playerTurn, board.getPlayerTurn());
		assertNotNull(board.getFinishedTime());
	}

	@Test
	public void test_isRated_NoRequiredMoves() throws GameMoveException {
		board.setFinishScore(new int[]{0, 0, 0});
		assertTrue(board.isRatedGame());

		board.terminate();

		assertFalse(board.isRatedGame());
		assertEquals(GameState.INTERRUPTED, board.getGameState());
		assertNotNull(board.getFinishedTime());
	}

	@Test
	public void test_isRated_hasRequiredMoves() throws GameMoveException {
		board.setFinishScore(new int[]{0, 0, 0});
		assertTrue(board.isRatedGame());

		final List<GamePlayerHand> playersHands = board.getPlayersHands();
		for (GamePlayerHand playersHand : playersHands) { //make turns for each player
			board.setPoints(10);
			board.setMoveFinished(false);
			board.makeMove(new MakeTurnMove(board.getPlayerTurn().getPlayerId()));
		}

		for (GamePlayerHand playersHand : playersHands) { //pass turns for each player
			board.makeMove(new PassTurnMove(board.getPlayerTurn().getPlayerId()));
		}

		board.terminate();

		assertTrue(board.isRatedGame());
		assertEquals(GameState.INTERRUPTED, board.getGameState());
		assertNotNull(board.getFinishedTime());
	}

	public static void increasePlayerPoints(GamePlayerHand h, int points) {
		h.increasePoints(points);
	}
}
