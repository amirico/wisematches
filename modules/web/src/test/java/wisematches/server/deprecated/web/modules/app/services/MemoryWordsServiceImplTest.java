package wisematches.server.deprecated.web.modules.app.services;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class MemoryWordsServiceImplTest {
/*
    private Player player;

    private MemoryWordManager memoryWordsDao;
    private MemoryWordsServiceImpl service;

    private ScribbleBoard board;
    private ScribblePlayerHand playerHand;

    @Before
    public void setUp() throws BoardLoadingException {
        memoryWordsDao = createStrictMock(MemoryWordManager.class);

        playerHand = new ScribblePlayerHand(13L);

        board = createMock(ScribbleBoard.class);
        expect(board.getPlayerHand(13L)).andReturn(playerHand).anyTimes();
        replay(board);

        final ScribbleBoardManager roomManager = createMock(ScribbleBoardManager.class);
        expect(roomManager.openBoard(123L)).andReturn(board).anyTimes();
        replay(roomManager);

        final RoomsManager roomsManager = createMock(RoomsManager.class);
        expect(roomsManager.getRoomManager(ScribbleBoardManager.ROOM)).andReturn(roomManager).anyTimes();
        replay(roomsManager);

        player = createNiceMock(Player.class);
        expect(player.getId()).andReturn(13L);
        replay(player);

        service = new MemoryWordsServiceImpl();
        service.setMemoryWordsManager(memoryWordsDao);
        service.setRoomsManager(roomsManager);
    }

    @After
    public void thearDown() {
        RemoteServiceContextAccessor.destroy();
    }

    @Test
    public void test_addMemoryWord() {
        final Word w = new Word(new Position(1, 2), Direction.HORIZONTAL, new Tile(1, 'a', 2), new Tile(2, 'b', 3), new Tile(3, 'c', 4));

        RemoteServiceContextAccessor.expectPlayer(player);
        memoryWordsDao.addMemoryWord(board, playerHand, new MemoryWord(1, w));
        replay(memoryWordsDao);

        service.addMemoryWord(123, new MemoryWord(1, w));
        verify(memoryWordsDao);
    }

    @Test
    public void test_removeMemoryWord() {
        RemoteServiceContextAccessor.expectPlayer(player);
        memoryWordsDao.removeMemoryWord(board, playerHand, 1);
        replay(memoryWordsDao);

        service.removeMemoryWord(123, 1);
        verify(memoryWordsDao);
    }

    @Test
    public void test_removeMemoryWords() {
        RemoteServiceContextAccessor.expectPlayer(player);
        memoryWordsDao.clearMemoryWords(board, playerHand);
        replay(memoryWordsDao);

        service.clearMemoryWords(123);
        verify(memoryWordsDao);
    }

    @Test
    public void test_getMemoryWords() {
        final Word w = new Word(new Position(1, 2), Direction.HORIZONTAL, new Tile(1, 'a', 2), new Tile(2, 'b', 3), new Tile(3, 'c', 4));

        RemoteServiceContextAccessor.expectPlayer(player);
        expect(memoryWordsDao.getMemoryWords(board, playerHand)).andReturn(Arrays.asList(new MemoryWord(1, w)));
        replay(memoryWordsDao);

        final MemoryWord[] words = service.getMemoryWords(123);
        assertEquals(1, words.length);
        assertSame(1, words[0].getNumber());
        assertSame(w, words[0].getWord());
        verify(memoryWordsDao);
    }
*/
}
