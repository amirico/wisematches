package wisematches.server.services.notify.impl;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.junit.After;
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
import wisematches.core.Membership;
import wisematches.core.Personality;
import wisematches.core.Player;
import wisematches.core.expiration.ExpirationListener;
import wisematches.core.personality.DefaultMember;
import wisematches.core.personality.DefaultVisitor;
import wisematches.core.search.Orders;
import wisematches.core.search.Range;
import wisematches.core.task.executor.TransactionAwareExecutor;
import wisematches.playground.*;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.propose.*;
import wisematches.playground.propose.impl.DefaultPrivateProposal;
import wisematches.playground.scribble.Direction;
import wisematches.playground.scribble.Position;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.playground.scribble.bank.TilesBank;
import wisematches.playground.scribble.bank.impl.TilesBankInfoEditor;
import wisematches.playground.scribble.expiration.ScribbleExpirationManager;
import wisematches.playground.scribble.expiration.ScribbleExpirationType;
import wisematches.playground.tourney.TourneyEntity;
import wisematches.playground.tourney.regular.*;
import wisematches.server.services.award.*;
import wisematches.server.services.dictionary.DictionarySuggestionListener;
import wisematches.server.services.dictionary.DictionarySuggestionManager;
import wisematches.server.services.dictionary.SuggestionType;
import wisematches.server.services.dictionary.WordSuggestion;
import wisematches.server.services.message.MessageListener;
import wisematches.server.services.message.MessageManager;
import wisematches.server.services.message.impl.HibernateMessage;
import wisematches.server.services.notify.Notification;
import wisematches.server.services.notify.NotificationScope;
import wisematches.server.services.notify.NotificationService;
import wisematches.server.services.props.impl.MemoryPropertiesManager;
import wisematches.server.services.relations.PlayerEntityBean;
import wisematches.server.services.relations.PlayerSearchArea;
import wisematches.server.services.relations.ScribblePlayerSearchManager;

import java.util.*;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/config/properties-config.xml",
		"classpath:/config/database-config.xml",
		"classpath:/config/personality-config.xml",
		"classpath:/config/playground-config.xml",
		"classpath:/config/scribble-config.xml",
})
public class NotificationOriginCenterTest {
	@Autowired
	private NotificationService notificationService;

	@Autowired
	private NotificationOriginCenter publisherCenter;

	@Autowired
	private PlatformTransactionManager transactionManager;

	@Autowired
	private ScribblePlayerSearchManager playerSearchManager;

	private ScribbleBoard board1;
	private ScribbleBoard board2;

	private Player p1;
	private Player p2;

	private final Capture<Notification> publishedNotifications = new Capture<>(CaptureType.ALL);

	public NotificationOriginCenterTest() {
	}

	@Before
	public void setUp() throws Exception {
		final TransactionAwareExecutor taskExecutor = new TransactionAwareExecutor();
		taskExecutor.setTaskExecutor(new SyncTaskExecutor());
		taskExecutor.setTransactionManager(transactionManager);

		final NotificationPublisher notificationPublisher = createMock(NotificationPublisher.class);
		expect(notificationPublisher.getNotificationScope()).andReturn(NotificationScope.GLOBAL).anyTimes();
		notificationPublisher.publishNotification(capture(publishedNotifications));
		expectLastCall().anyTimes();
		replay(notificationPublisher);

		if (notificationService instanceof PublishNotificationService) {
			final PublishNotificationService service = (PublishNotificationService) notificationService;
			service.setTaskExecutor(taskExecutor);
			service.setNotificationPublishers(Arrays.asList(notificationPublisher));
		} else {
			fail("NotificationService is not PublishNotificationService");
		}

		publisherCenter.setTaskExecutor(new SyncTaskExecutor());

		final Dictionary vocabulary = createNiceMock(Dictionary.class);
		expect(vocabulary.contains(isA(String.class))).andReturn(true).anyTimes();
		replay(vocabulary);

		final List<PlayerEntityBean> peb = playerSearchManager.searchEntities(new DefaultVisitor(Language.EN), PlayerSearchArea.PLAYERS, null, Range.limit(2));

		p1 = new DefaultMember(peb.get(0).getPlayer(), "mock1", "mock1@localhost", TimeZone.getDefault(), Membership.BASIC, Language.RU);
		p2 = new DefaultMember(peb.get(1).getPlayer(), "mock2", "mock2@localhost", TimeZone.getDefault(), Membership.BASIC, Language.EN);

		board1 = new ScribbleBoard(new ScribbleSettings("mock1", Language.RU, 3), Arrays.asList(p1, p2), new TilesBank(new TilesBankInfoEditor(Language.EN).add('A', 100, 1).createTilesBankInfo()), vocabulary);

		do {
			board2 = new ScribbleBoard(new ScribbleSettings("mock2", Language.EN, 5), Arrays.asList(p1, p2), new TilesBank(new TilesBankInfoEditor(Language.EN).add('A', 100, 1).createTilesBankInfo()), vocabulary);
		} while (board1.getPlayerTurn().getId().equals(board2.getPlayerTurn().getId()));
	}

	@After
	public void tearDown() throws Exception {
		for (Notification notification : publishedNotifications.getValues()) {
			System.out.println(notification);
		}
	}

	@Test
	public void testGameStarted() throws GameMoveException {
		final Capture<BoardListener> listenerCapture = new Capture<>();

		final BoardManager gamePlayManager = createStrictMock(BoardManager.class);
		gamePlayManager.addBoardListener(capture(listenerCapture));
		gamePlayManager.removeBoardListener(capture(listenerCapture));
		replay(gamePlayManager);

		publisherCenter.setBoardManager(gamePlayManager);

		final BoardListener gamePlayListener = listenerCapture.getValue();
		gamePlayListener.gameStarted(board1);
		gamePlayListener.gameStarted(board2);
		assertEquals(4, publishedNotifications.getValues().size());

		publisherCenter.setBoardManager(null);

		verify(gamePlayManager);
	}

	@Test
	public void testGameMoveDone() throws GameMoveException {
		final Capture<BoardListener> listenerCapture = new Capture<>();

		final BoardManager gamePlayManager = createStrictMock(BoardManager.class);
		gamePlayManager.addBoardListener(capture(listenerCapture));
		gamePlayManager.removeBoardListener(capture(listenerCapture));
		replay(gamePlayManager);

		publisherCenter.setBoardManager(gamePlayManager);

		final BoardListener gamePlayListener = listenerCapture.getValue();

		// Pass turn
		board1.passTurn(board1.getPlayerTurn());
		board2.passTurn(board2.getPlayerTurn());
		gamePlayListener.gameMoveDone(board1, null, null);
		gamePlayListener.gameMoveDone(board2, null, null);
		assertEquals(2, publishedNotifications.getValues().size());

		final wisematches.playground.scribble.Word word = new wisematches.playground.scribble.Word(new Position(7, 7), Direction.HORIZONTAL, board1.getPlayerHand(p1).getTiles());
		board1.makeTurn(board1.getPlayerTurn(), word);
		board2.makeTurn(board2.getPlayerTurn(), word);
		gamePlayListener.gameMoveDone(board1, null, null);
		gamePlayListener.gameMoveDone(board2, null, null);
		assertEquals(4, publishedNotifications.getValues().size());

		int[] tiles1 = new int[3];
		for (int i = 0; i < tiles1.length; i++) {
			tiles1[i] = board1.getPlayerHand(board1.getPlayerTurn()).getTiles()[i].getNumber();
		}
		int[] tiles2 = new int[3];
		for (int i = 0; i < tiles2.length; i++) {
			tiles2[i] = board2.getPlayerHand(board2.getPlayerTurn()).getTiles()[i].getNumber();
		}
		board1.exchangeTiles(board1.getPlayerTurn(), tiles1);
		board2.exchangeTiles(board2.getPlayerTurn(), tiles2);
		gamePlayListener.gameMoveDone(board1, null, null);
		gamePlayListener.gameMoveDone(board2, null, null);
		assertEquals(6, publishedNotifications.getValues().size());

		publisherCenter.setBoardManager(null);

		verify(gamePlayManager);
	}

	@Test
	public void testGameFinished() throws BoardUpdatingException {
		final Capture<BoardListener> listenerCapture = new Capture<>();

		final BoardManager gamePlayManager = createStrictMock(BoardManager.class);
		gamePlayManager.addBoardListener(capture(listenerCapture));
		gamePlayManager.removeBoardListener(capture(listenerCapture));
		replay(gamePlayManager);

		publisherCenter.setBoardManager(gamePlayManager);
		board1.passTurn(board1.getPlayerTurn());
		board2.passTurn(board2.getPlayerTurn());
		final wisematches.playground.scribble.Word word = new wisematches.playground.scribble.Word(new Position(7, 7), Direction.HORIZONTAL, board1.getPlayerHand(p1).getTiles());
		board1.makeTurn(board1.getPlayerTurn(), word);
		board2.makeTurn(board2.getPlayerTurn(), word);
		board1.passTurn(board1.getPlayerTurn());
		board2.passTurn(board2.getPlayerTurn());
		board1.resign(board1.getPlayerTurn());
		board2.resign(board2.getPlayerTurn());

		final BoardListener gamePlayListener = listenerCapture.getValue();
		gamePlayListener.gameFinished(board1, GameResolution.FINISHED, Collections.<Personality>singleton(p1));
		gamePlayListener.gameFinished(board1, GameResolution.STALEMATE, Collections.<Personality>singleton(p1));
		gamePlayListener.gameFinished(board1, GameResolution.RESIGNED, Collections.<Personality>singleton(p1));
		gamePlayListener.gameFinished(board1, GameResolution.INTERRUPTED, Collections.<Personality>singleton(p1));
		gamePlayListener.gameFinished(board2, GameResolution.FINISHED, Collections.<Personality>singleton(p1));
		gamePlayListener.gameFinished(board2, GameResolution.STALEMATE, Collections.<Personality>singleton(p1));
		gamePlayListener.gameFinished(board2, GameResolution.RESIGNED, Collections.<Personality>singleton(p1));
		gamePlayListener.gameFinished(board2, GameResolution.INTERRUPTED, Collections.<Personality>singleton(p1));
		assertEquals(16, publishedNotifications.getValues().size());

		publisherCenter.setBoardManager(null);

		verify(gamePlayManager);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testScribbleExpiring() throws BoardLoadingException {
		final Capture<ExpirationListener<Long, ScribbleExpirationType>> listenerCapture = new Capture<>();

		final BoardManager boardManager = createStrictMock(BoardManager.class);
		boardManager.addBoardListener(isA(BoardListener.class));
		expect(boardManager.openBoard(1L)).andReturn((GameBoard) board1).times(3);
		expect(boardManager.openBoard(2L)).andReturn((GameBoard) board2).times(3);
		boardManager.removeBoardListener(isA(BoardListener.class));
		replay(boardManager);

		final ScribbleExpirationManager expirationManager = createStrictMock(ScribbleExpirationManager.class);
		expirationManager.addExpirationListener(capture(listenerCapture));
		replay(expirationManager);

		publisherCenter.setBoardManager(boardManager);
		publisherCenter.setScribbleExpirationManager(expirationManager);

		listenerCapture.getValue().expirationTriggered(1L, ScribbleExpirationType.ONE_DAY);
		listenerCapture.getValue().expirationTriggered(1L, ScribbleExpirationType.HALF_DAY);
		listenerCapture.getValue().expirationTriggered(1L, ScribbleExpirationType.ONE_HOUR);
		listenerCapture.getValue().expirationTriggered(2L, ScribbleExpirationType.ONE_DAY);
		listenerCapture.getValue().expirationTriggered(2L, ScribbleExpirationType.HALF_DAY);
		listenerCapture.getValue().expirationTriggered(2L, ScribbleExpirationType.ONE_HOUR);

		assertEquals(6, publishedNotifications.getValues().size());

		publisherCenter.setBoardManager(null);

		verify(boardManager);
	}

	@Test
	public void testProposalInitiated() {
		final Capture<GameProposalListener> listenerCapture = new Capture<>();

		final GameProposalManager proposalManager = createStrictMock(GameProposalManager.class);
		proposalManager.addGameProposalListener(capture(listenerCapture));
		proposalManager.removeGameProposalListener(capture(listenerCapture));
		replay(proposalManager);

		publisherCenter.setProposalManager(proposalManager);

		final GameProposal<GameSettings> gp1 = new DefaultPrivateProposal<GameSettings>(1, null, new ScribbleSettings("Scribble game", Language.RU), p1, Arrays.asList(p2));
		final GameProposal<GameSettings> gp2 = new DefaultPrivateProposal<GameSettings>(1, "Hey, let's play!", new ScribbleSettings("Scribble game", Language.RU), p1, Arrays.asList(p2));
		final GameProposal<GameSettings> gp3 = new DefaultPrivateProposal<GameSettings>(1, null, new ScribbleSettings("Scribble game", Language.RU), p2, Arrays.asList(p1));
		final GameProposal<GameSettings> gp4 = new DefaultPrivateProposal<GameSettings>(1, "Hey, let's play!", new ScribbleSettings("Scribble game", Language.RU), p2, Arrays.asList(p1));

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

		final GameProposal<GameSettings> gp1 = new DefaultPrivateProposal<GameSettings>(1, null, new ScribbleSettings("Scribble game", Language.RU), p1, Arrays.asList(p2));
		final GameProposal<GameSettings> gp2 = new DefaultPrivateProposal<GameSettings>(1, null, new ScribbleSettings("Scribble game", Language.RU), p2, Arrays.asList(p1));

		listenerCapture.getValue().gameProposalUpdated(gp1, p1, ProposalDirective.ACCEPTED);  // no messages
		listenerCapture.getValue().gameProposalUpdated(gp2, p2, ProposalDirective.ACCEPTED);  // no messages

		listenerCapture.getValue().gameProposalFinalized(gp1, p2, ProposalResolution.READY); // no messages
		listenerCapture.getValue().gameProposalFinalized(gp2, p1, ProposalResolution.READY); // no messages

		listenerCapture.getValue().gameProposalFinalized(gp1, p2, ProposalResolution.REJECTED);
		listenerCapture.getValue().gameProposalFinalized(gp2, p1, ProposalResolution.REJECTED);

		listenerCapture.getValue().gameProposalFinalized(gp1, p2, ProposalResolution.REPUDIATED);
		listenerCapture.getValue().gameProposalFinalized(gp2, p1, ProposalResolution.REPUDIATED);

		listenerCapture.getValue().gameProposalFinalized(gp1, p2, ProposalResolution.TERMINATED);
		listenerCapture.getValue().gameProposalFinalized(gp2, p1, ProposalResolution.TERMINATED);

		assertEquals(6, publishedNotifications.getValues().size());

		publisherCenter.setProposalManager(null);

		verify(proposalManager);
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testProposalExpiration() {
		final Capture<ExpirationListener<Long, ProposalExpirationType>> listenerCapture = new Capture<>();

		final GameProposal<GameSettings> gp1 = new DefaultPrivateProposal<GameSettings>(1, null, new ScribbleSettings("Scribble game", Language.RU), p1, Arrays.asList(p2));
		final GameProposal<GameSettings> gp2 = new DefaultPrivateProposal<GameSettings>(1, null, new ScribbleSettings("Scribble game", Language.RU), p2, Arrays.asList(p1));

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
	@SuppressWarnings("unchecked")
	public void testTourneyAnnounced() throws InterruptedException {
		final RegularTourneyManager tourneyManager = createMock(RegularTourneyManager.class);
		final List<RegularTourneyEntity> value = Collections.emptyList();
		tourneyManager.addRegularTourneyListener(isA(RegularTourneyListener.class));
		expect(tourneyManager.searchTourneyEntities(isNull(Personality.class), isA(TourneyEntity.Context.class), isNull(Orders.class), isNull(Range.class))).andReturn(value);
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
		expect(searchManager.searchUnregisteredPlayers(t2, Range.limit(0, 1000))).andReturn(new long[]{p1.getId(), p2.getId()});
		replay(searchManager);

		reset(tourneyManager);
		expect(tourneyManager.searchTourneyEntities(isNull(Personality.class), isA(RegularTourneyEntity.Context.class), isNull(Orders.class), isNull(Range.class))).andReturn(Arrays.<RegularTourneyEntity>asList(t1, t2, t3));
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
	@SuppressWarnings("unchecked")
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
		suggestionManager.addDictionarySuggestionListener(capture(tourneyListener));
		suggestionManager.removeDictionarySuggestionListener(capture(tourneyListener));
		replay(suggestionManager);

		publisherCenter.setDictionarySuggestionManager(suggestionManager);

		final WordSuggestion suggestion = createMock(WordSuggestion.class);
		expect(suggestion.getRequester()).andReturn(p1.getId()).times(2);
		expect(suggestion.getWord()).andReturn("MockSuggest").times(2);
		expect(suggestion.getSuggestionType()).andReturn(SuggestionType.CREATE);
		expect(suggestion.getCommentary()).andReturn(null);
		replay(suggestion);

		tourneyListener.getValue().changeRequestApproved(suggestion);
		tourneyListener.getValue().changeRequestRejected(suggestion);

		assertEquals(2, publishedNotifications.getValues().size());
		System.out.println(publishedNotifications);

		publisherCenter.setDictionarySuggestionManager(null);

		verify(suggestionManager, suggestion);
	}
}