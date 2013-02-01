package wisematches.playground.scribble;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import wisematches.core.Language;
import wisematches.core.Personality;
import wisematches.core.PersonalityManager;
import wisematches.core.RobotType;
import wisematches.core.personality.DefaultRobot;
import wisematches.playground.BoardCreationException;
import wisematches.playground.BoardLoadingException;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.scribble.bank.TilesBank;
import wisematches.playground.scribble.bank.TilesBankingHouse;
import wisematches.playground.scribble.bank.impl.TilesBankInfoEditor;

import java.util.Arrays;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleBoardManagerTest {
	private Session session;

	private PersonalityManager playerManager;
	private DictionaryManager dictionaryManager;
	private TilesBankingHouse tilesBankingHouse;

	private ScribblePlayManager scribblePlayManager;

	private Personality player1 = new DefaultRobot(RobotType.DULL);
	private Personality player2 = new DefaultRobot(RobotType.TRAINEE);

	public ScribbleBoardManagerTest() {
	}

	@Before
	public void testSetUp() {
		session = createStrictMock(Session.class);

		final SessionFactory sessionFactory = createMock(SessionFactory.class);
		expect(sessionFactory.getCurrentSession()).andReturn(session).anyTimes();
		replay(sessionFactory);

		playerManager = createStrictMock(PersonalityManager.class);
		dictionaryManager = createStrictMock(DictionaryManager.class);
		tilesBankingHouse = createStrictMock(TilesBankingHouse.class);

		scribblePlayManager = new ScribblePlayManager();
		scribblePlayManager.setSessionFactory(sessionFactory);
		scribblePlayManager.setPersonalityManager(playerManager);
		scribblePlayManager.setDictionaryManager(dictionaryManager);
		scribblePlayManager.setTilesBankingHouse(tilesBankingHouse);
	}

	@Test
	public void testLoadBoardImpl() throws BoardLoadingException {
		final ScribbleSettings settings = new ScribbleSettings("Mock", Language.EN, 3);

		final Dictionary dictionary = createNiceMock(Dictionary.class);
		final TilesBank tilesBank = new TilesBank(new TilesBankInfoEditor(Language.EN).createTilesBankInfo());

		final ScribbleBoard board = createStrictMock(ScribbleBoard.class);
		expect(board.getSettings()).andReturn(settings);
		expect(board.getPlayersCount()).andReturn(3);
		board.initGameAfterLoading(tilesBank, dictionary, playerManager);
		replay(board);

		expect(session.get(ScribbleBoard.class, 1L)).andReturn(board);
		session.evict(board);
		replay(session);

		expect(dictionaryManager.getDictionary(Language.EN)).andReturn(dictionary);
		replay(dictionaryManager);

		expect(tilesBankingHouse.createTilesBank(Language.EN, 3, true)).andReturn(tilesBank);
		replay(tilesBankingHouse);

		final ScribbleBoard board1 = scribblePlayManager.loadBoardImpl(1L);
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

		scribblePlayManager.setDictionaryManager(dictionaryManager);
		scribblePlayManager.setTilesBankingHouse(tilesBankingHouse);

		final ScribbleBoard board1 = scribblePlayManager.createBoardImpl(settings, Arrays.asList(player1, player2), null);
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

		scribblePlayManager.saveBoardImpl(board);

		verify(session);
		verify(dictionaryManager);
		verify(tilesBankingHouse);
	}
}
