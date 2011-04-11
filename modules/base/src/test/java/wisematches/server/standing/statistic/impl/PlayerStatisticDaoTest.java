package wisematches.server.standing.statistic.impl;

import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.server.personality.Personality;
import wisematches.server.standing.statistic.PlayerStatistic;
import wisematches.server.standing.statistic.PlayerStatisticRating;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/test-server-base-config.xml",
		"classpath:/config/database-config.xml",
		"classpath:/config/server-base-config.xml"
})
public class PlayerStatisticDaoTest {
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private PlayerStatisticDao playerStatisticDao;

	public PlayerStatisticDaoTest() {
	}

	@Test
	public void test_playerStatistic() throws InterruptedException {
		final Personality person = Personality.person(1L);
		final HibernatePlayerStatistic statistic = new HibernatePlayerStatistic(person);
		final long time = System.currentTimeMillis() - 1000;

		statistic.setAverageTurnTime(1);
		statistic.incrementDrawGames();
		statistic.incrementDrawGames();
		statistic.setLastMoveTime(new Date(300000));
		statistic.incrementLostGames();
		statistic.incrementLostGames();
		statistic.incrementLostGames();
		statistic.incrementLostGames();
		statistic.incrementTimeouts();
		statistic.incrementTimeouts();
		statistic.incrementTimeouts();
		statistic.incrementTimeouts();
		statistic.incrementTimeouts();
		statistic.incrementTurnsCount();
		statistic.incrementTurnsCount();
		statistic.incrementTurnsCount();
		statistic.incrementTurnsCount();
		statistic.incrementTurnsCount();
		statistic.incrementTurnsCount();
		statistic.incrementWonGames();
		statistic.incrementWonGames();
		statistic.incrementWonGames();
		statistic.incrementWonGames();
		statistic.incrementWonGames();
		statistic.incrementWonGames();
		statistic.incrementWonGames();
		statistic.incrementActiveGames();
		statistic.incrementActiveGames();
		statistic.incrementActiveGames();
		statistic.incrementActiveGames();
		statistic.incrementActiveGames();
		statistic.incrementActiveGames();
		statistic.incrementActiveGames();
		statistic.incrementActiveGames();
		playerStatisticDao.savePlayerStatistic(statistic);

		final HibernatePlayerStatisticRating ri1 = statistic.getAllGamesStatisticRating();
		ri1.setHighestRating(1);
		ri1.setLowestRating(2);
		ri1.setAverageMovesPerGame(3);
		ri1.setAverageOpponentRating(4);
		ri1.setAverageRating(5);
		ri1.setHighestWonOpponentRating(6);
		ri1.setHighestWonOpponentId(7);
		ri1.setLowestLostOpponentRating(8);
		ri1.setLowestLostOpponentId(9);
		playerStatisticDao.savePlayerStatistic(statistic);

/*
		final HibernatePlayerStatisticRating ri2 = statistic.getNinetyDaysRatingInfo();
		ri2.setHighestRating(10);
		ri2.setLowestRating(20);
		ri2.setAverageMovesPerGame(30);
		ri2.setAverageOpponentRating(40);
		ri2.setAverageRating(50);
		ri2.setHighestWonOpponentRating(60);
		ri2.setHighestWonOpponentId(70);
		ri2.setLowestLostOpponentRating(80);
		ri2.setLowestLostOpponentId(90);
		playerStatisticManager.updatePlayerStatistic(statistic);

		final HibernatePlayerStatisticRating ri3 = statistic.getThirtyDaysRatingInfo();
		ri3.setHighestRating(100);
		ri3.setLowestRating(200);
		ri3.setAverageMovesPerGame(300);
		ri3.setAverageOpponentRating(400);
		ri3.setAverageRating(500);
		ri3.setHighestWonOpponentRating(600);
		ri3.setHighestWonOpponentId(700);
		ri3.setLowestLostOpponentRating(800);
		ri3.setLowestLostOpponentId(900);
		playerStatisticManager.updatePlayerStatistic(statistic);

		final HibernatePlayerStatisticRating ri4 = statistic.getYearRatingInfo();
		ri4.setHighestRating(1000);
		ri4.setLowestRating(2000);
		ri4.setAverageMovesPerGame(3000);
		ri4.setAverageOpponentRating(4000);
		ri4.setAverageRating(5000);
		ri4.setHighestWonOpponentRating(6000);
		ri4.setHighestWonOpponentId(7000);
		ri4.setLowestLostOpponentRating(8000);
		ri4.setLowestLostOpponentId(9000);
		playerStatisticManager.updatePlayerStatistic(statistic);
*/

		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();

		final HibernatePlayerStatistic s = playerStatisticDao.loadPlayerStatistic(person);
		assertEquals(1, s.getAverageTurnTime());
		assertEquals(2, s.getDrawGames());
		assertEquals(300000, s.getLastMoveTime().getTime());
		assertEquals(4, s.getLostGames());
		assertEquals(5, s.getTimeouts());
		assertEquals(6, s.getTurnsCount());
		assertEquals(7, s.getWonGames());
		assertEquals(8, s.getActiveGames());
		assertTrue(s.getUpdateTime().getTime() >= time);

		final PlayerStatisticRating sri1 = s.getAllGamesStatisticRating();
		assertEquals(1, sri1.getHighestRating());
		assertEquals(2, sri1.getLowestRating());
		assertEquals(3, sri1.getAverageMovesPerGame());
		assertEquals(4, sri1.getAverageOpponentRating());
		assertEquals(5, sri1.getAverageRating());
		assertEquals(6, sri1.getHighestWonOpponentRating());
		assertEquals(7, sri1.getHighestWonOpponentId());
		assertEquals(8, sri1.getLowestLostOpponentRating());
		assertEquals(9, sri1.getLowestLostOpponentId());

/*
		final HibernatePlayerStatisticRating sri2 = s.getNinetyDaysRatingInfo();
		assertEquals(10, sri2.getHighestRating());
		assertEquals(20, sri2.getLowestRating());
		assertEquals(30, sri2.getAverageMovesPerGame());
		assertEquals(40, sri2.getAverageOpponentRating());
		assertEquals(50, sri2.getAverageRating());
		assertEquals(60, sri2.getHighestWonOpponentRating());
		assertEquals(70, sri2.getHighestWonOpponentId());
		assertEquals(80, sri2.getLowestLostOpponentRating());
		assertEquals(90, sri2.getLowestLostOpponentId());

		final HibernatePlayerStatisticRating sri3 = s.getThirtyDaysRatingInfo();
		assertEquals(100, sri3.getHighestRating());
		assertEquals(200, sri3.getLowestRating());
		assertEquals(300, sri3.getAverageMovesPerGame());
		assertEquals(400, sri3.getAverageOpponentRating());
		assertEquals(500, sri3.getAverageRating());
		assertEquals(600, sri3.getHighestWonOpponentRating());
		assertEquals(700, sri3.getHighestWonOpponentId());
		assertEquals(800, sri3.getLowestLostOpponentRating());
		assertEquals(900, sri3.getLowestLostOpponentId());

		final HibernatePlayerStatisticRating sri4 = statistic.getYearRatingInfo();
		assertEquals(1000, sri4.getHighestRating());
		assertEquals(2000, sri4.getLowestRating());
		assertEquals(3000, sri4.getAverageMovesPerGame());
		assertEquals(4000, sri4.getAverageOpponentRating());
		assertEquals(5000, sri4.getAverageRating());
		assertEquals(6000, sri4.getHighestWonOpponentRating());
		assertEquals(7000, sri4.getHighestWonOpponentId());
		assertEquals(8000, sri4.getLowestLostOpponentRating());
		assertEquals(9000, sri4.getLowestLostOpponentId());
*/

		playerStatisticDao.removePlayerStatistic(s);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		assertNull(playerStatisticDao.loadPlayerStatistic(person));
	}
}
