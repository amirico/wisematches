package wisematches.playground.tournament.upcoming.impl;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Language;
import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.upcoming.TournamentAnnouncement;
import wisematches.playground.tournament.upcoming.TournamentSubscriptionManager;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.assertEquals;
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

	@Autowired
	private PlatformTransactionManager transactionManager;

	private HibernateTournamentSubscriptionManager tournamentSubscriptionManager;

	public HibernateTournamentSubscriptionManagerTest() {
	}

	@Before
	public void setUp() throws ParseException {
		tournamentSubscriptionManager = new HibernateTournamentSubscriptionManager();
		tournamentSubscriptionManager.setSessionFactory(sessionFactory);
		tournamentSubscriptionManager.setTransactionManager(transactionManager);
		tournamentSubscriptionManager.setCronExpression("0 0 0 * * ?");
	}

	@Test
	public void asd() {
		final TournamentAnnouncement announcement = tournamentSubscriptionManager.getTournamentAnnouncement();
		assertEquals(2, announcement.getNumber());
		assertEquals(2, announcement.getBoughtTickets(Language.RU, TournamentSection.GRANDMASTER));
		assertEquals(1, announcement.getBoughtTickets(Language.RU, TournamentSection.INTERMEDIATE));

//		final Date midnight = new Date(((System.currentTimeMillis()) / 86400000L) * 86400000L);
//		tournamentSubscriptionManager.breakingDayTime(midnight);
/*
		tournamentSubscriptionManager.

				assertNotNull(tournamentSubscriptionManager);
*/
	}
}
