package wisematches.playground.tourney.regular.impl;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import wisematches.personality.Language;
import wisematches.playground.RatingManager;
import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.TourneyEntityListener;
import wisematches.playground.tourney.regular.*;

import java.text.ParseException;
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
	private RatingManager ratingManager;

	@Autowired
	private SessionFactory sessionFactory;

	private HibernateTourneyManager tourneyManager;

	public HibernateTourneyManagerTest() {
	}

	@Before
	public void init() throws ParseException {
		tourneyManager = new HibernateTourneyManager();
		tourneyManager.setCronExpression(new CronExpression("* * * ? * 2#1"));
		tourneyManager.setSessionFactory(sessionFactory);
		tourneyManager.setTaskExecutor(new SyncTaskExecutor());
	}

	@Test
	public void test_subscription() throws TourneySubscriptionException {
		final Session session = sessionFactory.getCurrentSession();

		session.save(new HibernateTourney(1, new Date(System.currentTimeMillis() + 10000000L)));


		final TourneySubscriptionListener l = createStrictMock(TourneySubscriptionListener.class);
		tourneyManager.addTourneySubscriptionListener(l);

		l.subscribed(isA(TourneySubscription.class));
		l.subscribed(isA(TourneySubscription.class));
		l.subscribed(isA(TourneySubscription.class));
		l.unsubscribed(isA(TourneySubscription.class));
		replay(l);

		try {
			tourneyManager.subscribe(0, 101, Language.RU, TourneySection.ADVANCED);
			fail("Exception must be here");
		} catch (TourneySubscriptionException ignore) {
		}

		tourneyManager.subscribe(1, 101, Language.RU, TourneySection.ADVANCED);
		try {
			tourneyManager.subscribe(1, 101, Language.RU, TourneySection.ADVANCED);
			fail("Exception must be here");
		} catch (TourneySubscriptionException ignore) {
		}

		tourneyManager.subscribe(1, 102, Language.RU, TourneySection.ADVANCED);
		tourneyManager.subscribe(1, 103, Language.RU, TourneySection.GRANDMASTER);

		TourneySubscriptions subscriptions = tourneyManager.getSubscriptionStatus(1);
		assertEquals(0, subscriptions.getPlayers(Language.EN));
		assertEquals(0, subscriptions.getPlayers(Language.EN, TourneySection.ADVANCED));
		assertEquals(3, subscriptions.getPlayers(Language.RU));
		assertEquals(2, subscriptions.getPlayers(Language.RU, TourneySection.ADVANCED));
		assertEquals(1, subscriptions.getPlayers(Language.RU, TourneySection.GRANDMASTER));

		final TourneySubscription subscription = tourneyManager.getSubscription(1, 101);
		assertEquals(1, subscription.getTourney());
		assertEquals(101, subscription.getPlayer());
		assertEquals(Language.RU, subscription.getLanguage());
		assertEquals(TourneySection.ADVANCED, subscription.getSection());

		tourneyManager.unsubscribe(1, 102, Language.RU, TourneySection.GRANDMASTER);
		subscriptions = tourneyManager.getSubscriptionStatus(1);
		assertEquals(3, subscriptions.getPlayers(Language.RU));
		assertEquals(2, subscriptions.getPlayers(Language.RU, TourneySection.ADVANCED));
		assertEquals(1, subscriptions.getPlayers(Language.RU, TourneySection.GRANDMASTER));

		tourneyManager.unsubscribe(1, 102, Language.RU, TourneySection.ADVANCED);
		subscriptions = tourneyManager.getSubscriptionStatus(1);
		assertEquals(2, subscriptions.getPlayers(Language.RU));
		assertEquals(1, subscriptions.getPlayers(Language.RU, TourneySection.ADVANCED));
		assertEquals(1, subscriptions.getPlayers(Language.RU, TourneySection.GRANDMASTER));

		tourneyManager.removeTourneySubscriptionListener(l);

		verify(l);
	}

	@Test
	public void test_getTournamentEntity() {
		final Session session = sessionFactory.getCurrentSession();

		final HibernateTourney t = new HibernateTourney(1, new Date(System.currentTimeMillis() + 10000000L));
		final HibernateTourneyDivision d = new HibernateTourneyDivision(t, Language.RU, TourneySection.ADVANCED);

		final Criteria c0 = session.createCriteria(HibernateTourneyDivision.class);
		c0.createAlias("tourney", "t").add(Restrictions.eq("t.number", t.getNumber()));
		assertEquals(2, c0.list().size());

		final HibernateTourneyRound r1 = new HibernateTourneyRound(1, d, 10);
		assertNotNull(r1.getStartedDate());
		session.save(r1);

		final HibernateTourneyRound r2 = new HibernateTourneyRound(2, d, 10);
		assertNotNull(r2.getStartedDate());
		session.save(r2);

		final HibernateTourneyGroup g1 = new HibernateTourneyGroup(1, r1, new long[]{1, 2});
		assertNotNull(g1.getStartedDate());
		session.save(g1);

		final HibernateTourneyGroup g2 = new HibernateTourneyGroup(2, r1, new long[]{3, 4, 5, 6});
		assertNotNull(g2.getStartedDate());
		session.save(g2);

		final RegularTourney tourney = tourneyManager.getTournamentEntity(new RegularTourney.Id(1));
		assertSame(t, tourney);

		final TourneyDivision division = tourneyManager.getTournamentEntity(new TourneyDivision.Id(1, Language.RU, TourneySection.ADVANCED));
		assertSame(d, division);

		final TourneyRound round = tourneyManager.getTournamentEntity(new TourneyRound.Id(1, Language.RU, TourneySection.ADVANCED, 1));
		assertSame(r1, round);

		final TourneyGroup group = tourneyManager.getTournamentEntity(new TourneyGroup.Id(1, Language.RU, TourneySection.ADVANCED, 1, 1));
		assertSame(g1, group);
	}

	@Test
	public void testInitTourney() throws InterruptedException, TourneySubscriptionException {
		final Capture<TourneyEntity> entityCapture = new Capture<TourneyEntity>(CaptureType.ALL);
		final Capture<RegularTourney> tourneyCapture = new Capture<RegularTourney>();
		final Capture<TourneySubscription> subscriptionCapture = new Capture<TourneySubscription>(CaptureType.ALL);

		final RegularTourneyListener tourneyListener = createStrictMock(RegularTourneyListener.class);
		tourneyListener.tourneyAnnounced(capture(tourneyCapture));
		replay(tourneyListener);

		createStats(101, 1001);
		createStats(102, 1602);
		createStats(103, 1699);

		tourneyManager.addRegularTourneyListener(tourneyListener);

		// init new tourney
		final RegularTourney tourney = tourneyManager.startRegularTourney(HibernateTourneyManager.getMidnight());

		final TourneyEntityListener entityListener = createStrictMock(TourneyEntityListener.class);
		entityListener.entityStarted(capture(entityCapture)); // tourney
		entityListener.entityStarted(capture(entityCapture)); // division 1
		replay(entityListener);

		final TourneySubscriptionListener subscriptionListener = createMock(TourneySubscriptionListener.class);
		subscriptionListener.subscribed(capture(subscriptionCapture));
		subscriptionListener.subscribed(capture(subscriptionCapture));
		subscriptionListener.subscribed(capture(subscriptionCapture));
		subscriptionListener.resubscribed(101, tourney.getNumber(), TourneySection.CASUAL, TourneySection.ADVANCED);
		subscriptionListener.resubscribed(102, tourney.getNumber(), TourneySection.INTERMEDIATE, TourneySection.ADVANCED);
		subscriptionListener.resubscribed(103, tourney.getNumber(), TourneySection.EXPERT, null);
		replay(subscriptionListener);

		tourneyManager.addTourneyEntityListener(entityListener);
		tourneyManager.addTourneySubscriptionListener(subscriptionListener);

		tourneyManager.subscribe(tourney.getNumber(), 101, Language.RU, TourneySection.CASUAL);
		tourneyManager.subscribe(tourney.getNumber(), 102, Language.RU, TourneySection.INTERMEDIATE);
		tourneyManager.subscribe(tourney.getNumber(), 103, Language.RU, TourneySection.EXPERT);

		// new day!
		tourneyManager.breakingDayTime(HibernateTourneyManager.getMidnight());

		// TODO: check entities

		tourneyManager.removeTourneyEntityListener(entityListener);
		tourneyManager.removeRegularTourneyListener(tourneyListener);
		tourneyManager.removeTourneySubscriptionListener(subscriptionListener);

		verify(entityListener);
		verify(tourneyListener);
		verify(subscriptionListener);
	}

	@Test
	public void testFinishTourney() {
		//TODO: not implemented
	}

	private void createStats(long pid, int rating) {
		final Session session = sessionFactory.getCurrentSession();
		final SQLQuery sqlQuery = session.createSQLQuery("insert into scribble_statistic(playerId, rating) VALUES(?, ?)");
		sqlQuery.setParameter(0, pid);
		sqlQuery.setParameter(1, rating);
		sqlQuery.executeUpdate();
	}
}
