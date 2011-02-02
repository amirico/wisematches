package wisematches.server.deprecated.web.modules.app.playboard.memory;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import wisematches.server.gameplaying.scribble.Direction;
import wisematches.server.gameplaying.scribble.Position;
import wisematches.server.gameplaying.scribble.Tile;
import wisematches.server.gameplaying.scribble.Word;
import wisematches.server.gameplaying.scribble.board.ScribbleBoard;
import wisematches.server.gameplaying.scribble.board.ScribblePlayerHand;

import java.util.Collection;

import static org.easymock.EasyMock.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class MemoryWordsDaoTest extends AbstractTransactionalDataSourceSpringContextTests {
	private MemoryWordsDao memoryWordsDao;

	private final Word word1 = new Word(new Position(1, 3), Direction.HORIZONTAL, new Tile(1, 'a', 1), new Tile(2, 'b', 1), new Tile(12, 'c', 5));
	private final Word word2 = new Word(new Position(9, 2), Direction.VERTICAL, new Tile(4, 'r', 2), new Tile(5, 'j', 4), new Tile(123, 'h', 5));

	private ScribbleBoard board;

	private ScribblePlayerHand hand1;
	private ScribblePlayerHand hand2;

	protected String[] getConfigLocations() {
		return new String[]{"classpath:/config/test-web-app-config.xml"};
	}

	@Override
	protected void onSetUp() throws Exception {
		deleteFromTables(new String[]{"scribble_memory"});

		hand1 = new ScribblePlayerHand(13L);
		hand2 = new ScribblePlayerHand(14L);

		board = createMock(ScribbleBoard.class);
		expect(board.getBoardId()).andReturn(1L).anyTimes();
		expect(board.getPlayerHand(13L)).andReturn(hand1).anyTimes();
		expect(board.getPlayerHand(14L)).andReturn(hand2).anyTimes();
		replay(board);

		super.onSetUp();
	}

	@Override
	protected void onTearDown() throws Exception {
		super.onTearDown();

		deleteFromTables(new String[]{"scribble_memory"});
	}

	public void test_addMemory() {
		memoryWordsDao.addMemoryWord(board, hand1, new MemoryWord(1, word1));
		assertEquals(1, countRowsInTable("scribble_memory"));

		memoryWordsDao.addMemoryWord(board, hand1, new MemoryWord(2, word2));
		assertEquals(2, countRowsInTable("scribble_memory"));

		memoryWordsDao.addMemoryWord(board, hand2, new MemoryWord(1, word1));
		assertEquals(3, countRowsInTable("scribble_memory"));

		// Add memory word twise. Word should be changed
		memoryWordsDao.addMemoryWord(board, hand2, new MemoryWord(1, word2));
		assertEquals(3, countRowsInTable("scribble_memory"));
	}

	public void test_removeMemoryWord() {
		memoryWordsDao.addMemoryWord(board, hand1, new MemoryWord(1, word1));
		memoryWordsDao.addMemoryWord(board, hand1, new MemoryWord(2, word2));
		memoryWordsDao.addMemoryWord(board, hand2, new MemoryWord(1, word1));
		assertEquals(3, countRowsInTable("scribble_memory"));

		//Test remove unknown word
		memoryWordsDao.removeMemoryWord(board, hand1, 3);
		assertEquals(3, countRowsInTable("scribble_memory"));

		memoryWordsDao.removeMemoryWord(board, hand1, 2);
		assertEquals(2, countRowsInTable("scribble_memory"));

		memoryWordsDao.removeMemoryWord(board, hand2, 1);
		assertEquals(1, countRowsInTable("scribble_memory"));

		memoryWordsDao.removeMemoryWord(board, hand1, 1);
		assertEquals(0, countRowsInTable("scribble_memory"));

		memoryWordsDao.removeMemoryWord(board, hand1, 1);
		assertEquals(0, countRowsInTable("scribble_memory"));
	}

	public void test_removeMemoryWords() {
		memoryWordsDao.addMemoryWord(board, hand1, new MemoryWord(1, word1));
		memoryWordsDao.addMemoryWord(board, hand1, new MemoryWord(2, word2));
		memoryWordsDao.addMemoryWord(board, hand2, new MemoryWord(1, word1));
		assertEquals(3, countRowsInTable("scribble_memory"));

		memoryWordsDao.removeMemoryWords(board, hand1);
		assertEquals(1, countRowsInTable("scribble_memory"));

		//Test the same thing
		memoryWordsDao.removeMemoryWords(board, hand1);
		assertEquals(1, countRowsInTable("scribble_memory"));

		memoryWordsDao.removeMemoryWords(board, hand2);
		assertEquals(0, countRowsInTable("scribble_memory"));
	}

	public void test_getMemoryWords() {
		final Collection<MemoryWord> wordCollection = memoryWordsDao.getMemoryWords(board, hand1);
		assertEquals(0, wordCollection.size());

		memoryWordsDao.addMemoryWord(board, hand1, new MemoryWord(1, word1));
		memoryWordsDao.getHibernateTemplate().clear(); //clear cache
		final Collection<MemoryWord> wc1 = memoryWordsDao.getMemoryWords(board, hand1);
		assertEquals(1, wc1.size());
		assertTrue(wc1.contains(new MemoryWord(1, word1)));

		memoryWordsDao.addMemoryWord(board, hand1, new MemoryWord(2, word2));
		memoryWordsDao.getHibernateTemplate().clear(); //clear cache
		final Collection<MemoryWord> wc2 = memoryWordsDao.getMemoryWords(board, hand1);
		assertEquals(2, wc2.size());
		assertTrue(wc2.contains(new MemoryWord(1, word1)));
		assertTrue(wc2.contains(new MemoryWord(2, word2)));

		memoryWordsDao.removeMemoryWord(board, hand1, 1);
		memoryWordsDao.getHibernateTemplate().clear(); //clear cache
		final Collection<MemoryWord> wc3 = memoryWordsDao.getMemoryWords(board, hand1);
		assertEquals(1, wc3.size());
		assertFalse(wc3.contains(new MemoryWord(1, word1)));
		assertTrue(wc3.contains(new MemoryWord(2, word2)));

		final int i = getJdbcTemplate().update("insert into scribble_memory(boardId, playerId, wordNumber, row, col, direction, word) values (1, 14, 5, 9, 12, 1, '1a1|2b2')");
		assertEquals(1, i);
		final Collection<MemoryWord> memoryWords = memoryWordsDao.getMemoryWords(board, hand2);
		assertEquals(1, memoryWords.size());

		final MemoryWord memoryWord = memoryWords.iterator().next();
		assertEquals(5, memoryWord.getNumber());
		final Word word = memoryWord.getWord();
		assertEquals(new Position(9, 12), word.getPosition());
		assertEquals(Direction.HORIZONTAL, word.getDirection());
		assertEquals(2, word.getTiles().length);
		assertEquals(new Tile(1, 'a', 1), word.getTiles()[0]);
		assertEquals(new Tile(2, 'b', 2), word.getTiles()[1]);
	}

	public MemoryWordsDao getMemoryWordsDao() {
		return memoryWordsDao;
	}

	public void setMemoryWordsDao(MemoryWordsDao memoryWordsDao) {
		this.memoryWordsDao = memoryWordsDao;
	}
}