package wisematches.server.deprecated.web.modules.app.services;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ApplicationStatisticServiceImplTest {
/*
    @Test
    public void test_getTopRatedPlayers() {
        Player p1 = createMockPlayer(1, 400);
        Player p2 = createMockPlayer(2, 300);
        Player p3 = createMockPlayer(3, 200);

        final PlayerRatingsManager ratingsManager = createStrictMock(PlayerRatingsManager.class);
        expect(ratingsManager.getTopRatedPlayers()).andReturn(Arrays.asList(p1, p2, p3));
        ratingsManager.addTopPlayersListener(isA(TopPlayersListener.class));
        replay(ratingsManager);

        final ApplicationStatisticServiceImpl impl = new ApplicationStatisticServiceImpl();
        impl.setRatingsManager(ratingsManager);

        final PlayerInfoBean[] playerInfoBeans = impl.getTopRatedPlayers();
        assertEquals(3, playerInfoBeans.length);
        assertEquals(1, playerInfoBeans[0].getPlayerId());
        assertEquals(2, playerInfoBeans[1].getPlayerId());
        assertEquals(3, playerInfoBeans[2].getPlayerId());

        final PlayerInfoBean[] playerInfoBeans2 = impl.getTopRatedPlayers();
        assertEquals(3, playerInfoBeans2.length);
        assertEquals(1, playerInfoBeans2[0].getPlayerId());
        assertEquals(2, playerInfoBeans2[1].getPlayerId());
        assertEquals(3, playerInfoBeans2[2].getPlayerId());

        verify(ratingsManager);
    }

    @Test
    public void test_playerRatingChanged() {
        final Player p1 = createMockPlayer(1, 400);
        final Player p2 = createMockPlayer(2, 300);
        final Player p3 = createMockPlayer(3, 200);

        final Player p4 = createMockPlayer(4, 400);
        final Player p5 = createMockPlayer(5, 300);
        final Player p6 = createMockPlayer(6, 200);

        final Capture<TopPlayersListener> topPlayersListener = new Capture<TopPlayersListener>();
        final PlayerRatingsManager ratingsManager = createStrictMock(PlayerRatingsManager.class);
        expect(ratingsManager.getTopRatedPlayers()).andReturn(Arrays.asList(p3, p2, p1));
        ratingsManager.addTopPlayersListener(capture(topPlayersListener));
        expect(ratingsManager.getTopRatedPlayers()).andReturn(Arrays.asList(p4, p5, p6));
        replay(ratingsManager);

        final ApplicationStatisticServiceImpl impl = new ApplicationStatisticServiceImpl();
        impl.setRatingsManager(ratingsManager);

        topPlayersListener.getValue().topPlayersChanged();

        final PlayerInfoBean[] topRatedPlayers = impl.getTopRatedPlayers();
        assertEquals(3, topRatedPlayers.length);
        assertEquals(4, topRatedPlayers[0].getPlayerId());
        assertEquals(5, topRatedPlayers[1].getPlayerId());
        assertEquals(6, topRatedPlayers[2].getPlayerId());

        verify(ratingsManager);
    }

    @Test
    public void test_getSiteStatistic() {
        final Capture<AccountListener> accountListener = new Capture<AccountListener>();
        final AccountManager accountManager = createStrictMock(AccountManager.class);
        accountManager.addAccountListener(capture(accountListener));
        expect(accountManager.getRegistredPlayersCount()).andReturn(12);
        replay(accountManager);

        final Capture<PlayerOnlineStateListener> playerStateListener = new Capture<PlayerOnlineStateListener>();
        final PlayerSessionsManager playerSessionsManager = createStrictMock(PlayerSessionsManager.class);
        playerSessionsManager.addPlayerOnlineStateListener(capture(playerStateListener));
        expect(playerSessionsManager.getOnlinePlayers()).andReturn(Arrays.<Player>asList(null, null, null));
        replay(playerSessionsManager);

        final BoardsSearchEngine searchesEngine = createStrictMock(BoardsSearchEngine.class);
        expect(searchesEngine.getGamesCount(EnumSet.of(GameState.DRAW, GameState.FINISHED, GameState.INTERRUPTED))).andReturn(4);
        expect(searchesEngine.getGamesCount(EnumSet.of(GameState.ACTIVE))).andReturn(8);
        replay(searchesEngine);

        final RoomManager roomManager = createStrictMock(RoomManager.class);
        expect(roomManager.getSearchesEngine()).andReturn(searchesEngine);
        replay(roomManager);

        final Capture<GameBoardListener> gameStateListener = new Capture<GameBoardListener>();
        final RoomManagerFacade roomManagerFacade = createStrictMock(RoomManagerFacade.class);
        roomManagerFacade.addGameBoardListener(capture(gameStateListener));
        expect(roomManagerFacade.getRoomManager()).andReturn(roomManager);
        replay(roomManagerFacade);

        final ApplicationStatisticServiceImpl service = new ApplicationStatisticServiceImpl();
        service.setAccountManager(accountManager);
        service.setPlayerSessionsManager(playerSessionsManager);
        service.setRoomManagerFacade(roomManagerFacade);


        final SiteStatisticBean bean = service.getSiteStatisticBean();
        assertEquals(4, bean.getCompletedGames());
        assertEquals(8, bean.getGamesInProgress());
        assertEquals(3, bean.getOnlinePlayers());
        assertEquals(12, bean.getRegistredPlayers());

        accountListener.getValue().accountCreated(null);
        assertEquals(13, service.getSiteStatisticBean().getRegistredPlayers());

        accountListener.getValue().accountDeleted(null);
        assertEquals(12, service.getSiteStatisticBean().getRegistredPlayers());

        playerStateListener.getValue().playerIsOnline(null);
        assertEquals(4, service.getSiteStatisticBean().getOnlinePlayers());

        playerStateListener.getValue().playerIsOffline(null);
        assertEquals(3, service.getSiteStatisticBean().getOnlinePlayers());

        gameStateListener.getValue().gameDraw(null);
        assertEquals(5, bean.getCompletedGames());
        assertEquals(7, bean.getGamesInProgress());

        gameStateListener.getValue().gameFinished(null, null);
        assertEquals(6, bean.getCompletedGames());
        assertEquals(6, bean.getGamesInProgress());

        gameStateListener.getValue().gameInterrupted(null, null, false);
        assertEquals(7, bean.getCompletedGames());
        assertEquals(5, bean.getGamesInProgress());

        gameStateListener.getValue().gameStarted(null, null);
        assertEquals(7, bean.getCompletedGames());
        assertEquals(6, bean.getGamesInProgress());

        verify(accountManager, playerSessionsManager, roomManagerFacade, roomManager, searchesEngine);
    }

    private Player createMockPlayer(long id, int rating) {
        Player p = createMock(Player.class);
        expect(p.getId()).andReturn(id);
        expect(p.getNickname()).andReturn("Player" + id);
        expect(p.getRating()).andReturn(rating);
        replay(p);
        return p;
    }
*/
}
