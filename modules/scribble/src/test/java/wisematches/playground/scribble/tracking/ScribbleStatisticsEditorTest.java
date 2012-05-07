package wisematches.playground.scribble.tracking;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.account.*;
import wisematches.playground.scribble.Direction;
import wisematches.playground.scribble.Position;
import wisematches.playground.scribble.Tile;
import wisematches.playground.scribble.Word;
import wisematches.playground.tracking.impl.PlayerTrackingCenterDao;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/config/database-junit-config.xml",
        "classpath:/config/accounts-config.xml",
        "classpath:/config/playground-config.xml",
        "classpath:/config/scribble-junit-config.xml"})
public class ScribbleStatisticsEditorTest {
    private Account person;

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private PlayerTrackingCenterDao playerTrackingCenterDao;

    public ScribbleStatisticsEditorTest() {
    }

    @Before
    public void setUp() throws InadmissibleUsernameException, DuplicateAccountException {
        final String uuid = UUID.randomUUID().toString();
        person = accountManager.createAccount(new AccountEditor(uuid + "@mock.wm", uuid, "AS").createAccount());
    }

    @After
    public void tearDown() throws UnknownAccountException {
        accountManager.removeAccount(person);
    }

    @Test
    public void test_playerStatistic() throws InterruptedException, InadmissibleUsernameException, DuplicateAccountException {
        final ScribbleStatisticsEditor editor = playerTrackingCenterDao.loadPlayerStatistic(ScribbleStatisticsEditor.class, person);
        assertNotNull(editor);

        editor.setActiveGames(1);
        editor.setAverageMovesPerGame(2);
        editor.setDraws(3);
        editor.setFinishedGames(4);
        editor.setLoses(5);
        editor.setTimeouts(6);
        editor.setResigned(7);
        editor.setWins(8);
        editor.setStalemates(9);
        playerTrackingCenterDao.savePlayerStatistic(editor);

        editor.setAverageMoveTime(1);
        editor.setAverageWordLength(2);
        editor.setAveragePoints(3);
        editor.setExchangesCount(4);
        editor.setLastLongestWord(new Word(new Position(1, 2), Direction.HORIZONTAL, new Tile(1, 'A', 1), new Tile(2, 'B', 2)));
        editor.setLastValuableWord(new Word(new Position(3, 4), Direction.VERTICAL, new Tile(3, 'C', 3), new Tile(4, 'D', 4)));
        editor.setLastMoveTime(new Date(30000));
        editor.setHighestPoints(5);
        editor.setLowestPoints(6);
        editor.setPassesCount(7);
        editor.setTurnsCount(8);
        editor.setWordsCount(9);
        playerTrackingCenterDao.savePlayerStatistic(editor);

        editor.setAverageRating((short) 1);
        editor.setAverageOpponentRating((short) 2);
        editor.setHighestRating((short) 3);
        editor.setHighestWonOpponentId(4);
        editor.setHighestWonOpponentRating((short) 5);
        editor.setLowestRating((short) 6);
        editor.setLowestLostOpponentId(7);
        editor.setLowestLostOpponentRating((short) 8);
        playerTrackingCenterDao.savePlayerStatistic(editor);

        final ScribbleStatisticsEditor s = playerTrackingCenterDao.loadPlayerStatistic(ScribbleStatisticsEditor.class, person);
        assertEquals(1, s.getActiveGames());
        assertEquals(2, s.getAverageMovesPerGame(), 0.00000000000001);
        assertEquals(3, s.getDraws());
        assertEquals(4, s.getFinishedGames());
        assertEquals(5, s.getLoses());
        assertEquals(6, s.getTimeouts());
        assertEquals(7, s.getResigned());
        assertEquals(8, s.getWins());
        assertEquals(9, s.getStalemates());
        assertEquals(8 + 5 + 3, s.getRatedGames());
        assertEquals(4 - (8 + 5 + 3), s.getUnratedGames());

        assertEquals(1, s.getAverageRating(), 0.00000000000001);
        assertEquals(2, s.getAverageOpponentRating(), 0.00000000000001);
        assertEquals((short) 3, s.getHighestRating());
        assertEquals(4, s.getHighestWonOpponentId());
        assertEquals((short) 5, s.getHighestWonOpponentRating());
        assertEquals((short) 6, s.getLowestRating());
        assertEquals(7, s.getLowestLostOpponentId());
        assertEquals((short) 8, s.getLowestLostOpponentRating());

        assertEquals(1, s.getAverageMoveTime(), 0.00000000000001);
        assertEquals(2, s.getAverageWordLength(), 0.00000000000001);
        assertEquals(3, s.getAveragePoints(), 0.00000000000001);
        assertEquals(4, s.getExchangesCount());
        assertEquals(new Word(new Position(1, 2), Direction.HORIZONTAL, new Tile(1, 'A', 1), new Tile(2, 'B', 2)), s.getLastLongestWord());
        assertEquals(new Word(new Position(3, 4), Direction.VERTICAL, new Tile(3, 'C', 3), new Tile(4, 'D', 4)), s.getLastValuableWord());

        assertEquals(new Date(30000), s.getLastMoveTime());
        assertEquals(5, s.getHighestPoints());
        assertEquals(6, s.getLowestPoints());
        assertEquals(7, s.getPassesCount());
        assertEquals(8, s.getTurnsCount());
        assertEquals(9, s.getWordsCount());

        playerTrackingCenterDao.removePlayerStatistic(s);
        assertNull(playerTrackingCenterDao.loadPlayerStatistic(ScribbleStatisticsEditor.class, person));
    }
}
