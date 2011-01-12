package wisematches.server.games.scribble.board;

import static org.easymock.EasyMock.*;
import org.hibernate.SessionFactory;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import wisematches.kernel.player.Player;
import wisematches.server.core.board.GameMoveException;
import wisematches.server.core.board.GameState;
import wisematches.server.core.board.PassTurnMove;
import wisematches.server.core.room.ExpiringBoardInfo;
import wisematches.server.core.room.RatedBoardsInfo;
import wisematches.server.core.words.dict.Dictionary;
import wisematches.server.games.scribble.*;
import wisematches.server.games.scribble.bank.TilesBank;
import wisematches.server.games.scribble.Direction;
import wisematches.server.games.scribble.Tile;
import wisematches.server.games.scribble.Word;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/config/test-game-modules-config.xml"})
public class ScribbleBoardDaoTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Resource
    private SessionFactory sessionFactory;
    @Autowired
    private ScribbleBoardDao scribbleBoardDao;

    private static final Locale LOCALE = new Locale("ru");

    @Before
    public void onSetUp() throws Exception {
        // This test clear scribble_board and scribble_player tables after execution.
        deleteFromTables("scribble_board", "scribble_player");
    }

    @After
    public void onTearDown() throws Exception {
        // This test clear scribble_board and scribble_player tables after execution.
        deleteFromTables("scribble_board", "scribble_player");
    }

    @Test
    public void test_getWaitingGames() {
        final Player p1 = createMockPlayer(1L, 800);
        final Player p2 = createMockPlayer(2L, 800);

        final TilesBank tilesBank = new TilesBank(new TilesBank.TilesInfo('a', 100, 1));
        final Dictionary dictionary = createStrictMock(Dictionary.class);
        expect(dictionary.getLocale()).andReturn(LOCALE);
        expect(dictionary.getLocale()).andReturn(LOCALE);
        replay(dictionary);

        final ScribbleSettings ss1 = new ScribbleSettings("This is scribble board game", new Date(), 3, "ru", 3, 100, 1000);
        final ScribbleBoard sb1 = new ScribbleBoard(ss1);
        sb1.setTilesBank(tilesBank);
        sb1.setDictionary(dictionary);
        sb1.addPlayer(p1);
        sb1.addPlayer(p2);
        scribbleBoardDao.saveScribbleBoard(sb1);

        final Collection<Long> ids1 = scribbleBoardDao.getWaitingBoards();
        assertEquals(GameState.WAITING, sb1.getGameState());
        assertEquals(1, ids1.size());
        assertEquals(sb1.getBoardId(), ids1.toArray()[0]);

        assertEquals(1, scribbleBoardDao.getActiveBoards(p1).size());

        // Next boards
        final ScribbleSettings ss2 = new ScribbleSettings("This is scribble board game", new Date(), 3, "ru");
        final ScribbleBoard sb2 = new ScribbleBoard(ss2);
        sb2.setTilesBank(tilesBank);
        sb2.setDictionary(dictionary);
        sb2.addPlayer(p1);
        sb2.addPlayer(p2);
        scribbleBoardDao.saveScribbleBoard(sb2);

        final Collection<Long> idss = scribbleBoardDao.getWaitingBoards();
        assertEquals(GameState.WAITING, sb2.getGameState());
        assertEquals(2, idss.size());
        assertEquals(sb1.getBoardId(), idss.toArray()[0]);
        assertEquals(sb2.getBoardId(), idss.toArray()[1]);

        assertEquals(2, scribbleBoardDao.getActiveBoards(p1).size());
    }

    @Test
    public void test_activeGames() {
        final Player p1 = createMockPlayer(1L, 800);
        final Player p2 = createMockPlayer(2L, 800);
        final Player p3 = createMockPlayer(3L, 800);
        final Player p4 = createMockPlayer(4L, 800);

        final TilesBank tilesBank = new TilesBank(new TilesBank.TilesInfo('a', 100, 1));
        final Dictionary dictionary = createStrictMock(Dictionary.class);
        expect(dictionary.getLocale()).andReturn(LOCALE);
        expect(dictionary.getLocale()).andReturn(LOCALE);
        replay(dictionary);

        final ScribbleSettings ss1 = new ScribbleSettings("This is scribble board game", new Date(), 3, "ru", 3, 100, 1000);
        final ScribbleBoard sb1 = new ScribbleBoard(ss1);
        sb1.setTilesBank(tilesBank);
        sb1.setDictionary(dictionary);
        sb1.addPlayer(p1);
        sb1.addPlayer(p2);
        sb1.addPlayer(p3);
        scribbleBoardDao.saveScribbleBoard(sb1);

        assertEquals(GameState.IN_PROGRESS, sb1.getGameState());
        assertEquals(0, scribbleBoardDao.getWaitingBoards().size());
        assertEquals(1, scribbleBoardDao.getActiveBoards(p1).size());
        assertEquals(1, scribbleBoardDao.getActiveBoards(p2).size());
        assertEquals(1, scribbleBoardDao.getActiveBoards(p3).size());
        assertEquals(0, scribbleBoardDao.getActiveBoards(p4).size());

        // Next boards
        final ScribbleSettings ss2 = new ScribbleSettings("This is scribble board game", new Date(), 3, "ru", 3, 100, 1000);
        final ScribbleBoard sb2 = new ScribbleBoard(ss2);
        sb2.setTilesBank(tilesBank);
        sb2.setDictionary(dictionary);
        sb2.addPlayer(p1);
        sb2.addPlayer(p2);
        scribbleBoardDao.saveScribbleBoard(sb2);

        assertEquals(GameState.WAITING, sb2.getGameState());
        assertEquals(1, scribbleBoardDao.getWaitingBoards().size());
        assertEquals(2, scribbleBoardDao.getActiveBoards(p1).size());
        assertEquals(2, scribbleBoardDao.getActiveBoards(p2).size());
        assertEquals(1, scribbleBoardDao.getActiveBoards(p3).size());
        assertEquals(0, scribbleBoardDao.getActiveBoards(p4).size());

        sb2.addPlayer(p4);
        scribbleBoardDao.saveScribbleBoard(sb2);
        assertEquals(GameState.IN_PROGRESS, sb1.getGameState());
        assertEquals(0, scribbleBoardDao.getWaitingBoards().size());
        assertEquals(2, scribbleBoardDao.getActiveBoards(p1).size());
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
        final Dictionary dictionary = createStrictMock(Dictionary.class);
        expect(dictionary.getLocale()).andReturn(LOCALE);
        replay(dictionary);

        final ScribbleSettings ss1 = new ScribbleSettings("This is scribble board game", new Date(), 3, "ru", 3, 100, 1000);
        final ScribbleBoard sb1 = new ScribbleBoard(ss1);
        sb1.setTilesBank(tilesBank);
        sb1.setDictionary(dictionary);
        sb1.addPlayer(p1);
        sb1.addPlayer(p2);
        sb1.addPlayer(p3);

        scribbleBoardDao.saveScribbleBoard(sb1);

        final Collection<ExpiringBoardInfo> collection = scribbleBoardDao.findExpiringBoards();
        assertEquals(1, collection.size());

        final ExpiringBoardInfo info = collection.iterator().next();
        assertEquals(sb1.getBoardId(), info.getBoardId());
        assertEquals(sb1.getGameSettings().getDaysPerMove(), info.getDaysPerMove());
        assertEquals(sb1.getLastMoveTime(), info.getLastMoveTime());
    }

    @Test
    public void test_getGamesCount() {
        assertEquals(0, scribbleBoardDao.getGamesCount(null));
        scribbleBoardDao.getGamesCount(EnumSet.of(GameState.DRAW));
        scribbleBoardDao.getGamesCount(EnumSet.of(GameState.DRAW, GameState.INTERRUPTED));
    }

    @Test
    public void test_getRatedBoards() throws GameMoveException {
        final Player p1 = createMockPlayer(1L, 800);
        final Player p2 = createMockPlayer(2L, 800);
        final Player p3 = createMockPlayer(3L, 800);

        final TilesBank tilesBank = new TilesBank(new TilesBank.TilesInfo('a', 100, 1));
        final Dictionary dictionary = createStrictMock(Dictionary.class);
        expect(dictionary.getLocale()).andReturn(LOCALE);
        replay(dictionary);

        final ScribbleSettings ss1 = new ScribbleSettings("This is scribble board game", new Date(), 3, "ru", 3, 100, 1000);
        final ScribbleBoard sb1 = new ScribbleBoard(ss1);
        sb1.setTilesBank(tilesBank);
        sb1.setDictionary(dictionary);
        sb1.addPlayer(p1);
        sb1.addPlayer(p2);
        sb1.addPlayer(p3);

        sb1.terminate();

        scribbleBoardDao.saveScribbleBoard(sb1);

        final RatedBoardsInfo boardsInfo = scribbleBoardDao.getRatedBoards(1L, new Date(System.currentTimeMillis() - 1000000L), null);
        final Iterator<RatedBoardsInfo.Record> iter = boardsInfo.iterator();
        assertTrue(iter.hasNext());

        final RatedBoardsInfo.Record record = iter.next();
        assertEquals(sb1.getBoardId(), record.getBoardId());
        assertEquals(sb1.getFinishedTime(), record.getTime());
        assertEquals(sb1.getPlayerHand(p1.getId()).getRating(), record.getRating());

        assertFalse(iter.hasNext());

        final RatedBoardsInfo boardsInfo2 = scribbleBoardDao.getRatedBoards(1L, new Date(System.currentTimeMillis() - 2000000L), new Date(System.currentTimeMillis() - 1000000L));
        assertFalse(boardsInfo2.iterator().hasNext());
    }

    @Test
    public void test_saveLoadScribbleBoard() throws GameMoveException {
        final Player p1 = createMockPlayer(1L, 800);
        final Player p2 = createMockPlayer(2L, 800);
        final Player p3 = createMockPlayer(3L, 800);

        final TilesBank tilesBank = new TilesBank(new TilesBank.TilesInfo('a', 100, 1));
        final Dictionary dictionary = createStrictMock(Dictionary.class);
        expect(dictionary.getLocale()).andReturn(LOCALE);
        expect(dictionary.getWord("aaaa")).andReturn(new wisematches.server.core.words.dict.Word("aaaa", LOCALE));
        replay(dictionary);

        final ScribbleSettings ss = new ScribbleSettings("This is scribble board game", new Date(), 3, "ru", 3, 100, 1000);
        final ScribbleBoard sb = new ScribbleBoard(ss);
        sb.setTilesBank(tilesBank);
        sb.setDictionary(dictionary);
        scribbleBoardDao.saveScribbleBoard(sb);

        sb.addPlayer(p1);
        scribbleBoardDao.saveScribbleBoard(sb);

        sb.addPlayer(p2);
        scribbleBoardDao.saveScribbleBoard(sb);

        sb.addPlayer(p3);
        scribbleBoardDao.saveScribbleBoard(sb);

        ScribblePlayerHand hand = sb.getPlayerTrun();
        final MakeWordMove move1 = new MakeWordMove(hand.getPlayerId(), new Word(new Position(7, 6), Direction.HORIZONTAL, Arrays.copyOfRange(hand.getTiles(), 0, 4)));
        sb.makeMove(move1);
        scribbleBoardDao.saveScribbleBoard(sb);


        //Test loading board from storage after clearing
        scribbleBoardDao.getHibernateTemplate().clear();

        final ScribbleBoard loaded = checkLoadedDatabase(sb);
        Tile[] tiles = Arrays.copyOfRange(loaded.getPlayerTrun().getTiles(), 0, 4);
        tiles[1] = move1.getWord().getTiles()[2];
        final MakeWordMove move2 = new MakeWordMove(loaded.getPlayerTrun().getPlayerId(), new Word(new Position(6, 8), Direction.VERTICAL, tiles));
        loaded.makeMove(move2);
        final PassTurnMove move3 = new PassTurnMove(loaded.getPlayerTrun().getPlayerId());
        loaded.makeMove(move3);
        final ExchangeTilesMove move4 = new ExchangeTilesMove(loaded.getPlayerTrun().getPlayerId(), new int[]{
                loaded.getPlayerTrun().getTiles()[0].getNumber(),
                loaded.getPlayerTrun().getTiles()[1].getNumber()
        });
        loaded.makeMove(move4);
        scribbleBoardDao.saveScribbleBoard(loaded);

        //Test loading board from storage after clearing
        sessionFactory.getCurrentSession().clear();

        ScribbleBoard loaded2 = checkLoadedDatabase2(loaded);
        scribbleBoardDao.getHibernateTemplate().delete(loaded2);
    }


    private ScribbleBoard checkLoadedDatabase2(ScribbleBoard sb) {
        final TilesBank tilesBank = new TilesBank(new TilesBank.TilesInfo('a', 100, 1));
        final Dictionary dictionary = createStrictMock(Dictionary.class);
        expect(dictionary.getLocale()).andReturn(LOCALE);
        replay(dictionary);

        final ScribbleBoard board = scribbleBoardDao.getScribbleBoard(sb.getBoardId());
        board.setDictionary(dictionary);
        board.setTilesBank(tilesBank);

        assertEquals(4, board.getGameMoves().size());
        for (int i = 0; i < 4; i++) {
            assertEquals(sb.getGameMoves().get(i), board.getGameMoves().get(i));
        }
        return board;
    }

    private ScribbleBoard checkLoadedDatabase(ScribbleBoard originalBoard) {
        final TilesBank tilesBank = new TilesBank(new TilesBank.TilesInfo('a', 100, 1));
        final Dictionary dictionary = createStrictMock(Dictionary.class);
        expect(dictionary.getLocale()).andReturn(LOCALE);
        expect(dictionary.getWord("aaaa")).andReturn(new wisematches.server.core.words.dict.Word("aaaa", LOCALE));
        replay(dictionary);

        final ScribbleBoard loadedBoard = scribbleBoardDao.getScribbleBoard(originalBoard.getBoardId());
        loadedBoard.setDictionary(dictionary);
        loadedBoard.setTilesBank(tilesBank);
        assertNotNull(loadedBoard);

        final ScribbleSettings originalSettings = originalBoard.getGameSettings();
        final ScribbleSettings loadedSettings = loadedBoard.getGameSettings();
        assertEquals(originalSettings.getTitle(), loadedSettings.getTitle());
        assertEquals(originalSettings.getLanguage(), loadedSettings.getLanguage());
        assertEquals(originalSettings.getDaysPerMove(), loadedSettings.getDaysPerMove());
        assertEquals(originalSettings.getMaxPlayers(), loadedSettings.getMaxPlayers());
        assertEquals(originalSettings.getMaxRating(), loadedSettings.getMaxRating());
        assertEquals(originalSettings.getMinRating(), loadedSettings.getMinRating());
        assertEquals(originalSettings.getCreateDate(), loadedSettings.getCreateDate());

        assertEquals(originalBoard.getPassesCount(), loadedBoard.getPassesCount());
        assertEquals(originalBoard.getLastMoveTime(), loadedBoard.getLastMoveTime());
        assertEquals(originalBoard.getBoardId(), loadedBoard.getBoardId());
        assertEquals(originalBoard.getGameState(), loadedBoard.getGameState());
        assertEquals(GameState.IN_PROGRESS, loadedBoard.getGameState());
        assertEquals(originalBoard.getPlayerTrun().getPlayerId(), loadedBoard.getPlayerTrun().getPlayerId());

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
        assertTrue(loadedBoard.getGameMoves().get(0).getMoveTime() != 0);

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
}

