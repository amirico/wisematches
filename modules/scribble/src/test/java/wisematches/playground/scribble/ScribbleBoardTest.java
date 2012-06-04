package wisematches.playground.scribble;

import junit.framework.TestCase;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.GameMove;
import wisematches.playground.GameMoveException;
import wisematches.playground.GameMoveScore;
import wisematches.playground.PassTurnMove;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryNotFoundException;
import wisematches.playground.scribble.bank.TilesBank;
import wisematches.playground.scribble.bank.impl.TilesBankInfoEditor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import static org.easymock.EasyMock.*;
import static wisematches.playground.scribble.ScribbleBoard.LETTERS_IN_HAND;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleBoardTest extends TestCase {
	private ScribbleSettings settings;

	private ScribblePlayerHand h1;
	private ScribblePlayerHand h2;
	private ScribblePlayerHand h3;

	private final TilesBankInfoEditor editor = new TilesBankInfoEditor(Language.EN);

	private static final Locale LOCALE = new Locale("en");

	public ScribbleBoardTest() {
	}

	protected void setUp() throws Exception {
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

	public void test_initializeBoard() {
		final Dictionary dict = createDictionary();
		final TilesBank tilesBank = new TilesBank(new TilesBankInfoEditor(Language.EN).add('a', 103, 1).createTilesBankInfo());

		final ScribbleBoard board = new ScribbleBoard(settings,
				Arrays.asList(Personality.person(1), Personality.person(2), Personality.person(3)), tilesBank, dict);
		h1 = board.getPlayerHand(1);
		h2 = board.getPlayerHand(2);
		h3 = board.getPlayerHand(3);

		assertEquals(LETTERS_IN_HAND, h1.getTiles().length);
		assertEquals(LETTERS_IN_HAND, h2.getTiles().length);
		assertEquals(LETTERS_IN_HAND, h3.getTiles().length);
		assertNull(board.getGameResolution());
	}

	public void test_smallGamePlay() throws GameMoveException {
		final Dictionary dictionary = createDictionary("abcde", "qrdts", "skel");
		final TilesBank tilesBank = createTilesBank("abcd*qrt*lkelt", 19);

		final ScribbleBoard board = new ScribbleBoard(settings,
				Arrays.asList(Personality.person(1), Personality.person(2), Personality.person(3)), tilesBank, dictionary);
		h1 = board.getPlayerHand(1);
		h2 = board.getPlayerHand(2);
		h3 = board.getPlayerHand(3);

		assertEquals(7, h1.getTiles().length);
		assertEquals(7, h2.getTiles().length);
		assertEquals(7, h3.getTiles().length);
		assertEquals(12, tilesBank.getTilesLimit());

		ScribblePlayerHand hand = board.getPlayerTurn();
		hand.setTiles(new Tile[]{
				getTile(tilesBank, 'a', 0),
				getTile(tilesBank, 'b', 0),
				getTile(tilesBank, 'c', 0),
				getTile(tilesBank, 'd', 0),
				getTile(tilesBank, '*', 0),
				getTile(tilesBank, 'z', 0),
				getTile(tilesBank, 'z', 1)
		});

		board.makeMove(new MakeWordMove(hand.getPlayerId(), new Word(new Position(4, 7), Direction.VERTICAL,
				getTile(tilesBank, 'a', 0),
				getTile(tilesBank, 'b', 0),
				getTile(tilesBank, 'c', 0),
				getTile(tilesBank, 'd', 0),
				getTile(tilesBank, '*', 0).redefine('e')
		)));
		assertEquals(7, hand.getTiles().length);
		assertEquals(7, tilesBank.getTilesLimit());

		hand = board.getPlayerTurn();
		hand.setTiles(new Tile[]{
				getTile(tilesBank, 'q', 0),
				getTile(tilesBank, 'r', 0),
				getTile(tilesBank, 't', 0),
				getTile(tilesBank, '*', 1),
				getTile(tilesBank, 'l', 0),
				getTile(tilesBank, 'z', 2),
				getTile(tilesBank, 'z', 3)
		});

		board.makeMove(new MakeWordMove(hand.getPlayerId(), new Word(new Position(7, 5), Direction.HORIZONTAL,
				getTile(tilesBank, 'q', 0),
				getTile(tilesBank, 'r', 0),
				getTile(tilesBank, 'd', 0),
				getTile(tilesBank, 't', 0),
				getTile(tilesBank, '*', 1).redefine('s')
		)));
		assertEquals(7, hand.getTiles().length);
		assertEquals(3, tilesBank.getTilesLimit());

		hand = board.getPlayerTurn();
		hand.setTiles(new Tile[]{
				getTile(tilesBank, 'k', 0),
				getTile(tilesBank, 'e', 0),
				getTile(tilesBank, 'l', 1),
				getTile(tilesBank, 'z', 4),
				getTile(tilesBank, 'z', 5),
				getTile(tilesBank, 'z', 6),
				getTile(tilesBank, 'z', 7)
		});
		board.makeMove(new MakeWordMove(hand.getPlayerId(), new Word(new Position(7, 9), Direction.VERTICAL,
				getTile(tilesBank, '*', 1).redefine('s'),
				getTile(tilesBank, 'k', 0),
				getTile(tilesBank, 'e', 0),
				getTile(tilesBank, 'l', 1)
		)));
		assertEquals(7, hand.getTiles().length);
		assertEquals(0, tilesBank.getTilesLimit());

		assertTrue(tilesBank.isEmpty());
	}

	public void test_checkState() throws GameMoveException {
		final Dictionary dictionary = createDictionary();
		final TilesBank tilesBank = createTilesBank("", 19);

		final ScribbleBoard board = new ScribbleBoard(settings,
				Arrays.asList(Personality.person(1), Personality.person(2), Personality.person(3)), tilesBank, dictionary);
		board.checkState();
	}

	public void test_checkMove_Incorrect() throws GameMoveException, DictionaryNotFoundException {
		final Dictionary dictionary = createDictionary("aad");
		final TilesBank tilesBank = createTilesBank("abcabcd", 19);

		final ScribbleBoard board = new ScribbleBoard(settings,
				Arrays.asList(Personality.person(1), Personality.person(2), Personality.person(3)), tilesBank, dictionary);
		h1 = board.getPlayerHand(1);
		h2 = board.getPlayerHand(2);
		h3 = board.getPlayerHand(3);

		//=============== first move not in center 1
		ScribblePlayerHand hand = board.getPlayerTurn();
		MakeWordMove move = new MakeWordMove(hand.getPlayerId(),
				new Word(new Position(0, 0), Direction.HORIZONTAL, tilesBank.getTiles(0, 1, 2)));
		try {
			board.checkMove(move);
			fail("Exception must be: move in not center");
		} catch (IncorrectPositionException ex) {
			assertTrue(ex.isMustBeInCenter());
		}

		//=============== first move not in center 2
		hand = board.getPlayerTurn();
		move = new MakeWordMove(hand.getPlayerId(),
				new Word(new Position(5, 7), Direction.HORIZONTAL, tilesBank.getTiles(0, 1, 2)));
		try {
			board.checkMove(move);
			fail("Exception must be: move in not center");
		} catch (IncorrectPositionException ex) {
			assertTrue(ex.isMustBeInCenter());
		}

		//=============== first move not in center 3
		hand = board.getPlayerTurn();
		move = new MakeWordMove(hand.getPlayerId(),
				new Word(new Position(7, 5), Direction.VERTICAL, tilesBank.getTiles(0, 1, 2)));
		try {
			board.checkMove(move);
			fail("Exception must be: move in not center");
		} catch (IncorrectPositionException ex) {
			assertTrue(ex.isMustBeInCenter());
		}

		//=============== not any on the board
		hand.setTiles(tilesBank.getTiles(0, 1, 2));
		board.makeMove(new MakeWordMove(hand.getPlayerId(),
				new Word(new Position(7, 7), Direction.HORIZONTAL, tilesBank.getTiles(0, 1, 2))));
		assertEquals(1, board.getGameMoves().size());

		hand = board.getPlayerTurn();
		hand.setTiles(tilesBank.getTiles(3, 4, 5));
		try {
			board.makeMove(new MakeWordMove(hand.getPlayerId(),
					new Word(new Position(8, 7), Direction.HORIZONTAL, tilesBank.getTiles(3, 4, 5))));
			fail("Exception must be: no one letter from board is taken");
		} catch (IncorrectTilesException ex) {
			assertEquals(IncorrectTilesException.Type.NO_BOARD_TILES, ex.getType());
		}

		//place tile that already on board
		try {
			board.makeMove(new MakeWordMove(hand.getPlayerId(),
					new Word(new Position(7, 7), Direction.VERTICAL, tilesBank.getTiles(0, 1, 2))));
			fail("Exception must be: tiles already on board");
		} catch (IncorrectTilesException ex) {
			assertEquals(IncorrectTilesException.Type.TILE_ALREADY_PLACED, ex.getType());
		}

		//place tile into busy cell
		try {
			board.makeMove(new MakeWordMove(hand.getPlayerId(),
					new Word(new Position(7, 7), Direction.VERTICAL, tilesBank.getTiles(3, 4, 5))));
			fail("Exception must be: plate tile in busy cell");
		} catch (IncorrectTilesException ex) {
			assertEquals(IncorrectTilesException.Type.CELL_ALREADY_BUSY, ex.getType());
		}

		// no one from hand
		try {
			board.makeMove(new MakeWordMove(hand.getPlayerId(),
					new Word(new Position(7, 7), Direction.HORIZONTAL, tilesBank.getTiles(0, 1, 2))));
			fail("Exception must be: no one from hand is taken");
		} catch (IncorrectTilesException ex) {
			assertEquals(IncorrectTilesException.Type.NO_HAND_TILES, ex.getType());
		}

		// not required in hand
		try {
			board.makeMove(new MakeWordMove(hand.getPlayerId(),
					new Word(new Position(9, 7), Direction.VERTICAL, tilesBank.getTiles(5, 6))));
			fail("Exception must be: not required in hand");
		} catch (IncorrectTilesException ex) {
			assertEquals(IncorrectTilesException.Type.UNKNOWN_TILE, ex.getType());
		}

		// incorrect word place
		try {
			board.makeMove(new MakeWordMove(hand.getPlayerId(),
					new Word(new Position(7, 7), Direction.VERTICAL, tilesBank.getTiles(5, 0))));
			fail("Exception must be: incorrect word place");
		} catch (GameMoveException ignore) {
		}

		// incorrect word place2
		hand.setTiles(tilesBank.getTiles(7, 8, 9, 10, 11, 12));
		try {
			board.makeMove(new MakeWordMove(hand.getPlayerId(),
					new Word(new Position(7, 9), Direction.HORIZONTAL, tilesBank.getTiles(2, 7, 8, 9, 10, 11, 12))));
			fail("Exception must be: incorrect word place");
		} catch (IncorrectPositionException ignore) {
		}

		//not in dictionary
		board.setDictionary(createDictionary(new String[]{null}));
		try {
			board.makeMove(new MakeWordMove(hand.getPlayerId(),
					new Word(new Position(7, 7), Direction.VERTICAL, tilesBank.getTiles(0, 7))));
			fail("Exception must be: no in dictionary");
		} catch (UnknownWordException ignore) {
		}
	}

	public void test_checkMove_Correct() throws GameMoveException, DictionaryNotFoundException {
		final Dictionary dictionary = createDictionary("abcd", "def", "fefgabcd");

		final TilesBank tilesBank = new TilesBank(editor.createTilesBankInfo());
		final ScribbleBoard board = new ScribbleBoard(settings,
				Arrays.asList(Personality.person(1), Personality.person(2), Personality.person(3)), tilesBank, dictionary);

		h1 = board.getPlayerHand(1);
		h2 = board.getPlayerHand(2);
		h3 = board.getPlayerHand(3);

		h1.setTiles(tilesBank.getTiles(0, 3, 6, 9, 12, 15, 18)); //abcdefg
		h2.setTiles(tilesBank.getTiles(1, 4, 7, 10, 13, 16, 19));  //abcdefg
		h3.setTiles(tilesBank.getTiles(2, 5, 8, 11, 14, 17, 20));  //abcdefg

		ScribblePlayerHand hand = board.getPlayerTurn();
		final Tile[] moveTiles1 = Arrays.copyOf(hand.getTiles(), 4);
		board.makeMove(new MakeWordMove(hand.getPlayerId(),
				new Word(new Position(7, 7), Direction.HORIZONTAL, moveTiles1))  //abcd
		);
		assertEquals(1, board.getGameMoves().size());
		assertEquals(9, tilesBank.getTilesLimit());
		assertEquals(7, hand.getTiles().length);
		assertEquals(10, hand.getPoints());

		hand = board.getPlayerTurn();
		final Tile[] moveTiles2 = new Tile[3];
		moveTiles2[0] = moveTiles1[3]; //last 'd' letter
		System.arraycopy(hand.getTiles(), 4, moveTiles2, 1, 2);
		board.makeMove(new MakeWordMove(hand.getPlayerId(),
				new Word(new Position(7, 10), Direction.HORIZONTAL, moveTiles2)) //def
		);
		assertEquals(2, board.getGameMoves().size());
		assertEquals(7, tilesBank.getTilesLimit());
		assertEquals(7, hand.getTiles().length);
		assertEquals(30, hand.getPoints());

		hand = board.getPlayerTurn();
		final Tile[] moveTiles3 = new Tile[8];
		moveTiles3[0] = moveTiles2[2];// last 'f' letter
		System.arraycopy(hand.getTiles(), 4, moveTiles3, 1, 3);
		System.arraycopy(hand.getTiles(), 0, moveTiles3, 4, 4);
		board.makeMove(new MakeWordMove(hand.getPlayerId(),
				new Word(new Position(7, 12), Direction.VERTICAL, moveTiles3)) //fefgabcd
		);
		assertEquals(3, board.getGameMoves().size());
		assertEquals(0, tilesBank.getTilesLimit());
		assertEquals(7, hand.getTiles().length);
		assertEquals(74, hand.getPoints()); // 6 + 5 + 6 + 7*2 + 1 + 2 + 3*2 + 4 +(30-all hand) = 74
	}

	public void test_checkMove_exchange() throws GameMoveException {
		final Dictionary dictionary = createDictionary("abcd", "def", "fefgabcd");
		final TilesBank tilesBank = new TilesBank(editor.createTilesBankInfo());
		final ScribbleBoard board = new ScribbleBoard(settings,
				Arrays.asList(Personality.person(1), Personality.person(2), Personality.person(3)), tilesBank, dictionary);

		h1 = board.getPlayerHand(1);
		h2 = board.getPlayerHand(2);
		h3 = board.getPlayerHand(3);

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

		final ScribblePlayerHand hand = board.getPlayerTurn();
		final Tile[] tiles = hand.getTiles().clone();
		board.makeMove(new ExchangeTilesMove(hand.getPlayerId(), new int[]{
				tiles[0].getNumber(), tiles[1].getNumber(), tiles[2].getNumber()
		}));

		assertEquals(7, hand.getTiles().length);
		assertEquals(1, board.getGameMoves().size());
		assertEquals(ExchangeTilesMove.class, board.getGameMoves().get(0).getPlayerMove().getClass());
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

	public void test_checkMove_pass() throws GameMoveException {
		final Dictionary dictionary = createDictionary("abcd", "def", "fefgabcd");
		final TilesBank tilesBank = new TilesBank(editor.createTilesBankInfo());
		final ScribbleBoard board = new ScribbleBoard(settings,
				Arrays.asList(Personality.person(1), Personality.person(2), Personality.person(3)), tilesBank, dictionary);

		h1 = board.getPlayerHand(1);
		h2 = board.getPlayerHand(2);
		h3 = board.getPlayerHand(3);

		h1.setTiles(tilesBank.getTiles(0, 3, 6, 9, 12, 15, 18)); //abcdefg
		h2.setTiles(tilesBank.getTiles(1, 4, 7, 10, 13, 16, 19));  //abcdefg
		h3.setTiles(tilesBank.getTiles(2, 5, 8, 11, 14, 17, 20));  //abcdefg

		final ScribblePlayerHand hand = board.getPlayerTurn();
		board.makeMove(new PassTurnMove(hand.getPlayerId()));

		assertEquals(7, hand.getTiles().length);
		assertEquals(1, board.getGameMoves().size());
		assertEquals(PassTurnMove.class, board.getGameMoves().get(0).getPlayerMove().getClass());
		assertEquals(13, tilesBank.getTilesLimit());

		assertNotSame(hand, board.getPlayerTurn());
	}

	public void test_calculateMovePoints() {
		final Dictionary dictionary = createDictionary();
		final TilesBank tilesBank = createTilesBank("abcdefgabcdefgabcdefg", 19);

		final ScribbleBoard board = new ScribbleBoard(settings,
				Arrays.asList(Personality.person(1), Personality.person(2), Personality.person(3)), tilesBank, dictionary);

		h1 = board.getPlayerHand(1);
		h2 = board.getPlayerHand(2);
		h3 = board.getPlayerHand(3);

		h1.setTiles(tilesBank.getTiles(0, 1, 2, 3, 4, 5, 6));
		h2.setTiles(tilesBank.getTiles(7, 8, 9, 10, 11, 12, 13));
		h2.setTiles(tilesBank.getTiles(14, 15, 16, 17, 18, 19, 20));
		ScribblePlayerHand hand = board.getPlayerTurn();

		final MakeWordMove wordMove = new MakeWordMove(hand.getPlayerId(), new Word(new Position(7, 7), Direction.HORIZONTAL, tilesBank.getTiles(0, 1, 2, 3)));
		final GameMoveScore points = board.calculateMoveScores(wordMove);
		assertEquals(4, points.getPoints());
	}

	public void test_processMoveFinished() {
		final Dictionary dictionary = createDictionary();
		final TilesBank tilesBank = createTilesBank("abcdefgabcdefgabcdefg", 19);

		final ScribbleBoard board = new ScribbleBoard(settings,
				Arrays.asList(Personality.person(1), Personality.person(2), Personality.person(3)), tilesBank, dictionary);

		h1 = board.getPlayerHand(1);
		h2 = board.getPlayerHand(2);
		h3 = board.getPlayerHand(3);

		h1.setTiles(tilesBank.getTiles(0, 1, 2, 3, 4, 5, 6));
		h2.setTiles(tilesBank.getTiles(7, 8, 9, 10, 11, 12, 13));
		h2.setTiles(tilesBank.getTiles(14, 15, 16, 17, 18, 19, 20));
		ScribblePlayerHand hand = board.getPlayerTurn();

		final MakeWordMove wordMove = new MakeWordMove(hand.getPlayerId(), new Word(new Position(7, 7), Direction.HORIZONTAL, tilesBank.getTiles(0, 1, 2, 3)));
		board.processMoveFinished(hand, new GameMove(wordMove, 0, 0, new Date()));
		assertTrue(board.isBoardTile(0));
		assertTrue(board.isBoardTile(1));
		assertTrue(board.isBoardTile(2));
		assertTrue(board.isBoardTile(3));
	}

	public void test_checkGameFinished() {
		final Dictionary dictionary = createDictionary();
		final TilesBank tilesBank = createTilesBank("abcdefgabcdefgabcdefg", 19);

		final ScribbleBoard board = new ScribbleBoard(settings,
				Arrays.asList(Personality.person(1), Personality.person(2), Personality.person(3)), tilesBank, dictionary);

		h1 = board.getPlayerHand(1);
		h2 = board.getPlayerHand(2);
		h3 = board.getPlayerHand(3);

		//not finished
		assertFalse(board.checkGameFinished());

		h2.setTiles(new Tile[0]);
		assertFalse(board.checkGameFinished());

		tilesBank.requestTiles(tilesBank.getTilesLimit()); //now bank is empty

		// Players have a letters
		h2.setTiles(tilesBank.getTiles(0, 1, 2));
		assertFalse(board.checkGameFinished());

		h2.setTiles(new Tile[0]);
		assertTrue(board.checkGameFinished());
	}

	public void test_processGameFinished_noTiles() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		final Dictionary dictionary = createDictionary();
		final TilesBank tilesBank = new TilesBank(editor.createTilesBankInfo());

		final ScribbleBoard board = new ScribbleBoard(settings,
				Arrays.asList(Personality.person(1), Personality.person(2), Personality.person(3)), tilesBank, dictionary);

		h1 = board.getPlayerHand(1);
		h2 = board.getPlayerHand(2);
		h3 = board.getPlayerHand(3);

		h1.setTiles(tilesBank.getTiles(0, 3, 6, 9));// abcd: 10 points
		h2.setTiles(tilesBank.getTiles(9, 12, 15, 18));//defg: 22 points
		h3.setTiles(new Tile[0]); // winner

		short[] ints = board.processGameFinished();
		assertEquals(3, ints.length);

		assertEquals(-10, ints[0]); // final points for h1: looser
		assertEquals(-22, ints[1]); // final points for h2: looser
		assertEquals(32, ints[2]);  // final points for h3: winned
	}

	/**
	 * If each player has tiles in hand its points should be decreased from final score and no one take
	 * a final bonus.
	 *
	 * @throws Exception if test failed.
	 */
	public void test_processGameFinished_haveTiles() throws Exception {
		final Dictionary dictionary = createDictionary();
		final TilesBank tilesBank = new TilesBank(editor.createTilesBankInfo());

		final ScribbleBoard board = new ScribbleBoard(settings,
				Arrays.asList(Personality.person(1), Personality.person(2), Personality.person(3)), tilesBank, dictionary);

		h1 = board.getPlayerHand(1);
		h2 = board.getPlayerHand(2);
		h3 = board.getPlayerHand(3);

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

	private Dictionary createDictionary(String... words) {
		Dictionary dictionary = createMock(Dictionary.class);
		expect(dictionary.getLocale()).andReturn(LOCALE);
		for (String word : words) {
			if (word != null) {
				expect(dictionary.getWord(word)).andReturn(new wisematches.playground.dictionary.Word(word, LOCALE));
			} else {
				expect(dictionary.getWord(isA(CharSequence.class))).andReturn(null);
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
