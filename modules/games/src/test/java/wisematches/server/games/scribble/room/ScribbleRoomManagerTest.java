package wisematches.server.games.scribble.room;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.Test;
import wisematches.kernel.player.Player;
import wisematches.server.core.room.BoardCreationException;
import wisematches.server.core.room.BoardLoadingException;
import wisematches.server.core.words.dict.Dictionary;
import wisematches.server.core.words.dict.DictionaryManager;
import wisematches.server.core.words.dict.DictionaryNotFoundException;
import wisematches.server.games.scribble.bank.TilesBank;
import wisematches.server.games.scribble.bank.TilesBankingHouse;
import wisematches.server.games.scribble.board.ScribbleBoard;
import wisematches.server.games.scribble.board.ScribbleBoardDao;
import wisematches.server.games.scribble.board.ScribbleSettings;
import wisematches.server.games.scribble.room.ScribbleRoomManager;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleRoomManagerTest {
    private ScribbleRoomManager scribbleRoomManager;

    private DictionaryManager dictionaryManager;
    private TilesBankingHouse tilesBankingHouse;
    private ScribbleBoardDao scribbleBoardDao;
    private static final Locale LOCALE = new Locale("en");

    @Before
    public void testSetUp() {
        dictionaryManager = createStrictMock(DictionaryManager.class);
        tilesBankingHouse = createStrictMock(TilesBankingHouse.class);
        scribbleBoardDao = createStrictMock(ScribbleBoardDao.class);

        scribbleRoomManager = new ScribbleRoomManager();
        scribbleRoomManager.setDictionaryManager(dictionaryManager);
        scribbleRoomManager.setTilesBankingHouse(tilesBankingHouse);
        scribbleRoomManager.setScribbleBoardDao(scribbleBoardDao);
    }

    @Test
    public void testLoadBoard() throws BoardLoadingException, DictionaryNotFoundException {
        final ScribbleSettings settings = new ScribbleSettings("Mock", new Date(), 3, "en");

        final Dictionary dictionary = createNiceMock(Dictionary.class);
        final TilesBank tilesBank = new TilesBank(new TilesBank.TilesInfo[0]);

        final ScribbleBoard board = createStrictMock(ScribbleBoard.class);
        expect(board.getGameSettings()).andReturn(settings);
        board.setDictionary(dictionary);
        board.setTilesBank(tilesBank);
        replay(board);

        expect(scribbleBoardDao.getScribbleBoard(1L)).andReturn(board);
        replay(scribbleBoardDao);

        expect(dictionaryManager.getDictionary(LOCALE)).andReturn(dictionary);
        replay(dictionaryManager);

        expect(tilesBankingHouse.createTilesBank(LOCALE, 3, true)).andReturn(tilesBank);
        replay(tilesBankingHouse);

        final ScribbleBoard board1 = scribbleRoomManager.loadBoard(1L);
        assertSame(board, board1);

        verify(board);
        verify(scribbleBoardDao);
        verify(dictionaryManager);
        verify(tilesBankingHouse);
    }

    @Test
    public void testCreateBoard() throws DictionaryNotFoundException, BoardCreationException {
        final ScribbleSettings settings = new ScribbleSettings("Mock", new Date(), 3, "en");

        final Dictionary dictionary = createNiceMock(Dictionary.class);
        expect(dictionary.getLocale()).andReturn(LOCALE);
        replay(dictionary);

        final TilesBank tilesBank = new TilesBank(new TilesBank.TilesInfo[0]);

        expect(dictionaryManager.getDictionary(LOCALE)).andReturn(dictionary);
        replay(dictionaryManager);

        expect(tilesBankingHouse.createTilesBank(LOCALE, 3, true)).andReturn(tilesBank);
        replay(tilesBankingHouse);

        final ScribbleBoard board1 = scribbleRoomManager.createBoard(settings);
        assertNotNull(board1);

        verify(dictionaryManager);
        verify(tilesBankingHouse);
    }

    @Test
    public void testSaveBoard() {
        final ScribbleBoard board = createNiceMock(ScribbleBoard.class);

        scribbleBoardDao.saveScribbleBoard(board);
        replay(scribbleBoardDao);

        replay(dictionaryManager);
        replay(tilesBankingHouse);

        scribbleRoomManager.saveBoard(board);

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

    @Test
    public void testLoadWaitingBoards() {
        final Collection<Long> ids = Arrays.asList(1L, 2L, 3L);

        expect(scribbleBoardDao.getWaitingBoards()).andReturn(ids);
        replay(scribbleBoardDao);

        replay(dictionaryManager);
        replay(tilesBankingHouse);

        final Collection<Long> longCollection = scribbleRoomManager.loadWaitingBoards();
        assertSame(ids, longCollection);

        verify(scribbleBoardDao);
        verify(dictionaryManager);
        verify(tilesBankingHouse);
    }
}
