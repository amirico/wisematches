package wisematches.playground.tourney.regular.impl;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
import wisematches.playground.*;
import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.TourneyEntityListener;
import wisematches.playground.tourney.regular.*;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

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
public class HibernateRegularTourneyManagerTest {
	@Autowired
	private SessionFactory sessionFactory;

	private HibernateRegularTourneyManager<GameSettings> tourneyManager;

	public HibernateRegularTourneyManagerTest() {
	}

	@Before
	public void init() throws ParseException, BoardCreationException {
		@SuppressWarnings("unchecked")
		final GameSettingsProvider<GameSettings, TourneyGroup> settingsProvider = createMock(GameSettingsProvider.class);
		expect(settingsProvider.createGameSettings(isA(TourneyGroup.class))).andAnswer(new IAnswer<GameSettings>() {
			@Override
			public GameSettings answer() throws Throwable {
				final TourneyGroup g = (TourneyGroup) EasyMock.getCurrentArguments()[0];
				return new MockGameSettings("Mock tourney: " + g.getId().getRoundId().getDivisionId().getTourneyId().getNumber(), 3);
			}
		}).anyTimes();
		replay(settingsProvider);

		@SuppressWarnings("unchecked")
		final BoardManager<GameSettings, GameBoard<GameSettings, GamePlayerHand>> boardManager = createMock(BoardManager.class);
		expect(boardManager.createBoard(anyObject(GameSettings.class), anyObject(Collection.class))).andAnswer(new IAnswer<GameBoard<GameSettings, GamePlayerHand>>() {
			private long c = 1;

			@Override
			public GameBoard<GameSettings, GamePlayerHand> answer() throws Throwable {
				@SuppressWarnings("unchecked")
				GameBoard<GameSettings, GamePlayerHand> s = createMock(GameBoard.class);
				expect(s.getBoardId()).andReturn(c++).anyTimes();
				replay(s);
				return s;
			}
		}).anyTimes();
		replay(boardManager);

		tourneyManager = new HibernateRegularTourneyManager<GameSettings>();
		tourneyManager.setCronExpression(new CronExpression("* * * ? * 2#1"));
		tourneyManager.setSessionFactory(sessionFactory);
		tourneyManager.setTaskExecutor(new SyncTaskExecutor());
		tourneyManager.setBoardManager(boardManager);
		tourneyManager.setSettingsProvider(settingsProvider);
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
		session.save(t);

		final HibernateTourneyDivision d = new HibernateTourneyDivision(t, Language.RU, TourneySection.ADVANCED);
		session.save(d);

		final HibernateTourneyRound r1 = new HibernateTourneyRound(1, d);
		assertNull(r1.getStartedDate());
		session.save(r1);

		final HibernateTourneyRound r2 = new HibernateTourneyRound(2, d);
		r2.startRound(12);
		assertNotNull(r2.getStartedDate());
		session.save(r2);

		final HibernateTourneyGroup g1 = new HibernateTourneyGroup(1, r1, new long[]{1, 2});
		assertNull(g1.getStartedDate());
		session.save(g1);

		final HibernateTourneyGroup g2 = new HibernateTourneyGroup(2, r1, new long[]{3, 4, 5, 6});
		assertNull(g2.getStartedDate());
		session.save(g2);

		final Tourney tourney = tourneyManager.getTournamentEntity(new Tourney.Id(1));
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
		final Capture<Tourney> tourneyCapture = new Capture<Tourney>();
		final Capture<TourneySubscription> subscriptionCapture = new Capture<TourneySubscription>(CaptureType.ALL);

		final RegularTourneyListener tourneyListener = createStrictMock(RegularTourneyListener.class);
		tourneyListener.tourneyAnnounced(capture(tourneyCapture));
		replay(tourneyListener);

		createStats(101, 1001);
		createStats(102, 1602);
		createStats(103, 1699);

		tourneyManager.addRegularTourneyListener(tourneyListener);

		// init new tourney
		final Tourney tourney = tourneyManager.startRegularTourney(HibernateRegularTourneyManager.getMidnight());

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
		tourneyManager.breakingDayTime(HibernateRegularTourneyManager.getMidnight());

		assertTrue(1 <= tourneyManager.getTotalCount(null, new Tourney.Context(EnumSet.of(Tourney.State.ACTIVE))));
		final List<Tourney> tourneys = tourneyManager.searchTournamentEntities(null, new Tourney.Context(EnumSet.of(Tourney.State.ACTIVE)), null, null, null);
		assertTrue(tourneys.size() >= 1);

		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyDivision.Context(tourney.getId())));
		final List<TourneyDivision> divisions = tourneyManager.searchTournamentEntities(null, new TourneyDivision.Context(tourney.getId()), null, null, null);
		assertEquals(1, divisions.size());

		final TourneyDivision division = divisions.iterator().next();
		assertEquals(1, division.getActiveRound());
		assertEquals(tourney.getId(), division.getTourney().getId());
		assertNull(division.getFinishedDate());
		assertNotNull(division.getStartedDate());

		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyRound.Context(division.getId())));
		final List<TourneyRound> rounds = tourneyManager.searchTournamentEntities(null, new TourneyRound.Context(division.getId()), null, null, null);
		assertEquals(1, rounds.size());

		final TourneyRound round = rounds.iterator().next();
		assertEquals(1, round.getRound());
		assertEquals(1, round.getTotalGamesCount());
		assertEquals(0, round.getFinishedGamesCount());
		assertNull(round.getFinishedDate());
		assertNotNull(round.getStartedDate());

		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyGroup.Context(round.getId())));
		final List<TourneyGroup> groups = tourneyManager.searchTournamentEntities(null, new TourneyGroup.Context(round.getId()), null, null, null);
		assertEquals(1, groups.size());

		final TourneyGroup group = groups.iterator().next();
		assertEquals(1, group.getGroup());
		assertEquals(2, group.getPlayers().length);
		assertEquals(101, group.getPlayers()[0]);
		assertEquals(102, group.getPlayers()[1]);
		assertEquals(1, group.getGames().length);
		assertEquals(2, group.getScores().length);
		assertNull(group.getFinishedDate());
		assertNotNull(group.getStartedDate());

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
