package wisematches.server.gameplaying.scribble.room;

import org.junit.Test;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleRoomManagerTest {

	@Test
	public void commented() {
		throw new UnsupportedOperationException("Test has been commented");
	}
/*
	private ScribbleBoardManager scribbleRoomManager;

	private DictionaryManager dictionaryManager;
	private TilesBankingHouse tilesBankingHouse;
	private ScribbleBoardDao scribbleBoardDao;

	private static final Locale LOCALE = new Locale("en");

	@Before
	public void testSetUp() {
		dictionaryManager = createStrictMock(DictionaryManager.class);
		tilesBankingHouse = createStrictMock(TilesBankingHouse.class);
		scribbleBoardDao = createStrictMock(ScribbleBoardDao.class);

		scribbleRoomManager = new ScribbleBoardManager();
		scribbleRoomManager.setDictionaryManager(dictionaryManager);
		scribbleRoomManager.setTilesBankingHouse(tilesBankingHouse);
		scribbleRoomManager.setScribbleBoardDao(scribbleBoardDao);
	}

	@Test
	public void testLoadBoardImpl() throws BoardLoadingException, DictionaryNotFoundException {
		final ScribbleSettings settings = new ScribbleSettings("Mock", "en", 3);

		final Dictionary dictionary = createNiceMock(Dictionary.class);
		final TilesBank tilesBank = new TilesBank(new TilesBank.TilesInfo[0]);

		final ScribbleBoard board = createStrictMock(ScribbleBoard.class);
		expect(board.getGameSettings()).andReturn(settings);
		expect(board.getPlayersHands()).andReturn(Arrays.<ScribblePlayerHand>asList(null, null, null));
		board.initGameAfterLoading(tilesBank, dictionary);
		replay(board);

		expect(scribbleBoardDao.getScribbleBoard(1L)).andReturn(board);
		replay(scribbleBoardDao);

		expect(dictionaryManager.getDictionary(LOCALE)).andReturn(dictionary);
		replay(dictionaryManager);

		expect(tilesBankingHouse.createTilesBank(LOCALE, 3, true)).andReturn(tilesBank);
		replay(tilesBankingHouse);

		final ScribbleBoard board1 = scribbleRoomManager.loadBoardImpl(1L);
		assertSame(board, board1);

		verify(board);
		verify(scribbleBoardDao);
		verify(dictionaryManager);
		verify(tilesBankingHouse);
	}

	@Test
	public void testCreateBoardImpl() throws DictionaryNotFoundException, BoardCreationException {
		final ScribbleSettings settings = new ScribbleSettings("Mock", "en", 3);

		final Dictionary dictionary = createNiceMock(Dictionary.class);
		expect(dictionary.getLocale()).andReturn(LOCALE);
		replay(dictionary);

		final TilesBank tilesBank = new TilesBank(new TilesBank.TilesInfo[]{new TilesBank.TilesInfo('A', 100, 1)});

		expect(dictionaryManager.getDictionary(LOCALE)).andReturn(dictionary);
		replay(dictionaryManager);

		expect(tilesBankingHouse.createTilesBank(LOCALE, 2, true)).andReturn(tilesBank);
		replay(tilesBankingHouse);

		scribbleRoomManager.setDictionaryManager(dictionaryManager);
		scribbleRoomManager.setTilesBankingHouse(tilesBankingHouse);

		final ScribbleBoard board1 = scribbleRoomManager.createBoardImpl(settings,
				Arrays.asList(ScribbleBoardTest.createMockPlayer(1, 300), ScribbleBoardTest.createMockPlayer(2, 400)));
		assertNotNull(board1);

		verify(dictionaryManager);
		verify(tilesBankingHouse);
	}

	@Test
	public void testSaveBoardImpl() {
		final ScribbleBoard board = createNiceMock(ScribbleBoard.class);

		scribbleBoardDao.saveScribbleBoard(board);
		replay(scribbleBoardDao);

		replay(dictionaryManager);
		replay(tilesBankingHouse);

		scribbleRoomManager.saveBoardImpl(board);

		verify(scribbleBoardDao);
		verify(dictionaryManager);
		verify(tilesBankingHouse);
	}

	@Test
	public void testLoadActivePlayerBoards() {
		final Player player = createNiceMock(Player.class);

		final Collection<Long> ids = Arrays.asList(1L, 2L, 3L);

		expect(scribbleBoardDao.getActiveBoards(player)).andReturn(ids);
		replay(scribbleBoardDao);

		replay(dictionaryManager);
		replay(tilesBankingHouse);

		final Collection<Long> longCollection = scribbleRoomManager.loadActivePlayerBoards(player);
		assertSame(ids, longCollection);

		verify(scribbleBoardDao);
		verify(dictionaryManager);
		verify(tilesBankingHouse);
	}
*/
}
