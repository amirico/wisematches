package wisematches.playground.tournament.upcoming.impl;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.playground.tournament.upcoming.TournamentSubscriptionManager;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.assertNotNull;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional()
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml"
})
public class HibernateTournamentSubscriptionManagerTest {
	@Autowired
	private SessionFactory sessionFactory;
	private HibernateTournamentSubscriptionManager tournamentSubscriptionManager;

	public HibernateTournamentSubscriptionManagerTest() {
	}

	@Before
	public void setUp() throws ParseException {
		tournamentSubscriptionManager = new HibernateTournamentSubscriptionManager();
		tournamentSubscriptionManager.setSessionFactory(sessionFactory);
		tournamentSubscriptionManager.setCronExpression("0 0 0 * * ?");
	}

	@Test
	public void asd() {
		Date midnight = new Date(((System.currentTimeMillis()) / 86400000L) * 86400000L);
		tournamentSubscriptionManager.breakingDayTime(midnight);
/*
		tournamentSubscriptionManager.

				assertNotNull(tournamentSubscriptionManager);
*/
	}
}
