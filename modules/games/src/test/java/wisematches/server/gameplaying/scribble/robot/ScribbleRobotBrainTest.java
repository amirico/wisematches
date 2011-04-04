package wisematches.server.gameplaying.scribble.robot;

import org.easymock.IAnswer;
import org.junit.Test;
import wisematches.server.gameplaying.board.GameMove;
import wisematches.server.gameplaying.board.GameMoveException;
import wisematches.server.gameplaying.board.PlayerMove;
import wisematches.server.gameplaying.dictionary.IterableDictionary;
import wisematches.server.gameplaying.scribble.Direction;
import wisematches.server.gameplaying.scribble.Position;
import wisematches.server.gameplaying.scribble.Tile;
import wisematches.server.gameplaying.scribble.Word;
import wisematches.server.gameplaying.scribble.board.MakeWordMove;
import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.board.ScribblePlayerHand;
import wisematches.server.gameplaying.scribble.board.TilesPlacement;
import wisematches.server.gameplaying.scribble.scores.ScoreCalculation;
import wisematches.server.gameplaying.scribble.scores.ScoreEngine;
import wisematches.server.personality.player.computer.robot.RobotType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

		final ScoreCalculation calculation = createStrictMock(ScoreCalculation.class);
		expect(calculation.getPoints()).andReturn((short) 10).andReturn((short) 20).andReturn((short) 30).andReturn((short) 5);
		replay(calculation);

		final ScoreEngine scoreEngine = createStrictMock(ScoreEngine.class);
		expect(scoreEngine.calculateWordScore(w1, tilesPlacement)).andReturn(calculation);
		expect(scoreEngine.calculateWordScore(w2, tilesPlacement)).andReturn(calculation);
		expect(scoreEngine.calculateWordScore(w3, tilesPlacement)).andReturn(calculation);
		expect(scoreEngine.calculateWordScore(w4, tilesPlacement)).andReturn(calculation);
		replay(scoreEngine);

		final ScribbleRobotBrain brain = new ScribbleRobotBrain();
		final Word word = brain.selectResultWord(Arrays.asList(w1, w2, w3, w4), RobotType.EXPERT, scoreEngine, tilesPlacement);
		assertSame(w3, word);

		verify(calculation);
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

		final ScribblePlayerHand hand = new ScribblePlayerHand(1L, 1, new Tile[]{
				new Tile(6, 'a', 1),
				new Tile(7, 'b', 1),
				new Tile(8, 'c', 1),
				new Tile(9, 'd', 1),
				new Tile(10, '*', 0),
		});

		//
		final List<String> words = Arrays.asList("aheh", "ahehh", "ahed", "ahedc", "load");

		final IterableDictionary dictionary = createMock(IterableDictionary.class);
		expect(dictionary.iterator()).andReturn(words.iterator());
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

		final IterableDictionary dictionary = createMock(IterableDictionary.class);
		expect(dictionary.iterator()).andReturn(Arrays.asList("ahed").iterator());
		replay(dictionary);


		final ScribblePlayerHand hand = new ScribblePlayerHand(1L, 1, new Tile[]{
				new Tile(6, 'a', 1),
				new Tile(9, 'd', 1),
				new Tile(10, 'e', 1),
		});

		final ScribbleBoard scribbleBoard = createStrictMock(ScribbleBoard.class);
		expect(scribbleBoard.getBoardId()).andReturn(1L);
		expect(scribbleBoard.getPlayerTurn()).andReturn(hand);
		expect(scribbleBoard.getDictionary()).andReturn(dictionary);
		expect(scribbleBoard.getBoardTile(anyInt(), anyInt())).andAnswer(new IAnswer<Tile>() {
			public Tile answer() throws Throwable {
				return board.getBoardTile((Integer) getCurrentArguments()[0], (Integer) getCurrentArguments()[1]);
			}
		}).anyTimes();
		expect(scribbleBoard.getScoreEngine()).andReturn(null);
		expect(scribbleBoard.makeMove(isA(MakeWordMove.class))).andReturn(new GameMove(createMock(PlayerMove.class), 12, 1, new Date()));
		replay(scribbleBoard);

		final ScribbleRobotBrain brain = new ScribbleRobotBrain();
		brain.putInAction(scribbleBoard, RobotType.TRAINEE);

		verify(scribbleBoard);
	}

	/**
	 * This test doesn't check anything. It just calculate time of move for robot for random and sorted dictionary.
	 */
	@Test
	public void test_calculateSerachTime() throws IOException {
		final Set<String> treeSet = new TreeSet<String>();
		final Set<String> hashSet = new TreeSet<String>();

		final InputStream stream = getClass().getResourceAsStream("/dicts/dictionary_en.properties");
		final BufferedReader br = new BufferedReader(new InputStreamReader(stream));

		String s = br.readLine();
		while (s != null) {
			treeSet.add(s);
			hashSet.add(s);
			s = br.readLine();
		}
		br.close();

		//
		final IterableDictionary dictionary = createMock(IterableDictionary.class);

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

		final ScribblePlayerHand hand = new ScribblePlayerHand(1L, 1, new Tile[]{
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
			expect(dictionary.iterator()).andReturn(treeSet.iterator());
			expect(dictionary.iterator()).andReturn(hashSet.iterator());
			replay(dictionary);

			System.out.println("Size of dictionary: " + treeSet.size());
			System.out.println("Iterator " + i);

			long freeMemory;
			long timeNS;
			long timeMS;

			freeMemory = runtime.freeMemory();
			timeMS = System.currentTimeMillis();
			timeNS = System.nanoTime();
			List<Word> list = brain.analyzeValidWords(board, hand, RobotType.DULL, dictionary);
			System.out.println("TreeSet Seach time: " + (System.nanoTime() - timeNS) + "ns or " + (System.currentTimeMillis() - timeMS) + "ms" +
					". Free memory taken: " + (runtime.freeMemory() - freeMemory) +
					". Found: " + list.size() + " words");

			freeMemory = runtime.freeMemory();
			timeMS = System.currentTimeMillis();
			timeNS = System.nanoTime();
			list = brain.analyzeValidWords(board, hand, RobotType.DULL, dictionary);
			System.out.println("HashSet seach time: " + (System.nanoTime() - timeNS) + "ns or " + (System.currentTimeMillis() - timeMS) + "ms" +
					". Free memory taken: " + (runtime.freeMemory() - freeMemory) +
					". Found: " + list.size() + " words");
		}
	}
}
