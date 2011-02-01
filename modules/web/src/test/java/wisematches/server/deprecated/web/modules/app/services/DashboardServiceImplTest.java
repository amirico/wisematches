package wisematches.server.deprecated.web.modules.app.services;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class DashboardServiceImplTest {
/*
    private RoomsManager roomsManager;
    private ScribbleRoomManager manager;

    private static final Room ROOM = ScribbleRoomManager.ROOM;

    @Before
    public void init() {
        manager = createStrictMock(ScribbleRoomManager.class);

        roomsManager = createStrictMock(RoomsManager.class);
        expect(roomsManager.getRoomManager(ROOM)).andReturn(manager);
        replay(roomsManager);
    }

    @After
    public void thearDown() {
        RemoteServiceContextAccessor.destroy();
    }

    @Test
    public void test_getWaitingDashboardItems() {
        final ScribbleSettings settings = new ScribbleSettings("Title", new Date(), 3, "en", 3, 1000, 3000);

        final ScribbleBoard board = createMock(ScribbleBoard.class);
        expect(board.getBoardId()).andReturn(14L).andReturn(15L).andReturn(16L);
        expect(board.getGameSettings()).andReturn(settings).times(3);
        expect(board.getPlayersHands()).andReturn(Collections.<ScribblePlayerHand>emptyList()).times(3);
        replay(board);

        expect(manager.getWaitingBoards()).andReturn(Arrays.asList(board, board, board));
        replay(manager);

        final DashboardServiceImpl service = new DashboardServiceImpl();
        service.setRoomsManager(roomsManager);

        final DashboardItemBean[] waitingDashboardItems = service.getWaitingDashboardItems();
        assertEquals(3, waitingDashboardItems.length);
        assertEquals(14L, waitingDashboardItems[0].getBoardId());
        assertEquals(15L, waitingDashboardItems[1].getBoardId());
        assertEquals(16L, waitingDashboardItems[2].getBoardId());

        verify(manager, roomsManager, board);
    }

    @Test
    public void test_createBoard() throws BoardCreationException, SerializationException {
        final DashboardServiceImpl service = new DashboardServiceImpl();

        final Queue<ScribbleSettings> settingsQueue = new LinkedList<ScribbleSettings>();
        final Player player = createNiceMock(Player.class);
        final Player player18 = createNiceMock(Player.class);

        RemoteServiceContextAccessor.expectPlayer(player);

        final ScribbleBoard scribbleBoard = createNiceMock(ScribbleBoard.class);
        expect(scribbleBoard.getBoardId()).andReturn(123L);
        expect(scribbleBoard.addPlayer(player18)).andReturn(new ScribblePlayerHand(18L));
        replay(scribbleBoard);

        final PlayerManager playerManager = createStrictMock(PlayerManager.class);
        expect(playerManager.getPlayer(18L)).andReturn(player18);
        replay(playerManager);

        expect(manager.createBoard(same(player), isA(ScribbleSettings.class))).andAnswer(new IAnswer<ScribbleBoard>() {
            public ScribbleBoard answer() throws Throwable {
                settingsQueue.add((ScribbleSettings) getCurrentArguments()[1]);
                return scribbleBoard;
            }
        });
        replay(manager);

        service.setRoomsManager(roomsManager);
        service.setPlayerManager(playerManager);

        PlayerInfoBean[] opponents = new PlayerInfoBean[2];
        opponents[0] = new PlayerInfoBean();
        opponents[0].setPlayerId(18L);

        final DashboardItemBean itemBean = new DashboardItemBean("Title", opponents, "en", 3);
        itemBean.setMinRating(1000);
        itemBean.setMaxRating(3000);

        assertEquals(123L, service.createBoard(itemBean).longValue());

        final ScribbleSettings settings = settingsQueue.poll();
        assertEquals(itemBean.getTitle(), settings.getTitle());
        assertEquals(itemBean.getMaxRating(), settings.getMaxRating());
        assertEquals(itemBean.getMinRating(), settings.getMinRating());
        assertEquals(itemBean.getPlayers().length + 1, settings.getMaxPlayers());
        assertEquals(itemBean.getLocale(), settings.getLanguage());

        verify(manager, scribbleBoard, playerManager);
    }

    @Test
    public void test_joinToBoard() throws BoardLoadingException, SerializationException {
        final DashboardServiceImpl service = new DashboardServiceImpl();

        final Player player = createNiceMock(Player.class);

        replay(manager);
        service.setRoomsManager(roomsManager);
        verify(manager);

        reset(manager);
        RemoteServiceContextAccessor.expectPlayer(player);
        expect(manager.openBoard(13L)).andThrow(new BoardLoadingException("Test exception"));
        replay(manager);

        assertEquals(DashboardService.JoinResult.UNKNOWN_GAME, service.joinToBoard(13L));
        verify(manager);

        final ScribbleBoard board = createStrictMock(ScribbleBoard.class);
        expect(board.addPlayer(player)).andThrow(new TooManyPlayersException(3));
        expect(board.addPlayer(player)).andThrow(new IllegalArgumentException());
        expect(board.addPlayer(player)).andThrow(new IllegalStateException());
        expect(board.addPlayer(player)).andReturn(null);
        replay(board);

        final PlayerSessionBean playerSessionBean = RemoteServiceContextAccessor.expectPlayerSessionBean();
        expect(playerSessionBean.getPlayer()).andReturn(player).times(4);
        replay(playerSessionBean);

        reset(manager);
        expect(manager.openBoard(13L)).andReturn(board).times(4);
        replay(manager);

        assertEquals(DashboardService.JoinResult.TO_MANY_PLAYERS, service.joinToBoard(13L));
        assertEquals(DashboardService.JoinResult.ALREDY_IN_GAME, service.joinToBoard(13L));
        assertEquals(DashboardService.JoinResult.INTERNAL_ERROR, service.joinToBoard(13L));
        assertEquals(DashboardService.JoinResult.SUCCESS, service.joinToBoard(13L));

        verify(board, manager, playerSessionBean);
    }

    @Test
    public void test_assertGuestPlayer() {
        final RobotPlayer rp = createMock(RobotPlayer.class);
        expect(rp.getRobotType()).andReturn(RobotType.EXPERT);
        expect(rp.getRobotType()).andReturn(RobotType.DULL);
        replay(rp);

        final PlayerManager playerManager = createMock(PlayerManager.class);
        expect(playerManager.getPlayer(12L)).andReturn(createNiceMock(Player.class));
        expect(playerManager.getPlayer(12L)).andReturn(rp);
        expect(playerManager.getPlayer(12L)).andReturn(rp);
        replay(playerManager);

        final DashboardServiceImpl impl = new DashboardServiceImpl();
        impl.setPlayerManager(playerManager);

        try {
            impl.assertGuestPlayer(new PlayerInfoBean[]{null, null});
            fail("Exception must be here: more then one opponent");
        } catch (GuestRestrictionException ex) {
            ;
        }

        try {
            impl.assertGuestPlayer(new PlayerInfoBean[]{null});
            fail("Exception must be here: waiting opponent");
        } catch (GuestRestrictionException ex) {
            ;
        }

        try {
            impl.assertGuestPlayer(new PlayerInfoBean[]{new PlayerInfoBean(12L, "asd", MemberType.BASIC, 123)});
            fail("Exception must be here: opponent is not robot");
        } catch (GuestRestrictionException ex) {
            ;
        }

        try {
            impl.assertGuestPlayer(new PlayerInfoBean[]{new PlayerInfoBean(12L, "asd", MemberType.BASIC, 123)});
            fail("Exception must be here: robot is not dull");
        } catch (GuestRestrictionException ex) {
            ;
        }

        impl.assertGuestPlayer(new PlayerInfoBean[]{new PlayerInfoBean(12L, "asd", MemberType.BASIC, 123)});
    }

    private Player createMockPlayer() {
        final Player player = createMock(Player.class);
        expect(player.getId()).andReturn(13L).anyTimes();
        expect(player.getNickname()).andReturn("test").anyTimes();
        expect(player.getRating()).andReturn(123).anyTimes();
        replay(player);
        return player;
    }
*/
}
