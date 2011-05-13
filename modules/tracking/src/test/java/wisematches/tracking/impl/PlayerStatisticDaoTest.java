package wisematches.tracking.impl;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Personality;
import wisematches.server.standing.statistic.RatingsStatistic;
import wisematches.server.standing.statistic.statistician.GamesStatisticEditor;
import wisematches.server.standing.statistic.statistician.RatingsStatisticEditor;

import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/test-server-base-config.xml",
		"classpath:/src/main/resources/config/database-config.xml",
		"classpath:/config/playground-config.xml"
})
public class PlayerStatisticDaoTest {
	private Account person;

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private AccountManager accountManager;

	@Autowired
	private PlayerStatisticDao playerStatisticDao;

	public PlayerStatisticDaoTest() {
	}

	@Before
	public void setUp() throws InadmissibleUsernameException, DuplicateAccountException {
		final String uuid = UUID.randomUUID().toString();
//		person = accountManager.createAccount(new AccountEditor(uuid + "@mock.wm", uuid, "AS").createAccount());
	}

	@After
	public void tearDown() throws UnknownAccountException {
//		accountManager.removeAccount(person);
	}

	@Test
	public void test_playerStatistic() throws InterruptedException, InadmissibleUsernameException, DuplicateAccountException {
		final MockPlayerStatistic statistic = playerStatisticDao.loadPlayerStatistic(MockPlayerStatistic.class, Personality.person(1002));
		assertNotNull(statistic);

		final GamesStatisticEditor gs = statistic.getGamesStatisticEditor();
		gs.setActive(1);
		gs.setAverageMovesPerGame(2);
		gs.setDraws(3);
		gs.setFinished(4);
		gs.setLoses(5);
		gs.setTimeouts(6);
		gs.setUnrated(7);
		gs.setWins(8);
		playerStatisticDao.savePlayerStatistic(statistic);

		final MockMovesStatisticEditor ms = statistic.getMovesStatisticEditor();
		ms.setAverageTurnTime(1);
		ms.setAverageWordLength(2);
		ms.setAvgPoints(3);
		ms.setExchangesCount(4);
		ms.setLastLongestWord("asd");
		ms.setLastMoveTime(new Date(30000));
		ms.setLastValuableWord("qwe");
		ms.setMaxPoints(5);
		ms.setMinPoints(6);
		ms.setPassesCount(7);
		ms.setTurnsCount(8);
		ms.setWordsCount(9);
		playerStatisticDao.savePlayerStatistic(statistic);

		final RatingsStatisticEditor rs = statistic.getRatingsStatisticEditor();
		rs.setAverage((short) 1);
		rs.setAverageOpponentRating((short) 2);
		rs.setHighest((short) 3);
		rs.setHighestWonOpponentId(4);
		rs.setHighestWonOpponentRating((short) 5);
		rs.setLowest((short) 6);
		rs.setLowestLostOpponentId(7);
		rs.setLowestLostOpponentRating((short) 8);
		playerStatisticDao.savePlayerStatistic(statistic);

		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();

		final MockPlayerStatistic s = playerStatisticDao.loadPlayerStatistic(MockPlayerStatistic.class, person);

		final GamesStatisticEditor gsil = s.getGamesStatisticEditor();
		assertEquals(1, gsil.getActive());
		assertEquals(2, gsil.getAverageMovesPerGame());
		assertEquals(3, gsil.getDraws());
		assertEquals(4, gsil.getFinished());
		assertEquals(5, gsil.getLoses());
		assertEquals(6, gsil.getTimeouts());
		assertEquals(7, gsil.getUnrated());
		assertEquals(8, gsil.getWins());

		final RatingsStatistic sri1 = s.getRatingsStatistic();
		assertEquals((short) 1, sri1.getAverage());
		assertEquals((short) 2, sri1.getAverageOpponentRating());
		assertEquals((short) 3, sri1.getHighest());
		assertEquals(4, sri1.getHighestWonOpponentId());
		assertEquals((short) 5, sri1.getHighestWonOpponentRating());
		assertEquals((short) 6, sri1.getLowest());
		assertEquals(7, sri1.getLowestLostOpponentId());
		assertEquals((short) 8, sri1.getLowestLostOpponentRating());

		final MockMovesStatistic msil = s.getMovesStatistic();
		assertEquals(1, msil.getAverageTurnTime());
		assertEquals(2, msil.getAverageWordLength());
		assertEquals(3, msil.getAvgPoints());
		assertEquals(4, msil.getExchangesCount());
		assertEquals("asd", msil.getLastLongestWord());
		assertEquals(new Date(30000), msil.getLastMoveTime());
		assertEquals("qwe", msil.getLastValuableWord());
		assertEquals(5, msil.getMaxPoints());
		assertEquals(6, msil.getMinPoints());
		assertEquals(7, msil.getPassesCount());
		assertEquals(8, msil.getTurnsCount());
		assertEquals(9, msil.getWordsCount());

		playerStatisticDao.removePlayerStatistic(s);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		assertNull(playerStatisticDao.loadPlayerStatistic(MockPlayerStatistic.class, person));
	}
}
