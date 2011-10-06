package wisematches.playground;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import wisematches.personality.Personality;

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
	private BoardStateListener stateListener;

	public AbstractGameBoardTest() {
	}

	@Before
	public void setUp() throws Exception {
		stateListener = createNiceMock(BoardStateListener.class);

		gameSettings = new MockGameSettings("Mock", 3);

		board = new MockGameBoard(gameSettings,
				Arrays.<Personality>asList(Personality.person(1), Personality.person(2), Personality.person(3)));
		board.setStateListener(stateListener);
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
			new MockGameBoard(gameSettings, Arrays.<Personality>asList(Personality.person(1)));
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
			;
		}

		//players is null
		try {
			new MockGameBoard(gameSettings, Arrays.<Personality>asList(Personality.person(1), null));
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
			;
		}
	}

	@Test
	public void test_defaultValues() {
		assertNotNull(board.getPlayerTurn());
		assertSame(gameSettings, board.getGameSettings());
		assertNull(board.getGameResolution());
		assertTrue(board.isGameActive());
		assertEquals(0, board.getGameMoves().size());
		assertSame(h1, board.getPlayerHand(1));
		assertSame(h2, board.getPlayerHand(2));
		assertSame(h3, board.getPlayerHand(3));
		assertNull(board.getPlayerHand(4));
	}

	@Test
	public void test_selectFirstPlayer() {
		List<GamePlayerHand> c = Arrays.asList(h1, h2, h3);

		int sum = 0;
		int[] values = new int[c.size()];
		for (int i = 0; i < 1000; i++) {
			byte b = board.selectFirstPlayer(null, c);
			values[b]++;
			sum += (b + 1);
		}
		System.out.println("First move distribution values: " + Arrays.toString(values));

		assertTrue("The sum [" + sum + "] is less than 1500. It means only first player is selected", sum > 1500);
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
			board.makeMove(new MakeTurnMove(board.getNextPlayerTurn().getPlayerId()));
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
		board.setFinishScore(new short[]{0, 0, 0});
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
		final BoardStateListener l = createStrictMock(BoardStateListener.class);
		//move maden
		final PlayerMove m1 = new MakeTurnMove(board.getPlayerTurn().getPlayerId());
		final Capture<GameMove> move = new Capture<GameMove>();
		l.gameMoveDone(same(board), capture(move));
		replay(l);

		board.setPoints((short) 10);
		board.setMoveFinished(false);
		board.setStateListener(l);

		GamePlayerHand turn = board.getPlayerTurn();
		GamePlayerHand nextTurn = board.getNextPlayerTurn();

		board.makeMove(m1);
		assertEquals(10, move.getValue().getPoints());
		assertEquals(1, board.getGameMoves().size());
		assertSame(m1, board.getGameMoves().get(0).getPlayerMove());
		assertSame(10, board.getGameMoves().get(0).getPoints());
		assertSame(0, board.getGameMoves().get(0).getMoveNumber());
		assertEquals(10, turn.getPoints());
		assertSame(nextTurn, board.getPlayerTurn());
		assertTrue(board.isMoveFinished());
		verify(l);

		//move passed
		reset(l);
		PlayerMove m2 = new PassTurnMove(board.getPlayerTurn().getPlayerId());
		final Capture<GameMove> gm2 = new Capture<GameMove>();
		l.gameMoveDone(same(board), capture(gm2));
		replay(l);

		turn = board.getPlayerTurn();
		nextTurn = board.getNextPlayerTurn();

		board.setPoints((short) 2);
		board.setMoveFinished(false);
		board.makeMove(m2);
		assertEquals(2, board.getGameMoves().size());
		assertSame(nextTurn, board.getPlayerTurn());
		assertSame(m2, board.getGameMoves().get(1).getPlayerMove());
		assertSame(2, board.getGameMoves().get(1).getPoints());
		assertSame(1, board.getGameMoves().get(1).getMoveNumber());
		assertTrue(board.isMoveFinished());
		verify(l);
	}

	@Test
	public void test_getWonPlayer() throws GameMoveException {
		board.setFinishScore(new short[]{0, 0, 0});
		board.resign(board.getPlayerTurn());
		assertEquals(0, board.getWonPlayers().size());

		h1.increasePoints((short) 1);
		assertEquals(Arrays.asList(h1), board.getWonPlayers());

		h2.increasePoints((short) 2);
		assertEquals(Arrays.asList(h2), board.getWonPlayers());

		h3.increasePoints((short) 2);
		assertEquals(Arrays.asList(h2, h3), board.getWonPlayers());
	}

	@Test
	public void test_finishByWin() throws GameMoveException {
		h1.increasePoints((short) 1);

		BoardStateListener l = createStrictMock(BoardStateListener.class);
		l.gameMoveDone(same(board), EasyMock.<GameMove>anyObject());
		l.gameMoveDone(same(board), EasyMock.<GameMove>anyObject());
		l.gameFinished(board, GameResolution.FINISHED, Collections.singletonList(h1));
		replay(l);

		board.setStateListener(l);

		board.makeMove(new MakeTurnMove(board.getPlayerTurn().getPlayerId()));
		board.setGameFinished(true);
		board.setFinishScore(new short[]{10, 3, 4});
		board.makeMove(new MakeTurnMove(board.getPlayerTurn().getPlayerId()));

		assertEquals(GameResolution.FINISHED, board.getGameResolution());
		assertNotNull(board.getFinishedTime());
		assertNotNull(board.getPlayerTurn());

		verify(l);
		assertNull(board.getFinishScore());
		assertEquals(11, h1.getPoints());
		assertEquals(3, h2.getPoints());
		assertEquals(4, h3.getPoints());
	}

	@Test
	public void test_finishByDraw_NoWins() throws GameMoveException {
		BoardStateListener l = createStrictMock(BoardStateListener.class);
		l.gameMoveDone(same(board), EasyMock.<GameMove>anyObject());
		l.gameMoveDone(same(board), EasyMock.<GameMove>anyObject());
		l.gameFinished(board, GameResolution.FINISHED, Collections.<GamePlayerHand>emptyList());
		replay(l);

		board.setStateListener(l);

		board.makeMove(new MakeTurnMove(board.getPlayerTurn().getPlayerId()));
		board.setGameFinished(true);
		board.setFinishScore(new short[]{0, 0, 0});
		board.makeMove(new MakeTurnMove(board.getPlayerTurn().getPlayerId()));

		assertEquals(GameResolution.FINISHED, board.getGameResolution());
		assertNotNull(board.getPlayerTurn());
		assertNotNull(board.getFinishedTime());
		assertEquals(0, board.getWonPlayers().size());

		verify(l);
	}

	@Test
	public void test_finishByDraw_NoMoves() throws GameMoveException {
		BoardStateListener l = createStrictMock(BoardStateListener.class);
		l.gameMoveDone(same(board), EasyMock.<GameMove>anyObject());
		l.gameMoveDone(same(board), EasyMock.<GameMove>anyObject());
		l.gameFinished(board, GameResolution.STALEMATE, Collections.<GamePlayerHand>emptyList());
		replay(l);

		board.setStateListener(l);

		board.makeMove(new PassTurnMove(board.getPlayerTurn().getPlayerId()));
		board.setGamePassed(true);
		board.setFinishScore(new short[]{0, 0, 0});
		board.makeMove(new PassTurnMove(board.getPlayerTurn().getPlayerId()));
		board.setGamePassed(false);

		assertEquals(GameResolution.STALEMATE, board.getGameResolution());
		assertNull(board.getPlayerTurn());
		assertNotNull(board.getFinishedTime());
		assertEquals(Collections.<GamePlayerHand>emptyList(), board.getWonPlayers());

		verify(l);
	}

	@Test
	public void test_finishByClose() throws GameMoveException {
		BoardStateListener l = createStrictMock(BoardStateListener.class);
		l.gameFinished(board, GameResolution.RESIGNED, Collections.<GamePlayerHand>emptyList());
		replay(l);

		board.setStateListener(l);
		board.setFinishScore(new short[]{0, 0, 0});

		try {
			board.resign(new GamePlayerHand(13L));
			fail("Exception must be here");
		} catch (UnsuitablePlayerException ex) {
			;
		}

		board.resign(h1);
		verify(l);

		assertEquals(GameResolution.RESIGNED, board.getGameResolution());
		assertSame(h1, board.getPlayerTurn());
		assertNotNull(board.getFinishedTime());
	}

	@Test
	public void test_finishByTermination() throws GameMoveException {
		BoardStateListener l = createStrictMock(BoardStateListener.class);
		final GamePlayerHand playerTurn = board.getPlayerTurn();
		l.gameFinished(board, GameResolution.TIMEOUT, Collections.<GamePlayerHand>emptyList());
		replay(l);

		board.setStateListener(l);
		board.setFinishScore(new short[]{0, 0, 0});
		board.setLastMoveTime(new Date(System.currentTimeMillis() / 2));
		board.terminate();
		verify(l);

		assertEquals(GameResolution.TIMEOUT, board.getGameResolution());
		assertSame(playerTurn, board.getPlayerTurn());
		assertNotNull(board.getFinishedTime());
	}

	@Test
	public void test_isRated_hasRequiredMoves() throws GameMoveException {
		board.setFinishScore(new short[]{0, 0, 0});
		assertTrue(board.isRatedGame());

		final Collection<GamePlayerHand> playersHands = board.getPlayersHands();
		for (GamePlayerHand playersHand : playersHands) { //make turns for each player
			board.setPoints((short) 10);
			board.setMoveFinished(false);
			board.makeMove(new MakeTurnMove(board.getPlayerTurn().getPlayerId()));
		}

		for (GamePlayerHand playersHand : playersHands) { //pass turns for each player
			board.makeMove(new PassTurnMove(board.getPlayerTurn().getPlayerId()));
		}

		board.setLastMoveTime(new Date(System.currentTimeMillis() / 2));
		board.terminate();

		assertTrue(board.isRatedGame());
		assertEquals(GameResolution.TIMEOUT, board.getGameResolution());
		assertNotNull(board.getFinishedTime());
	}

	public static void increasePlayerPoints(GamePlayerHand h, short points) {
		h.increasePoints(points);
	}
}
