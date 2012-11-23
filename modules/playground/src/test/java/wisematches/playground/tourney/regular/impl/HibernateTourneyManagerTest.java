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
import wisematches.database.Range;
import wisematches.personality.Language;
import wisematches.personality.Personality;
import wisematches.playground.*;
import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.regular.*;

import java.text.ParseException;
import java.util.*;

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
@SuppressWarnings("unchecked")
public class HibernateTourneyManagerTest {
	@Autowired
	private SessionFactory sessionFactory;

	private BoardStateListener boardStateListener;

	private HibernateTourneyManager<GameSettings> tourneyManager;

	public HibernateTourneyManagerTest() {
	}

	@Before
	public void init() throws ParseException, BoardCreationException, InterruptedException {
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
		boardManager.addBoardStateListener(isA(BoardStateListener.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			@Override
			public Object answer() throws Throwable {
				boardStateListener = (BoardStateListener) getCurrentArguments()[0];
				return null;
			}
		});
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

		tourneyManager = new HibernateTourneyManager<GameSettings>();
		tourneyManager.setSessionFactory(sessionFactory);
		tourneyManager.setTaskExecutor(new SyncTaskExecutor());
		tourneyManager.setBoardManager(boardManager);
		tourneyManager.setSettingsProvider(settingsProvider);
	}

	@Test
	public void test_startTourney() throws ParseException {
		tourneyManager.setCronExpression(new CronExpression("0/5 * * * * ?"));
		assertNotNull(tourneyManager.startRegularTourney());
		assertNull(tourneyManager.startRegularTourney());
		tourneyManager.setCronExpression(new CronExpression("0/5 * * * * ?"));
	}

	@Test
	public void test_subscription() throws RegistrationException {
		final Session session = sessionFactory.getCurrentSession();

		final HibernateTourney tourney = new HibernateTourney(10000, new Date(System.currentTimeMillis() + 10000000L));
		session.save(tourney);


		final RegistrationListener l = createStrictMock(RegistrationListener.class);
		tourneyManager.addTourneySubscriptionListener(l);

		l.registered(isA(RegistrationRecord.class), isNull(String.class));
		l.registered(isA(RegistrationRecord.class), isNull(String.class));
		l.registered(isA(RegistrationRecord.class), isNull(String.class));
		l.unregistered(isA(RegistrationRecord.class), isNull(String.class));
		replay(l);

		final RegistrationSearchManager searchManager = tourneyManager.getRegistrationSearchManager();
		assertEquals(0, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000)));

		try {
			tourneyManager.register(Personality.person(101), null, Language.RU, TourneySection.ADVANCED);
			fail("Exception must be here");
		} catch (RegistrationException ignore) {
			assertEquals(0, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000)));
		}

		tourneyManager.register(Personality.person(101), tourney, Language.RU, TourneySection.ADVANCED);
		assertEquals(1, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000)));
		assertEquals(0, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000, Language.EN)));
		assertEquals(1, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000, Language.RU)));

		try {
			tourneyManager.register(Personality.person(101), tourney, Language.RU, TourneySection.ADVANCED);
			fail("Exception must be here");
		} catch (RegistrationException ignore) {
			assertEquals(1, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000)));
		}

		tourneyManager.register(Personality.person(102), tourney, Language.RU, TourneySection.ADVANCED);
		tourneyManager.register(Personality.person(103), tourney, Language.RU, TourneySection.GRANDMASTER);

		RegistrationsSummary subscriptions = tourneyManager.getRegistrationsSummary(tourney);
		assertEquals(0, subscriptions.getPlayers(Language.EN));
		assertEquals(0, subscriptions.getPlayers(Language.EN, TourneySection.ADVANCED));
		assertEquals(3, subscriptions.getPlayers(Language.RU));
		assertEquals(2, subscriptions.getPlayers(Language.RU, TourneySection.ADVANCED));
		assertEquals(1, subscriptions.getPlayers(Language.RU, TourneySection.GRANDMASTER));

		final RegistrationRecord subscription = tourneyManager.getRegistration(Personality.person(101), tourney);
		assertEquals(10000, subscription.getTourney());
		assertEquals(101, subscription.getPlayer());
		assertEquals(Language.RU, subscription.getLanguage());
		assertEquals(TourneySection.ADVANCED, subscription.getSection());

		assertEquals(3, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000)));
		assertEquals(0, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000, Language.EN)));
		assertEquals(3, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000, Language.RU)));

		tourneyManager.unregister(Personality.person(102), tourney, Language.RU, TourneySection.GRANDMASTER);
		subscriptions = tourneyManager.getRegistrationsSummary(tourney);
		assertEquals(3, subscriptions.getPlayers(Language.RU));
		assertEquals(2, subscriptions.getPlayers(Language.RU, TourneySection.ADVANCED));
		assertEquals(1, subscriptions.getPlayers(Language.RU, TourneySection.GRANDMASTER));

		tourneyManager.unregister(Personality.person(102), tourney, Language.RU, TourneySection.ADVANCED);
		subscriptions = tourneyManager.getRegistrationsSummary(tourney);
		assertEquals(2, subscriptions.getPlayers(Language.RU));
		assertEquals(1, subscriptions.getPlayers(Language.RU, TourneySection.ADVANCED));
		assertEquals(1, subscriptions.getPlayers(Language.RU, TourneySection.GRANDMASTER));

		assertEquals(2, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000)));
		assertEquals(0, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000, Language.EN)));
		assertEquals(2, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000, Language.RU)));

		tourneyManager.removeTourneySubscriptionListener(l);

		verify(l);
	}

	@Test
	public void test_getTournamentEntity() {
		final Session session = sessionFactory.getCurrentSession();

		final HibernateTourney t = new HibernateTourney(10000, new Date(System.currentTimeMillis() + 10000000L));
		session.save(t);

		final HibernateTourneyDivision d = new HibernateTourneyDivision(t, Language.RU, TourneySection.ADVANCED);
		session.save(d);

		final HibernateTourneyRound r1 = new HibernateTourneyRound(d, 1);
		assertNull(r1.getStartedDate());
		session.save(r1);

		final HibernateTourneyRound r2 = new HibernateTourneyRound(d, 2);
		r2.gamesStarted(12);
		assertNotNull(r2.getStartedDate());
		session.save(r2);

		final HibernateTourneyGroup g1 = new HibernateTourneyGroup(1, r1, new long[]{1, 2});
		assertNull(g1.getStartedDate());
		session.save(g1);

		final HibernateTourneyGroup g2 = new HibernateTourneyGroup(2, r1, new long[]{3, 4, 5, 6});
		assertNull(g2.getStartedDate());
		session.save(g2);

		final Tourney tourney = tourneyManager.getTourneyEntity(new Tourney.Id(10000));
		assertSame(t, tourney);

		final TourneyDivision division = tourneyManager.getTourneyEntity(new TourneyDivision.Id(10000, Language.RU, TourneySection.ADVANCED));
		assertSame(d, division);

		final TourneyRound round = tourneyManager.getTourneyEntity(new TourneyRound.Id(10000, Language.RU, TourneySection.ADVANCED, 1));
		assertSame(r1, round);

		final TourneyGroup group = tourneyManager.getTourneyEntity(new TourneyGroup.Id(10000, Language.RU, TourneySection.ADVANCED, 1, 1));
		assertSame(g1, group);
	}

	@Test
	public void testInitTourney() throws InterruptedException, RegistrationException, ParseException {
		final Capture<Tourney> tourneyCapture = new Capture<Tourney>(CaptureType.ALL);
		final Capture<RegistrationRecord> subscriptionCapture = new Capture<RegistrationRecord>(CaptureType.ALL);

		final RegularTourneyListener tourneyListener = createStrictMock(RegularTourneyListener.class);
		tourneyListener.tourneyAnnounced(capture(tourneyCapture));
		tourneyListener.tourneyAnnounced(capture(tourneyCapture)); //' second time created by breakingDayTime
		tourneyListener.tourneyStarted(capture(tourneyCapture));
		replay(tourneyListener);

		createStats(101, 1001);
		createStats(102, 1602);
		createStats(103, 1699);

		tourneyManager.addRegularTourneyListener(tourneyListener);

		// init new tourney
		tourneyManager.setCronExpression(new CronExpression("0/1 * * * * ?"));
		final Tourney tourney = tourneyManager.startRegularTourney();
		tourneyManager.setCronExpression(new CronExpression("0 0 0 */1 * ?"));
		Thread.sleep(1000);

		final RegistrationListener subscriptionListener = createMock(RegistrationListener.class);
		subscriptionListener.registered(capture(subscriptionCapture), isNull(String.class));
		subscriptionListener.registered(capture(subscriptionCapture), isNull(String.class));
		subscriptionListener.registered(capture(subscriptionCapture), isNull(String.class));
		subscriptionListener.sectionChanged(101, tourney.getNumber(), TourneySection.CASUAL, TourneySection.ADVANCED);
		subscriptionListener.sectionChanged(102, tourney.getNumber(), TourneySection.INTERMEDIATE, TourneySection.ADVANCED);
		subscriptionListener.sectionChanged(103, tourney.getNumber(), TourneySection.EXPERT, null);
		replay(subscriptionListener);

		tourneyManager.addTourneySubscriptionListener(subscriptionListener);

		tourneyManager.register(Personality.person(101), tourney, Language.RU, TourneySection.CASUAL);
		tourneyManager.register(Personality.person(102), tourney, Language.RU, TourneySection.INTERMEDIATE);
		tourneyManager.register(Personality.person(103), tourney, Language.RU, TourneySection.EXPERT);

		// new day!
		tourneyManager.breakingDayTime(null);

		assertTrue(1 <= tourneyManager.getTotalCount(null, new Tourney.Context(EnumSet.of(Tourney.State.ACTIVE))));
		final List<Tourney> tourneys = tourneyManager.searchTourneyEntities(null, new Tourney.Context(EnumSet.of(Tourney.State.ACTIVE)), null, null, null);
		assertTrue(tourneys.size() >= 1);

		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyDivision.Context(tourney.getId())));
		final List<TourneyDivision> divisions = tourneyManager.searchTourneyEntities(null, new TourneyDivision.Context(tourney.getId()), null, null, null);
		assertEquals(1, divisions.size());

		final TourneyDivision division = divisions.iterator().next();
		assertEquals(1, division.getActiveRound());
		assertEquals(tourney.getId(), division.getTourney().getId());
		assertNull(division.getFinishedDate());
		assertNotNull(division.getStartedDate());

		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyRound.Context(division.getId(), null)));
		final List<TourneyRound> rounds = tourneyManager.searchTourneyEntities(null, new TourneyRound.Context(division.getId(), null), null, null, null);
		assertEquals(1, rounds.size());

		final TourneyRound round = rounds.iterator().next();
		assertEquals(1, round.getRound());
		assertEquals(1, round.getTotalGamesCount());
		assertEquals(0, round.getFinishedGamesCount());
		assertNull(round.getFinishedDate());
		assertNotNull(round.getStartedDate());

		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyGroup.Context(round.getId(), null)));
		final List<TourneyGroup> groups = tourneyManager.searchTourneyEntities(null, new TourneyGroup.Context(round.getId(), null), null, null, null);
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

		tourneyManager.removeRegularTourneyListener(tourneyListener);
		tourneyManager.removeTourneySubscriptionListener(subscriptionListener);

		verify(tourneyListener);
		verify(subscriptionListener);
	}

	@Test
	public void testFinishTourney() throws RegistrationException, InterruptedException, ParseException {
		final int activeTourneys = tourneyManager.getTotalCount(null, new Tourney.Context(EnumSet.of(TourneyEntity.State.ACTIVE)));
		final int finishedTourneys = tourneyManager.getTotalCount(null, new Tourney.Context(EnumSet.of(TourneyEntity.State.FINISHED)));

		final Capture<Tourney> tourneyCapture = new Capture<Tourney>(CaptureType.ALL);
		final Capture<RegistrationRecord> subscriptionCapture = new Capture<RegistrationRecord>(CaptureType.ALL);

		for (int i = 0; i < 7; i++) {
			createStats(101 + i, 1001);
		}

		// init new tourney
		tourneyManager.setCronExpression(new CronExpression("0/1 * * * * ?"));
		final Tourney tourney = tourneyManager.startRegularTourney();
		tourneyManager.setCronExpression(new CronExpression("0 0 0 */1 * ?"));
		Thread.sleep(1000);

		for (int i = 0; i < 2; i++) {
			tourneyManager.register(Personality.person(101 + i), tourney, Language.RU, TourneySection.CASUAL);
		}
		for (int i = 0; i < 5; i++) {
			tourneyManager.register(Personality.person(103 + i), tourney, Language.RU, TourneySection.EXPERT);
		}

		// new day!
		tourneyManager.breakingDayTime(null);

		final RegularTourneyListener tourneyListener = createMock(RegularTourneyListener.class);
		tourneyListener.tourneyAnnounced(capture(tourneyCapture));
		expectLastCall().times(0, 2);
		tourneyListener.tourneyStarted(capture(tourneyCapture));
		expectLastCall().times(0, 2);
		tourneyListener.tourneyFinished(capture(tourneyCapture));
		replay(tourneyListener);
		tourneyManager.addRegularTourneyListener(tourneyListener);

		final RegistrationListener subscriptionListener = createMock(RegistrationListener.class);
		subscriptionListener.registered(capture(subscriptionCapture), eq("won.tourney.group"));
		expectLastCall().times(2);
		replay(subscriptionListener);
		tourneyManager.addTourneySubscriptionListener(subscriptionListener);

		final TourneyDivision.Id casualDivision = new TourneyDivision.Id(tourney.getNumber(), Language.RU, TourneySection.CASUAL);
		final TourneyDivision.Id expertDivision = new TourneyDivision.Id(tourney.getNumber(), Language.RU, TourneySection.EXPERT);

		final TourneyRound.Id casualRound1 = new TourneyRound.Id(casualDivision, 1);
		final TourneyRound.Id expertRound1 = new TourneyRound.Id(expertDivision, 1);
		final TourneyRound.Id expertRound2 = new TourneyRound.Id(expertDivision, 2);

		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyGroup.Context(casualRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(2, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound2, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyRound.Context(casualDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyRound.Context(expertDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(2, tourneyManager.getTotalCount(null, new TourneyDivision.Context(new Tourney.Id(tourney.getNumber()), EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(activeTourneys + 1, tourneyManager.getTotalCount(null, new Tourney.Context(EnumSet.of(TourneyEntity.State.ACTIVE))));

		final List<TourneyGroup> tourneyCasualGroups1 = tourneyManager.searchTourneyEntities(null, new TourneyGroup.Context(casualRound1, null), null, null, null);
		final TourneyGroup casualGroup = tourneyCasualGroups1.iterator().next();
		assertEquals(1, casualGroup.getGames().length);

		final GameBoard<GameSettings, GamePlayerHand> board1 = createMock(GameBoard.class);
		expect(board1.getBoardId()).andReturn(casualGroup.getGames()[0]);
		expect(board1.getWonPlayers()).andReturn(Arrays.asList(createMockHand(101, 100)));
		replay(board1);

		boardStateListener.gameFinished(board1, null, null);
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(casualRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(2, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound2, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyRound.Context(casualDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyRound.Context(expertDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyDivision.Context(new Tourney.Id(tourney.getNumber()), EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(activeTourneys + 1, tourneyManager.getTotalCount(null, new Tourney.Context(EnumSet.of(TourneyEntity.State.ACTIVE))));

		final List<TourneyGroup> tourneyExpertGroups1 = tourneyManager.searchTourneyEntities(null, new TourneyGroup.Context(expertRound1, null), null, null, null);
		final GameBoard<GameSettings, GamePlayerHand> board2 = createMock(GameBoard.class);
		expect(board2.getBoardId()).andReturn(tourneyExpertGroups1.get(0).getGames()[0]);
		expect(board2.getWonPlayers()).andReturn(Arrays.asList(createMockHand(103, 100)));
		replay(board2);
		boardStateListener.gameFinished(board2, null, null);
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(casualRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(2, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound2, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyRound.Context(casualDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyRound.Context(expertDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyDivision.Context(new Tourney.Id(tourney.getNumber()), EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(activeTourneys + 1, tourneyManager.getTotalCount(null, new Tourney.Context(EnumSet.of(TourneyEntity.State.ACTIVE))));

		final GameBoard<GameSettings, GamePlayerHand> board3 = createMock(GameBoard.class);
		expect(board3.getBoardId()).andReturn(tourneyExpertGroups1.get(0).getGames()[1]);
		expect(board3.getWonPlayers()).andReturn(Arrays.asList(createMockHand(103, 100)));
		replay(board3);
		boardStateListener.gameFinished(board3, null, null);
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(casualRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(2, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound2, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyRound.Context(casualDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyRound.Context(expertDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyDivision.Context(new Tourney.Id(tourney.getNumber()), EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(activeTourneys + 1, tourneyManager.getTotalCount(null, new Tourney.Context(EnumSet.of(TourneyEntity.State.ACTIVE))));

		final GameBoard<GameSettings, GamePlayerHand> board4 = createMock(GameBoard.class);
		expect(board4.getBoardId()).andReturn(tourneyExpertGroups1.get(0).getGames()[2]);
		expect(board4.getWonPlayers()).andReturn(Arrays.asList(createMockHand(104, 100)));
		replay(board4);
		boardStateListener.gameFinished(board4, null, null);
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(casualRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound2, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyRound.Context(casualDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyRound.Context(expertDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyDivision.Context(new Tourney.Id(tourney.getNumber()), EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(activeTourneys + 1, tourneyManager.getTotalCount(null, new Tourney.Context(EnumSet.of(TourneyEntity.State.ACTIVE))));

		final GameBoard<GameSettings, GamePlayerHand> board5 = createMock(GameBoard.class);
		expect(board5.getBoardId()).andReturn(tourneyExpertGroups1.get(1).getGames()[0]);
		expect(board5.getWonPlayers()).andReturn(Arrays.asList(createMockHand(106, 100)));
		replay(board5);
		boardStateListener.gameFinished(board5, null, null);
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(casualRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound2, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyRound.Context(casualDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyRound.Context(expertDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyDivision.Context(new Tourney.Id(tourney.getNumber()), EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(activeTourneys + 1, tourneyManager.getTotalCount(null, new Tourney.Context(EnumSet.of(TourneyEntity.State.ACTIVE))));

		tourneyManager.breakingDayTime(null);

		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(casualRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound2, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyRound.Context(casualDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyRound.Context(expertDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyDivision.Context(new Tourney.Id(tourney.getNumber()), EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(activeTourneys + 1, tourneyManager.getTotalCount(null, new Tourney.Context(EnumSet.of(TourneyEntity.State.ACTIVE))));

		final List<TourneyGroup> tourneyExpertGroups2 = tourneyManager.searchTourneyEntities(null, new TourneyGroup.Context(expertRound2, null), null, null, null);
		final GameBoard<GameSettings, GamePlayerHand> board6 = createMock(GameBoard.class);
		expect(board6.getBoardId()).andReturn(tourneyExpertGroups2.get(0).getGames()[0]);
		expect(board6.getWonPlayers()).andReturn(Arrays.asList(createMockHand(103, 100), createMockHand(106, 100)));
		replay(board6);
		boardStateListener.gameFinished(board6, null, null);
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(casualRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound2, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyRound.Context(casualDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyRound.Context(expertDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyDivision.Context(new Tourney.Id(tourney.getNumber()), EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(activeTourneys, tourneyManager.getTotalCount(null, new Tourney.Context(EnumSet.of(TourneyEntity.State.ACTIVE))));

		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyGroup.Context(casualRound1, EnumSet.of(TourneyEntity.State.FINISHED))));
		assertEquals(2, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound1, EnumSet.of(TourneyEntity.State.FINISHED))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound2, EnumSet.of(TourneyEntity.State.FINISHED))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyRound.Context(casualDivision, EnumSet.of(TourneyEntity.State.FINISHED))));
		assertEquals(2, tourneyManager.getTotalCount(null, new TourneyRound.Context(expertDivision, EnumSet.of(TourneyEntity.State.FINISHED))));
		assertEquals(2, tourneyManager.getTotalCount(null, new TourneyDivision.Context(new Tourney.Id(tourney.getNumber()), EnumSet.of(TourneyEntity.State.FINISHED))));
		assertEquals(finishedTourneys + 1, tourneyManager.getTotalCount(null, new Tourney.Context(EnumSet.of(TourneyEntity.State.FINISHED))));

		verify(board1, board2, board3, board4, board5, board6, tourneyListener, subscriptionListener);
	}

	@Test
	public void testRegistrationSearchManager() throws RegistrationException, InterruptedException, ParseException {
		final RegistrationSearchManager searchManager = tourneyManager.getRegistrationSearchManager();

		tourneyManager.setCronExpression(new CronExpression("0/1 * * * * ?"));
		final Tourney tourney = tourneyManager.startRegularTourney();
		tourneyManager.setCronExpression(new CronExpression("0 0 0 */1 * ?"));
		Thread.sleep(1000);

		final long[] longs = searchManager.searchUnregisteredPlayers(tourney, Range.limit(10));
		assertEquals(10, longs.length);
	}

	private void createStats(long pid, int rating) {
		final Session session = sessionFactory.getCurrentSession();
		final SQLQuery sqlQuery = session.createSQLQuery("insert into scribble_statistic(playerId, rating) VALUES(:pid, :rating)");
		sqlQuery.setLong("pid", pid);
		sqlQuery.setLong("rating", rating);
		sqlQuery.executeUpdate();
	}

	private GamePlayerHand createMockHand(long pid, int points) {
		GamePlayerHand s = EasyMock.createMock(GamePlayerHand.class);
		expect(s.getPlayerId()).andReturn(pid).anyTimes();
		expect(s.getPoints()).andReturn((short) points).anyTimes();
		replay(s);
		return s;
	}
}
