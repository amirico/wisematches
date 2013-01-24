package wisematches.server.web.services.notify.impl;

import freemarker.template.Configuration;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.IMockBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Language;
import wisematches.core.Personality;
import wisematches.core.expiration.ExpirationListener;
import wisematches.core.personality.player.MemberPlayerManager;
import wisematches.core.personality.player.MemberPlayer;
import wisematches.core.personality.player.account.Account;
import wisematches.core.search.Orders;
import wisematches.core.search.Range;
import wisematches.core.search.SearchFilter;
import wisematches.core.task.executor.TransactionAwareExecutor;
import wisematches.playground.*;
import wisematches.playground.award.*;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.message.MessageListener;
import wisematches.playground.message.MessageManager;
import wisematches.playground.message.impl.HibernateMessage;
import wisematches.playground.propose.*;
import wisematches.playground.propose.impl.DefaultGameProposal;
import wisematches.playground.scribble.*;
import wisematches.playground.scribble.bank.TilesBank;
import wisematches.playground.scribble.bank.impl.TilesBankInfoEditor;
import wisematches.playground.scribble.expiration.ScribbleExpirationManager;
import wisematches.playground.scribble.expiration.ScribbleExpirationType;
import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.regular.*;
import wisematches.server.web.services.dictionary.ChangeSuggestion;
import wisematches.server.web.services.dictionary.DictionarySuggestionListener;
import wisematches.server.web.services.dictionary.DictionarySuggestionManager;
import wisematches.server.web.services.dictionary.SuggestionType;
import wisematches.server.web.services.notify.Notification;
import wisematches.server.web.services.notify.NotificationScope;
import wisematches.server.web.services.props.impl.MemoryPropertiesManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/database-junit-config.xml",
		"classpath:/config/accounts-config.xml",
		"classpath:/config/playground-config.xml",
		"classpath:/config/scribble-config.xml",
		"classpath:/config/services-config.xml",
		"classpath:/config/notifications-config.xml",
		"classpath:/config/services-junit-config.xml"
})
public class NotificationOriginCenterTest {
	@Autowired
	private Configuration notificationFreemarkerConfig;

	@Autowired
	private HibernateNotificationService notificationService;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private NotificationOriginCenter publisherCenter;

	private ScribbleBoard board1;
	private ScribbleBoard board2;

	private final Account p1 = createMockPlayer(1001, Language.RU);
	private final Account p2 = createMockPlayer(1002, Language.EN);

	private final Capture<Notification> publishedNotifications = new Capture<>(CaptureType.ALL);

	public NotificationOriginCenterTest() {
	}

	@Before
	public void setUp() throws Exception {
		final MemberPlayerManager playerManager = createMock(MemberPlayerManager.class);
		expect(playerManager.getPlayer(1001L)).andReturn(new MemberPlayer(p1)).anyTimes();
		expect(playerManager.getPlayer(1002L)).andReturn(new MemberPlayer(p2)).anyTimes();
		expect(playerManager.getPlayer(p1)).andReturn(new MemberPlayer(p1)).anyTimes();
		expect(playerManager.getPlayer(p2)).andReturn(new MemberPlayer(p2)).anyTimes();
		replay(playerManager);

		final RatingManager ratingManager = createMock(RatingManager.class);
		expect(ratingManager.getRating(Personality.person(1001))).andReturn((short) 1234).anyTimes();
		expect(ratingManager.getRating(Personality.person(1002))).andReturn((short) 2122).anyTimes();
		replay(ratingManager);

		publisherCenter.setPlayerManager(playerManager);

		notificationFreemarkerConfig.setSharedVariable("playerManager", playerManager);
		notificationFreemarkerConfig.setSharedVariable("ratingManager", ratingManager);

		final TransactionAwareExecutor taskExecutor = new TransactionAwareExecutor();
		taskExecutor.setTaskExecutor(new SyncTaskExecutor());
		taskExecutor.setTransactionManager(transactionManager);

		publisherCenter.setTaskExecutor(new SyncTaskExecutor());

		final NotificationPublisher notificationPublisher = createMock(NotificationPublisher.class);
		expect(notificationPublisher.getNotificationScope()).andReturn(NotificationScope.GLOBAL).anyTimes();
		notificationPublisher.publishNotification(capture(publishedNotifications));
		expectLastCall().anyTimes();
		replay(notificationPublisher);

		notificationService.setTaskExecutor(taskExecutor);
		notificationService.setPublishers(Arrays.asList(notificationPublisher));

		final Dictionary vocabulary = createNiceMock(Dictionary.class);
		expect(vocabulary.contains(isA(String.class))).andReturn(true).anyTimes();
		replay(vocabulary);

		board1 = new ScribbleBoard(new ScribbleSettings("mock1", Language.RU, 3), Arrays.asList(p1, p2), new TilesBank(new TilesBankInfoEditor(Language.EN).add('A', 100, 1).createTilesBankInfo()), vocabulary);

		do {
			board2 = new ScribbleBoard(new ScribbleSettings("mock2", Language.EN, 5), Arrays.asList(p1, p2), new TilesBank(new TilesBankInfoEditor(Language.EN).add('A', 100, 1).createTilesBankInfo()), vocabulary);
		} while (board1.getPlayerTurn().getPlayerId() == board2.getPlayerTurn().getPlayerId());
	}

	@Test
	public void testGameStarted() throws GameMoveException {
		final Capture<GamePlayListener> listenerCapture = new Capture<>();

		final GamePlayManager gamePlayManager = createStrictMock(GamePlayManager.class);
		gamePlayManager.addGamePlayListener(capture(listenerCapture));
		gamePlayManager.removeGamePlayListener(capture(listenerCapture));
		replay(gamePlayManager);

		publisherCenter.setGamePlayManager(gamePlayManager);

		final GamePlayListener gamePlayListener = listenerCapture.getValue();
		gamePlayListener.gameStarted(board1);
		gamePlayListener.gameStarted(board2);
		assertEquals(4, publishedNotifications.getValues().size());

		publisherCenter.setGamePlayManager(null);

		verify(gamePlayManager);
	}

	@Test
	public void testGameMoveDone() throws GameMoveException {
		final Capture<GamePlayListener> listenerCapture = new Capture<>();

		final GamePlayManager gamePlayManager = createStrictMock(GamePlayManager.class);
		gamePlayManager.addGamePlayListener(capture(listenerCapture));
		gamePlayManager.removeGamePlayListener(capture(listenerCapture));
		replay(gamePlayManager);

		publisherCenter.setGamePlayManager(gamePlayManager);

		final GamePlayListener gamePlayListener = listenerCapture.getValue();

		// Pass turn
		board1.makeMove(new PassTurn(board1.getPlayerTurn().getPlayerId()));
		board2.makeMove(new PassTurn(board2.getPlayerTurn().getPlayerId()));
		gamePlayListener.gameMoveDone(board1, null, null);
		gamePlayListener.gameMoveDone(board2, null, null);
		assertEquals(2, publishedNotifications.getValues().size());
		publishedNotifications.reset();

		final wisematches.playground.scribble.Word word = new wisematches.playground.scribble.Word(new Position(7, 7), Direction.HORIZONTAL, board1.getPlayerHand(p1.getId()).getTiles());
		board1.makeMove(new MakeTurn(board1.getPlayerTurn().getPlayerId(), word));
		board2.makeMove(new MakeTurn(board2.getPlayerTurn().getPlayerId(), word));
		gamePlayListener.gameMoveDone(board1, null, null);
		gamePlayListener.gameMoveDone(board2, null, null);
		assertEquals(2, publishedNotifications.getValues().size());
		publishedNotifications.reset();

		int[] tiles1 = new int[3];
		for (int i = 0; i < tiles1.length; i++) {
			tiles1[i] = board1.getPlayerTurn().getTiles()[i].getNumber();
		}
		int[] tiles2 = new int[3];
		for (int i = 0; i < tiles2.length; i++) {
			tiles2[i] = board2.getPlayerTurn().getTiles()[i].getNumber();
		}
		board1.makeMove(new ExchangeMove(board1.getPlayerTurn().getPlayerId(), tiles1));
		board2.makeMove(new ExchangeMove(board2.getPlayerTurn().getPlayerId(), tiles2));
		gamePlayListener.gameMoveDone(board1, null, null);
		gamePlayListener.gameMoveDone(board2, null, null);
		assertEquals(2, publishedNotifications.getValues().size());
		publishedNotifications.reset();

		publisherCenter.setGamePlayManager(null);

		verify(gamePlayManager);
	}

	@Test
	public void testGameFinished() throws GameMoveException {
		final Capture<GamePlayListener> listenerCapture = new Capture<>();

		final GamePlayManager gamePlayManager = createStrictMock(GamePlayManager.class);
		gamePlayManager.addGamePlayListener(capture(listenerCapture));
		gamePlayManager.removeGamePlayListener(capture(listenerCapture));
		replay(gamePlayManager);

		publisherCenter.setGamePlayManager(gamePlayManager);
		board1.makeMove(new PassTurn(board1.getPlayerTurn().getPlayerId()));
		board2.makeMove(new PassTurn(board2.getPlayerTurn().getPlayerId()));
		final wisematches.playground.scribble.Word word = new wisematches.playground.scribble.Word(new Position(7, 7), Direction.HORIZONTAL, board1.getPlayerHand(p1.getId()).getTiles());
		board1.makeMove(new MakeTurn(board1.getPlayerTurn().getPlayerId(), word));
		board2.makeMove(new MakeTurn(board2.getPlayerTurn().getPlayerId(), word));
		board1.makeMove(new PassTurn(board1.getPlayerTurn().getPlayerId()));
		board2.makeMove(new PassTurn(board2.getPlayerTurn().getPlayerId()));
		board1.resign(board1.getPlayerTurn());
		board2.resign(board2.getPlayerTurn());

		final GamePlayListener gamePlayListener = listenerCapture.getValue();
		gamePlayListener.gameFinished(board1, GameResolution.FINISHED, Collections.<GamePlayerHand>singleton(board1.getPlayerHand(p1.getId())));
		gamePlayListener.gameFinished(board1, GameResolution.STALEMATE, Collections.<GamePlayerHand>singleton(board1.getPlayerHand(p1.getId())));
		gamePlayListener.gameFinished(board1, GameResolution.RESIGNED, Collections.<GamePlayerHand>singleton(board1.getPlayerHand(p1.getId())));
		gamePlayListener.gameFinished(board1, GameResolution.INTERRUPTED, Collections.<GamePlayerHand>singleton(board1.getPlayerHand(p1.getId())));
		gamePlayListener.gameFinished(board2, GameResolution.FINISHED, Collections.<GamePlayerHand>singleton(board1.getPlayerHand(p1.getId())));
		gamePlayListener.gameFinished(board2, GameResolution.STALEMATE, Collections.<GamePlayerHand>singleton(board1.getPlayerHand(p1.getId())));
		gamePlayListener.gameFinished(board2, GameResolution.RESIGNED, Collections.<GamePlayerHand>singleton(board1.getPlayerHand(p1.getId())));
		gamePlayListener.gameFinished(board2, GameResolution.INTERRUPTED, Collections.<GamePlayerHand>singleton(board1.getPlayerHand(p1.getId())));
		assertEquals(16, publishedNotifications.getValues().size());

		publisherCenter.setGamePlayManager(null);

		verify(gamePlayManager);
	}

	@Test
	public void testScribbleExpiring() throws BoardLoadingException {
		final Capture<ExpirationListener<Long, ScribbleExpirationType>> listenerCapture = new Capture<>();

		final GamePlayManager gamePlayManager = createStrictMock(GamePlayManager.class);
		gamePlayManager.addGamePlayListener(isA(GamePlayListener.class));
		expect(gamePlayManager.openBoard(1L)).andReturn(board1).times(3);
		expect(gamePlayManager.openBoard(2L)).andReturn(board2).times(3);
		gamePlayManager.removeGamePlayListener(isA(GamePlayListener.class));
		replay(gamePlayManager);

		final ScribbleExpirationManager expirationManager = createStrictMock(ScribbleExpirationManager.class);
		expirationManager.addExpirationListener(capture(listenerCapture));
		replay(expirationManager);

		publisherCenter.setGamePlayManager(gamePlayManager);
		publisherCenter.setScribbleExpirationManager(expirationManager);

		listenerCapture.getValue().expirationTriggered(1L, ScribbleExpirationType.ONE_DAY);
		listenerCapture.getValue().expirationTriggered(1L, ScribbleExpirationType.HALF_DAY);
		listenerCapture.getValue().expirationTriggered(1L, ScribbleExpirationType.ONE_HOUR);
		listenerCapture.getValue().expirationTriggered(2L, ScribbleExpirationType.ONE_DAY);
		listenerCapture.getValue().expirationTriggered(2L, ScribbleExpirationType.HALF_DAY);
		listenerCapture.getValue().expirationTriggered(2L, ScribbleExpirationType.ONE_HOUR);

		assertEquals(6, publishedNotifications.getValues().size());

		publisherCenter.setGamePlayManager(null);

		verify(gamePlayManager);
	}

	@Test
	public void testProposalInitiated() {
		final Capture<GameProposalListener> listenerCapture = new Capture<>();

		final GameProposalManager proposalManager = createStrictMock(GameProposalManager.class);
		proposalManager.addGameProposalListener(capture(listenerCapture));
		proposalManager.removeGameProposalListener(capture(listenerCapture));
		replay(proposalManager);

		publisherCenter.setProposalManager(proposalManager);

		final DefaultGameProposal<GameSettings> gp1 = new DefaultGameProposal<GameSettings>(1, new ScribbleSettings("Scribble game", Language.RU), p1, new Personality[]{p2});
		final DefaultGameProposal<GameSettings> gp2 = new DefaultGameProposal<GameSettings>(1, "Hey, let's play!", new ScribbleSettings("Scribble game", Language.RU), p1, new Personality[]{p2});
		final DefaultGameProposal<GameSettings> gp3 = new DefaultGameProposal<GameSettings>(1, new ScribbleSettings("Scribble game", Language.RU), p2, new Personality[]{p1});
		final DefaultGameProposal<GameSettings> gp4 = new DefaultGameProposal<GameSettings>(1, "Hey, let's play!", new ScribbleSettings("Scribble game", Language.RU), p2, new Personality[]{p1});

		listenerCapture.getValue().gameProposalInitiated(gp1);
		listenerCapture.getValue().gameProposalInitiated(gp2);
		listenerCapture.getValue().gameProposalInitiated(gp3);
		listenerCapture.getValue().gameProposalInitiated(gp4);
		assertEquals(4, publishedNotifications.getValues().size());

		publisherCenter.setProposalManager(null);

		verify(proposalManager);
	}

	@Test
	public void testProposalFinalized() {
		final Capture<GameProposalListener> listenerCapture = new Capture<>();

		final GameProposalManager proposalManager = createStrictMock(GameProposalManager.class);
		proposalManager.addGameProposalListener(capture(listenerCapture));
		proposalManager.removeGameProposalListener(capture(listenerCapture));
		replay(proposalManager);

		publisherCenter.setProposalManager(proposalManager);

		final DefaultGameProposal<GameSettings> gp1 = new DefaultGameProposal<GameSettings>(1, new ScribbleSettings("Scribble game", Language.RU), p1, new Personality[]{p2});
		final DefaultGameProposal<GameSettings> gp2 = new DefaultGameProposal<GameSettings>(1, new ScribbleSettings("Scribble game", Language.RU), p2, new Personality[]{p1});

		listenerCapture.getValue().gameProposalUpdated(gp1, p1, ProposalDirective.ACCEPTED);  // no messages
		listenerCapture.getValue().gameProposalUpdated(gp2, p2, ProposalDirective.ACCEPTED);  // no messages

		listenerCapture.getValue().gameProposalFinalized(gp1, ProposalResolution.READY, p2); // no messages
		listenerCapture.getValue().gameProposalFinalized(gp2, ProposalResolution.READY, p1); // no messages

		listenerCapture.getValue().gameProposalFinalized(gp1, ProposalResolution.REJECTED, p2);
		listenerCapture.getValue().gameProposalFinalized(gp2, ProposalResolution.REJECTED, p1);

		listenerCapture.getValue().gameProposalFinalized(gp1, ProposalResolution.REPUDIATED, p2);
		listenerCapture.getValue().gameProposalFinalized(gp2, ProposalResolution.REPUDIATED, p1);

		listenerCapture.getValue().gameProposalFinalized(gp1, ProposalResolution.TERMINATED, p2);
		listenerCapture.getValue().gameProposalFinalized(gp2, ProposalResolution.TERMINATED, p1);

		assertEquals(6, publishedNotifications.getValues().size());

		publisherCenter.setProposalManager(null);

		verify(proposalManager);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testProposalExpiration() {
		final Capture<ExpirationListener<Long, ProposalExpirationType>> listenerCapture = new Capture<>();

		final DefaultGameProposal<GameSettings> gp1 = new DefaultGameProposal<GameSettings>(1, new ScribbleSettings("Scribble game", Language.RU), p1, new Personality[]{p2});
		final DefaultGameProposal<GameSettings> gp2 = new DefaultGameProposal<GameSettings>(1, new ScribbleSettings("Scribble game", Language.RU), p2, new Personality[]{p1});

		final GameProposalManager proposalManager = createStrictMock(GameProposalManager.class);
		proposalManager.addGameProposalListener(isA(GameProposalListener.class));
		expect(proposalManager.getProposal(1L)).andReturn(gp1).times(2);
		expect(proposalManager.getProposal(2L)).andReturn(gp2).times(2);
		proposalManager.removeGameProposalListener(isA(GameProposalListener.class));
		replay(proposalManager);

		final ProposalExpirationManager<ScribbleSettings> expirationManager = createStrictMock(ProposalExpirationManager.class);
		expirationManager.addExpirationListener(capture(listenerCapture));
		replay(expirationManager);

		publisherCenter.setProposalManager(proposalManager);
		publisherCenter.setProposalExpirationManager(expirationManager);

		listenerCapture.getValue().expirationTriggered(1L, ProposalExpirationType.THREE_DAYS);
		listenerCapture.getValue().expirationTriggered(1L, ProposalExpirationType.ONE_DAY);
		listenerCapture.getValue().expirationTriggered(2L, ProposalExpirationType.THREE_DAYS);
		listenerCapture.getValue().expirationTriggered(2L, ProposalExpirationType.ONE_DAY);

		assertEquals(4, publishedNotifications.getValues().size());

		publisherCenter.setProposalManager(null);

		verify(expirationManager);
	}

	@Test
	public void testMessage() {
		final Capture<MessageListener> listenerCapture = new Capture<>();

		final MessageManager messageManager = createStrictMock(MessageManager.class);
		messageManager.addMessageListener(capture(listenerCapture));
		replay(messageManager);

		publisherCenter.setMessageManager(messageManager);

		listenerCapture.getValue().messageSent(new HibernateMessage(p1, "Huy"), true);
		listenerCapture.getValue().messageSent(new HibernateMessage(p1, "Huy", p2), true);
		listenerCapture.getValue().messageSent(new HibernateMessage(p2, "Huy"), true);
		listenerCapture.getValue().messageSent(new HibernateMessage(p2, "Huy", p1), true);
		listenerCapture.getValue().messageSent(new HibernateMessage(p1, "Huy"), false);
		listenerCapture.getValue().messageSent(new HibernateMessage(p1, "Huy", p2), false);
		listenerCapture.getValue().messageSent(new HibernateMessage(p2, "Huy"), false);
		listenerCapture.getValue().messageSent(new HibernateMessage(p2, "Huy", p1), false);
		assertEquals(4, publishedNotifications.getValues().size());

		verify(messageManager);
	}

	@Test
	public void testTourneyAnnounced() throws InterruptedException {
		final RegularTourneyManager tourneyManager = createMock(RegularTourneyManager.class);
		final List<RegularTourneyEntity> value = Collections.emptyList();
		tourneyManager.addRegularTourneyListener(isA(RegularTourneyListener.class));
		expect(tourneyManager.searchTourneyEntities(isNull(Personality.class), isA(TourneyEntity.Context.class), isNull(SearchFilter.class), isNull(Orders.class), isNull(Range.class))).andReturn(value);
		replay(tourneyManager);
		publisherCenter.setTourneyManager(tourneyManager);

		publisherCenter.breakingDayTime(null);
		assertEquals(0, publishedNotifications.getValues().size());

		Tourney t1 = createMock(Tourney.class);
		expect(t1.getId()).andReturn(new Tourney.Id(1)).anyTimes();
		expect(t1.getNumber()).andReturn(1).anyTimes();
		expect(t1.getScheduledDate()).andReturn(new Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000)).anyTimes(); // in 10 days
		replay(t1);

		Tourney t2 = createMock(Tourney.class);
		expect(t2.getId()).andReturn(new Tourney.Id(2)).anyTimes();
		expect(t2.getNumber()).andReturn(2).anyTimes();
		expect(t2.getScheduledDate()).andReturn(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)).anyTimes(); // in 7 days
		replay(t2);

		Tourney t3 = createMock(Tourney.class);
		expect(t3.getId()).andReturn(new Tourney.Id(3)).anyTimes();
		expect(t3.getNumber()).andReturn(3).anyTimes();
		expect(t3.getScheduledDate()).andReturn(new Date(System.currentTimeMillis() + 1 * 24 * 60 * 60 * 1000)).anyTimes(); // in 2 days
		replay(t3);

		@SuppressWarnings("unchecked")
		final RegistrationSearchManager searchManager = createMock(RegistrationSearchManager.class);
		expect(searchManager.searchUnregisteredPlayers(t2, Range.limit(0, 1000))).andReturn(new long[]{1001L, 1002L});
		replay(searchManager);

		reset(tourneyManager);
		expect(tourneyManager.searchTourneyEntities(isNull(Personality.class), isA(RegularTourneyEntity.Context.class), isNull(SearchFilter.class), isNull(Orders.class), isNull(Range.class))).andReturn(Arrays.<RegularTourneyEntity>asList(t1, t2, t3));
		expect(tourneyManager.getRegistrationSearchManager()).andReturn(searchManager);
		tourneyManager.removeRegularTourneyListener(isA(RegularTourneyListener.class));
		replay(tourneyManager);

		publisherCenter.setPropertiesManager(new MemoryPropertiesManager());
		publisherCenter.breakingDayTime(null);
		assertEquals(2, publishedNotifications.getValues().size());

		System.out.println(publishedNotifications);
		publisherCenter.setTourneyManager(null);

		verify(searchManager);
	}

/*
	@Test
	public void testTourneyFinished() throws InterruptedException {
		final Capture<RegularTourneyListener> tourneyListener = new Capture<>();

		final RegularTourneyManager tourneyManager = createMock(RegularTourneyManager.class);
		tourneyManager.addRegularTourneyListener(capture(tourneyListener));
		tourneyManager.removeRegularTourneyListener(capture(tourneyListener));
		replay(tourneyManager);

		publisherCenter.setTourneyManager(tourneyManager);

		final Tourney tourney = createMock(Tourney.class);
		expect(tourney.getId()).andReturn(new Tourney.Id(1)).anyTimes();
		expect(tourney.getNumber()).andReturn(1).anyTimes();
		replay(tourney);

		final TourneyDivision division = createMock(TourneyDivision.class);
		expect(division.getTourneyWinners()).andReturn(Arrays.<TourneyWinner>asList(
				new HibernateTourneyWinner(p1.getId(), TourneyPlace.FIRST),
				new HibernateTourneyWinner(p2.getId(), TourneyPlace.SECOND)));
		expect(division.getSection()).andReturn(TourneySection.CASUAL).times(2);
		replay(division);

		tourneyListener.getValue().tourneyFinished(tourney, division);

		assertEquals(2, publishedNotifications.getValues().size());
		System.out.println(publishedNotifications);

		publisherCenter.setTourneyManager(null);

		verify(tourneyManager);
	}
*/

	@Test
	public void testAwardGranted() throws Exception {
		final Capture<AwardsListener> awardsListener = new Capture<>();

		final AwardsManager awardsManager = createMock(AwardsManager.class);
		awardsManager.addAwardsListener(capture(awardsListener));
		awardsManager.removeAwardsListener(capture(awardsListener));
		replay(awardsManager);

		publisherCenter.setAwardsManager(awardsManager);

		final AwardDescriptor ad = new AwardDescriptor("tourney.winner", AwardType.MEDAL);

		final Award award = createMock(Award.class);
		expect(award.getCode()).andReturn("tourney.winner").times(2);
		expect(award.getAwardedDate()).andReturn(new Date()).times(2);
		expect(award.getRelationship()).andReturn(new GameRelationship(1, 1)).times(2);
		expect(award.getWeight()).andReturn(AwardWeight.BRONZE).times(2);
		replay(award);

		awardsListener.getValue().playerAwarded(p1, ad, award);
		awardsListener.getValue().playerAwarded(p2, ad, award);

		assertEquals(2, publishedNotifications.getValues().size());
		System.out.println(publishedNotifications);

		publisherCenter.setAwardsManager(null);

		verify(awardsManager);
	}

	@Test
	public void testDictionaryChange() throws InterruptedException {
		final Capture<DictionarySuggestionListener> tourneyListener = new Capture<>(CaptureType.ALL);

		final DictionarySuggestionManager suggestionManager = createMock(DictionarySuggestionManager.class);
		suggestionManager.addDictionaryChangeListener(capture(tourneyListener));
		suggestionManager.removeDictionaryChangeListener(capture(tourneyListener));
		replay(suggestionManager);

		publisherCenter.setDictionarySuggestionManager(suggestionManager);

		final ChangeSuggestion suggestion = createMock(ChangeSuggestion.class);
		expect(suggestion.getRequester()).andReturn(1001L).times(2);
		expect(suggestion.getWord()).andReturn("MockSuggest").times(2);
		expect(suggestion.getSuggestionType()).andReturn(SuggestionType.ADD);
		replay(suggestion);

		tourneyListener.getValue().changeRequestApproved(suggestion);
		tourneyListener.getValue().changeRequestRejected(suggestion);

		assertEquals(2, publishedNotifications.getValues().size());
		System.out.println(publishedNotifications);

		publisherCenter.setDictionarySuggestionManager(null);

		verify(suggestionManager, suggestion);
	}

	private Account createMockPlayer(long i, Language en) {
		IMockBuilder<Account> mockBuilder = createMockBuilder(Account.class);
		mockBuilder.withConstructor(i);

		final Account mock = mockBuilder.createMock("mock" + i);
		expect(mock.getLanguage()).andReturn(en).anyTimes();
		expect(mock.getNickname()).andReturn("mock" + i).anyTimes();
		expect(mock.getEmail()).andReturn("support@localhost").anyTimes();
		replay(mock);
		return mock;
	}
}