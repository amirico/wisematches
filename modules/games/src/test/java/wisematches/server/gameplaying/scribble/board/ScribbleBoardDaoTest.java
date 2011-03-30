package wisematches.server.gameplaying.scribble.board;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.SimpleJdbcTestUtils;
import wisematches.server.gameplaying.board.GameMoveException;
import wisematches.server.gameplaying.board.GameResolution;
import wisematches.server.gameplaying.board.PassTurnMove;
import wisematches.server.gameplaying.room.search.ExpiringBoard;
import wisematches.server.gameplaying.scribble.Direction;
import wisematches.server.gameplaying.scribble.Position;
import wisematches.server.gameplaying.scribble.Tile;
import wisematches.server.gameplaying.scribble.Word;
import wisematches.server.gameplaying.scribble.bank.TilesBank;
import wisematches.server.player.Player;

import javax.sql.DataSource;
import java.util.*;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-config.xml",
		"classpath:/config/server-base-config.xml",
		"classpath:/config/game-scribble-config.xml",
		"classpath:/config/test-game-modules-config.xml"})
public class ScribbleBoardDaoTest {
	@Autowired
	private DataSource dataSource;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private ScribbleBoardDao scribbleBoardDao;

	private static final Locale LOCALE = new Locale("ru");

	public ScribbleBoardDaoTest() {
	}

	@Before
	public void onSetUp() throws Exception {
		// This test clear scribble_board and scribble_player tables after execution.
		SimpleJdbcTestUtils.deleteFromTables(new SimpleJdbcTemplate(dataSource), "scribble_board", "scribble_player");
	}

	@After
	public void onTearDown() throws Exception {
		// This test clear scribble_board and scribble_player tables after execution.
		SimpleJdbcTestUtils.deleteFromTables(new SimpleJdbcTemplate(dataSource), "scribble_board", "scribble_player");
	}

	@Test
	public void test_activeGames() {
		final Player p1 = createMockPlayer(1L, 800);
		final Player p2 = createMockPlayer(2L, 800);
		final Player p3 = createMockPlayer(3L, 800);
		final Player p4 = createMockPlayer(4L, 800);

		final TilesBank tilesBank = new TilesBank(new TilesBank.TilesInfo('a', 100, 1));
		final wisematches.server.gameplaying.dictionary.Dictionary dictionary = createStrictMock(wisematches.server.gameplaying.dictionary.Dictionary.class);
		expect(dictionary.getLocale()).andReturn(LOCALE);
		expect(dictionary.getLocale()).andReturn(LOCALE);
		replay(dictionary);

		final ScribbleSettings ss1 = new ScribbleSettings("This is scribble board game", "ru", 3);
		final ScribbleBoard sb1 = new ScribbleBoard(ss1, Arrays.asList(p2, p3, p4), tilesBank, dictionary);
		scribbleBoardDao.saveScribbleBoard(sb1);

		assertTrue(sb1.isGameActive());
		assertEquals(0, scribbleBoardDao.getActiveBoards(p1).size());
		assertEquals(1, scribbleBoardDao.getActiveBoards(p2).size());
		assertEquals(1, scribbleBoardDao.getActiveBoards(p3).size());
		assertEquals(1, scribbleBoardDao.getActiveBoards(p4).size());

		// Next boards
		final ScribbleSettings ss2 = new ScribbleSettings("This is scribble board game", "ru", 3);
		final ScribbleBoard sb2 = new ScribbleBoard(ss2, Arrays.asList(p1, p2), tilesBank, dictionary);
		scribbleBoardDao.saveScribbleBoard(sb2);

		assertTrue(sb2.isGameActive());
		assertEquals(1, scribbleBoardDao.getActiveBoards(p1).size());
		assertEquals(2, scribbleBoardDao.getActiveBoards(p2).size());
		assertEquals(1, scribbleBoardDao.getActiveBoards(p3).size());
		assertEquals(1, scribbleBoardDao.getActiveBoards(p4).size());
	}

	@Test
	public void test_findExpiringBoards() {
		final Player p1 = createMockPlayer(1L, 800);
		final Player p2 = createMockPlayer(2L, 800);
		final Player p3 = createMockPlayer(3L, 800);

		final TilesBank tilesBank = new TilesBank(new TilesBank.TilesInfo('a', 100, 1));
		final wisematches.server.gameplaying.dictionary.Dictionary dictionary = createStrictMock(wisematches.server.gameplaying.dictionary.Dictionary.class);
		expect(dictionary.getLocale()).andReturn(LOCALE);
		replay(dictionary);

		final ScribbleSettings ss1 = new ScribbleSettings("This is scribble board game", "ru", 3);
		final ScribbleBoard sb1 = new ScribbleBoard(ss1, Arrays.asList(p1, p2, p3), tilesBank, dictionary);
		scribbleBoardDao.saveScribbleBoard(sb1);

		final Collection<ExpiringBoard> collection = scribbleBoardDao.findExpiringBoards();
		assertEquals(1, collection.size());

		final ExpiringBoard info = collection.iterator().next();
		assertEquals(sb1.getBoardId(), info.getBoardId());
		assertEquals(sb1.getGameSettings().getDaysPerMove(), info.getDaysPerMove());
		assertDates(sb1.getLastMoveTime(), info.getLastMoveTime());
	}

	@Test
	public void test_getGamesCount() {
		scribbleBoardDao.getGamesCount(null);
		scribbleBoardDao.getGamesCount(EnumSet.of(GameResolution.FINISHED));
		scribbleBoardDao.getGamesCount(EnumSet.of(GameResolution.TIMEOUT, GameResolution.RESIGNED));
	}
/*

	@Test
	public void test_getRatedBoards() throws GameMoveException {
		final Player p1 = createMockPlayer(1L, 800);
		final Player p2 = createMockPlayer(2L, 800);
		final Player p3 = createMockPlayer(3L, 800);

		final TilesBank tilesBank = new TilesBank(new TilesBank.TilesInfo('a', 100, 1));
		final wisematches.server.gameplaying.dictionary.Dictionary dictionary = createStrictMock(wisematches.server.gameplaying.dictionary.Dictionary.class);
		expect(dictionary.getLocale()).andReturn(LOCALE);
		replay(dictionary);

		final ScribbleSettings ss1 = new ScribbleSettings("This is scribble board game", "ru", 3);
		final ScribbleBoard sb1 = new ScribbleBoard(ss1, Arrays.asList(p1, p2, p3), tilesBank, dictionary);

		sb1.terminate();

		scribbleBoardDao.saveScribbleBoard(sb1);

		final RatedBoardsInfo boardsInfo = scribbleBoardDao.getRatedBoards(1L, new Date(System.currentTimeMillis() - 1000000L), null);
		final Iterator<RatedBoardsInfo.Record> iter = boardsInfo.iterator();
		assertTrue(iter.hasNext());

		final RatedBoardsInfo.Record record = iter.next();
		assertEquals(sb1.getBoardId(), record.getBoardId());
		assertDates(sb1.getFinishedTime(), record.getTime());
		assertEquals(sb1.getPlayerHand(p1.getId()).getRating(), record.getRating());

		assertFalse(iter.hasNext());

		final RatedBoardsInfo boardsInfo2 = scribbleBoardDao.getRatedBoards(1L, new Date(System.currentTimeMillis() - 2000000L), new Date(System.currentTimeMillis() - 1000000L));
		assertFalse(boardsInfo2.iterator().hasNext());
	}
*/

	@Test
	public void test_saveLoadScribbleBoard() throws GameMoveException {
		final Player p1 = createMockPlayer(1L, 800);
		final Player p2 = createMockPlayer(2L, 800);
		final Player p3 = createMockPlayer(3L, 800);

		final TilesBank tilesBank = new TilesBank(new TilesBank.TilesInfo('a', 100, 1));
		final wisematches.server.gameplaying.dictionary.Dictionary dictionary = createStrictMock(wisematches.server.gameplaying.dictionary.Dictionary.class);
		expect(dictionary.getWord("aaaa")).andReturn(new wisematches.server.gameplaying.dictionary.Word("aaaa", LOCALE));
		replay(dictionary);

		final ScribbleSettings ss = new ScribbleSettings("This is scribble board game", "ru", 3);
		final ScribbleBoard sb = new ScribbleBoard(ss, Arrays.asList(p1, p2, p3), tilesBank, dictionary);
		scribbleBoardDao.saveScribbleBoard(sb);

		ScribblePlayerHand hand = sb.getPlayerTurn();
		final MakeWordMove move1 = new MakeWordMove(hand.getPlayerId(), new Word(new Position(7, 6), Direction.HORIZONTAL, Arrays.copyOfRange(hand.getTiles(), 0, 4)));
		sb.makeMove(move1);
		scribbleBoardDao.saveScribbleBoard(sb);


		//Test loading board from storage after clearing
		scribbleBoardDao.getHibernateTemplate().clear();

		final ScribbleBoard loaded = checkLoadedDatabase(sb);
		Tile[] tiles = Arrays.copyOfRange(loaded.getPlayerTurn().getTiles(), 0, 4);
		tiles[1] = move1.getWord().getTiles()[2];
		final MakeWordMove move2 = new MakeWordMove(loaded.getPlayerTurn().getPlayerId(), new Word(new Position(6, 8), Direction.VERTICAL, tiles));
		loaded.makeMove(move2);
		final PassTurnMove move3 = new PassTurnMove(loaded.getPlayerTurn().getPlayerId());
		loaded.makeMove(move3);
		final ExchangeTilesMove move4 = new ExchangeTilesMove(loaded.getPlayerTurn().getPlayerId(), new int[]{
				loaded.getPlayerTurn().getTiles()[0].getNumber(),
				loaded.getPlayerTurn().getTiles()[1].getNumber()
		});
		loaded.makeMove(move4);
		scribbleBoardDao.saveScribbleBoard(loaded);

		ScribbleBoard loaded2 = checkLoadedDatabase2(loaded);
		scribbleBoardDao.getHibernateTemplate().delete(loaded2);
	}


	private ScribbleBoard checkLoadedDatabase2(ScribbleBoard sb) {
		final TilesBank tilesBank = new TilesBank(new TilesBank.TilesInfo('a', 100, 1));
		final wisematches.server.gameplaying.dictionary.Dictionary dictionary = createStrictMock(wisematches.server.gameplaying.dictionary.Dictionary.class);
		expect(dictionary.getLocale()).andReturn(LOCALE);
		replay(dictionary);

		final ScribbleBoard board = scribbleBoardDao.getScribbleBoard(sb.getBoardId());
		board.initGameAfterLoading(tilesBank, dictionary);

		assertEquals(4, board.getGameMoves().size());
		for (int i = 0; i < 4; i++) {
			assertEquals(sb.getGameMoves().get(i), board.getGameMoves().get(i));
		}
		return board;
	}

	private ScribbleBoard checkLoadedDatabase(ScribbleBoard originalBoard) {
		final TilesBank tilesBank = new TilesBank(new TilesBank.TilesInfo('a', 100, 1));
		final wisematches.server.gameplaying.dictionary.Dictionary dictionary = createStrictMock(wisematches.server.gameplaying.dictionary.Dictionary.class);
		expect(dictionary.getWord("aaaa")).andReturn(new wisematches.server.gameplaying.dictionary.Word("aaaa", LOCALE));
		replay(dictionary);

		final ScribbleBoard loadedBoard = scribbleBoardDao.getScribbleBoard(originalBoard.getBoardId());
		loadedBoard.initGameAfterLoading(tilesBank, dictionary);
		assertNotNull(loadedBoard);

		final ScribbleSettings originalSettings = originalBoard.getGameSettings();
		final ScribbleSettings loadedSettings = loadedBoard.getGameSettings();
		assertEquals(originalSettings.getTitle(), loadedSettings.getTitle());
		assertEquals(originalSettings.getLanguage(), loadedSettings.getLanguage());
		assertEquals(originalSettings.getDaysPerMove(), loadedSettings.getDaysPerMove());

		assertEquals(originalBoard.getPassesCount(), loadedBoard.getPassesCount());
		assertDates(originalBoard.getLastMoveTime(), loadedBoard.getLastMoveTime());
		assertEquals(originalBoard.getBoardId(), loadedBoard.getBoardId());
		assertEquals(originalBoard.getGameResolution(), loadedBoard.getGameResolution());
		assertEquals(originalBoard.getPlayerTurn().getPlayerId(), loadedBoard.getPlayerTurn().getPlayerId());
		assertTrue(loadedBoard.isGameActive());

		final List<ScribblePlayerHand> playerHands = loadedBoard.getPlayersHands();
		assertEquals(3, playerHands.size());
		assertEquals(playerHands.get(0).getPlayerId(), 1L);
		assertEquals(playerHands.get(1).getPlayerId(), 2L);
		assertEquals(playerHands.get(2).getPlayerId(), 3L);

		checkPlayer(originalBoard, loadedBoard, 1L);
		checkPlayer(originalBoard, loadedBoard, 2L);
		checkPlayer(originalBoard, loadedBoard, 3L);

		assertEquals(1, loadedBoard.getGameMoves().size());
		assertEquals(originalBoard.getGameMoves().get(0), loadedBoard.getGameMoves().get(0));
		assertNotNull(loadedBoard.getGameMoves().get(0).getMoveTime());

		assertEquals("75 tiles must be n bank after restoring", 75, tilesBank.getTilesLimit());
		return loadedBoard;
	}

	private void checkPlayer(final ScribbleBoard originalBoard, final ScribbleBoard loadedBoard, final long playerId) {
		assertArrayEquals(originalBoard.getPlayerHand(playerId).getTiles(), loadedBoard.getPlayerHand(playerId).getTiles());
		assertEquals(originalBoard.getPlayerHand(playerId).getPlayerIndex(), loadedBoard.getPlayerHand(playerId).getPlayerIndex());
		assertEquals(originalBoard.getPlayerHand(playerId).getPoints(), loadedBoard.getPlayerHand(playerId).getPoints());
	}

	private Player createMockPlayer(long id, int rating) {
		Player p = createMock(Player.class);
		expect(p.getId()).andReturn(id).anyTimes();
		expect(p.getRating()).andReturn(rating).anyTimes();
		replay(p);
		return p;
	}

	private void assertDates(Date d1, Date d2) {
		assertEquals((d1.getTime() / 1000) * 1000, (d2.getTime() / 1000) * 1000);
	}
}

