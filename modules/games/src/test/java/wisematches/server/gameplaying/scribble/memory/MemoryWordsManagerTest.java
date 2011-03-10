package wisematches.server.gameplaying.scribble.memory;

import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class MemoryWordsManagerTest extends AbstractTransactionalDataSourceSpringContextTests {
/*	private MemoryWordsManager memoryWordsManager;

	private final Word word1 = new Word(new Position(1, 3), Direction.HORIZONTAL, new Tile(1, 'a', 1), new Tile(2, 'b', 1), new Tile(12, 'c', 5));
	private final Word word2 = new Word(new Position(9, 2), Direction.VERTICAL, new Tile(4, 'r', 2), new Tile(5, 'j', 4), new Tile(123, 'h', 5));

	private ScribbleBoard board;

	private ScribblePlayerHand hand1;
	private ScribblePlayerHand hand2;

	protected String[] getConfigLocations() {
		return new String[]{"classpath:/config/test-application-settings.xml"};
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
		memoryWordsManager.addMemoryWord(board, hand1, new MemoryWord(1, word1));
		assertEquals(1, countRowsInTable("scribble_memory"));

		memoryWordsManager.addMemoryWord(board, hand1, new MemoryWord(2, word2));
		assertEquals(2, countRowsInTable("scribble_memory"));

		memoryWordsManager.addMemoryWord(board, hand2, new MemoryWord(1, word1));
		assertEquals(3, countRowsInTable("scribble_memory"));

		// Add memory word twise. Word should be changed
		memoryWordsManager.addMemoryWord(board, hand2, new MemoryWord(1, word2));
		assertEquals(3, countRowsInTable("scribble_memory"));
	}

	public void test_removeMemoryWord() {
		memoryWordsManager.addMemoryWord(board, hand1, new MemoryWord(1, word1));
		memoryWordsManager.addMemoryWord(board, hand1, new MemoryWord(2, word2));
		memoryWordsManager.addMemoryWord(board, hand2, new MemoryWord(1, word1));
		assertEquals(3, countRowsInTable("scribble_memory"));

		//Test remove unknown word
		memoryWordsManager.removeMemoryWord(board, hand1, 3);
		assertEquals(3, countRowsInTable("scribble_memory"));

		memoryWordsManager.removeMemoryWord(board, hand1, 2);
		assertEquals(2, countRowsInTable("scribble_memory"));

		memoryWordsManager.removeMemoryWord(board, hand2, 1);
		assertEquals(1, countRowsInTable("scribble_memory"));

		memoryWordsManager.removeMemoryWord(board, hand1, 1);
		assertEquals(0, countRowsInTable("scribble_memory"));

		memoryWordsManager.removeMemoryWord(board, hand1, 1);
		assertEquals(0, countRowsInTable("scribble_memory"));
	}

	public void test_removeMemoryWords() {
		memoryWordsManager.addMemoryWord(board, hand1, new MemoryWord(1, word1));
		memoryWordsManager.addMemoryWord(board, hand1, new MemoryWord(2, word2));
		memoryWordsManager.addMemoryWord(board, hand2, new MemoryWord(1, word1));
		assertEquals(3, countRowsInTable("scribble_memory"));

		memoryWordsManager.clearMemoryWords(board, hand1);
		assertEquals(1, countRowsInTable("scribble_memory"));

		//Test the same thing
		memoryWordsManager.clearMemoryWords(board, hand1);
		assertEquals(1, countRowsInTable("scribble_memory"));

		memoryWordsManager.clearMemoryWords(board, hand2);
		assertEquals(0, countRowsInTable("scribble_memory"));
	}

	public void test_getMemoryWords() {
		final Collection<MemoryWord> wordCollection = memoryWordsManager.getMemoryWords(board, hand1);
		assertEquals(0, wordCollection.size());

		memoryWordsManager.addMemoryWord(board, hand1, new MemoryWord(1, word1));
		memoryWordsManager.getHibernateTemplate().clear(); //clear cache
		final Collection<MemoryWord> wc1 = memoryWordsManager.getMemoryWords(board, hand1);
		assertEquals(1, wc1.size());
		assertTrue(wc1.contains(new MemoryWord(1, word1)));

		memoryWordsManager.addMemoryWord(board, hand1, new MemoryWord(2, word2));
		memoryWordsManager.getHibernateTemplate().clear(); //clear cache
		final Collection<MemoryWord> wc2 = memoryWordsManager.getMemoryWords(board, hand1);
		assertEquals(2, wc2.size());
		assertTrue(wc2.contains(new MemoryWord(1, word1)));
		assertTrue(wc2.contains(new MemoryWord(2, word2)));

		memoryWordsManager.removeMemoryWord(board, hand1, 1);
		memoryWordsManager.getHibernateTemplate().clear(); //clear cache
		final Collection<MemoryWord> wc3 = memoryWordsManager.getMemoryWords(board, hand1);
		assertEquals(1, wc3.size());
		assertFalse(wc3.contains(new MemoryWord(1, word1)));
		assertTrue(wc3.contains(new MemoryWord(2, word2)));

		final int i = getJdbcTemplate().update("insert into scribble_memory(boardId, playerId, wordNumber, row, col, direction, word) values (1, 14, 5, 9, 12, 1, '1a1|2b2')");
		assertEquals(1, i);
		final Collection<MemoryWord> memoryWords = memoryWordsManager.getMemoryWords(board, hand2);
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

	public MemoryWordsManager getMemoryWordsManager() {
		return memoryWordsManager;
	}

	public void setMemoryWordsManager(MemoryWordsManager memoryWordsManager) {
		this.memoryWordsManager = memoryWordsManager;
	}*/
}