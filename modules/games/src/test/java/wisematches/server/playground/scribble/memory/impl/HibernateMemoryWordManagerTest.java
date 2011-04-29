package wisematches.server.playground.scribble.memory.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.server.playground.scribble.Direction;
import wisematches.server.playground.scribble.Position;
import wisematches.server.playground.scribble.Tile;
import wisematches.server.playground.scribble.Word;
import wisematches.server.playground.scribble.board.ScribbleBoard;
import wisematches.server.playground.scribble.board.ScribblePlayerHand;
import wisematches.server.playground.scribble.memory.MemoryWordManager;

import java.util.Collection;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-config.xml",
		"classpath:/config/server-base-config.xml",
		"classpath:/config/game-scribble-config.xml",
		"classpath:/config/test-game-modules-config.xml"})
public class HibernateMemoryWordManagerTest {
	@Autowired
	private MemoryWordManager memoryWordManager;

	private final Word word1 = new Word(new Position(1, 3), Direction.HORIZONTAL, new Tile(1, 'a', 1), new Tile(2, 'b', 1), new Tile(12, 'c', 5));
	private final Word word2 = new Word(new Position(9, 2), Direction.VERTICAL, new Tile(4, 'r', 2), new Tile(5, 'j', 4), new Tile(123, 'h', 5));

	private ScribbleBoard board;

	private ScribblePlayerHand hand1;
	private ScribblePlayerHand hand2;

	public HibernateMemoryWordManagerTest() {
	}

	@Before
	public void onSetUp() throws Exception {
		hand1 = new ScribblePlayerHand(13L);
		hand2 = new ScribblePlayerHand(14L);

		board = createMock(ScribbleBoard.class);
		expect(board.getBoardId()).andReturn(1L).anyTimes();
		expect(board.getPlayerHand(13L)).andReturn(hand1).anyTimes();
		expect(board.getPlayerHand(14L)).andReturn(hand2).anyTimes();
		replay(board);
	}

	@After
	public void onTearDown() throws Exception {
	}

	@Test
	public void test_addMemory() {
		memoryWordManager.addMemoryWord(board, hand1, word1);
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, hand1));
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, hand2));
		assertEquals(1, memoryWordManager.getMemoryWords(board, hand1).size());
		assertEquals(0, memoryWordManager.getMemoryWords(board, hand2).size());

		memoryWordManager.addMemoryWord(board, hand1, word2);
		assertEquals(2, memoryWordManager.getMemoryWordsCount(board, hand1));
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, hand2));
		assertEquals(2, memoryWordManager.getMemoryWords(board, hand1).size());
		assertEquals(0, memoryWordManager.getMemoryWords(board, hand2).size());

		memoryWordManager.addMemoryWord(board, hand2, word1);
		assertEquals(2, memoryWordManager.getMemoryWordsCount(board, hand1));
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, hand2));
		assertEquals(2, memoryWordManager.getMemoryWords(board, hand1).size());
		assertEquals(1, memoryWordManager.getMemoryWords(board, hand2).size());

		// Add memory word twice. Word should be changed
		memoryWordManager.addMemoryWord(board, hand2, word2);
		assertEquals(2, memoryWordManager.getMemoryWordsCount(board, hand1));
		assertEquals(2, memoryWordManager.getMemoryWordsCount(board, hand2));
		assertEquals(2, memoryWordManager.getMemoryWords(board, hand1).size());
		assertEquals(2, memoryWordManager.getMemoryWords(board, hand2).size());
	}

	@Test
	public void test_removeMemoryWord() {
		memoryWordManager.addMemoryWord(board, hand1, word1);
		memoryWordManager.addMemoryWord(board, hand1, word2);
		memoryWordManager.addMemoryWord(board, hand2, word1);
		assertEquals(2, memoryWordManager.getMemoryWords(board, hand1).size());
		assertEquals(1, memoryWordManager.getMemoryWords(board, hand2).size());
		assertEquals(2, memoryWordManager.getMemoryWordsCount(board, hand1));
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, hand2));

		//Test remove unknown word
		memoryWordManager.removeMemoryWord(board, hand1, word1);
		assertEquals(1, memoryWordManager.getMemoryWords(board, hand1).size());
		assertEquals(1, memoryWordManager.getMemoryWords(board, hand2).size());
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, hand1));
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, hand2));

		memoryWordManager.removeMemoryWord(board, hand1, word1);
		assertEquals(1, memoryWordManager.getMemoryWords(board, hand1).size());
		assertEquals(1, memoryWordManager.getMemoryWords(board, hand2).size());
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, hand1));
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, hand2));

		memoryWordManager.removeMemoryWord(board, hand2, word2);
		assertEquals(1, memoryWordManager.getMemoryWords(board, hand1).size());
		assertEquals(1, memoryWordManager.getMemoryWords(board, hand2).size());
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, hand1));
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, hand2));

		memoryWordManager.removeMemoryWord(board, hand2, word1);
		assertEquals(1, memoryWordManager.getMemoryWords(board, hand1).size());
		assertEquals(0, memoryWordManager.getMemoryWords(board, hand2).size());
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, hand1));
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, hand2));

		memoryWordManager.removeMemoryWord(board, hand1, word2);
		assertEquals(0, memoryWordManager.getMemoryWords(board, hand1).size());
		assertEquals(0, memoryWordManager.getMemoryWords(board, hand2).size());
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, hand1));
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, hand2));
	}

	@Test
	public void test_clearMemoryWords() {
		memoryWordManager.addMemoryWord(board, hand1, word1);
		memoryWordManager.addMemoryWord(board, hand1, word2);
		memoryWordManager.addMemoryWord(board, hand2, word1);
		assertEquals(2, memoryWordManager.getMemoryWords(board, hand1).size());
		assertEquals(1, memoryWordManager.getMemoryWords(board, hand2).size());
		assertEquals(2, memoryWordManager.getMemoryWordsCount(board, hand1));
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, hand2));

		memoryWordManager.clearMemoryWords(board, hand1);
		assertEquals(0, memoryWordManager.getMemoryWords(board, hand1).size());
		assertEquals(1, memoryWordManager.getMemoryWords(board, hand2).size());
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, hand1));
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, hand2));

		//Test the same thing
		memoryWordManager.clearMemoryWords(board, hand1);
		assertEquals(0, memoryWordManager.getMemoryWords(board, hand1).size());
		assertEquals(1, memoryWordManager.getMemoryWords(board, hand2).size());
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, hand1));
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, hand2));

		memoryWordManager.clearMemoryWords(board, hand2);
		assertEquals(0, memoryWordManager.getMemoryWords(board, hand1).size());
		assertEquals(0, memoryWordManager.getMemoryWords(board, hand2).size());
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, hand1));
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, hand2));
	}

	@Test
	public void test_getMemoryWords() {
		final Collection<Word> words = memoryWordManager.getMemoryWords(board, hand1);
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, hand1));
		assertEquals(0, words.size());

		memoryWordManager.addMemoryWord(board, hand1, word1);
		final Collection<Word> wc1 = memoryWordManager.getMemoryWords(board, hand1);
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, hand1));
		assertEquals(1, wc1.size());
		assertTrue(wc1.contains(word1));

		memoryWordManager.addMemoryWord(board, hand1, word2);
		final Collection<Word> wc2 = memoryWordManager.getMemoryWords(board, hand1);
		assertEquals(2, memoryWordManager.getMemoryWordsCount(board, hand1));
		assertEquals(2, wc2.size());
		assertTrue(wc2.contains(word1));
		assertTrue(wc2.contains(word2));

		memoryWordManager.removeMemoryWord(board, hand1, word1);
		final Collection<Word> wc3 = memoryWordManager.getMemoryWords(board, hand1);
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, hand1));
		assertEquals(1, wc3.size());
		assertFalse(wc3.contains(word1));
		assertTrue(wc3.contains(word2));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test_clearMemoryWords_2() {
		memoryWordManager.addMemoryWord(board, hand1, word1);
		memoryWordManager.addMemoryWord(board, hand1, word2);
		memoryWordManager.addMemoryWord(board, hand2, word1);
		assertEquals(2, memoryWordManager.getMemoryWords(board, hand1).size());
		assertEquals(1, memoryWordManager.getMemoryWords(board, hand2).size());
		assertEquals(2, memoryWordManager.getMemoryWordsCount(board, hand1));
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, hand2));

		memoryWordManager.clearMemoryWords(board);

		assertEquals(0, memoryWordManager.getMemoryWords(board, hand1).size());
		assertEquals(0, memoryWordManager.getMemoryWords(board, hand2).size());
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, hand1));
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, hand2));
	}
}