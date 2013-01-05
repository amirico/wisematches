package wisematches.playground.scribble;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;
import org.junit.Before;
import org.junit.Test;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.BoardCreationException;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.GameState;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.scribble.bank.TilesBank;
import wisematches.playground.scribble.bank.TilesBankingHouse;
import wisematches.playground.scribble.bank.impl.TilesBankInfoEditor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleBoardManagerTest {
    private Session session;

    private DictionaryManager dictionaryManager;
    private TilesBankingHouse tilesBankingHouse;

    private ScribbleBoardManager scribbleRoomManager;

    public ScribbleBoardManagerTest() {
    }

    @Before
    public void testSetUp() {
        session = createStrictMock(Session.class);

        final SessionFactory sessionFactory = createMock(SessionFactory.class);
        expect(sessionFactory.getCurrentSession()).andReturn(session).anyTimes();
        replay(sessionFactory);

        dictionaryManager = createStrictMock(DictionaryManager.class);
        tilesBankingHouse = createStrictMock(TilesBankingHouse.class);

        scribbleRoomManager = new ScribbleBoardManager();
        scribbleRoomManager.setSessionFactory(sessionFactory);
        scribbleRoomManager.setDictionaryManager(dictionaryManager);
        scribbleRoomManager.setTilesBankingHouse(tilesBankingHouse);
    }

    @Test
    public void testLoadBoardImpl() throws BoardLoadingException {
        final ScribbleSettings settings = new ScribbleSettings("Mock", Language.EN, 3);

        final Dictionary dictionary = createNiceMock(Dictionary.class);
        final TilesBank tilesBank = new TilesBank(new TilesBankInfoEditor(Language.EN).createTilesBankInfo());

        final ScribbleBoard board = createStrictMock(ScribbleBoard.class);
        expect(board.getSettings()).andReturn(settings);
        expect(board.getPlayersHands()).andReturn(Arrays.<ScribblePlayerHand>asList(null, null, null));
        board.initGameAfterLoading(tilesBank, dictionary);
        replay(board);

        expect(session.get(ScribbleBoard.class, 1L)).andReturn(board);
        session.evict(board);
        replay(session);

        expect(dictionaryManager.getDictionary(Language.EN)).andReturn(dictionary);
        replay(dictionaryManager);

        expect(tilesBankingHouse.createTilesBank(Language.EN, 3, true)).andReturn(tilesBank);
        replay(tilesBankingHouse);

        final ScribbleBoard board1 = scribbleRoomManager.loadBoardImpl(1L);
        assertSame(board, board1);

        verify(board);
        verify(session);
        verify(dictionaryManager);
        verify(tilesBankingHouse);
    }

    @Test
    public void testCreateBoardImpl() throws BoardCreationException {
        final ScribbleSettings settings = new ScribbleSettings("Mock", Language.EN, 3);

        final Dictionary dictionary = createNiceMock(Dictionary.class);
        replay(dictionary);

        final TilesBankInfoEditor editor = new TilesBankInfoEditor(Language.EN);
        final TilesBank tilesBank = new TilesBank(editor.add('A', 100, 1).createTilesBankInfo());

        expect(dictionaryManager.getDictionary(Language.EN)).andReturn(dictionary);
        replay(dictionaryManager);

        expect(tilesBankingHouse.createTilesBank(Language.EN, 2, true)).andReturn(tilesBank);
        replay(tilesBankingHouse);

        scribbleRoomManager.setDictionaryManager(dictionaryManager);
        scribbleRoomManager.setTilesBankingHouse(tilesBankingHouse);

        final ScribbleBoard board1 = scribbleRoomManager.createBoardImpl(settings,
                null, Arrays.asList(Personality.person(1), Personality.person(2)));
        assertNotNull(board1);

        verify(dictionaryManager);
        verify(tilesBankingHouse);
    }

    @Test
    public void testSaveBoardImpl() {
        final ScribbleBoard board = createNiceMock(ScribbleBoard.class);

        session.saveOrUpdate(board);
        session.flush();
        session.evict(board);
        replay(session);

        replay(dictionaryManager);
        replay(tilesBankingHouse);

        scribbleRoomManager.saveBoardImpl(board);

        verify(session);
        verify(dictionaryManager);
        verify(tilesBankingHouse);
    }

    @Test
    public void testActivePlayerBoards() {
        final List<Long> ids = Arrays.asList(1L, 2L, 3L);

        final Criteria criteria = createMock(Criteria.class);
        expect(criteria.createAlias("playerHands", "hand")).andReturn(criteria);
        expect(criteria.add(anyObject(Criterion.class))).andReturn(criteria).anyTimes();
        expect(criteria.setProjection(anyObject(Projection.class))).andReturn(criteria).anyTimes();
        expect(criteria.list()).andReturn(ids);
        replay(criteria);

        expect(session.createCriteria(ScribbleBoard.class)).andReturn(criteria);
        replay(session);

        replay(dictionaryManager);
        replay(tilesBankingHouse);

        final Collection<Long> longCollection = scribbleRoomManager.loadPlayerBoards(Personality.person(1), GameState.ACTIVE, null, null, null);
        assertSame(ids, longCollection);

        verify(criteria);
        verify(session);
        verify(dictionaryManager);
        verify(tilesBankingHouse);
    }
}
