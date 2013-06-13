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
import wisematches.core.Language;
import wisematches.core.Personality;
import wisematches.core.PersonalityManager;
import wisematches.core.Player;
import wisematches.core.personality.DefaultMember;
import wisematches.core.search.Range;
import wisematches.playground.*;
import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.TourneyPlace;
import wisematches.playground.tourney.TourneyWinner;
import wisematches.playground.tourney.regular.*;

import java.text.ParseException;
import java.util.*;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/properties-config.xml",
		"classpath:/config/database-config.xml",
		"classpath:/config/personality-config.xml"
})
@SuppressWarnings("unchecked")
public class HibernateTourneyManagerTest {
	@Autowired
	private SessionFactory sessionFactory;

	private BoardListener gamePlayListener;

	private HibernateTourneyManager<GameSettings> tourneyManager;

	private static final Player player1 = new DefaultMember(901, null, null, null, null, null);
	private static final Player player2 = new DefaultMember(902, null, null, null, null, null);
	private static final Player player3 = new DefaultMember(903, null, null, null, null, null);
	private static final Player player4 = new DefaultMember(904, null, null, null, null, null);
	private static final Player player5 = new DefaultMember(905, null, null, null, null, null);
	private static final Player player6 = new DefaultMember(906, null, null, null, null, null);
	private static final Player player7 = new DefaultMember(907, null, null, null, null, null);

	public HibernateTourneyManagerTest() {
	}

	@Before
	public void init() throws ParseException, BoardCreationException, InterruptedException {
		final PersonalityManager personalityManager = createMock(PersonalityManager.class);
		expect(personalityManager.getPerson(901L)).andReturn(player1).anyTimes();
		expect(personalityManager.getPerson(902L)).andReturn(player2).anyTimes();
		expect(personalityManager.getPerson(903L)).andReturn(player3).anyTimes();
		expect(personalityManager.getPerson(904L)).andReturn(player4).anyTimes();
		expect(personalityManager.getPerson(905L)).andReturn(player5).anyTimes();
		expect(personalityManager.getPerson(906L)).andReturn(player6).anyTimes();
		expect(personalityManager.getPerson(907L)).andReturn(player7).anyTimes();
		replay(personalityManager);

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
		final GamePlayManager<GameSettings, GameBoard<GameSettings, GamePlayerHand, GameMove>> gamePlayManager = createMock(GamePlayManager.class);
		gamePlayManager.addBoardListener(isA(BoardListener.class));
		expectLastCall().andAnswer(new IAnswer<Object>() {
			@Override
			public Object answer() throws Throwable {
				gamePlayListener = (BoardListener) getCurrentArguments()[0];
				return null;
			}
		});
		expect(gamePlayManager.createBoard(anyObject(GameSettings.class), anyObject(Collection.class), isA(GameRelationship.class))).andAnswer(new IAnswer<GameBoard<GameSettings, GamePlayerHand, GameMove>>() {
			private long c = 1;

			@Override
			public GameBoard<GameSettings, GamePlayerHand, GameMove> answer() throws Throwable {
				@SuppressWarnings("unchecked")
				GameBoard<GameSettings, GamePlayerHand, GameMove> s = createMock(GameBoard.class);
				expect(s.getBoardId()).andReturn(c++).anyTimes();
				replay(s);
				return s;
			}
		}).anyTimes();
		replay(gamePlayManager);

		tourneyManager = new HibernateTourneyManager<>();
		tourneyManager.setSessionFactory(sessionFactory);
		tourneyManager.setTaskExecutor(new SyncTaskExecutor());
		tourneyManager.setGamePlayManager(gamePlayManager);
		tourneyManager.setSettingsProvider(settingsProvider);
		tourneyManager.setPersonalityManager(personalityManager);
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
		assertEquals(0, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000, 1)));

		try {
			tourneyManager.register(player1, null, Language.RU, TourneySection.ADVANCED);
			fail("Exception must be here");
		} catch (RegistrationException ignore) {
			assertEquals(0, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000, 1)));
		}

		tourneyManager.register(player1, tourney, Language.RU, TourneySection.ADVANCED);
		assertEquals(1, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000, 1)));
		assertEquals(0, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000, Language.EN, 1)));
		assertEquals(1, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000, Language.RU, 1)));

		try {
			tourneyManager.register(player1, tourney, Language.RU, TourneySection.ADVANCED);
			fail("Exception must be here");
		} catch (RegistrationException ignore) {
			assertEquals(1, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000, 1)));
		}

		tourneyManager.register(player2, tourney, Language.RU, TourneySection.ADVANCED);
		tourneyManager.register(player3, tourney, Language.RU, TourneySection.GRANDMASTER);

		RegistrationsSummary subscriptions = tourneyManager.getRegistrationsSummary(tourney);
		assertEquals(0, subscriptions.getPlayers(Language.EN));
		assertEquals(0, subscriptions.getPlayers(Language.EN, TourneySection.ADVANCED));
		assertEquals(3, subscriptions.getPlayers(Language.RU));
		assertEquals(2, subscriptions.getPlayers(Language.RU, TourneySection.ADVANCED));
		assertEquals(1, subscriptions.getPlayers(Language.RU, TourneySection.GRANDMASTER));

		final RegistrationRecord subscription = tourneyManager.getRegistration(player1, tourney);
		assertEquals(10000, subscription.getTourney());
		assertEquals(901, subscription.getPlayer());
		assertEquals(Language.RU, subscription.getLanguage());
		assertEquals(TourneySection.ADVANCED, subscription.getSection());

		assertEquals(3, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000, 1)));
		assertEquals(0, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000, Language.EN, 1)));
		assertEquals(3, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000, Language.RU, 1)));

		tourneyManager.unregister(player2, tourney, Language.RU, TourneySection.GRANDMASTER);
		subscriptions = tourneyManager.getRegistrationsSummary(tourney);
		assertEquals(3, subscriptions.getPlayers(Language.RU));
		assertEquals(2, subscriptions.getPlayers(Language.RU, TourneySection.ADVANCED));
		assertEquals(1, subscriptions.getPlayers(Language.RU, TourneySection.GRANDMASTER));

		tourneyManager.unregister(player2, tourney, Language.RU, TourneySection.ADVANCED);
		subscriptions = tourneyManager.getRegistrationsSummary(tourney);
		assertEquals(2, subscriptions.getPlayers(Language.RU));
		assertEquals(1, subscriptions.getPlayers(Language.RU, TourneySection.ADVANCED));
		assertEquals(1, subscriptions.getPlayers(Language.RU, TourneySection.GRANDMASTER));

		assertEquals(2, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000, 1)));
		assertEquals(0, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000, Language.EN, 1)));
		assertEquals(2, searchManager.getTotalCount(null, new RegistrationRecord.Context(10000, Language.RU, 1)));

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
		r2.initiateGames(12, 2);
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
		final Capture<Tourney> tourneyCapture = new Capture<>(CaptureType.ALL);
		final Capture<RegistrationRecord> subscriptionCapture = new Capture<>(CaptureType.ALL);

		final RegularTourneyListener tourneyListener = createStrictMock(RegularTourneyListener.class);
		tourneyListener.tourneyAnnounced(capture(tourneyCapture));
		tourneyListener.tourneyAnnounced(capture(tourneyCapture)); //' second time created by breakingDayTime
		tourneyListener.tourneyStarted(capture(tourneyCapture));
		replay(tourneyListener);

		createStats(901, 1001);
		createStats(902, 1602);
		createStats(903, 1699);

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
		subscriptionListener.sectionChanged(901, tourney.getNumber(), TourneySection.CASUAL, TourneySection.ADVANCED);
		subscriptionListener.sectionChanged(902, tourney.getNumber(), TourneySection.INTERMEDIATE, TourneySection.ADVANCED);
		subscriptionListener.sectionChanged(903, tourney.getNumber(), TourneySection.EXPERT, null);
		replay(subscriptionListener);

		tourneyManager.addTourneySubscriptionListener(subscriptionListener);

		tourneyManager.register(player1, tourney, Language.RU, TourneySection.CASUAL);
		tourneyManager.register(player2, tourney, Language.RU, TourneySection.INTERMEDIATE);
		tourneyManager.register(player3, tourney, Language.RU, TourneySection.EXPERT);

		// new day!
		tourneyManager.breakingDayTime(null);

		assertEquals(0, tourneyManager.getTotalCount(player3, new Tourney.Context(EnumSet.of(Tourney.State.ACTIVE)))); // excluded from tourney
		assertTrue(1 <= tourneyManager.getTotalCount(null, new Tourney.Context(EnumSet.of(Tourney.State.ACTIVE))));
		assertTrue(1 <= tourneyManager.getTotalCount(player1, new Tourney.Context(EnumSet.of(Tourney.State.ACTIVE)))); // has tourney
		assertTrue(1 <= tourneyManager.getTotalCount(player2, new Tourney.Context(EnumSet.of(Tourney.State.ACTIVE)))); // has tourney
		final List<Tourney> tourneys = tourneyManager.searchTourneyEntities(null, new Tourney.Context(EnumSet.of(Tourney.State.ACTIVE)), null, null);
		assertTrue(tourneys.size() >= 1);


		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyDivision.Context(tourney.getId(), null)));
		final List<TourneyDivision> divisions = tourneyManager.searchTourneyEntities(null, new TourneyDivision.Context(tourney.getId(), null), null, null);
		assertEquals(1, divisions.size());

		final TourneyDivision division = divisions.iterator().next();
		assertEquals(1, division.getActiveRound());
		assertEquals(tourney.getId(), division.getTourney().getId());
		assertNull(division.getFinishedDate());
		assertNotNull(division.getStartedDate());

		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyRound.Context(division.getId(), null)));
		final List<TourneyRound> rounds = tourneyManager.searchTourneyEntities(null, new TourneyRound.Context(division.getId(), null), null, null);
		assertEquals(1, rounds.size());

		final TourneyRound round = rounds.iterator().next();
		assertEquals(1, round.getRound());
		assertEquals(1, round.getTotalGamesCount());
		assertEquals(0, round.getFinishedGamesCount());
		assertNull(round.getFinishedDate());
		assertNotNull(round.getStartedDate());

		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyGroup.Context(round.getId(), null)));
		final List<TourneyGroup> groups = tourneyManager.searchTourneyEntities(null, new TourneyGroup.Context(round.getId(), null), null, null);
		assertEquals(1, groups.size());

		final TourneyGroup group = groups.iterator().next();
		assertEquals(1, group.getGroup());
		assertEquals(2, group.getPlayers().length);
		assertEquals(901, group.getPlayers()[0]);
		assertEquals(902, group.getPlayers()[1]);
		assertNull(group.getPlayerSuccess(901, 902));
		assertNull(group.getPlayerSuccess(902, 901));
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

		final Capture<Tourney> tourneyCapture = new Capture<>(CaptureType.ALL);
		final Capture<TourneyDivision> divisionCapture = new Capture<>(CaptureType.ALL);
		final Capture<RegistrationRecord> subscriptionCapture = new Capture<>(CaptureType.ALL);

		for (int i = 0; i < 7; i++) {
			createStats(901 + i, 1001);
		}

		// init new tourney
		tourneyManager.setCronExpression(new CronExpression("0/1 * * * * ?"));
		final Tourney tourney = tourneyManager.startRegularTourney();
		tourneyManager.setCronExpression(new CronExpression("0 0 0 */1 * ?"));
		Thread.sleep(1000);

		for (int i = 0; i < 2; i++) {
			tourneyManager.register(new DefaultMember(901 + i, null, null, null, null, null), tourney, Language.RU, TourneySection.CASUAL);
		}
		for (int i = 0; i < 5; i++) {
			tourneyManager.register(new DefaultMember(903 + i, null, null, null, null, null), tourney, Language.RU, TourneySection.EXPERT);
		}

		// new day!
		tourneyManager.breakingDayTime(null);

		final RegularTourneyListener tourneyListener = createMock(RegularTourneyListener.class);
		tourneyListener.tourneyAnnounced(capture(tourneyCapture));
		expectLastCall().times(0, 2);
		tourneyListener.tourneyStarted(capture(tourneyCapture));
		expectLastCall().times(0, 2);
		tourneyListener.tourneyFinished(capture(tourneyCapture), capture(divisionCapture));
		expectLastCall().times(2);
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

		final List<TourneyGroup> tourneyCasualGroups1 = tourneyManager.searchTourneyEntities(null, new TourneyGroup.Context(casualRound1, null), null, null);
		final TourneyGroup casualGroup = tourneyCasualGroups1.iterator().next();
		assertEquals(2, casualGroup.getPlayers().length);

		final GameBoard<GameSettings, GamePlayerHand, GameMove> board1 = createMock(GameBoard.class);
		expect(board1.getBoardId()).andReturn(casualGroup.getGameId(901, 902)).anyTimes();
		expect(board1.getWonPlayers()).andReturn(Arrays.<Personality>asList(player1));
		replay(board1);

		gamePlayListener.gameFinished(board1, null, null);
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(casualRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(2, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound2, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyRound.Context(casualDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyRound.Context(expertDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyDivision.Context(new Tourney.Id(tourney.getNumber()), EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(activeTourneys + 1, tourneyManager.getTotalCount(null, new Tourney.Context(EnumSet.of(TourneyEntity.State.ACTIVE))));

		final List<TourneyGroup> tourneyExpertGroups1 = tourneyManager.searchTourneyEntities(null, new TourneyGroup.Context(expertRound1, null), null, null);
		final TourneyGroup group0 = tourneyExpertGroups1.get(0);

		final GameBoard<GameSettings, GamePlayerHand, GameMove> board2 = createMock(GameBoard.class);
		expect(board2.getBoardId()).andReturn(group0.getGameId(903, 904)).anyTimes();
		expect(board2.getWonPlayers()).andReturn(Arrays.<Personality>asList(player3));
		replay(board2);
		gamePlayListener.gameFinished(board2, null, null);
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(casualRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(2, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound2, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyRound.Context(casualDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyRound.Context(expertDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyDivision.Context(new Tourney.Id(tourney.getNumber()), EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(activeTourneys + 1, tourneyManager.getTotalCount(null, new Tourney.Context(EnumSet.of(TourneyEntity.State.ACTIVE))));

		final GameBoard<GameSettings, GamePlayerHand, GameMove> board3 = createMock(GameBoard.class);
		expect(board3.getBoardId()).andReturn(group0.getGameId(903, 905)).anyTimes();
		expect(board3.getWonPlayers()).andReturn(Arrays.<Personality>asList(player3));
		replay(board3);
		gamePlayListener.gameFinished(board3, null, null);
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(casualRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(2, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound2, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyRound.Context(casualDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyRound.Context(expertDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyDivision.Context(new Tourney.Id(tourney.getNumber()), EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(activeTourneys + 1, tourneyManager.getTotalCount(null, new Tourney.Context(EnumSet.of(TourneyEntity.State.ACTIVE))));

		final GameBoard<GameSettings, GamePlayerHand, GameMove> board4 = createMock(GameBoard.class);
		expect(board4.getBoardId()).andReturn(group0.getGameId(904, 905)).anyTimes();
		expect(board4.getWonPlayers()).andReturn(Arrays.<Personality>asList(player4));
		replay(board4);
		gamePlayListener.gameFinished(board4, null, null);
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(casualRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound1, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyGroup.Context(expertRound2, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(0, tourneyManager.getTotalCount(null, new TourneyRound.Context(casualDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyRound.Context(expertDivision, EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(1, tourneyManager.getTotalCount(null, new TourneyDivision.Context(new Tourney.Id(tourney.getNumber()), EnumSet.of(TourneyEntity.State.ACTIVE))));
		assertEquals(activeTourneys + 1, tourneyManager.getTotalCount(null, new Tourney.Context(EnumSet.of(TourneyEntity.State.ACTIVE))));

		final GameBoard<GameSettings, GamePlayerHand, GameMove> board5 = createMock(GameBoard.class);
		final TourneyGroup group1 = tourneyExpertGroups1.get(1);
		expect(board5.getBoardId()).andReturn(group1.getGameId(906, 907)).anyTimes();
		expect(board5.getWonPlayers()).andReturn(Arrays.<Personality>asList(player6));
		replay(board5);
		gamePlayListener.gameFinished(board5, null, null);
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

		final List<TourneyGroup> tourneyExpertGroups2 = tourneyManager.searchTourneyEntities(null, new TourneyGroup.Context(expertRound2, null), null, null);
		final GameBoard<GameSettings, GamePlayerHand, GameMove> board6 = createMock(GameBoard.class);
		final TourneyGroup group3 = tourneyExpertGroups2.get(0);
		expect(board6.getBoardId()).andReturn(group3.getGameId(903, 906)).anyTimes();
		expect(board6.getWonPlayers()).andReturn(Arrays.<Personality>asList(player3, player6));
		replay(board6);
		gamePlayListener.gameFinished(board6, null, null);
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

		final TourneyDivision tourneyEntity = tourneyManager.getTourneyEntity(casualDivision);
		final Collection<TourneyWinner> winners = tourneyEntity.getTourneyWinners();
		assertNotNull(winners.size());

		assertEquals(2, divisionCapture.getValues().size());
		assertEquals(casualDivision, divisionCapture.getValues().get(0).getId());
		assertEquals(expertDivision, divisionCapture.getValues().get(1).getId());

		assertEquals(2, winners.size());
		final Iterator<TourneyWinner> iterator = winners.iterator();
		final TourneyWinner first = iterator.next();
		assertEquals(901L, first.getPlayer().longValue());
		assertEquals(TourneyPlace.FIRST, first.getPlace());

		final TourneyWinner second = iterator.next();
		assertEquals(902L, second.getPlayer().longValue());
		assertEquals(TourneyPlace.SECOND, second.getPlace());

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
		final SQLQuery sqlQuery = session.createSQLQuery("INSERT INTO scribble_statistic(playerId, rating) VALUES(:pid, :rating)");
		sqlQuery.setParameter("pid", pid);
		sqlQuery.setParameter("rating", rating);
		sqlQuery.executeUpdate();
	}

}
