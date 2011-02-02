package wisematches.server.deprecated.web.modules.app.notification;

import org.junit.Test;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class EMailNotificationsSenderTest extends AbstractTransactionalDataSourceSpringContextTests {
	@Test
	public void test() {
		throw new UnsupportedOperationException("Commented");
	}
/*
	private Capture<RoomBoardsListener> roomBoardsListener = new Capture<RoomBoardsListener>();
	private EMailNotificationsSender notificationsSender;

	private Capture<GamePlayersListener> gamePlayersListener = new Capture<GamePlayersListener>();
	private Capture<GameStateListener> gameStateListener = new Capture<GameStateListener>();
	private Capture<GameMoveListener> gameMoveListener = new Capture<GameMoveListener>();

	private Capture<GameTimeoutListener> gameTimeoutListener = new Capture<GameTimeoutListener>();

	private static final Room ROOM = Room.valueOf("MOCK");


	public void test_isPlayerAllowsNotification() {
		final PlayerNotification notification = createNiceMock(PlayerNotification.class);

		final PlayerNotifications notifications = createStrictMock(PlayerNotifications.class);
		expect(notifications.isNotificationEnabled(notification)).andReturn(true);
		expect(notifications.isNotificationEnabled(notification)).andReturn(false);
		replay(notifications);

		final Player player = createStrictMock(Player.class);
		expect(player.getPlayerNotifications()).andReturn(notifications).times(2);
		replay(player);

		assertTrue(notificationsSender.isPlayerAllowsNotification(player, notification));
		assertFalse(notificationsSender.isPlayerAllowsNotification(player, notification));

		verify(notifications);
		verify(player);
	}


	public void test() {
		//TODO: there is no tests!!!!
	}

	public void real_testGameStarted() throws BoardLoadingException {
		final GameBoard board = createMock(GameBoard.class);
		board.addGameStateListener(capture(gameStateListener));
		board.addGameMoveListener(capture(gameMoveListener));
		board.addGamePlayersListener(capture(gamePlayersListener));
		replay(board);

		final RoomManager roomManager = createMock(RoomManager.class);
		roomManager.addRoomBoardsListener(capture(roomBoardsListener));
		expect(roomManager.openBoard(13L)).andReturn(board);
		replay(roomManager);

		final RoomsManager roomsManager = createMock(RoomsManager.class);
		expect(roomsManager.getRoomManagers()).andReturn(Arrays.asList(roomManager));
		expect(roomsManager.getRoomManager(ROOM)).andReturn(roomManager);
		replay(roomsManager);

		final PlayerManager playerManager = createMock(PlayerManager.class);
		expect(playerManager.getPlayer(anyLong())).andAnswer(new IAnswer<Player>() {
			public Player answer() throws Throwable {
				return createMockPlayer((Long) getCurrentArguments()[0]);
			}
		}).anyTimes();
		replay(playerManager);

		final GameTimeoutTerminator timeoutProcessor = createStrictMock(GameTimeoutTerminator.class);
		timeoutProcessor.addGameTimeoutListener(capture(gameTimeoutListener));
		replay(timeoutProcessor);

		final MailSender mailSender = createMock(MailSender.class);
//        mailSender.sendSystemMail(FromTeam.UNDEFINED, );
		replay(mailSender);

		notificationsSender.setRoomsManager(roomsManager);
		notificationsSender.setPlayerManager(playerManager);
		notificationsSender.setGameTimeoutProcessor(timeoutProcessor);
		notificationsSender.setMailSender(mailSender);

		roomBoardsListener.getValue().boardOpened(ROOM, 13L);
		verify(board, roomManager, roomsManager);

		final GamePlayerHand h1 = new GamePlayerHand(13L, 100, 1000, -10);
		final GamePlayerHand h2 = new GamePlayerHand(14L, 200, 1200, 13);
		final GamePlayerHand h3 = new GamePlayerHand(15L, 300, 1800, -9);

		reset(board);
		expect(board.getBoardId()).andReturn(145L).anyTimes();
		expect(board.getPlayersHands()).andReturn(Arrays.asList(h1, h2, h3)).anyTimes();
		expect(board.getPlayerHand(h1.getPlayerId())).andReturn(h1).anyTimes();
		expect(board.getPlayerHand(h2.getPlayerId())).andReturn(h2).anyTimes();
		expect(board.getPlayerHand(h3.getPlayerId())).andReturn(h3).anyTimes();
		expect(board.getWonPlayer()).andReturn(h3).anyTimes();
		expect(board.getPlayerTrun()).andReturn(h2).anyTimes();
		expect(board.getGameSettings()).andReturn(new ScribbleSettings("This is title", new Date(), 3, "en", 3)).anyTimes();
		replay(board);

		gameTimeoutListener.getValue().timeIsRunningOut(new GameTimeoutEvent(ROOM, 145L, RemainderType.ONE_HOUR));

		final MakeWordMove move = null;
//        final ScribbleMove move = new ScribbleMove(14L, 4000,
//                new Word(new Position(7, 7), Direction.HORIZONTAL,
//                        new Tile(1, 'a', 1), new Tile(2, 'b', 1), new Tile(3, 'c', 2)));
//        gameMoveListener.playerMoved(new GameMoveEvent(board, h2, h3, move, 0));
//        gamePlayersListener.playerRemoved(board, createMockPlayer(13L));
	}

	protected String[] getConfigLocations() {
		return new String[]{"classpath:/config/test-web-app-config.xml"};
	}

	public void setNotificationsSender(EMailNotificationsSender notificationsSender) {
		this.notificationsSender = notificationsSender;
	}

	private Player createMockPlayer(long id) {
		Player p = createMock(Player.class);
		expect(p.getId()).andReturn(id).anyTimes();
		expect(p.getNickname()).andReturn("MockPlayer" + id).anyTimes();
		expect(p.getEmail()).andReturn("test@localhost");
		expect(p.getLanguage()).andReturn(Language.RUSSIAN);
		replay(p);
		return p;
	}
*/
}
