package wisematches.playground;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import wisematches.core.Personality;
import wisematches.core.Player;

import java.util.Arrays;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@SuppressWarnings("unchecked")
public class AbstractGameBoardTest {
	private Player player1 = new MockPlayer(1L);
	private Player player2 = new MockPlayer(2L);
	private Player player3 = new MockPlayer(3L);

	private AbstractPlayerHand h1;
	private AbstractPlayerHand h2;
	private AbstractPlayerHand h3;

	private MockGameBoard board;
	private GameSettings settings;
	private GamePlayListener stateListener;

	public AbstractGameBoardTest() {
	}

	@Before
	public void setUp() throws Exception {
		stateListener = createNiceMock(GamePlayListener.class);

		settings = new MockGameSettings("Mock", 3);

		board = new MockGameBoard(settings,
				Arrays.<Personality>asList(player1, player2, player3));
		board.setGamePlayListener(stateListener);
		h1 = board.getPlayerHand(player1);
		h2 = board.getPlayerHand(player2);
		h3 = board.getPlayerHand(player3);
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
			new MockGameBoard(settings, null);
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
			;
		}

		//players is null
		try {
			new MockGameBoard(settings, Arrays.<Personality>asList(player1));
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
			;
		}

		//players is null
		try {
			new MockGameBoard(settings, Arrays.<Personality>asList(player1, null));
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
			;
		}
	}

	@Test
	public void test_defaultValues() {
		assertNotNull(board.getPlayerTurn());
		assertSame(settings, board.getSettings());
		assertNull(board.getResolution());
		assertTrue(board.isActive());
		assertEquals(0, board.getGameMoves().size());
		assertSame(h1, board.getPlayerHand(player1));
		assertSame(h2, board.getPlayerHand(player2));
		assertSame(h3, board.getPlayerHand(player3));
		assertNull(board.getPlayerHand(new MockPlayer(4)));
	}

	@Test
	public void test_selectFirstPlayer() {
		List<Personality> c = Arrays.<Personality>asList(player1, player2, player3);

		int sum = 0;
		int[] values = new int[c.size()];
		for (int i = 0; i < 1000; i++) {
			byte b = board.selectFirstPlayer(c);
			values[b]++;
			sum += (b + 1);
		}
		System.out.println("First move distribution values: " + Arrays.toString(values));

		assertTrue("The sum [" + sum + "] is less than 1500. It means only first player is selected", sum > 1500);
		assertTrue("First player selected", c.contains(board.getPlayerTurn()));
	}

	@Test
	public void test_playerHand() {
		assertSame(h1, board.getPlayerHand(player1));
		assertSame(h2, board.getPlayerHand(player2));
		assertSame(h3, board.getPlayerHand(player3));
		assertNull(board.getPlayerHand(new MockPlayer(4)));
	}

	@Test
	public void test_illegalMoves() throws GameMoveException {
		//unknown player
		try {
			board.makeMove(new MockPlayer(13));
			fail("Exception must be here");
		} catch (UnsuitablePlayerException ignore) {
		}

		//unsuitable player
		try {
			board.makeMove(board.getPlayerTurn() == player1 ? player2 : player1);
			fail("Exception must be here");
		} catch (UnsuitablePlayerException ignore) {
		}

		//illegal move
		board.setAllowNextMove(false);
		try {
			board.makeMove(board.getPlayerTurn());
		} catch (IncorrectMoveException ignore) {
		}
		board.setAllowNextMove(true);

		//game was finished
		board.setGameFinished(true);
		board.setFinishScore(new short[]{0, 0, 0});
		board.makeMove(board.getPlayerTurn());
		try {
			board.makeMove(player2);
			fail("Exception must be here: GameFinishedException");
		} catch (GameFinishedException ignore) {
		}
		board.setGameFinished(false);

		//game was passed
		board.setGamePassed(true);
		try {
			board.makeMove(board.getPlayerTurn());
			fail("Exception must be here: GameFinishedException");
		} catch (GameFinishedException ignore) {
		}
		try {
			board.makeMove(board.getPlayerTurn());
			fail("Exception must be here: GameFinishedException");
		} catch (GameFinishedException ignore) {
		}
		board.setGamePassed(false);
	}

	@Test
	public void test_gameMoves() throws GameMoveException {
		final GamePlayListener l = createStrictMock(GamePlayListener.class);
		//move done
		final Capture<GameMove> move = new Capture<>();
		l.gameMoveDone(same(board), capture(move), isA(GameMoveScore.class));
		replay(l);

		board.setPoints((short) 10);
		board.setMoveFinished(false);
		board.setGamePlayListener(l);

		final Personality turn1 = board.getPlayerTurn();

		final GameMove m1 = board.makeMove(turn1);
		assertEquals(10, move.getValue().getPoints());
		assertEquals(1, board.getGameMoves().size());
		assertSame(m1, board.getGameMoves().get(0));
		assertSame(10, board.getGameMoves().get(0).getPoints());
		assertSame(0, board.getGameMoves().get(0).getMoveNumber());
		verify(l);
	}

	@Test
	public void test_getWonPlayer() throws BoardUpdatingException {
		// TODO: incrorrect approac

		board.setFinishScore(new short[]{0, 0, 0});
		board.resign(board.getPlayerTurn());
		assertEquals(0, board.getWonPlayers().size());

		h1.finalize((short) 100, (short) 102);
		h2.finalize((short) 102, (short) 100);
		h3.finalize((short) 102, (short) 100);
		assertEquals(Arrays.<Personality>asList(player1), board.getWonPlayers());

		h1.finalize((short) 102, (short) 100);
		h2.finalize((short) 100, (short) 102);
		h3.finalize((short) 102, (short) 100);
		assertEquals(Arrays.<Personality>asList(player2), board.getWonPlayers());

		h1.finalize((short) 102, (short) 100);
		h2.finalize((short) 102, (short) 100);
		h3.finalize((short) 102, (short) 100);
		assertEquals(Arrays.<Personality>asList(player2, player3), board.getWonPlayers());
	}

	/*
	@Test
	public void test_finishByWin() throws GameMoveException {
		h1.increasePoints((short) 1);

		GamePlayListener l = createStrictMock(GamePlayListener.class);
		l.gameMoveDone(same(board), EasyMock.<GameMove>anyObject(), isA(GameMoveScore.class));
		l.gameMoveDone(same(board), EasyMock.<GameMove>anyObject(), isA(GameMoveScore.class));
		l.gameFinished(board, GameResolution.FINISHED, Collections.singletonList(h1));
		replay(l);

		board.setGamePlayListener(l);

		board.makeMove(new MakeTurnMove(board.getPlayerTurn().getPlayerId()));
		board.setGameFinished(true);
		board.setFinishScore(new short[]{10, 3, 4});
		board.makeMove(new MakeTurnMove(board.getPlayerTurn().getPlayerId()));

		assertEquals(GameResolution.FINISHED, board.getResolution());
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
		GamePlayListener l = createStrictMock(GamePlayListener.class);
		l.gameMoveDone(same(board), EasyMock.<GameMove>anyObject(), isA(GameMoveScore.class));
		l.gameMoveDone(same(board), EasyMock.<GameMove>anyObject(), isA(GameMoveScore.class));
		l.gameFinished(board, GameResolution.FINISHED, Collections.<GamePlayerHand>emptyList());
		replay(l);

		board.setGamePlayListener(l);

		board.makeMove(new MakeTurnMove(board.getPlayerTurn().getPlayerId()));
		board.setGameFinished(true);
		board.setFinishScore(new short[]{0, 0, 0});
		board.makeMove(new MakeTurnMove(board.getPlayerTurn().getPlayerId()));

		assertEquals(GameResolution.FINISHED, board.getResolution());
		assertNotNull(board.getPlayerTurn());
		assertNotNull(board.getFinishedTime());
		assertEquals(0, board.getWonPlayers().size());

		verify(l);
	}

	@Test
	public void test_finishByDraw_NoMoves() throws GameMoveException {
		GamePlayListener l = createStrictMock(GamePlayListener.class);
		l.gameMoveDone(same(board), EasyMock.<GameMove>anyObject(), isA(GameMoveScore.class));
		l.gameMoveDone(same(board), EasyMock.<GameMove>anyObject(), isA(GameMoveScore.class));
		l.gameFinished(board, GameResolution.STALEMATE, Collections.<GamePlayerHand>emptyList());
		replay(l);

		board.setGamePlayListener(l);

		board.makeMove(new PassTurn(board.getPlayerTurn().getPlayerId()));
		board.setGamePassed(true);
		board.setFinishScore(new short[]{0, 0, 0});
		board.makeMove(new PassTurn(board.getPlayerTurn().getPlayerId()));
		board.setGamePassed(false);

		assertEquals(GameResolution.STALEMATE, board.getResolution());
		assertNull(board.getPlayerTurn());
		assertNotNull(board.getFinishedTime());
		assertEquals(Collections.<GamePlayerHand>emptyList(), board.getWonPlayers());

		verify(l);
	}

	@Test
	public void test_finishByClose() throws GameMoveException {
		GamePlayListener l = createStrictMock(GamePlayListener.class);
		l.gameFinished(board, GameResolution.RESIGNED, Collections.<GamePlayerHand>emptyList());
		replay(l);

		board.setGamePlayListener(l);
		board.setFinishScore(new short[]{0, 0, 0});

		try {
			board.resign(new GamePlayerHand(13L));
			fail("Exception must be here");
		} catch (UnsuitablePlayerException ex) {
			;
		}

		board.resign(h1);
		verify(l);

		assertEquals(GameResolution.RESIGNED, board.getResolution());
		assertSame(h1, board.getPlayerTurn());
		assertNotNull(board.getFinishedTime());
	}

	@Test
	public void test_finishByTermination() throws GameMoveException {
		GamePlayListener l = createStrictMock(GamePlayListener.class);
		final GamePlayerHand playerTurn = board.getPlayerTurn();
		l.gameFinished(board, GameResolution.INTERRUPTED, Collections.<GamePlayerHand>emptyList());
		replay(l);

		board.setGamePlayListener(l);
		board.setFinishScore(new short[]{0, 0, 0});
		board.setLastMoveTime(new Date(System.currentTimeMillis() / 2));
		board.terminate();
		verify(l);

		assertEquals(GameResolution.INTERRUPTED, board.getResolution());
		assertSame(playerTurn, board.getPlayerTurn());
		assertNotNull(board.getFinishedTime());
	}

	@Test
	public void test_isRated_hasRequiredMoves() throws GameMoveException {
		board.setFinishScore(new short[]{0, 0, 0});
		assertTrue(board.isRated());

		final Collection<GamePlayerHand> playersHands = board.getPlayers();
		for (GamePlayerHand playersHand : playersHands) { //make turns for each player
			board.setPoints((short) 10);
			board.setMoveFinished(false);
			board.makeMove(new MakeTurnMove(board.getPlayerTurn().getPlayerId()));
		}

		for (GamePlayerHand playersHand : playersHands) { //pass turns for each player
			board.makeMove(new PassTurn(board.getPlayerTurn().getPlayerId()));
		}

		board.setLastMoveTime(new Date(System.currentTimeMillis() / 2));
		board.terminate();

		assertTrue(board.isRated());
		assertEquals(GameResolution.INTERRUPTED, board.getResolution());
		assertNotNull(board.getFinishedTime());
	}

	public static void increasePlayerPoints(GamePlayerHand h, short points) {
		h.increasePoints(points);
	}
*/
}
