package wisematches.playground.scribble;

import org.junit.Before;
import org.junit.Test;
import wisematches.core.Language;
import wisematches.core.Personality;
import wisematches.core.RobotType;
import wisematches.playground.GameMoveException;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.scribble.bank.TilesBank;
import wisematches.playground.scribble.bank.impl.TilesBankInfoEditor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import static wisematches.playground.scribble.ScribbleBoard.LETTERS_IN_HAND;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleBoardTest {
	private ScribbleSettings settings;

	private Personality player1 = RobotType.DULL.getPlayer();
	private Personality player2 = RobotType.TRAINEE.getPlayer();
	private Personality player3 = RobotType.EXPERT.getPlayer();

	private ScribblePlayerHand h1;
	private ScribblePlayerHand h2;
	private ScribblePlayerHand h3;

	private final TilesBankInfoEditor editor = new TilesBankInfoEditor(Language.EN);

	public ScribbleBoardTest() {
	}

	@Before
	public void setUp() throws Exception {
		settings = new ScribbleSettings("test", Language.EN, 3);

		editor.add('a', 3, 1); // 0-2
		editor.add('b', 3, 2); // 3-5
		editor.add('c', 3, 3); // 6-8
		editor.add('d', 3, 4); // 9-11
		editor.add('e', 3, 5); // 12-14
		editor.add('f', 3, 6); // 15-17
		editor.add('g', 3, 7);  // 18-20
		editor.add('m', 13, 0);
	}

	@Test
	public void test_initializeBoard() {
		final Dictionary dict = createDictionary();
		final TilesBank tilesBank = new TilesBank(new TilesBankInfoEditor(Language.EN).add('a', 103, 1).createTilesBankInfo());

		final ScribbleBoard board = new ScribbleBoard(settings, Arrays.asList(player1, player2, player3), tilesBank, dict);
		h1 = board.getPlayerHand(player1);
		h2 = board.getPlayerHand(player2);
		h3 = board.getPlayerHand(player3);

		assertEquals(LETTERS_IN_HAND, h1.getTiles().length);
		assertEquals(LETTERS_IN_HAND, h2.getTiles().length);
		assertEquals(LETTERS_IN_HAND, h3.getTiles().length);
		assertNull(board.getResolution());
	}

	@Test
	public void test_smallGamePlay() throws GameMoveException {
		final Dictionary dictionary = createDictionary("abcde", "qrdts", "skel");
		final TilesBank tilesBank = createTilesBank("abcd*qrt*lkelt", 19);

		final ScribbleBoard board = new ScribbleBoard(settings, Arrays.asList(player1, player2, player3), tilesBank, dictionary);
		h1 = board.getPlayerHand(player1);
		h2 = board.getPlayerHand(player2);
		h3 = board.getPlayerHand(player3);

		assertEquals(7, h1.getTiles().length);
		assertEquals(7, h2.getTiles().length);
		assertEquals(7, h3.getTiles().length);
		assertEquals(12, tilesBank.getTilesLimit());

		ScribblePlayerHand hand = board.getPlayerHand(board.getPlayerTurn());
		hand.setTiles(new Tile[]{
				getTile(tilesBank, 'a', 0),
				getTile(tilesBank, 'b', 0),
				getTile(tilesBank, 'c', 0),
				getTile(tilesBank, 'd', 0),
				getTile(tilesBank, '*', 0),
				getTile(tilesBank, 'z', 0),
				getTile(tilesBank, 'z', 1)
		});

		board.makeTurn(board.getPlayerTurn(), new Word(new Position(4, 7), Direction.VERTICAL,
				getTile(tilesBank, 'a', 0),
				getTile(tilesBank, 'b', 0),
				getTile(tilesBank, 'c', 0),
				getTile(tilesBank, 'd', 0),
				getTile(tilesBank, '*', 0).redefine('e')
		));
		assertEquals(7, hand.getTiles().length);
		assertEquals(7, tilesBank.getTilesLimit());

		hand = board.getPlayerHand(board.getPlayerTurn());
		hand.setTiles(new Tile[]{
				getTile(tilesBank, 'q', 0),
				getTile(tilesBank, 'r', 0),
				getTile(tilesBank, 't', 0),
				getTile(tilesBank, '*', 1),
				getTile(tilesBank, 'l', 0),
				getTile(tilesBank, 'z', 2),
				getTile(tilesBank, 'z', 3)
		});

		board.makeTurn(board.getPlayerTurn(), new Word(new Position(7, 5), Direction.HORIZONTAL,
				getTile(tilesBank, 'q', 0),
				getTile(tilesBank, 'r', 0),
				getTile(tilesBank, 'd', 0),
				getTile(tilesBank, 't', 0),
				getTile(tilesBank, '*', 1).redefine('s')
		));
		assertEquals(7, hand.getTiles().length);
		assertEquals(3, tilesBank.getTilesLimit());

		hand = board.getPlayerHand(board.getPlayerTurn());
		hand.setTiles(new Tile[]{
				getTile(tilesBank, 'k', 0),
				getTile(tilesBank, 'e', 0),
				getTile(tilesBank, 'l', 1),
				getTile(tilesBank, 'z', 4),
				getTile(tilesBank, 'z', 5),
				getTile(tilesBank, 'z', 6),
				getTile(tilesBank, 'z', 7)
		});
		board.makeTurn(board.getPlayerTurn(), new Word(new Position(7, 9), Direction.VERTICAL,
				getTile(tilesBank, '*', 1).redefine('s'),
				getTile(tilesBank, 'k', 0),
				getTile(tilesBank, 'e', 0),
				getTile(tilesBank, 'l', 1)
		));
		assertEquals(7, hand.getTiles().length);
		assertEquals(0, tilesBank.getTilesLimit());

		assertTrue(tilesBank.isEmpty());
	}

	@Test
	public void test_checkState() throws GameMoveException {
		final Dictionary dictionary = createDictionary();
		final TilesBank tilesBank = createTilesBank("", 19);

		final ScribbleBoard board = new ScribbleBoard(settings, Arrays.asList(player1, player2, player3), tilesBank, dictionary);
		board.checkState();
	}

	@Test
	public void test_checkMove_Incorrect() throws GameMoveException {
		final Dictionary dictionary = createDictionary("aad", null);
		final TilesBank tilesBank = createTilesBank("abcabcd", 19);

		final ScribbleBoard board = new ScribbleBoard(settings, Arrays.asList(player1, player2, player3), tilesBank, dictionary);
		h1 = board.getPlayerHand(player1);
		h2 = board.getPlayerHand(player2);
		h3 = board.getPlayerHand(player3);

		//=============== first move not in center 1
		try {
			board.makeTurn(board.getPlayerTurn(), new Word(new Position(0, 0), Direction.HORIZONTAL, tilesBank.getTiles(0, 1, 2)));
			fail("Exception must be: move in not center");
		} catch (IncorrectPositionException ex) {
			assertTrue(ex.isMustBeInCenter());
		}

		//=============== first move not in center 2
		try {
			board.makeTurn(board.getPlayerTurn(), new Word(new Position(5, 7), Direction.HORIZONTAL, tilesBank.getTiles(0, 1, 2)));
			fail("Exception must be: move in not center");
		} catch (IncorrectPositionException ex) {
			assertTrue(ex.isMustBeInCenter());
		}

		//=============== first move not in center 3
		try {
			board.makeTurn(board.getPlayerTurn(), new Word(new Position(7, 5), Direction.VERTICAL, tilesBank.getTiles(0, 1, 2)));
			fail("Exception must be: move in not center");
		} catch (IncorrectPositionException ex) {
			assertTrue(ex.isMustBeInCenter());
		}

		//=============== not any on the board
		board.getPlayerHand(board.getPlayerTurn()).setTiles(tilesBank.getTiles(0, 1, 2));
		board.makeTurn(board.getPlayerTurn(), new Word(new Position(7, 7), Direction.HORIZONTAL, tilesBank.getTiles(0, 1, 2)));
		assertEquals(1, board.getGameMoves().size());

		board.getPlayerHand(board.getPlayerTurn()).setTiles(tilesBank.getTiles(3, 4, 5));
		try {
			board.makeTurn(board.getPlayerTurn(), new Word(new Position(8, 7), Direction.HORIZONTAL, tilesBank.getTiles(3, 4, 5)));
			fail("Exception must be: no one letter from board is taken");
		} catch (IncorrectTilesException ex) {
			assertEquals(IncorrectTilesException.Type.NO_BOARD_TILES, ex.getType());
		}

		//place tile that already on board
		try {
			board.makeTurn(board.getPlayerTurn(), new Word(new Position(7, 7), Direction.VERTICAL, tilesBank.getTiles(0, 1, 2)));
			fail("Exception must be: tiles already on board");
		} catch (IncorrectTilesException ex) {
			assertEquals(IncorrectTilesException.Type.TILE_ALREADY_PLACED, ex.getType());
		}

		//place tile into busy cell
		try {
			board.makeTurn(board.getPlayerTurn(), new Word(new Position(7, 7), Direction.VERTICAL, tilesBank.getTiles(3, 4, 5)));
			fail("Exception must be: plate tile in busy cell");
		} catch (IncorrectTilesException ex) {
			assertEquals(IncorrectTilesException.Type.CELL_ALREADY_BUSY, ex.getType());
		}

		// no one from person
		try {
			board.makeTurn(board.getPlayerTurn(), new Word(new Position(7, 7), Direction.HORIZONTAL, tilesBank.getTiles(0, 1, 2)));
			fail("Exception must be: no one from person is taken");
		} catch (IncorrectTilesException ex) {
			assertEquals(IncorrectTilesException.Type.NO_HAND_TILES, ex.getType());
		}

		// not required in person
		try {
			board.makeTurn(board.getPlayerTurn(), new Word(new Position(9, 7), Direction.VERTICAL, tilesBank.getTiles(5, 6)));
			fail("Exception must be: not required in person");
		} catch (IncorrectTilesException ex) {
			assertEquals(IncorrectTilesException.Type.UNKNOWN_TILE, ex.getType());
		}

		// incorrect word place
		try {
			board.makeTurn(board.getPlayerTurn(), new Word(new Position(7, 7), Direction.VERTICAL, tilesBank.getTiles(5, 0)));
			fail("Exception must be: incorrect word place");
		} catch (GameMoveException ignore) {
		}

		// incorrect word place2
		board.getPlayerHand(board.getPlayerTurn()).setTiles(tilesBank.getTiles(7, 8, 9, 10, 11, 12));
		try {
			board.makeTurn(board.getPlayerTurn(), new Word(new Position(7, 9), Direction.HORIZONTAL, tilesBank.getTiles(2, 7, 8, 9, 10, 11, 12)));
			fail("Exception must be: incorrect word place");
		} catch (IncorrectPositionException ignore) {
		}

		//not in dictionary
		try {
			board.makeTurn(board.getPlayerTurn(), new Word(new Position(7, 7), Direction.VERTICAL, tilesBank.getTiles(0, 7)));
			fail("Exception must be: no in dictionary");
		} catch (UnknownWordException ignore) {
		}
	}

	@Test
	public void test_checkMove_Correct() throws GameMoveException {
		final Dictionary dictionary = createDictionary("abcd", "def", "fefgabcd");
		final TilesBank tilesBank = new TilesBank(editor.createTilesBankInfo());
		final ScribbleBoard board = new ScribbleBoard(settings, Arrays.asList(player1, player2, player3), tilesBank, dictionary);
		h1 = board.getPlayerHand(player1);
		h2 = board.getPlayerHand(player2);
		h3 = board.getPlayerHand(player3);

		h1.setTiles(tilesBank.getTiles(0, 3, 6, 9, 12, 15, 18)); //abcdefg
		h2.setTiles(tilesBank.getTiles(1, 4, 7, 10, 13, 16, 19));  //abcdefg
		h3.setTiles(tilesBank.getTiles(2, 5, 8, 11, 14, 17, 20));  //abcdefg

		Personality person = board.getPlayerTurn();
		ScribblePlayerHand hand = board.getPlayerHand(person);
		final Tile[] moveTiles1 = Arrays.copyOf(hand.getTiles(), 4);
		board.makeTurn(person, new Word(new Position(7, 7), Direction.HORIZONTAL, moveTiles1));  //abcd
		assertEquals(1, board.getGameMoves().size());
		assertEquals(9, tilesBank.getTilesLimit());
		assertEquals(7, hand.getTiles().length);
		assertEquals(10, hand.getPoints());

		person = board.getPlayerTurn();
		hand = board.getPlayerHand(person);
		final Tile[] moveTiles2 = new Tile[3];
		moveTiles2[0] = moveTiles1[3]; //last 'd' letter
		System.arraycopy(hand.getTiles(), 4, moveTiles2, 1, 2);
		board.makeTurn(person, new Word(new Position(7, 10), Direction.HORIZONTAL, moveTiles2)); //def
		assertEquals(2, board.getGameMoves().size());
		assertEquals(7, tilesBank.getTilesLimit());
		assertEquals(7, hand.getTiles().length);
		assertEquals(30, hand.getPoints());

		person = board.getPlayerTurn();
		hand = board.getPlayerHand(person);
		final Tile[] moveTiles3 = new Tile[8];
		moveTiles3[0] = moveTiles2[2];// last 'f' letter
		System.arraycopy(hand.getTiles(), 4, moveTiles3, 1, 3);
		System.arraycopy(hand.getTiles(), 0, moveTiles3, 4, 4);
		board.makeTurn(person, new Word(new Position(7, 12), Direction.VERTICAL, moveTiles3)); //fefgabcd

		assertEquals(3, board.getGameMoves().size());
		assertEquals(0, tilesBank.getTilesLimit());
		assertEquals(7, hand.getTiles().length);
		assertEquals(74, hand.getPoints()); // 6 + 5 + 6 + 7*2 + 1 + 2 + 3*2 + 4 +(30-all person) = 74
	}

	@Test
	public void test_checkMove_exchange() throws GameMoveException {
		final Dictionary dictionary = createDictionary("abcd", "def", "fefgabcd");
		final TilesBank tilesBank = new TilesBank(editor.createTilesBankInfo());
		final ScribbleBoard board = new ScribbleBoard(settings, Arrays.asList(player1, player2, player3), tilesBank, dictionary);
		h1 = board.getPlayerHand(player1);
		h2 = board.getPlayerHand(player2);
		h3 = board.getPlayerHand(player3);

		h1.setTiles(tilesBank.getTiles(0, 3, 6, 9, 12, 15, 18)); //abcdefg
		h2.setTiles(tilesBank.getTiles(1, 4, 7, 10, 13, 16, 19));  //abcdefg
		h3.setTiles(tilesBank.getTiles(2, 5, 8, 11, 14, 17, 20));  //abcdefg
		// roll all tiles back and request tiles from 0 to 20
		final int capacity = tilesBank.getBankCapacity();
		for (int i = 0; i < capacity; i++) {
			tilesBank.rollbackTile(i);
		}
		for (int i = 0; i < 21; i++) {
			tilesBank.requestTile(i);
		}

		assertEquals(13, tilesBank.getTilesLimit());

		final Personality person = board.getPlayerTurn();
		final ScribblePlayerHand hand = board.getPlayerHand(person);
		final Tile[] tiles = hand.getTiles().clone();
		board.exchangeTiles(person, new int[]{
				tiles[0].getNumber(), tiles[1].getNumber(), tiles[2].getNumber()
		});

		assertEquals(7, hand.getTiles().length);
		assertEquals(1, board.getGameMoves().size());
		assertEquals(ExchangeMove.class, board.getGameMoves().get(0).getClass());
		assertEquals(13, tilesBank.getTilesLimit());

		//Check that tiles was rolled back to bank
		assertFalse(tilesBank.isTileInUse(tiles[0].getNumber()));
		assertFalse(tilesBank.isTileInUse(tiles[1].getNumber()));
		assertFalse(tilesBank.isTileInUse(tiles[2].getNumber()));

		//Check that tiles with number > 20 is taken
		assertTrue(hand.getTiles()[0].getNumber() > 20);
		assertTrue(hand.getTiles()[1].getNumber() > 20);
		assertTrue(hand.getTiles()[2].getNumber() > 20);

		//Check no points
		assertEquals(0, hand.getPoints());
	}

	@Test
	public void test_checkMove_pass() throws GameMoveException {
		final Dictionary dictionary = createDictionary("abcd", "def", "fefgabcd");
		final TilesBank tilesBank = new TilesBank(editor.createTilesBankInfo());
		final ScribbleBoard board = new ScribbleBoard(settings, Arrays.asList(player1, player2, player3), tilesBank, dictionary);
		h1 = board.getPlayerHand(player1);
		h2 = board.getPlayerHand(player2);
		h3 = board.getPlayerHand(player3);

		h1.setTiles(tilesBank.getTiles(0, 3, 6, 9, 12, 15, 18)); //abcdefg
		h2.setTiles(tilesBank.getTiles(1, 4, 7, 10, 13, 16, 19));  //abcdefg
		h3.setTiles(tilesBank.getTiles(2, 5, 8, 11, 14, 17, 20));  //abcdefg

		final Personality person = board.getPlayerTurn();
		final ScribblePlayerHand hand = board.getPlayerHand(person);
		board.passTurn(person);

		assertEquals(7, hand.getTiles().length);
		assertEquals(1, board.getGameMoves().size());
		assertEquals(PassTurn.class, board.getGameMoves().get(0).getClass());
		assertEquals(13, tilesBank.getTilesLimit());

		assertNotSame(hand, board.getPlayerTurn());
	}

	@Test
	public void test_calculateMovePoints() throws GameMoveException {
		final Dictionary dictionary = createDictionary("eeea");
		final TilesBank tilesBank = createTilesBank("abcdefgabcdefgabcdefg", 19);

		final ScribbleBoard board = new ScribbleBoard(settings, Arrays.asList(player1, player2, player3), tilesBank, dictionary);
		h1 = board.getPlayerHand(player1);
		h2 = board.getPlayerHand(player2);
		h3 = board.getPlayerHand(player3);

		h1.setTiles(tilesBank.getTiles(0, 1, 2, 3, 4, 5, 6));
		h2.setTiles(tilesBank.getTiles(7, 8, 9, 10, 11, 12, 13));
		h2.setTiles(tilesBank.getTiles(14, 15, 16, 17, 18, 19, 20));
		Personality person = board.getPlayerTurn();

		final MakeTurn makeTurn = board.makeTurn(person, new Word(new Position(7, 7), Direction.HORIZONTAL, tilesBank.getTiles(0, 1, 2, 3)));
		assertEquals(4, makeTurn.getPoints());
	}

	@Test
	public void test_processMoveFinished() throws GameMoveException {
		final Dictionary dictionary = createDictionary("eeea");
		final TilesBank tilesBank = createTilesBank("abcdefgabcdefgabcdefg", 19);

		final ScribbleBoard board = new ScribbleBoard(settings, Arrays.asList(player1, player2, player3), tilesBank, dictionary);
		h1 = board.getPlayerHand(player1);
		h2 = board.getPlayerHand(player2);
		h3 = board.getPlayerHand(player3);

		h1.setTiles(tilesBank.getTiles(0, 1, 2, 3, 4, 5, 6));
		h2.setTiles(tilesBank.getTiles(7, 8, 9, 10, 11, 12, 13));
		h2.setTiles(tilesBank.getTiles(14, 15, 16, 17, 18, 19, 20));
		Personality person = board.getPlayerTurn();
		board.makeTurn(person, new Word(new Position(7, 7), Direction.HORIZONTAL, tilesBank.getTiles(0, 1, 2, 3)));
		assertTrue(board.isBoardTile(0));
		assertTrue(board.isBoardTile(1));
		assertTrue(board.isBoardTile(2));
		assertTrue(board.isBoardTile(3));
	}

	@Test
	public void test_isGameFinished() {
		final Dictionary dictionary = createDictionary();
		final TilesBank tilesBank = createTilesBank("abcdefgabcdefgabcdefg", 19);

		final ScribbleBoard board = new ScribbleBoard(settings, Arrays.asList(player1, player2, player3), tilesBank, dictionary);
		h1 = board.getPlayerHand(player1);
		h2 = board.getPlayerHand(player2);
		h3 = board.getPlayerHand(player3);

		//not finished
		assertFalse(board.isGameFinished());

		h2.setTiles(new Tile[0]);
		assertFalse(board.isGameFinished());

		tilesBank.requestTiles(tilesBank.getTilesLimit()); //now bank is empty

		// Players have a letters
		h2.setTiles(tilesBank.getTiles(0, 1, 2));
		assertFalse(board.isGameFinished());

		h2.setTiles(new Tile[0]);
		assertTrue(board.isGameFinished());
	}

	@Test
	public void test_processGameFinished_noTiles() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		final Dictionary dictionary = createDictionary();
		final TilesBank tilesBank = new TilesBank(editor.createTilesBankInfo());

		final ScribbleBoard board = new ScribbleBoard(settings, Arrays.asList(player1, player2, player3), tilesBank, dictionary);
		h1 = board.getPlayerHand(player1);
		h2 = board.getPlayerHand(player2);
		h3 = board.getPlayerHand(player3);

		h1.setTiles(tilesBank.getTiles(0, 3, 6, 9));// abcd: 10 points
		h2.setTiles(tilesBank.getTiles(9, 12, 15, 18));//defg: 22 points
		h3.setTiles(new Tile[0]); // winner

		short[] ints = board.processGameFinished();
		assertEquals(3, ints.length);

		assertEquals(-10, ints[0]); // final points for h1: looser
		assertEquals(-22, ints[1]); // final points for h2: looser
		assertEquals(32, ints[2]);  // final points for h3: winned
	}

	@Test
	public void test_processGameFinished_haveTiles() throws Exception {
		final Dictionary dictionary = createDictionary();
		final TilesBank tilesBank = new TilesBank(editor.createTilesBankInfo());

		final ScribbleBoard board = new ScribbleBoard(settings, Arrays.asList(player1, player2, player3), tilesBank, dictionary);
		h1 = board.getPlayerHand(player1);
		h2 = board.getPlayerHand(player2);
		h3 = board.getPlayerHand(player3);

		h1.setTiles(tilesBank.getTiles(0, 3, 6, 9));// abcd: 10 points - should be winner
		h2.setTiles(tilesBank.getTiles(9, 12, 15, 18));//defg: 22 points
		h3.setTiles(tilesBank.getTiles(16, 17, 19, 20)); // ffgg: 26 points

		//we need increate player points to make it winner.
		final Method method = h3.getClass().getSuperclass().getDeclaredMethod("increasePoints", short.class);
		method.setAccessible(true);
		method.invoke(h3, (short) 1); // Player h3 has 1 points and other have 0 points.

		short[] ints = board.processGameFinished();
		assertEquals(3, ints.length);

		assertEquals(-10, ints[0]); // final points for h1: looser
		assertEquals(-22, ints[1]); // final points for h2: looser
		assertEquals(-26, ints[2]);  // final points for h3: winned
	}

	private Dictionary createDictionary(String... valid) {
		Dictionary dictionary = createMock(Dictionary.class);
		for (String word : valid) {
			if (word != null) {
				expect(dictionary.contains(word)).andReturn(true);
			} else {
				expect(dictionary.contains(isA(String.class))).andReturn(false);
			}
		}
		replay(dictionary);
		return dictionary;
	}

	private TilesBank createTilesBank(String tiles, int stubChars) {
		final TilesBankInfoEditor editor = new TilesBankInfoEditor(Language.EN);
		String s = "*qwertyuiopasdfghjklzxcvbnm";
		for (char c : s.toCharArray()) {
			int count = 0;
			int index = -1;
			while ((index = tiles.indexOf(c, index + 1)) != -1) {
				count++;
			}
			if (count != 0) {
				final int cost = (c == '*' ? 0 : 1);
				editor.add(c, count, cost);
			}
		}
		editor.add('z', stubChars, 1);
		return new TilesBank(editor.createTilesBankInfo());
	}

	private Tile getTile(TilesBank bank, char ch, int pos) {
		int index = 0;
		final int bankCapacity = bank.getBankCapacity();
		for (int i = 0; i < bankCapacity; i++) {
			final Tile tile = bank.getTile(i);
			if ((ch == '*' && tile.getCost() == 0) || (ch != '*' && tile.getLetter() == ch && tile.getCost() != 0)) {
				if (pos == index) {
					return tile;
				} else {
					index++;
				}
			}
		}
		throw new IllegalArgumentException("Unknown tile: " + ch + "[" + pos + "]");
	}
}
