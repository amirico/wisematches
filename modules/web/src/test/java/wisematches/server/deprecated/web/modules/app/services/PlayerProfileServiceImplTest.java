package wisematches.server.deprecated.web.modules.app.services;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerProfileServiceImplTest {
/*
    private final long ONE_DAY = 24 * 60 * 60 * 1000; //one day
    private final long ONE_STEP = 10 * ONE_DAY; //one step

    private static final Set<PlayerNotification> allNoficationsSet = new HashSet<PlayerNotification>();

    @BeforeClass
    public static void initNotificationsSet() {
        allNoficationsSet.addAll(Arrays.asList(GameBoardNotification.values()));
    }

    @After
    public void thearDown() {
        RemoteServiceContextAccessor.destroy();
    }

    @Test
    public void test_getSettings() throws Exception {
        final Player player = createPlayer(13L, null, null, null, 0, null, new HashSet<PlayerNotification>());

        final PlayerSessionBean bean = createMock(PlayerSessionBean.class);
        expect(bean.getPlayer()).andReturn(player);
        replay(bean);

        final PlayerManager playerManager = createMock(PlayerManager.class);
        expect(playerManager.getPlayer(13L)).andReturn(player); //Reload player to reattach to session
        replay(playerManager);

        final PlayerProfileServiceImpl service = new PlayerProfileServiceImpl();
        service.setPlayerManager(playerManager);

        RemoteServiceContextAccessor.setSessionBean(bean);

        checkPlayerSettingsBean(service.getSettings(), 13L, null, null, null, null, null, 0, new HashSet<PlayerNotification>());
        verify(bean, playerManager);
    }

    @Test
    public void test_updateSettings() throws Exception {
        final Date dateOfBird = new Date(System.currentTimeMillis() - 28L * 365 * 24 * 60 * 60 * 1000);

        final PlayerProfile profile = createMock(PlayerProfile.class);
        profile.setAdditionalInfo("additional info");
        profile.setCity("city");
        profile.setCountryCode("en");
        profile.setDateOfBirth(dateOfBird);
        profile.setGender(Gender.MALE);
        profile.setHomepage("homepage");
        profile.setRealName("real name");
        profile.setTimezone(9);
        replay(profile);

        final PlayerNotifications notifications = createMock(PlayerNotifications.class);
        notifications.removeDisabledNotification(GameBoardNotification.PLAYER_ADDED);
        notifications.removeDisabledNotification(GameBoardNotification.TIME_IS_RUNNING);
        notifications.addDisabledNotification(GameBoardNotification.GAME_FINISHED);
        notifications.addDisabledNotification(GameBoardNotification.PLAYER_MOVED);
        replay(notifications);

        final Player player = createMock(Player.class);
        expect(player.getId()).andReturn(13L);
        player.setEmail("test@test.en");
        expect(player.getPlayerProfile()).andReturn(profile);
        expect(player.getPlayerNotifications()).andReturn(notifications);
        player.setLanguage(Language.RUSSIAN);
        replay(player);

        final PlayerSessionBean bean = createMock(PlayerSessionBean.class);
        expect(bean.getPlayer()).andReturn(player);
        replay(bean);

        final PlayerManager playerManager = createMock(PlayerManager.class);
        expect(playerManager.getPlayer(13L)).andReturn(player); //Reload player to reattach to session
        playerManager.updatePlayer(player); // Update method should be invoked to save changes...
        replay(playerManager);

        RemoteServiceContextAccessor.setSessionBean(bean);

        final PlayerProfileServiceImpl service = new PlayerProfileServiceImpl();
        service.setPlayerManager(playerManager);

        final PlayerSettingsBean bean1 = new PlayerSettingsBean();
        bean1.setAdditionalInfo("additional info");
        bean1.setCity("city");
        bean1.setCountryCode("en");
        bean1.setCountryName("My Country");
        bean1.setDateOfBirth(dateOfBird);
        bean1.setEmail("test@test.en");
        bean1.setHomepage("homepage");
        bean1.setPlayerGender(PlayerGender.MALE);
        bean1.setPlayerId(13L);
        bean1.setRealName("real name");
        bean1.setTimezone(9);
        bean1.setLanguage("ru");
        bean1.setNickname("username");
        bean1.setEnabledNotifications(new HashSet<String>(Arrays.asList("BOARD-TIME_IS_RUNNING", "BOARD-PLAYER_ADDED")));
        bean1.setDisabledNotifications(new HashSet<String>(Arrays.asList("BOARD-GAME_FINISHED", "BOARD-PLAYER_MOVED")));

        service.updateSettings(bean1);

        verify(bean, player, profile, notifications, playerManager);
    }

    @Test
    public void test_getPlayerStatisticBean() throws UnknownPlayerException {
        final PlayerManager playerManager = createStrictMock(PlayerManager.class);
        final PlayerRatingsManager ratingsManager = createStrictMock(PlayerRatingsManager.class);
        final PlayerStatisticsManager statisticsManager = createStrictMock(PlayerStatisticsManager.class);

        final PlayerProfileServiceImpl service = new PlayerProfileServiceImpl();
        service.setPlayerManager(playerManager);
        service.setRatingManager(ratingsManager);
        service.setPlayerStatisticsManager(statisticsManager);

        expect(playerManager.getPlayer(1L)).andReturn(null);
        replay(playerManager);

        assertNull(service.getPlayerStatisticBean(1L));
        verify(playerManager);

        final Player player = createStrictMock(Player.class);
        expect(player.getRating()).andReturn(321);
        replay(player);

        reset(playerManager);
        expect(playerManager.getPlayer(1L)).andReturn(player);
        replay(playerManager);

        final PlayerStatistic statistic = new PlayerStatistic(1L);
        statistic.setActiveGames(1);
        statistic.setAverageTurnTime(2);
        statistic.setDrawGames(3);
        statistic.setLastCleanupTime(4);
        statistic.setLastMoveTime(5);
        statistic.setLostGames(6);
        statistic.setTimeouts(7);
        statistic.setTurnsCount(8);
        statistic.setUpdateTime(9);
        statistic.setWonGames(10);

        expect(statisticsManager.getPlayerStatistic(1L)).andReturn(statistic);
        replay(statisticsManager);

        expect(ratingsManager.getPlayerPosition(1L)).andReturn(123L);
        replay(ratingsManager);

        final PlayerStatisticBean bean = service.getPlayerStatisticBean(1L);
        assertEquals(1, bean.getActiveGames());
        assertEquals(123, bean.getCurrentPosition());
        assertEquals(321, bean.getCurrentRating());
        assertEquals(3, bean.getDrawGames());
        assertEquals(3 + 6 + 10, bean.getFinishedGames());
        assertEquals(6, bean.getLostGames());
        assertEquals(10, bean.getWonGames());

        verify(playerManager, statisticsManager, ratingsManager);
    }


    @Test
    public void test_getPlayerRatingBean() {
        final PlayerManager playerManager = createStrictMock(PlayerManager.class);

        final PlayerProfileServiceImpl service = new PlayerProfileServiceImpl();
        service.setPlayerManager(playerManager);

        final Player p1 = createMock(Player.class);
        expect(p1.getNickname()).andReturn("p1");

        final Player p2 = createMock(Player.class);
        expect(p2.getNickname()).andReturn("p2");
        replay(p1, p2);

        expect(playerManager.getPlayer(13L)).andReturn(p1);
        expect(playerManager.getPlayer(14L)).andReturn(p2);
        replay(playerManager);

        final PlayerRatingInfo info = new PlayerRatingInfo();
        info.setAverageMovesPerGame(1);
        info.setAverageOpponentRating(2);
        info.setAverageRating(3);
        info.setHighestRating(4);
        info.setHighestWonOpponentRating(5);
        info.setHighestWonOpponentId(13L);
        info.setLowestLostOpponentRating(6);
        info.setLowestLostOpponentId(14L);
        info.setLowestRating(7);

        final PlayerRatingBean bean = service.cratePlayerRating(info);
        assertEquals(1, bean.getAverageMovesPerGame());
        assertEquals(2, bean.getAverageOpponentRating());
        assertEquals(3, bean.getAverageRating());
        assertEquals(4, bean.getHighestRating());
        assertEquals(7, bean.getLowestRating());

        final PlayerInfoBean won = bean.getHighestWonOpponentBean();
        assertEquals(13L, won.getPlayerId());
        assertEquals("p1", won.getPlayerName());
        assertEquals(5, won.getCurrentRating());

        final PlayerInfoBean lost = bean.getLowestLostOpponentBean();
        assertEquals(6, lost.getCurrentRating());
        assertEquals(14L, lost.getPlayerId());
        assertEquals("p2", lost.getPlayerName());

        verify(playerManager);
    }

    @Test
    public void test_getPlayerRatingBean_NullOpponents() {
        final PlayerManager playerManager = createStrictMock(PlayerManager.class);

        final PlayerProfileServiceImpl service = new PlayerProfileServiceImpl();
        service.setPlayerManager(playerManager);

        expect(playerManager.getPlayer(1L)).andReturn(null);
        replay(playerManager);

        final PlayerRatingInfo info = new PlayerRatingInfo();
        info.setLowestLostOpponentRating(6);
        info.setLowestLostOpponentId(1L);

        final PlayerRatingBean bean = service.cratePlayerRating(info);

        assertNull(bean.getHighestWonOpponentBean());
        assertNull(bean.getLowestLostOpponentBean());

        verify(playerManager);
    }


    @Test
    public void test_updateupdateBeanInfo() {
        final PlayerSettingsBean result = new PlayerSettingsBean();

        final CountriesManager manager = createStrictMock(CountriesManager.class);
        expect(manager.getCountry("en", Language.ENGLISH)).andReturn(new Country("en", "Test Country", Language.ENGLISH));
        replay(manager);

        final PlayerProfileServiceImpl service = new PlayerProfileServiceImpl();
        service.setCountriesManager(manager);

        final Date dateOfBird = new Date(System.currentTimeMillis() - 28L * 365 * 24 * 60 * 60 * 1000);

        final Set<PlayerNotification> disabled = new HashSet<PlayerNotification>();
        disabled.add(GameBoardNotification.GAME_FINISHED);
        disabled.add(GameBoardNotification.PLAYER_MOVED);

        final HttpServletRequest request = createMock(HttpServletRequest.class);
        RemoteServiceContextAccessor.setRequest(request);
        expect(request.getAttribute(Language.class.getSimpleName())).andReturn(Language.ENGLISH);
        replay(request);


        final Player player = createPlayer(13L, Language.RUSSIAN, dateOfBird, Gender.FEMALE, 18, "en", disabled);
        service.updateBeanInfo(player, result);
        checkPlayerSettingsBean(result, 13L, Language.RUSSIAN, "en", "Test Country", dateOfBird, PlayerGender.FEMALE, 18, disabled); //12 + 6

        verify(player, manager, request);
    }

    @Test
    public void test_calculateStartTime() {
        final PlayerProfileServiceImpl service = new PlayerProfileServiceImpl();

        final Calendar calendar = Calendar.getInstance();
        service.calculateStartTime(calendar);

        final long l = calendar.getTimeInMillis();

        final long currentTime = System.currentTimeMillis();
        final long endTime = l + (360L * 24 * 60 * 60 * 1000); //360 days
        assertTrue("End date in future: " + (l - currentTime), endTime >= currentTime); // end in future

        calendar.setTimeInMillis(endTime);
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH)); //it's first day of next month
        assertEquals(0, endTime % (60 * 60 * 1000)); //no hours, minutes, seconds and milliseconds
    }

    @Test
    public void test_getRatingsHistory() {
        final PlayerProfileServiceImpl service = new PlayerProfileServiceImpl();

        final Calendar instance = Calendar.getInstance();
        service.calculateStartTime(instance);

        final long startTime = instance.getTimeInMillis();

        final RatedBoardsInfo history = new RatedBoardsInfo(
                new long[]{
                        1, 2, 3, 4,
                        5, 6,
                        7, 8, 9
                },
                new long[]{
                        startTime, startTime + ONE_DAY, startTime + 3 * ONE_DAY, startTime + 9 * ONE_DAY,
                        startTime + ONE_STEP * 4, startTime + ONE_STEP * 4 + 3 * ONE_DAY,
                        startTime + ONE_STEP * 35 + 4 * ONE_DAY, startTime + ONE_STEP * 35 + 2 * ONE_DAY, startTime + ONE_STEP * 35
                },
                new int[]{
                        1100, 1200, 1300, 1000,
                        1000, 1400,
                        1000, 1100, 1200
                });

        final BoardsSearchEngine searchesEngine = createStrictMock(BoardsSearchEngine.class);
        expect(searchesEngine.getRatedBoards(13L, instance.getTime(), null)).andReturn(history);
        replay(searchesEngine);

        final RoomManager roomManager = createStrictMock(RoomManager.class);
        expect(roomManager.getSearchesEngine()).andReturn(searchesEngine);
        replay(roomManager);

        final RoomsManager roomsManager = createStrictMock(RoomsManager.class);
        expect(roomsManager.getRoomManager(ScribbleBoardManager.ROOM)).andReturn(roomManager);
        replay(roomsManager);

        service.setRoomsManager(roomsManager);

        final RatingHistoryBean bean = service.createRatingsHistory(13L);
        assertEquals(1150, bean.getRating()[0]);
        assertEquals(1200, bean.getRating()[4]);
        assertEquals(1100, bean.getRating()[35]);
        for (int i = 0; i < bean.getRating().length; i++) {
            if (i == 0 || i == 4 || i == 35) {
                continue;
            }
            assertEquals(0, bean.getRating()[i]);
        }
        verify(roomsManager, roomManager, searchesEngine);
    }


    private Player createPlayer(final long playerId, Language language, Date dateOfBird, final Gender gender,
                                final int timeZone, final String country, Set<PlayerNotification> disabled) {
        final PlayerProfile playerProfile = createMock(PlayerProfile.class);
        expect(playerProfile.getCity()).andReturn("City" + playerId);
        expect(playerProfile.getCountryCode()).andReturn(country);
        expect(playerProfile.getRealName()).andReturn("real name" + playerId);
        expect(playerProfile.getAdditionalInfo()).andReturn("additional info" + playerId);
        expect(playerProfile.getDateOfBirth()).andReturn(dateOfBird); //about 28 years
        expect(playerProfile.getGender()).andReturn(gender);
        expect(playerProfile.getHomepage()).andReturn("homepage" + playerId);
        expect(playerProfile.getTimezone()).andReturn(timeZone); //Russia, Novosibirsk
        replay(playerProfile);

        final PlayerNotifications notifications = createMock(PlayerNotifications.class);
        for (GameBoardNotification notification : GameBoardNotification.values()) {
            if (disabled.contains(notification)) {
                expect(notifications.isNotificationEnabled(notification)).andReturn(false);
            } else {
                expect(notifications.isNotificationEnabled(notification)).andReturn(true);
            }
        }
        replay(notifications);

        final Player player = createMock(Player.class);
        expect(player.getId()).andReturn(playerId).anyTimes();
        expect(player.getEmail()).andReturn("test_" + playerId + "@test.en");
        expect(player.getNickname()).andReturn("test" + playerId);
        expect(player.getPlayerProfile()).andReturn(playerProfile);
        expect(player.getPlayerNotifications()).andReturn(notifications);
        expect(player.getLanguage()).andReturn(language);
        replay(player);
        return player;
    }

    private void checkPlayerSettingsBean(PlayerSettingsBean result, long playerId,
                                         Language language, String countyCode, String countryName,
                                         Date dateOfBird, final PlayerGender gender, final int timezone,
                                         Set<PlayerNotification> disabled) {
        assertEquals("additional info" + playerId, result.getAdditionalInfo());
        assertEquals("City" + playerId, result.getCity());
        assertEquals(countyCode, result.getCountryCode());
        assertEquals(countryName, result.getCountryName());
        assertEquals(dateOfBird, result.getDateOfBirth());
        assertEquals("test_" + playerId + "@test.en", result.getEmail());
        assertEquals("homepage" + playerId, result.getHomepage());
        assertEquals(gender, result.getPlayerGender());
        assertEquals(playerId, result.getPlayerId());
        assertEquals("real name" + playerId, result.getRealName());
        assertEquals(timezone, result.getTimezone());
        assertEquals(language, Language.byCode(result.getLanguage()));
        assertEquals("test" + playerId, result.getNickname());

        final Set<String> disabledN = new HashSet<String>();
        final Set<String> enabledN = new HashSet<String>();

        for (PlayerNotification nt : allNoficationsSet) {
            final String code = PlayerProfileServiceImpl.getNotificationCode(nt);
            if (disabled.contains(nt)) {
                disabledN.add(code);
            } else {
                enabledN.add(code);
            }
        }

        assertEquals(enabledN, result.getEnabledNotifications());
        assertEquals(disabledN, result.getDisabledNotifications());
    }
*/
}
