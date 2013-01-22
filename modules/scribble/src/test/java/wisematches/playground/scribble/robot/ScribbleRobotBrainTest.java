package wisematches.playground.scribble.robot;

import org.easymock.IAnswer;
import org.junit.Test;
import wisematches.core.Language;
import wisematches.core.personality.proprietary.robot.RobotType;
import wisematches.playground.GameMove;
import wisematches.playground.GameMoveException;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryException;
import wisematches.playground.dictionary.WordEntry;
import wisematches.playground.dictionary.impl.FileDictionary;
import wisematches.playground.scribble.*;
import wisematches.playground.scribble.score.ScoreBonus;
import wisematches.playground.scribble.score.ScoreEngine;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleRobotBrainTest {
	private Word w1 = new Word(new Position(7, 7), Direction.VERTICAL, new Tile(1, 'a', 1), new Tile(2, 'b', 2));
	private Word w2 = new Word(new Position(8, 8), Direction.VERTICAL, new Tile(1, 'a', 1), new Tile(2, 'b', 2), new Tile(3, 'c', 3));
	private Word w3 = new Word(new Position(9, 9), Direction.VERTICAL, new Tile(1, 'a', 1), new Tile(2, 'b', 2));
	private Word w4 = new Word(new Position(10, 10), Direction.VERTICAL, new Tile(1, 'a', 1), new Tile(2, 'b', 2));

	public ScribbleRobotBrainTest() {
	}

	@Test
	public void test_selectResultWordDull() {
		final ScribbleRobotBrain brain = new ScribbleRobotBrain();
		final Word word = brain.selectResultWord(Arrays.asList(w1, w2, w3, w4), RobotType.DULL, null, null);
		assertNotNull(word);
	}

	@Test
	public void test_selectResultWordStager() {
		final ScribbleRobotBrain brain = new ScribbleRobotBrain();
		final Word word = brain.selectResultWord(Arrays.asList(w1, w2, w3, w4), RobotType.TRAINEE, null, null);
		assertSame(w2, word);
	}

	@Test
	public void test_selectResultWordExceprt() {
		final TilesPlacement tilesPlacement = createNiceMock(TilesPlacement.class);

		final ScribbleMoveScore c1 = new ScribbleMoveScore((short) 10, false, ScoreBonus.Type.values(), "asd");
		final ScribbleMoveScore c2 = new ScribbleMoveScore((short) 20, false, ScoreBonus.Type.values(), "asd");
		final ScribbleMoveScore c3 = new ScribbleMoveScore((short) 30, false, ScoreBonus.Type.values(), "asd");
		final ScribbleMoveScore c4 = new ScribbleMoveScore((short) 5, false, ScoreBonus.Type.values(), "asd");

		final ScoreEngine scoreEngine = createStrictMock(ScoreEngine.class);
		expect(scoreEngine.calculateWordScore(w1, tilesPlacement)).andReturn(c1);
		expect(scoreEngine.calculateWordScore(w2, tilesPlacement)).andReturn(c2);
		expect(scoreEngine.calculateWordScore(w3, tilesPlacement)).andReturn(c3);
		expect(scoreEngine.calculateWordScore(w4, tilesPlacement)).andReturn(c4);
		replay(scoreEngine);

		final ScribbleRobotBrain brain = new ScribbleRobotBrain();
		final Word word = brain.selectResultWord(Arrays.asList(w1, w2, w3, w4), RobotType.EXPERT, scoreEngine, tilesPlacement);
		assertSame(w3, word);

		verify(scoreEngine);
	}

	@Test
	public void test_analizeValidMoves() {
		// 'hello' word placed horizontally at position (7;5)
		final List<WorkTile> workTiles = Arrays.asList(
				new WorkTile(new Tile(1, 'h', 1), new Position(7, 5)),
				new WorkTile(new Tile(2, 'e', 1), new Position(7, 6)),
				new WorkTile(new Tile(3, 'l', 1), new Position(7, 7)),
				new WorkTile(new Tile(4, 'l', 1), new Position(7, 8)),
				new WorkTile(new Tile(5, 'o', 1), new Position(7, 9))
		);

		final TilesPlacement board = new MockTilesPlacement(workTiles);

		final ScribblePlayerHand hand = new ScribblePlayerHand(1L, new Tile[]{
				new Tile(6, 'a', 1),
				new Tile(7, 'b', 1),
				new Tile(8, 'c', 1),
				new Tile(9, 'd', 1),
				new Tile(10, '*', 0),
		});

		//
		final Collection<WordEntry> words = Arrays.asList(
				new WordEntry("aheh"), new WordEntry("ahehh"), new WordEntry("ahed"),
				new WordEntry("ahedc"), new WordEntry("load"));

		final Dictionary dictionary = createMock(Dictionary.class);
		expect(dictionary.getWordEntries()).andReturn(words);
		replay(dictionary);

		final ScribbleRobotBrain brain = new ScribbleRobotBrain();
		final List<Word> list = brain.analyzeValidWords(board, hand, RobotType.DULL, dictionary);
		assertEquals(10, list.size()); //two 'ahed' and six 'load' can be placed.
	}

	@Test
	public void test_putInAction() throws GameMoveException {
		// 'hello' word placed horizontally at position (7;5)
		final List<WorkTile> workTiles = Arrays.asList(
				new WorkTile(new Tile(1, 'h', 1), new Position(7, 5)),
				new WorkTile(new Tile(2, 'e', 1), new Position(7, 6)),
				new WorkTile(new Tile(3, 'l', 1), new Position(7, 7)),
				new WorkTile(new Tile(4, 'l', 1), new Position(7, 8)),
				new WorkTile(new Tile(5, 'o', 1), new Position(7, 9))
		);

		final TilesPlacement board = new MockTilesPlacement(workTiles);

		final Collection<WordEntry> words = Arrays.asList(
				new WordEntry("ahed"));

		final Dictionary dictionary = createMock(Dictionary.class);
		expect(dictionary.getWordEntries()).andReturn(words);
		replay(dictionary);

		final ScribblePlayerHand hand = new ScribblePlayerHand(1L, new Tile[]{
				new Tile(6, 'a', 1),
				new Tile(9, 'd', 1),
				new Tile(10, 'e', 1),
		});

		final ScribbleBoard scribbleBoard = createStrictMock(ScribbleBoard.class);
		expect(scribbleBoard.isActive()).andReturn(true);
		expect(scribbleBoard.getBoardId()).andReturn(1L);
		expect(scribbleBoard.getPlayerTurn()).andReturn(hand);
		expect(scribbleBoard.getDictionary()).andReturn(dictionary);
		expect(scribbleBoard.getBoardTile(anyInt(), anyInt())).andAnswer(new IAnswer<Tile>() {
			public Tile answer() throws Throwable {
				return board.getBoardTile((Integer) getCurrentArguments()[0], (Integer) getCurrentArguments()[1]);
			}
		}).anyTimes();
		expect(scribbleBoard.getScoreEngine()).andReturn(null);
		expect(scribbleBoard.makeMove(isA(MakeTurn.class))).andReturn(new GameMove(createMock(PlayerMove.class), 12, 1, new Date()));
		expect(scribbleBoard.isActive()).andReturn(false);
		replay(scribbleBoard);

		final ScribbleRobotBrain brain = new ScribbleRobotBrain();
		brain.putInAction(scribbleBoard, RobotType.TRAINEE);
		brain.putInAction(scribbleBoard, RobotType.TRAINEE); // no action - finished

		verify(scribbleBoard);
	}

	/**
	 * This test doesn't check anything. It just calculate time of move for robot for random and sorted dictionary.
	 */
	@Test
	public void test_calculateSearchTime() throws IOException, DictionaryException {
		final Set<WordEntry> hashSet = new HashSet<>();
		final Set<WordEntry> treeSet = new TreeSet<>();

		FileDictionary fd = new FileDictionary(Language.EN, new File("../../resources/dictionaries/en.dic"));
		for (WordEntry entry : fd.getWordEntries()) {
			treeSet.add(entry);
			hashSet.add(entry);
		}

		//
		final Dictionary dictionary = createMock(Dictionary.class);

		// 'hello' word placed horizontally at position (7;5)
		final List<WorkTile> workTiles = Arrays.asList(
				new WorkTile(new Tile(1, 'h', 1), new Position(7, 5)),
				new WorkTile(new Tile(2, 'e', 1), new Position(7, 6)),
				new WorkTile(new Tile(3, 'l', 1), new Position(7, 7)),
				new WorkTile(new Tile(4, 'l', 1), new Position(7, 8)),
				new WorkTile(new Tile(5, 'o', 1), new Position(7, 9)),

				new WorkTile(new Tile(11, 'h', 1), new Position(0, 1)),
				new WorkTile(new Tile(12, 'e', 1), new Position(0, 2)),
				new WorkTile(new Tile(13, 'l', 1), new Position(0, 3)),
				new WorkTile(new Tile(14, 'l', 1), new Position(0, 4)),
				new WorkTile(new Tile(15, 'o', 1), new Position(0, 5)),
				new WorkTile(new Tile(16, 'e', 1), new Position(1, 1)),
				new WorkTile(new Tile(17, 'l', 1), new Position(2, 1)),
				new WorkTile(new Tile(18, 'l', 1), new Position(3, 1)),
				new WorkTile(new Tile(19, 'o', 1), new Position(4, 1))
		);

		final TilesPlacement board = new MockTilesPlacement(workTiles);

		final ScribblePlayerHand hand = new ScribblePlayerHand(1L, new Tile[]{
				new Tile(6, 'a', 1),
				new Tile(7, 'b', 1),
				new Tile(8, 'c', 1),
				new Tile(9, 'd', 1),
				new Tile(10, '*', 0),
		});

		final ScribbleRobotBrain brain = new ScribbleRobotBrain();

		final Runtime runtime = Runtime.getRuntime();
		for (int i = 0; i < 10; i++) {
			reset(dictionary);
			expect(dictionary.getWordEntries()).andReturn(treeSet);
			expect(dictionary.getWordEntries()).andReturn(hashSet);
			expect(dictionary.getWordEntries()).andReturn(fd.getWordEntries());
			replay(dictionary);

			System.out.println("Size of dictionary: " + treeSet.size());
			System.out.println("Iterator " + i);

			long freeMemory;
			long timeNS;
			long timeMS;

			freeMemory = runtime.freeMemory();
			timeMS = System.currentTimeMillis();
			timeNS = System.nanoTime();
			List<Word> list = brain.analyzeValidWords(board, hand, RobotType.EXPERT, dictionary);
			System.out.println("TreeSet search time: " + (System.nanoTime() - timeNS) + "ns or " + (System.currentTimeMillis() - timeMS) + "ms" +
					". Free memory taken: " + (runtime.freeMemory() - freeMemory) +
					". Found: " + list.size() + " words");

			freeMemory = runtime.freeMemory();
			timeMS = System.currentTimeMillis();
			timeNS = System.nanoTime();
			list = brain.analyzeValidWords(board, hand, RobotType.EXPERT, dictionary);
			System.out.println("HashSet search time: " + (System.nanoTime() - timeNS) + "ns or " + (System.currentTimeMillis() - timeMS) + "ms" +
					". Free memory taken: " + (runtime.freeMemory() - freeMemory) +
					". Found: " + list.size() + " words");

			freeMemory = runtime.freeMemory();
			timeMS = System.currentTimeMillis();
			timeNS = System.nanoTime();
			list = brain.analyzeValidWords(board, hand, RobotType.EXPERT, dictionary);
			System.out.println("Default search time: " + (System.nanoTime() - timeNS) + "ns or " + (System.currentTimeMillis() - timeMS) + "ms" +
					". Free memory taken: " + (runtime.freeMemory() - freeMemory) +
					". Found: " + list.size() + " words");
		}
	}
}
