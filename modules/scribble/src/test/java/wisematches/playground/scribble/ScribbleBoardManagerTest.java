package wisematches.playground.scribble;

import org.junit.Before;
import org.junit.Test;
import org.springframework.orm.hibernate3.HibernateTemplate;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.BoardCreationException;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.dictionary.DictionaryNotFoundException;
import wisematches.playground.scribble.bank.TilesBank;
import wisematches.playground.scribble.bank.TilesBankingHouse;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleBoardManagerTest {
	private ScribbleBoardManager scribbleRoomManager;

	private HibernateTemplate hibernateTemplate;
	private DictionaryManager dictionaryManager;
	private TilesBankingHouse tilesBankingHouse;

	private static final Locale LOCALE = new Locale("en");

	public ScribbleBoardManagerTest() {
	}

	@Before
	public void testSetUp() {
		dictionaryManager = createStrictMock(DictionaryManager.class);
		tilesBankingHouse = createStrictMock(TilesBankingHouse.class);
		hibernateTemplate = createStrictMock(HibernateTemplate.class);

		scribbleRoomManager = new ScribbleBoardManager();
		scribbleRoomManager.setDictionaryManager(dictionaryManager);
		scribbleRoomManager.setTilesBankingHouse(tilesBankingHouse);
		scribbleRoomManager.setHibernateTemplate(hibernateTemplate);
	}

	@Test
	public void testLoadBoardImpl() throws BoardLoadingException, DictionaryNotFoundException {
		final ScribbleSettings settings = new ScribbleSettings("Mock", Language.EN, 3);

		final Dictionary dictionary = createNiceMock(Dictionary.class);
		final TilesBank tilesBank = new TilesBank();

		final ScribbleBoard board = createStrictMock(ScribbleBoard.class);
		expect(board.getGameSettings()).andReturn(settings);
		expect(board.getPlayersHands()).andReturn(Arrays.<ScribblePlayerHand>asList(null, null, null));
		board.initGameAfterLoading(tilesBank, dictionary);
		replay(board);

		expect(hibernateTemplate.get(ScribbleBoard.class, 1L)).andReturn(board);
		hibernateTemplate.evict(board);
		replay(hibernateTemplate);

		expect(dictionaryManager.getDictionary(LOCALE)).andReturn(dictionary);
		replay(dictionaryManager);

		expect(tilesBankingHouse.createTilesBank(LOCALE, 3, true)).andReturn(tilesBank);
		replay(tilesBankingHouse);

		final ScribbleBoard board1 = scribbleRoomManager.loadBoardImpl(1L);
		assertSame(board, board1);

		verify(board);
		verify(hibernateTemplate);
		verify(dictionaryManager);
		verify(tilesBankingHouse);
	}

	@Test
	public void testCreateBoardImpl() throws DictionaryNotFoundException, BoardCreationException {
		final ScribbleSettings settings = new ScribbleSettings("Mock", Language.EN, 3);

		final Dictionary dictionary = createNiceMock(Dictionary.class);
		expect(dictionary.getLocale()).andReturn(LOCALE);
		replay(dictionary);

		final TilesBank tilesBank = new TilesBank(new TilesBank.TilesInfo('A', 100, 1));

		expect(dictionaryManager.getDictionary(LOCALE)).andReturn(dictionary);
		replay(dictionaryManager);

		expect(tilesBankingHouse.createTilesBank(LOCALE, 2, true)).andReturn(tilesBank);
		replay(tilesBankingHouse);

		scribbleRoomManager.setDictionaryManager(dictionaryManager);
		scribbleRoomManager.setTilesBankingHouse(tilesBankingHouse);

		final ScribbleBoard board1 = scribbleRoomManager.createBoardImpl(settings,
				Arrays.asList(Personality.person(1), Personality.person(2)));
		assertNotNull(board1);

		verify(dictionaryManager);
		verify(tilesBankingHouse);
	}

	@Test
	public void testSaveBoardImpl() {
		final ScribbleBoard board = createNiceMock(ScribbleBoard.class);

		hibernateTemplate.saveOrUpdate(board);
		hibernateTemplate.flush();
		hibernateTemplate.evict(board);
		replay(hibernateTemplate);

		replay(dictionaryManager);
		replay(tilesBankingHouse);

		scribbleRoomManager.saveBoardImpl(board);

		verify(hibernateTemplate);
		verify(dictionaryManager);
		verify(tilesBankingHouse);
	}

	@Test
	public void testLoadActivePlayerBoards() {
		final List<Long> ids = Arrays.asList(1L, 2L, 3L);

		expect(hibernateTemplate.find(isA(String.class), eq(1L))).andReturn(ids);
		replay(hibernateTemplate);

		replay(dictionaryManager);
		replay(tilesBankingHouse);

		final Collection<Long> longCollection = scribbleRoomManager.loadActivePlayerBoards(Personality.person(1));
		assertSame(ids, longCollection);

		verify(hibernateTemplate);
		verify(dictionaryManager);
		verify(tilesBankingHouse);
	}
}
