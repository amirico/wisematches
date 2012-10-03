package wisematches.playground.tourney.regular.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Language;
import wisematches.playground.tourney.regular.*;

import java.util.Date;

import static junit.framework.Assert.*;
import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/accounts-config.xml",
		"classpath:/config/playground-config.xml"
})
public class HibernateTourneyManagerTest {
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private RegularTourneyManager regularTourneyManager;

	public HibernateTourneyManagerTest() {
	}

	@Test
	public void test_subscription() throws TourneySubscriptionException {
		final Session session = sessionFactory.getCurrentSession();

		System.out.println(session.save(new HibernateTourney(1, new Date(System.currentTimeMillis() + 10000000L))));


		final TourneySubscriptionListener l = createStrictMock(TourneySubscriptionListener.class);
		regularTourneyManager.addTourneySubscriptionListener(l);

		l.subscribed(isA(TourneySubscription.class));
		l.subscribed(isA(TourneySubscription.class));
		l.subscribed(isA(TourneySubscription.class));
		l.unsubscribed(isA(TourneySubscription.class));
		replay(l);

		try {
			regularTourneyManager.subscribe(0, 101, Language.RU, TourneySection.ADVANCED);
			fail("Exception must be here");
		} catch (TourneySubscriptionException ignore) {
		}

		regularTourneyManager.subscribe(1, 101, Language.RU, TourneySection.ADVANCED);
		try {
			regularTourneyManager.subscribe(1, 101, Language.RU, TourneySection.ADVANCED);
			fail("Exception must be here");
		} catch (TourneySubscriptionException ignore) {
		}

		regularTourneyManager.subscribe(1, 102, Language.RU, TourneySection.ADVANCED);
		regularTourneyManager.subscribe(1, 103, Language.RU, TourneySection.GRANDMASTER);

		TourneySubscriptions subscriptions = regularTourneyManager.getSubscriptionStatus(1);
		assertEquals(0, subscriptions.getPlayers(Language.EN));
		assertEquals(0, subscriptions.getPlayers(Language.EN, TourneySection.ADVANCED));
		assertEquals(3, subscriptions.getPlayers(Language.RU));
		assertEquals(2, subscriptions.getPlayers(Language.RU, TourneySection.ADVANCED));
		assertEquals(1, subscriptions.getPlayers(Language.RU, TourneySection.GRANDMASTER));

		final TourneySubscription subscription = regularTourneyManager.getSubscription(1, 101);
		assertEquals(1, subscription.getTourney());
		assertEquals(101, subscription.getPlayer());
		assertEquals(Language.RU, subscription.getLanguage());
		assertEquals(TourneySection.ADVANCED, subscription.getSection());

		regularTourneyManager.unsubscribe(1, 102, Language.RU, TourneySection.GRANDMASTER);
		subscriptions = regularTourneyManager.getSubscriptionStatus(1);
		assertEquals(3, subscriptions.getPlayers(Language.RU));
		assertEquals(2, subscriptions.getPlayers(Language.RU, TourneySection.ADVANCED));
		assertEquals(1, subscriptions.getPlayers(Language.RU, TourneySection.GRANDMASTER));

		regularTourneyManager.unsubscribe(1, 102, Language.RU, TourneySection.ADVANCED);
		subscriptions = regularTourneyManager.getSubscriptionStatus(1);
		assertEquals(2, subscriptions.getPlayers(Language.RU));
		assertEquals(1, subscriptions.getPlayers(Language.RU, TourneySection.ADVANCED));
		assertEquals(1, subscriptions.getPlayers(Language.RU, TourneySection.GRANDMASTER));

		verify(l);
	}

	@Test
	public void test_getTournamentEntity() {
		final Session session = sessionFactory.getCurrentSession();

		final HibernateTourney t = new HibernateTourney(1, new Date(System.currentTimeMillis() + 10000000L));
		System.out.println(session.save(t));

		final HibernateTourneyDivision d1 = new HibernateTourneyDivision(t, Language.RU, TourneySection.ADVANCED);
		System.out.println(session.save(d1));

		final HibernateTourneyDivision d2 = new HibernateTourneyDivision(t, Language.RU, TourneySection.GRANDMASTER);
		System.out.println(session.save(d2));

		final Criteria c0 = session.createCriteria(HibernateTourneyDivision.class);
		c0.createAlias("tourney", "t").add(Restrictions.eq("t.tourney", t.getTourney()));
		assertEquals(2, c0.list().size());

		final HibernateTourneyRound r1 = new HibernateTourneyRound(1, d1, 10);
		assertNotNull(r1.getStartedDate());
		session.save(r1);

		final HibernateTourneyRound r2 = new HibernateTourneyRound(2, d1, 10);
		assertNotNull(r2.getStartedDate());
		session.save(r2);

		final HibernateTourneyGroup g1 = new HibernateTourneyGroup(1, r1, new long[]{1, 2});
		assertNotNull(g1.getStartedDate());
		session.save(g1);

		final HibernateTourneyGroup g2 = new HibernateTourneyGroup(2, r1, new long[]{3, 4, 5, 6});
		assertNotNull(g2.getStartedDate());
		session.save(g2);

		final RegularTourney tourney = regularTourneyManager.getTournamentEntity(new RegularTourney.Id(1));
		assertSame(t, tourney);

		final TourneyDivision division = regularTourneyManager.getTournamentEntity(new TourneyDivision.Id(1, Language.RU, TourneySection.ADVANCED));
		assertSame(d1, division);

		final TourneyRound round = regularTourneyManager.getTournamentEntity(new TourneyRound.Id(1, Language.RU, TourneySection.ADVANCED, 1));
		assertSame(r1, round);

		final TourneyGroup group = regularTourneyManager.getTournamentEntity(new TourneyGroup.Id(1, Language.RU, TourneySection.ADVANCED, 1, 1));
		assertSame(g1, group);
	}
}
