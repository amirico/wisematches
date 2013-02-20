package wisematches.playground.scribble.memory.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Player;
import wisematches.core.personality.DefaultMember;
import wisematches.playground.scribble.*;
import wisematches.playground.scribble.memory.MemoryWordManager;

import java.util.Collection;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/personality-config.xml",
		"classpath:/config/scribble-junit-config.xml"
})
public class HibernateMemoryWordManagerTest {
	@Autowired
	private MemoryWordManager memoryWordManager;

	private final Word word1 = new Word(new Position(1, 3), Direction.HORIZONTAL, new Tile(1, 'a', 1), new Tile(2, 'b', 1), new Tile(12, 'c', 5));
	private final Word word2 = new Word(new Position(9, 2), Direction.VERTICAL, new Tile(4, 'r', 2), new Tile(5, 'j', 4), new Tile(123, 'h', 5));

	private ScribbleBoard board;

	private final Player player1 = new DefaultMember(901, null, null, null, null, null);
	private final Player player2 = new DefaultMember(902, null, null, null, null, null);

	public HibernateMemoryWordManagerTest() {
	}

	@Before
	public void onSetUp() throws Exception {
		board = createMock(ScribbleBoard.class);
		expect(board.getBoardId()).andReturn(1L).anyTimes();
		expect(board.getPlayerHand(player1)).andReturn(new ScribblePlayerHand(player1)).anyTimes();
		expect(board.getPlayerHand(player2)).andReturn(new ScribblePlayerHand(player2)).anyTimes();
		replay(board);
	}

	@After
	public void onTearDown() throws Exception {
	}

	@Test
	public void test_addMemory() {
		memoryWordManager.addMemoryWord(board, player1, word1);
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, player1));
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, player2));
		assertEquals(1, memoryWordManager.getMemoryWords(board, player1).size());
		assertEquals(0, memoryWordManager.getMemoryWords(board, player2).size());

		memoryWordManager.addMemoryWord(board, player1, word2);
		assertEquals(2, memoryWordManager.getMemoryWordsCount(board, player1));
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, player2));
		assertEquals(2, memoryWordManager.getMemoryWords(board, player1).size());
		assertEquals(0, memoryWordManager.getMemoryWords(board, player2).size());

		memoryWordManager.addMemoryWord(board, player2, word1);
		assertEquals(2, memoryWordManager.getMemoryWordsCount(board, player1));
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, player2));
		assertEquals(2, memoryWordManager.getMemoryWords(board, player1).size());
		assertEquals(1, memoryWordManager.getMemoryWords(board, player2).size());

		// Add memory word twice. Word should be changed
		memoryWordManager.addMemoryWord(board, player2, word2);
		assertEquals(2, memoryWordManager.getMemoryWordsCount(board, player1));
		assertEquals(2, memoryWordManager.getMemoryWordsCount(board, player2));
		assertEquals(2, memoryWordManager.getMemoryWords(board, player1).size());
		assertEquals(2, memoryWordManager.getMemoryWords(board, player2).size());
	}

	@Test
	public void test_removeMemoryWord() {
		memoryWordManager.addMemoryWord(board, player1, word1);
		memoryWordManager.addMemoryWord(board, player1, word2);
		memoryWordManager.addMemoryWord(board, player2, word1);
		assertEquals(2, memoryWordManager.getMemoryWords(board, player1).size());
		assertEquals(1, memoryWordManager.getMemoryWords(board, player2).size());
		assertEquals(2, memoryWordManager.getMemoryWordsCount(board, player1));
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, player2));

		//Test remove unknown word
		memoryWordManager.removeMemoryWord(board, player1, word1);
		assertEquals(1, memoryWordManager.getMemoryWords(board, player1).size());
		assertEquals(1, memoryWordManager.getMemoryWords(board, player2).size());
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, player1));
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, player2));

		memoryWordManager.removeMemoryWord(board, player1, word1);
		assertEquals(1, memoryWordManager.getMemoryWords(board, player1).size());
		assertEquals(1, memoryWordManager.getMemoryWords(board, player2).size());
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, player1));
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, player2));

		memoryWordManager.removeMemoryWord(board, player2, word2);
		assertEquals(1, memoryWordManager.getMemoryWords(board, player1).size());
		assertEquals(1, memoryWordManager.getMemoryWords(board, player2).size());
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, player1));
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, player2));

		memoryWordManager.removeMemoryWord(board, player2, word1);
		assertEquals(1, memoryWordManager.getMemoryWords(board, player1).size());
		assertEquals(0, memoryWordManager.getMemoryWords(board, player2).size());
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, player1));
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, player2));

		memoryWordManager.removeMemoryWord(board, player1, word2);
		assertEquals(0, memoryWordManager.getMemoryWords(board, player1).size());
		assertEquals(0, memoryWordManager.getMemoryWords(board, player2).size());
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, player1));
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, player2));
	}

	@Test
	public void test_clearMemoryWords() {
		memoryWordManager.addMemoryWord(board, player1, word1);
		memoryWordManager.addMemoryWord(board, player1, word2);
		memoryWordManager.addMemoryWord(board, player2, word1);
		assertEquals(2, memoryWordManager.getMemoryWords(board, player1).size());
		assertEquals(1, memoryWordManager.getMemoryWords(board, player2).size());
		assertEquals(2, memoryWordManager.getMemoryWordsCount(board, player1));
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, player2));

		memoryWordManager.clearMemoryWords(board, player1);
		assertEquals(0, memoryWordManager.getMemoryWords(board, player1).size());
		assertEquals(1, memoryWordManager.getMemoryWords(board, player2).size());
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, player1));
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, player2));

		//Test the same thing
		memoryWordManager.clearMemoryWords(board, player1);
		assertEquals(0, memoryWordManager.getMemoryWords(board, player1).size());
		assertEquals(1, memoryWordManager.getMemoryWords(board, player2).size());
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, player1));
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, player2));

		memoryWordManager.clearMemoryWords(board, player2);
		assertEquals(0, memoryWordManager.getMemoryWords(board, player1).size());
		assertEquals(0, memoryWordManager.getMemoryWords(board, player2).size());
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, player1));
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, player2));
	}

	@Test
	public void test_getMemoryWords() {
		final Collection<Word> words = memoryWordManager.getMemoryWords(board, player1);
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, player1));
		assertEquals(0, words.size());

		memoryWordManager.addMemoryWord(board, player1, word1);
		final Collection<Word> wc1 = memoryWordManager.getMemoryWords(board, player1);
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, player1));
		assertEquals(1, wc1.size());
		assertTrue(wc1.contains(word1));

		memoryWordManager.addMemoryWord(board, player1, word2);
		final Collection<Word> wc2 = memoryWordManager.getMemoryWords(board, player1);
		assertEquals(2, memoryWordManager.getMemoryWordsCount(board, player1));
		assertEquals(2, wc2.size());
		assertTrue(wc2.contains(word1));
		assertTrue(wc2.contains(word2));

		memoryWordManager.removeMemoryWord(board, player1, word1);
		final Collection<Word> wc3 = memoryWordManager.getMemoryWords(board, player1);
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, player1));
		assertEquals(1, wc3.size());
		assertFalse(wc3.contains(word1));
		assertTrue(wc3.contains(word2));
	}

	@Test
	@SuppressWarnings("unchecked")
	public void test_clearMemoryWords_2() {
		memoryWordManager.addMemoryWord(board, player1, word1);
		memoryWordManager.addMemoryWord(board, player1, word2);
		memoryWordManager.addMemoryWord(board, player2, word1);
		assertEquals(2, memoryWordManager.getMemoryWords(board, player1).size());
		assertEquals(1, memoryWordManager.getMemoryWords(board, player2).size());
		assertEquals(2, memoryWordManager.getMemoryWordsCount(board, player1));
		assertEquals(1, memoryWordManager.getMemoryWordsCount(board, player2));

		memoryWordManager.clearMemoryWords(board);

		assertEquals(0, memoryWordManager.getMemoryWords(board, player1).size());
		assertEquals(0, memoryWordManager.getMemoryWords(board, player2).size());
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, player1));
		assertEquals(0, memoryWordManager.getMemoryWordsCount(board, player2));
	}
}