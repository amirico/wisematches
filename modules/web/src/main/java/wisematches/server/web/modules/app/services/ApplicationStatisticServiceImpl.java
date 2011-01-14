package wisematches.server.web.modules.app.services;

import wisematches.server.web.rpc.GenericSecureRemoteService;

public class ApplicationStatisticServiceImpl extends GenericSecureRemoteService {//} implements ApplicationStatisticService {
/*    private PlayerRatingsManager ratingsManager;
    private AccountManager accountManager;
    private RoomManagerFacade roomManagerFacade;
    private PlayerSessionsManager playerSessionsManager;

    private final SiteStatisticBean siteStatisticBean = new SiteStatisticBean();
    private final Map<Player, PlayerInfoBean> topPlayers = new LinkedHashMap<Player, PlayerInfoBean>();

    private final ReentrantReadWriteLock statisticLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock topPlayersLock = new ReentrantReadWriteLock();

    private final TheStatisticListener statisticListener = new TheStatisticListener();
    private final TopPlayersListener ratingsChangeListener = new TheTopPlayersListener();

    private static final Log log = LogFactory.getLog("wisematches.server.web.statistic");

    public PlayerInfoBean[] getTopRatedPlayers() {
        topPlayersLock.readLock().lock();
        try {
            final Collection<PlayerInfoBean> collection = topPlayers.values();
            return collection.toArray(new PlayerInfoBean[collection.size()]);
        } finally {
            topPlayersLock.readLock().unlock();
        }
    }

    public SiteStatisticBean getSiteStatisticBean() {
        statisticLock.readLock().lock();
        try {
            return siteStatisticBean;
        } finally {
            statisticLock.readLock().unlock();
        }
    }


    private void initSiteStatistics() {
        if (playerSessionsManager != null && accountManager != null && roomManagerFacade != null) {
            loadCurrentStatistic();

            if (log.isDebugEnabled()) {
                log.debug("Site statistic loaded from DB: " + siteStatisticBean);
            }
        }
    }

    @Transactional(readOnly = true)
    protected void loadCurrentRatings() {
        topPlayersLock.writeLock().lock();
        try {
            topPlayers.clear();
            final List<Player> topRatedPlayers = ratingsManager.getTopRatedPlayers();
            for (Player player : topRatedPlayers) {
                topPlayers.put(player, new PlayerInfoBean(player.getId(), player.getNickname(), getMemberType(player), player.getRating()));
            }
        } finally {
            topPlayersLock.writeLock().unlock();
        }
    }

    @Transactional(readOnly = true)
    protected void loadCurrentStatistic() {
        statisticLock.writeLock().lock();
        try {
            siteStatisticBean.setOnlinePlayers(playerSessionsManager.getOnlinePlayers().size());
            siteStatisticBean.setRegistredPlayers(accountManager.getRegistredPlayersCount());

            final SearchesEngine searchesEngine = roomManagerFacade.getRoomManager().getSearchesEngine();
            siteStatisticBean.setCompletedGames(searchesEngine.getGamesCount(EnumSet.of(GameState.INTERRUPTED, GameState.DRAW, GameState.FINISHED)));
            siteStatisticBean.setGamesInProgress(searchesEngine.getGamesCount(EnumSet.of(GameState.IN_PROGRESS)));
        } finally {
            statisticLock.writeLock().unlock();
        }
    }


    public void setRatingsManager(PlayerRatingsManager ratingsManager) {
        if (this.ratingsManager != null) {
            ratingsManager.removeTopPlayersListener(ratingsChangeListener);
        }

        this.ratingsManager = ratingsManager;

        if (this.ratingsManager != null) {
            loadCurrentRatings();
            ratingsManager.addTopPlayersListener(ratingsChangeListener);
        }
    }

    public void setAccountManager(AccountManager accountManager) {
        if (this.accountManager != null) {
            this.accountManager.removeAccountListener(statisticListener);
        }

        this.accountManager = accountManager;

        if (this.accountManager != null) {
            this.accountManager.addAccountListener(statisticListener);
        }

        initSiteStatistics();
    }

    public void setPlayerSessionsManager(PlayerSessionsManager playerSessionsManager) {
        if (this.playerSessionsManager != null) {
            this.playerSessionsManager.removePlayerOnlineStateListener(statisticListener);
        }

        this.playerSessionsManager = playerSessionsManager;

        if (this.playerSessionsManager != null) {
            this.playerSessionsManager.addPlayerOnlineStateListener(statisticListener);
        }

        initSiteStatistics();
    }

    public void setRoomManagerFacade(RoomManagerFacade roomManagerFacade) {
        if (this.roomManagerFacade != null) {
            this.roomManagerFacade.removeGameStateListener(statisticListener);
        }

        this.roomManagerFacade = roomManagerFacade;

        if (this.roomManagerFacade != null) {
            this.roomManagerFacade.addGameStateListener(statisticListener);
        }

        initSiteStatistics();
    }

    private class TheTopPlayersListener implements TopPlayersListener {
        public void topPlayersChanged() {
            loadCurrentRatings();
        }
    }

    private class TheStatisticListener implements AccountListener, PlayerOnlineStateListener, GameStateListener {
        @Override
        public void accountCreated(Player player) {
            statisticLock.writeLock().lock();
            try {
                siteStatisticBean.setRegistredPlayers(siteStatisticBean.getRegistredPlayers() + 1);

                if (log.isDebugEnabled()) {
                    log.debug("Registread players increased: " + siteStatisticBean);
                }
            } finally {
                statisticLock.writeLock().unlock();
            }
        }

        @Override
        public void accountDeleted(Player player) {
            statisticLock.writeLock().lock();
            try {
                siteStatisticBean.setRegistredPlayers(siteStatisticBean.getRegistredPlayers() - 1);

                if (log.isDebugEnabled()) {
                    log.debug("Registread players decreased: " + siteStatisticBean);
                }
            } finally {
                statisticLock.writeLock().unlock();
            }
        }

        @Override
        public void playerIsOnline(Player player) {
            statisticLock.writeLock().lock();
            try {
                siteStatisticBean.setOnlinePlayers(siteStatisticBean.getOnlinePlayers() + 1);

                if (log.isDebugEnabled()) {
                    log.debug("Online players increased: " + siteStatisticBean);
                }
            } finally {
                statisticLock.writeLock().unlock();
            }
        }

        @Override
        public void playerIsOffline(Player player) {
            statisticLock.writeLock().lock();
            try {
                siteStatisticBean.setOnlinePlayers(siteStatisticBean.getOnlinePlayers() - 1);

                if (log.isDebugEnabled()) {
                    log.debug("Online players decreased: " + siteStatisticBean);
                }
            } finally {
                statisticLock.writeLock().unlock();
            }
        }

        @Override
        public void gameStarted(GameBoard board, GamePlayerHand playerTurn) {
            gameStarted();
        }

        @Override
        public void gameFinished(GameBoard board, GamePlayerHand wonPlayer) {
            gameCompleted();
        }

        @Override
        public void gameDraw(GameBoard board) {
            gameCompleted();
        }

        @Override
        public void gameInterrupted(GameBoard board, GamePlayerHand interrupterPlayer, boolean byTimeout) {
            gameCompleted();
        }

        private void gameStarted() {
            statisticLock.writeLock().lock();
            try {
                siteStatisticBean.setGamesInProgress(siteStatisticBean.getGamesInProgress() + 1);

                if (log.isDebugEnabled()) {
                    log.debug("Games in progress increased: " + siteStatisticBean);
                }
            } finally {
                statisticLock.writeLock().unlock();
            }
        }

        private void gameCompleted() {
            statisticLock.writeLock().lock();
            try {
                siteStatisticBean.setCompletedGames(siteStatisticBean.getCompletedGames() + 1);
                siteStatisticBean.setGamesInProgress(siteStatisticBean.getGamesInProgress() - 1);

                if (log.isDebugEnabled()) {
                    log.debug("Completed games increased and games in progress decreased: " + siteStatisticBean);
                }
            } finally {
                statisticLock.writeLock().unlock();
            }
        }
    }*/
}