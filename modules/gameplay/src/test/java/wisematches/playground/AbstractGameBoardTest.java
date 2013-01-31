package wisematches.playground;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import wisematches.core.Personality;
import wisematches.core.Player;

import java.util.*;

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

	public AbstractGameBoardTest() {
	}

	@Before
	public void setUp() throws Exception {
		settings = new MockGameSettings("Mock", 3);

		board = new MockGameBoard(settings,
				Arrays.<Personality>asList(player1, player2, player3));
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
		}

		//players is null
		try {
			new MockGameBoard(settings, null);
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
		}

		//players is null
		try {
			new MockGameBoard(settings, Arrays.<Personality>asList(player1));
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
		}

		//players is null
		try {
			new MockGameBoard(settings, Arrays.<Personality>asList(player1, null));
			fail("Exception must be here");
		} catch (IllegalArgumentException ex) {
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
	}

	@Test
	public void test_gameMoves() throws GameMoveException {
		final BoardListener l = createStrictMock(BoardListener.class);
		//move done
		final Capture<GameMove> move = new Capture<>();
		l.gameMoveDone(same(board), capture(move), isA(GameMoveScore.class));
		replay(l);

		board.setPoints((short) 10);
		board.setMoveFinished(false);
		board.setBoardListener(l);

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
		board.setFinishScore(new short[]{0, 0, 0});
		board.resign(board.getPlayerTurn());
		assertEquals(0, board.getWonPlayers().size());

		h1.markAsWinner();
		assertEquals(Arrays.<Personality>asList(player1), board.getWonPlayers());

		h3.markAsWinner();
		assertEquals(Arrays.<Personality>asList(player1, player3), board.getWonPlayers());
	}

	@Test
	public void test_finishByWin() throws GameMoveException {
		h1.increasePoints((short) 1);

		BoardListener l = createStrictMock(BoardListener.class);
		l.gameMoveDone(same(board), EasyMock.<GameMove>anyObject(), isA(GameMoveScore.class));
		l.gameMoveDone(same(board), EasyMock.<GameMove>anyObject(), isA(GameMoveScore.class));
		l.gameFinished(board, GameResolution.FINISHED, Collections.<Personality>singletonList(player1));
		replay(l);

		board.setBoardListener(l);

		board.makeMove(board.getPlayerTurn());
		board.setGameFinished(true);
		board.setFinishScore(new short[]{10, 3, 4});
		board.makeMove(board.getPlayerTurn());

		assertEquals(GameResolution.FINISHED, board.getResolution());
		assertNull(board.getPlayerTurn());
		assertNotNull(board.getFinishedTime());

		verify(l);
		assertNull(board.getFinishScore());
		assertEquals(11, h1.getPoints());
		assertEquals(3, h2.getPoints());
		assertEquals(4, h3.getPoints());
	}

	@Test
	public void test_finishByDraw_NoWins() throws GameMoveException {
		BoardListener l = createStrictMock(BoardListener.class);
		l.gameMoveDone(same(board), EasyMock.<GameMove>anyObject(), isA(GameMoveScore.class));
		l.gameMoveDone(same(board), EasyMock.<GameMove>anyObject(), isA(GameMoveScore.class));
		l.gameFinished(board, GameResolution.FINISHED, Collections.<Personality>emptyList());
		replay(l);

		board.setBoardListener(l);

		board.makeMove(board.getPlayerTurn());
		board.setGameFinished(true);
		board.setFinishScore(new short[]{0, 0, 0});
		board.makeMove(board.getPlayerTurn());

		assertEquals(GameResolution.FINISHED, board.getResolution());
		assertNull(board.getPlayerTurn());
		assertNotNull(board.getFinishedTime());
		assertEquals(0, board.getWonPlayers().size());

		verify(l);
	}

	@Test
	public void test_finishByClose() throws BoardUpdatingException {
		BoardListener l = createStrictMock(BoardListener.class);
		l.gameFinished(board, GameResolution.RESIGNED, Collections.<Personality>emptyList());
		replay(l);

		board.setBoardListener(l);
		board.setFinishScore(new short[]{0, 0, 0});

		try {
			board.resign(new MockPlayer(13));
			fail("Exception must be here");
		} catch (UnsuitablePlayerException ignore) {
		} catch (BoardUpdatingException e) {
			fail("UnsuitablePlayerException must be here");
		}

		board.resign(player1);
		verify(l);

		assertEquals(GameResolution.RESIGNED, board.getResolution());
		assertSame(player1, board.getPlayerTurn());
		assertNotNull(board.getFinishedTime());
	}

	@Test
	public void test_finishByTermination() throws GameMoveException {
		BoardListener l = createStrictMock(BoardListener.class);
		final Personality playerTurn = board.getPlayerTurn();
		l.gameFinished(board, GameResolution.INTERRUPTED, Collections.<Personality>emptyList());
		replay(l);

		board.setBoardListener(l);
		board.setFinishScore(new short[]{0, 0, 0});
		board.setLastMoveTime(new Date(System.currentTimeMillis() / 2));
		board.terminate();
		verify(l);

		assertEquals(GameResolution.INTERRUPTED, board.getResolution());
		assertSame(playerTurn, board.getPlayerTurn());
		assertNotNull(board.getFinishedTime());
	}

	@Test
	public void test_isRated_noRequiredMoves() throws GameMoveException {
		board.setFinishScore(new short[]{0, 0, 0});
		assertTrue(board.isRated());

		final Collection<Personality> playersHands = board.getPlayers();
		for (Personality ignored : playersHands) { //make turns for each player
			board.setPoints((short) 10);
			board.setMoveFinished(false);
			board.makeMove(board.getPlayerTurn());
		}

		board.setLastMoveTime(new Date(System.currentTimeMillis() / 2));
		board.terminate();

		assertFalse(board.isRated());
		assertEquals(GameResolution.INTERRUPTED, board.getResolution());
		assertNotNull(board.getFinishedTime());
	}

	@Test
	public void test_isRated_hasRequiredMoves() throws GameMoveException {
		board.setFinishScore(new short[]{0, 0, 0});
		assertTrue(board.isRated());

		final Collection<Personality> playersHands = board.getPlayers();
		for (int i = 0; i < 2; i++) {
			for (Personality ignored : playersHands) { //make turns for each player
				board.setPoints((short) 10);
				board.setMoveFinished(false);
				board.makeMove(board.getPlayerTurn());
			}
		}

		board.setLastMoveTime(new Date(System.currentTimeMillis() / 2));
		board.terminate();

		assertTrue(board.isRated());
		assertEquals(GameResolution.INTERRUPTED, board.getResolution());
		assertNotNull(board.getFinishedTime());
	}
}
