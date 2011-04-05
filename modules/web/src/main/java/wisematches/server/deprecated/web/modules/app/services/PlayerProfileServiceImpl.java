package wisematches.server.deprecated.web.modules.app.services;

import wisematches.server.deprecated.web.rpc.GenericSecureRemoteService;

public class PlayerProfileServiceImpl extends GenericSecureRemoteService {//} implements PlayerProfileService {
/*    private RoomsManager roomsManager;
    private PlayerManager playerManager;
    private PlayerRatingsManager ratingsManager;
    private PlayerStatisticManager statisticsManager;
    private CountriesManager countriesManager;

    private final Set<PlayerNotification> changeableNotifications;

    private static final long GRAPH_STEP = 10;
    private static final long GRAPH_STEP_MILLIS = GRAPH_STEP * 24 * 60 * 60 * 1000;
    private static final long DAYS_IN_YEAR = 360;

    private static final Log log = LogFactory.getLog(PlayerProfileServiceImpl.class);

    public PlayerProfileServiceImpl() {
        changeableNotifications = new HashSet<PlayerNotification>();
        changeableNotifications.addAll(Arrays.asList(GameBoardNotification.values()));

        if (log.isDebugEnabled()) {
            log.debug("Init changeable notification: " + changeableNotifications);
        }
    }

    @Transactional(readOnly = true)
    public PlayerProfileBean getPlayerProfile(long playerId) {
        final Player player = playerManager.getPlayer(playerId);
        if (player == null) {
            return null;
        }

        if (log.isDebugEnabled()) {
            log.debug("Get player profile for " + playerId);
        }

        final long playerPosition = ratingsManager.getPlayerPosition(playerId);
        final HibernatePlayerStatistic statistic = statisticsManager.getPlayerStatistic(playerId);

        final PlayerProfileBean bean = new PlayerProfileBean();
        bean.setActiveGames(statistic.getActiveGames());
        bean.setAverageTurnTime(statistic.getAverageTurnTime());
        bean.setWonGames(statistic.getWonGames());
        bean.setDrawGames(statistic.getDrawGames());
        bean.setLostGames(statistic.getLostGames());
        if (statistic.getLastMoveTime() != 0) {
            bean.setLastMoveAgo((int) (System.currentTimeMillis() - statistic.getLastMoveTime()));
        }
        if (player.getLastSigninDate() != null && player.getLastSigninDate().getTime() != 0) {
            bean.setLastSeenOnlineAgo((int) (System.currentTimeMillis() - player.getLastSigninDate().getTime()));
        }
        bean.setPlace((int) playerPosition);
        bean.setTimeouts(statistic.getTimeouts());
        bean.setTurnsCount(statistic.getTurnsCount());

        bean.setRegistredFrom(player.getCreationDate().getTime());
        bean.setPlayerInfoBean(new PlayerInfoBean(player.getId(), player.getNickname(), getMemberType(player), player.getRating()));

        updateBeanInfo(player, bean.getPlayerSettingsBean());

        bean.setAllGamesInfo(cratePlayerRating(statistic.getAllGamesStatisticRating()));
        bean.setNinetyDaysRaing(cratePlayerRating(statistic.getNinetyDaysRatingInfo()));
        bean.setThirtyDaysRaing(cratePlayerRating(statistic.getThirtyDaysRatingInfo()));
        bean.setYearRaing(cratePlayerRating(statistic.getYearRatingInfo()));

        bean.setRatingHistoryBean(createRatingsHistory(playerId));

        if (log.isDebugEnabled()) {
            log.debug("Generated profile bean: " + bean);
        }

        return bean;
    }

    @Transactional
    public PlayerStatisticBean getPlayerStatisticBean(long playerId) {
        final Player player = playerManager.getPlayer(playerId);
        if (player == null) {
            return null;
        }

        if (log.isDebugEnabled()) {
            log.debug("Get player statistic info: " + playerId);
        }

        final HibernatePlayerStatistic statistic = statisticsManager.getPlayerStatistic(playerId);

        final PlayerStatisticBean bean = new PlayerStatisticBean();
        bean.setCurrentRating(player.getRating());
        bean.setDrawGames(statistic.getDrawGames());
        bean.setActiveGames(statistic.getActiveGames());
        bean.setFinishedGames(statistic.getFinishedGames());
        bean.setLostGames(statistic.getLostGames());
        bean.setWonGames(statistic.getWonGames());
        bean.setCurrentPosition(ratingsManager.getPlayerPosition(playerId));

        if (log.isDebugEnabled()) {
            log.debug("Generated player statistic: " + bean);
        }

        return bean;
    }

    @Transactional(readOnly = true)
    public PlayerSettingsBean getSettings() {
        final PlayerSettingsBean bean = new PlayerSettingsBean();

        // We must load player again to reattach it to current session
        final long playerId = getPlayer().getId();
        final Player player = playerManager.getPlayer(playerId);
        updateBeanInfo(player, bean);

        if (log.isDebugEnabled()) {
            log.debug("Get current player settings (" + playerId + "): " + bean);
        }

        return bean;
    }

    @Transactional
    public void updateSettings(PlayerSettingsBean bean) {
        // We must load player again to reattach it to current session
        final long playerId = getPlayer().getId();
        final Player player = playerManager.getPlayer(playerId);
        if (log.isDebugEnabled()) {
            log.debug("Update settings bean for current player (" + playerId + "): " + bean);
        }
        updatePlayerInfo(player, bean);
        playerManager.updatePlayer(player);
    }


    protected RatingHistoryBean createRatingsHistory(long playerId) {
        final RatingHistoryBean bean = new RatingHistoryBean();

        final Calendar calendar = Calendar.getInstance();
        calculateStartTime(calendar);
        bean.setStartMounth(calendar.get(Calendar.MONTH)); //start month

        final long startDate = calendar.getTimeInMillis();
        final int count = (int) (DAYS_IN_YEAR / GRAPH_STEP);

        final int[] rating = new int[count];
        bean.setRating(rating);

        final RoomManager roomManager = roomsManager.getRoomManager(ScribbleBoardManager.ROOM);
        final BoardsSearchEngine searchesEngine = roomManager.getSearchesEngine();
        final RatedBoardsInfo history = searchesEngine.getRatedBoards(playerId, new Date(startDate), null);
        final Iterator<RatedBoardsInfo.Record> recordIterator = history.iterator();
        if (recordIterator.hasNext()) {
            int currentRating = 0;
            int currentPosition = 0;
            int pointsInPosition = 0;
            do {
                final RatedBoardsInfo.Record record = recordIterator.next();
                final int position = (int) ((record.getTime() - startDate) / GRAPH_STEP_MILLIS);

                if (position != currentPosition && pointsInPosition != 0) {
                    rating[currentPosition] = currentRating;
                    pointsInPosition = 0;
                    currentRating = 0;
                    currentPosition = position;
                }
                currentRating = (currentRating * pointsInPosition + record.getRating()) / (pointsInPosition + 1);
                pointsInPosition++;
            } while (recordIterator.hasNext());

            if (pointsInPosition != 0) {
                rating[currentPosition] = currentRating;
            }
        }
        return bean;
    }

    protected PlayerRatingBean cratePlayerRating(HibernatePlayerStatisticRating ratingInfo) {
        final PlayerRatingBean bean = new PlayerRatingBean();
        bean.setAverageMovesPerGame(ratingInfo.getAverageMovesPerGame());
        bean.setAverageOpponentRating(ratingInfo.getAverageOpponentRating());
        bean.setAverageRating(ratingInfo.getAverageRating());
        bean.setHighestRating(ratingInfo.getHighestRating());
        bean.setLowestRating(ratingInfo.getLowestRating());

        final long p1Id = ratingInfo.getHighestWonOpponentId();
        if (p1Id != 0) {
            final Player p1 = playerManager.getPlayer(p1Id);
            if (p1 != null) {
                bean.setHighestWonOpponentBean(new PlayerInfoBean(p1Id, p1.getNickname(), getMemberType(p1), ratingInfo.getHighestWonOpponentRating()));
            }
        }

        final long p2Id = ratingInfo.getLowestLostOpponentId();
        if (p2Id != 0) {
            final Player p2 = playerManager.getPlayer(p2Id);
            if (p2 != null) {
                bean.setLowestLostOpponentBean(new PlayerInfoBean(p2Id, p2.getNickname(), getMemberType(p2), ratingInfo.getLowestLostOpponentRating()));
            }
        }
        return bean;
    }


    protected void updateBeanInfo(Player player, PlayerSettingsBean bean) {
        final PlayerProfile profile = player.getPlayerProfile();
        bean.setPlayerId(player.getId());
        bean.setNickname(player.getNickname());
        bean.setEmail(player.getEmail());

        final String countryCode = profile.getCountryCode();
        if (countryCode != null) {
            final Country country = countriesManager.getCountry(countryCode, getLanguage());
            if (country != null) {
                bean.setCountryCode(country.getCode());
                bean.setCountryName(country.getName());
            }
        }
        bean.setCity(profile.getCity());
        bean.setDateOfBirth(profile.getDateOfBirth());
        bean.setHomepage(profile.getHomepage());

        final Language locale = player.getLanguage();
        if (locale != null) {
            bean.setLanguage(locale.code());
        } else {
            bean.setLanguage(null);
        }

        final Gender gender = profile.getGender();
        if (gender != null) {
            bean.setPlayerGender(PlayerGender.valueOf(gender.name()));
        }
        bean.setRealName(profile.getRealName());
        bean.setTimezone(profile.getTimezone());
        bean.setAdditionalInfo(profile.getAdditionalInfo());

        final PlayerNotifications notifications = player.getPlayerNotifications();
        final Set<String> enabled = new HashSet<String>();
        final Set<String> disabled = new HashSet<String>();
        for (PlayerNotification n : changeableNotifications) {
            if (notifications.isNotificationEnabled(n)) {
                enabled.add(getNotificationCode(n));
            } else {
                disabled.add(getNotificationCode(n));
            }
        }
        bean.setDisabledNotifications(disabled);
        bean.setEnabledNotifications(enabled);
    }

    protected void updatePlayerInfo(Player player, PlayerSettingsBean bean) {
        final PlayerProfile profile = player.getPlayerProfile();

        player.setEmail(bean.getEmail());

        if (bean.getPassword() != null && bean.getPassword().trim().length() != 0) {
            player.setPassword(bean.getPassword());
        }

        final String language = bean.getLanguage();
        if (language != null) {
            player.setLanguage(Language.byCode(language));
        } else {
            player.setLanguage(null);
        }

        profile.setAdditionalInfo(bean.getAdditionalInfo());
        profile.setCity(bean.getCity());
        profile.setCountryCode(bean.getCountryCode());
        profile.setDateOfBirth(bean.getDateOfBirth());
        if (bean.getPlayerGender() != null) {
            profile.setGender(Gender.valueOf(bean.getPlayerGender().name()));
        } else {
            profile.setGender(null);
        }
        profile.setHomepage(bean.getHomepage());
        profile.setRealName(bean.getRealName());
        profile.setTimezone(bean.getTimezone());


        final PlayerNotifications notifications = player.getPlayerNotifications();
        final Set<String> enabled = bean.getEnabledNotifications();
        final Set<String> disabled = bean.getDisabledNotifications();

        for (PlayerNotification n : changeableNotifications) {
            final String code = getNotificationCode(n);
            if (enabled.contains(code)) {
                notifications.removeDisabledNotification(n);
            } else if (disabled.contains(code)) {
                notifications.addDisabledNotification(n);
            }
        }
    }


    *//**
	 * Calculates start date for history ratings. This date is 365 days ago from last day of current mounth.
	 * <p/>
	 * Specified calendar will contains start date after calculation.
	 *
	 * @param calendar the calendar to calculation and updation.
	 *//*
    protected void calculateStartTime(Calendar calendar) {
        //get current time
        calendar.setTimeInMillis(System.currentTimeMillis());

        //clear all unnecessary fields
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        // move to last day of month
        calendar.roll(Calendar.MONTH, true);

        //and move back to 360 days
        calendar.add(Calendar.DAY_OF_MONTH, (int) -DAYS_IN_YEAR);
    }

    *//**
	 * Returns set of all player notifications which can be changed by user.
	 *
	 * @return the set of player notifications.
	 *//*
    protected Set<PlayerNotification> getChangeableNotifications() {
        return changeableNotifications;
    }

    *//**
	 * Returns string code of specified notification. Notification code
	 * is formed by following rule: {@code Notification.type() + "-" + Notification.name()}
	 *
	 * @param notification the notification to get code
	 * @return the notification code
	 * @throws NullPointerException if specified {@code notification} is null
	 *//*
    protected static String getNotificationCode(PlayerNotification notification) {
        if (notification == null) {
            throw new NullPointerException("Notification can't be null");
        }
        return notification.type() + "-" + notification.name();
    }


    public void setPlayerStatisticManager(PlayerStatisticManager statisticsManager) {
        this.statisticsManager = statisticsManager;
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public void setRatingManager(PlayerRatingsManager ratingsManager) {
        this.ratingsManager = ratingsManager;
    }

    public void setCountriesManager(CountriesManager countriesManager) {
        this.countriesManager = countriesManager;
    }

    public void setRoomsManager(RoomsManager roomsManager) {
        this.roomsManager = roomsManager;
    }*/
}