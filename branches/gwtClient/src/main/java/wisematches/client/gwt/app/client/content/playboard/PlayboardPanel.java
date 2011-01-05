package wisematches.client.gwt.app.client.content.playboard;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
class PlayboardPanel { //extends Panel {
//    private WordsMemoryPanel wordsMemory;
//
//    private ScribbleBoard scribbleBoard;
//
//    private final MessagesBar messagesPanel;
//    private final Panel searchPanel = new Panel();
//
//    private final long currentPlayer;
//    private final PlayboardItemBean playboardItemBean;
//
//    private final MinutesTickTimer minutesTickTimer = new MinutesTickTimer();
//
//    private final EventsDispatcher eventsDispatcher;
//    private final CollapsingListener collapsingListener;
//    private final MemoryWordsServiceAsync memoryWordsService = MemoryWordsService.App.getInstance();
//
//    public PlayboardPanel(long currentPlayer, PlayboardItemBean playboardItemBean,
//                          MessagesBar messagesPanel, EventsDispatcher eventsDispatcher, Settings settings) {
//        this.playboardItemBean = playboardItemBean;
//
//        this.messagesPanel = messagesPanel;
//
//        this.currentPlayer = currentPlayer;
//        this.eventsDispatcher = eventsDispatcher;
//
//        collapsingListener = new CollapsingListener(settings);
//
//        initPanel();
//
//        if (currentPlayer == playboardItemBean.getPlayerMove()) {
//            showMoveTransmitted(playboardItemBean.getPlayerInfoBean(currentPlayer));
//        }
//    }
//
//    private void initPanel() {
//        setBodyBorder(false);
//        setBodyStyle("background: transparent;");
//
//        final Panel centerPanel = createCenterPanel();
//        final Panel right = createRightInfoPanel();
//        final Panel left = createLeftInfoPanel();
//        centerPanel.setWidth(355);
//        centerPanel.setHeight(1);
//
//        final Panel p1 = new Panel();
//        p1.setBodyBorder(false);
//        p1.setLayout(new BorderLayout());
//        p1.add(right, new BorderLayoutData(RegionPosition.WEST));
//        p1.add(Box.createVerticalBox(0), new BorderLayoutData(RegionPosition.CENTER));
//        p1.setBodyStyle("background: transparent;");
//
//        final Panel p2 = new Panel();
//        p2.setBodyBorder(false);
//        p2.setLayout(new BorderLayout());
//        p2.add(centerPanel, new BorderLayoutData(RegionPosition.WEST, new Margins(0, 5, 5, 0)));
//        p2.add(p1, new BorderLayoutData(RegionPosition.CENTER));
//        p2.setBodyStyle("background: transparent;");
//
//        final Panel p3 = new Panel();
//        p3.setBodyBorder(false);
//        p3.setLayout(new BorderLayout());
//        p3.add(left, new BorderLayoutData(RegionPosition.WEST));
//        p3.add(p2, new BorderLayoutData(RegionPosition.CENTER));
//        p3.setBodyStyle("background: transparent;");
//
//        setLayout(new FitLayout());
//        add(p3);
//
//        minutesTickTimer.resetTicks();
//    }
//
//    private Panel createCenterPanel() {
//        final ScribbleBoardPanel scribbleBoardPanel =
//                new ScribbleBoardPanel(currentPlayer, playboardItemBean, new TheScribbleBoardCallback());
//        scribbleBoard = scribbleBoardPanel.getScribbleBoard();
//
//        final Panel inner = new Panel();
//        inner.setFrame(false);
//        inner.setBodyBorder(false);
//        inner.setLayout(new RowLayout());
//        inner.add(scribbleBoardPanel, new RowLayoutData(scribbleBoardPanel.getHeight()));
//        inner.add(createStubPanel(), new RowLayoutData(5));
//        inner.add(createSearchPanel(), new RowLayoutData(60));
//        inner.add(Box.createHorizontalBox(0));
//
//        return inner;
//    }
//
//    private Panel createLeftInfoPanel() {
//        final Panel p = new Panel();
//        p.setBodyBorder(false);
//        p.setLayout(new RowLayout());
//        p.setWidth(250);
//        p.setHeight(100);
//
//        GameInfoPanel gameInfo = new GameInfoPanel(currentPlayer, scribbleBoard, playboardItemBean);
//        collapsingListener.addPanelCollapser("game.info.collapsed", gameInfo);
//
//        TilesInfoPanel tilesInfoPanel = new TilesInfoPanel(playboardItemBean.getBankTiles());
//        collapsingListener.addPanelCollapser("tiles.info.collapsed", tilesInfoPanel);
//
//        HistoryInfoPanel historyInfo = new HistoryInfoPanel(playboardItemBean);
//        collapsingListener.addPanelCollapser("history.info.collapsed", historyInfo);
//
//        p.add(gameInfo, new RowLayoutData(190));
//        p.add(createStubPanel(), new RowLayoutData(5));
//        p.add(tilesInfoPanel);
//        p.add(createStubPanel(), new RowLayoutData(5));
//        p.add(historyInfo);
//
//        return p;
//    }
//
//    private Panel createRightInfoPanel() {
//        final Panel p = new Panel();
//        p.setBodyBorder(false);
//        p.setLayout(new RowLayout());
//        p.setWidth(250);
//        p.setHeight(100);
//
//        PlayersInfoPanel playersInfo = new PlayersInfoPanel(playboardItemBean);
//        collapsingListener.addPanelCollapser("players.info.collapsed", playersInfo);
//
//        MoveInfoPanel moveInfo = new MoveInfoPanel(scribbleBoard);
//        collapsingListener.addPanelCollapser("moves.info.collapsed", moveInfo);
//
//        wordsMemory = new WordsMemoryPanel(playboardItemBean, scribbleBoard, memoryWordsService);
//        collapsingListener.addPanelCollapser("words.info.collapsed", wordsMemory);
//
//        p.add(playersInfo, new RowLayoutData(50 + 50 * playboardItemBean.getPlayers().length));
//        p.add(createStubPanel(), new RowLayoutData(5));
//        p.add(moveInfo, new RowLayoutData(190));
//        p.add(createStubPanel(), new RowLayoutData(5));
//        p.add(wordsMemory);
//
//        return p;
//    }
//
//    private Widget createSearchPanel() {
//        searchPanel.setTitle(PB.ttlSearchUnknownWord());
//        searchPanel.setPaddings(5);
//        return searchPanel;
//    }
//
//    private Panel createStubPanel() {
//        final Panel panel = new Panel();
//        panel.setBodyBorder(false);
//        panel.setWidth(5);
//        panel.setHeight(5);
//        return panel;
//    }
//
//    protected void processPlayboardEvent(GameBoardEvent event) {
//        if (event.getBoardId() != playboardItemBean.getBoardId()) {
//            return;
//        }
//
//        if (event instanceof GameStartedEvent) {
//            final GameStartedEvent e = (GameStartedEvent) event;
//
//            if (currentPlayer == e.getPlayerHand()) {
//                playboardItemBean.setHandTiles(e.getPlayerTiles());
//            } else {
//                // Update tiles in hands for all players
//                final int[] tilesCount = playboardItemBean.getPlayersTilesCount();
//                for (int i = 0; i < tilesCount.length; i++) {
//                    tilesCount[i] = ScribbleBoard.HAND_SIZE;
//                }
//                playboardItemBean.setPlayersTilesCount(tilesCount);
//
//                playboardItemBean.setStartedTime(e.getStartTime());
//                playboardItemBean.setPlayerMove(e.getPlayerTrun());
//                playboardItemBean.setLastMoveTime(e.getStartTime());
//
//                minutesTickTimer.resetTicks();
//
//                showStartedMessage();
//            }
//        } else if (event instanceof GameTurnEvent) {
//            final GameTurnEvent e = (GameTurnEvent) event;
//
//            final PlayerMoveBean playerMove = e.getPlayerMoveBean();
//            final long movedPlayerId = playerMove.getPlayerId();
//
//            // Update tiles in hands for move player
//            final int[] tilesCount = playboardItemBean.getPlayersTilesCount();
//            tilesCount[playboardItemBean.getPlayerInfoIndex(movedPlayerId)] = e.getHandTilesCount();
//            playboardItemBean.setPlayersTilesCount(tilesCount);
//
//            final PlayerInfoBean bean = playboardItemBean.getPlayerInfoBean(movedPlayerId);
//            final int currentRating = bean.getCurrentRating();
//            bean.setCurrentRating(currentRating + playerMove.getPoints());
//            playboardItemBean.firePropertyChanged("playerScorePoints", currentRating, bean.getCurrentRating());
//
//            playboardItemBean.setPlayerMove(e.getNextPlayer());
//            playboardItemBean.setLastMoveTime(playerMove.getMoveTime());
//            minutesTickTimer.resetTicks();
//
//            // Fire playerMove changed again because after move this event was fired.
//            playboardItemBean.addPlayerMove(playerMove);
//            if (playerMove.getMoveType() == PlayerMoveBean.Type.MOVED && movedPlayerId != currentPlayer) {
//                selectHistoryWord(playboardItemBean.getPlayersMoves().size() - 1);
//            }
//            showMoveTransmitted(playboardItemBean.getPlayerInfoBean(e.getNextPlayer()));
//        } else if (event instanceof GamePlayersEvent) {
//            final GamePlayersEvent e = (GamePlayersEvent) event;
//
//            final PlayerInfoBean player = e.getPlayerInfoBean();
//            final GamePlayersEvent.Action action = e.getAction();
//            if (action == GamePlayersEvent.Action.ADDED) {
//                playboardItemBean.addOpponent(player);
//            } else {
//                playboardItemBean.removeOpponent(player);
//            }
//            showPlayersChanged(player, action);
//        } else if (event instanceof GameFinishedEvent) {
//            final GameFinishedEvent e = (GameFinishedEvent) event;
//            minutesTickTimer.cancel();
//
//            final GameFinishedEvent.FinalPoint[] finalPoints = e.getFinalPoints();
//            for (GameFinishedEvent.FinalPoint point : finalPoints) {
//                playboardItemBean.getPlayerInfoBean(point.getPlayerId()).setCurrentRating(point.getPoints());
//            }
//            playboardItemBean.firePropertyChanged("playerScorePoints", null, null);
//            playboardItemBean.setFinishedTime(e.getFinishedTime());
//            showFinishMessage(e);
//        }
//    }
//
//    private void showStartedMessage() {
//        final String gameBoardLink = getOpenGameBoardLink(playboardItemBean.getBoardId(), playboardItemBean.getTitle());
//
//        final long playerMove = playboardItemBean.getPlayerMove();
//        if (currentPlayer != playerMove) {
//            final PlayerInfoBean bean = playboardItemBean.getPlayerInfoBean(playerMove);
//            messagesPanel.addEvent(MPB.msgGameStarted(bean.getPlayerName(), gameBoardLink));
//            MessagesBox.showMessage(APP.ttlGameStarted(), APP.lblGameStarted());
//        } else {
//            messagesPanel.addEvent(MPB.msgItsYouTurn(gameBoardLink));
//            MessagesBox.showMessage(PB.ttlYouTurnAgain(), PB.lblYouTurnAgain());
//        }
//    }
//
//    private void showFinishMessage(GameFinishedEvent e) {
//        final String gameBoardLink = getOpenGameBoardLink(playboardItemBean.getBoardId(), playboardItemBean.getTitle());
//
//        final GameFinishedEvent.Type type = e.getType();
//        if (type == GameFinishedEvent.Type.FINISHED) {
//            if (e.getPlayerId() == 0) {
//                messagesPanel.addEvent(MPB.msgGameDrawn(gameBoardLink));
//                MessagesBox.showMessage(APP.ttlGameFinished(), MAPP.lblGameDraw());
//            } else {
//                if (e.getPlayerId() == currentPlayer) {
//                    messagesPanel.addEvent(MPB.msgYouWonGame(gameBoardLink));
//                    MessagesBox.showMessage(APP.ttlGameFinished(), MAPP.lblYouWon());
//                } else {
//                    messagesPanel.addEvent(MPB.msgYouLoseGame(gameBoardLink));
//                    MessagesBox.showMessage(APP.ttlGameFinished(), MAPP.lblYouLose());
//                }
//            }
//        } else if (type == GameFinishedEvent.Type.TIMEDOUT) {
//            final PlayerInfoBean bean = playboardItemBean.getPlayerInfoBean(e.getPlayerId());
//            messagesPanel.addEvent(MPB.msgGameTimedout(bean.getPlayerName(), gameBoardLink));
//            MessagesBox.showMessage(APP.ttlGameFinished(), MAPP.lblGameInterrupted());
//        } else if (type == GameFinishedEvent.Type.RESIGNED) {
//            final PlayerInfoBean bean = playboardItemBean.getPlayerInfoBean(e.getPlayerId());
//            messagesPanel.addEvent(MPB.msgGameResigned(bean.getPlayerName(), gameBoardLink));
//            MessagesBox.showMessage(APP.ttlGameFinished(), MAPP.lblGameResigned());
//        }
//    }
//
//    private void showMoveTransmitted(PlayerInfoBean nextPlayer) {
//        final String gameBoardLink = getOpenGameBoardLink(playboardItemBean.getBoardId(), playboardItemBean.getTitle());
//
//        if (nextPlayer != null) {
//            if (nextPlayer.getPlayerId() == currentPlayer) {
//                messagesPanel.addEvent(MPB.msgItsYouTurn(gameBoardLink));
//                MessagesBox.showMessage(PB.ttlYouTurnAgain(), PB.lblYouTurnAgain());
//            } else {
//                messagesPanel.addEvent(MPB.msgTurnTransmitted(nextPlayer.getPlayerName(), gameBoardLink));
//            }
//        }
//    }
//
//    private void showPlayersChanged(PlayerInfoBean player, GamePlayersEvent.Action action) {
//        final String gameBoardLink = getOpenGameBoardLink(playboardItemBean.getBoardId(), playboardItemBean.getTitle());
//
//        if (action == GamePlayersEvent.Action.ADDED) {
//            messagesPanel.addEvent(MPB.msgPlayerJoined(player.getPlayerName(), gameBoardLink));
//        } else {
//            messagesPanel.addEvent(MPB.msgPlayerLeft(player.getPlayerName(), gameBoardLink));
//        }
//    }
//
//
//    public void removeMemoryWord(int wordId) {
//        wordsMemory.removeWord(wordId);
//    }
//
//    public void selectMemoryWord(int wordId) {
//        final Word word = wordsMemory.getWord(wordId);
//        try {
//            scribbleBoard.setSelectedWord(word, true);
//        } catch (IncorrectWordException e) {
//            MessagesBox.showMessage(PB.ttlIncorrectHistoryWord(), PB.lblIncorrectHistoryWord());
//        }
//    }
//
//    public void selectHistoryWord(int wordId) {
//        final PlayerMoveBean bean = playboardItemBean.getPlayersMoves().get(wordId);
//        try {
//            scribbleBoard.setSelectedWord(bean.getWord(), true);
//        } catch (IncorrectWordException e) {
//            MessagesBox.showMessage(PB.ttlIncorrectHistoryWord(), PB.lblIncorrectHistoryWord());
//        }
//    }
//
//    public void activateBoard() {
//        final Widget panel = SponsorsBlockType.SEARCH.getWidget();
//        if (panel != null) {
//            panel.removeFromParent();
//
//            searchPanel.add(panel);
//            searchPanel.doLayout();
//        }
//    }
//
//    public void closeBoard() {
//        minutesTickTimer.cancel();
//    }
//
//    public PlayboardItemBean getPlayboardItemBean() {
//        return playboardItemBean;
//    }
//
//    private class MinutesTickTimer extends Timer {
//        @Override
//        public void run() {
//            // Just fire update event.
//            playboardItemBean.firePropertyChanged("lastMoveTime", 0, playboardItemBean.getLastMoveTime());
//        }
//
//        public void resetTicks() {
//            cancel();
//            scheduleRepeating(60000); //60 seconds = 1 minute
//        }
//    }
//
//    private class TheScribbleBoardCallback implements ScribbleBoardCallback {
//        public void playerMoved(long playerId, Word word, TurnResult t) {
//            playboardItemBean.setPlayerMove(t.getNextPlayerTurn());
//            playboardItemBean.setHandTiles(t.getHandTiles());
//
//            eventsDispatcher.requestEvents();
//        }
//
//        public void playerPassed(long playerId, TurnResult t) {
//            playboardItemBean.setPlayerMove(t.getNextPlayerTurn());
//            playboardItemBean.setHandTiles(t.getHandTiles());
//
//            eventsDispatcher.requestEvents();
//        }
//
//        public void playerExchanged(long playerId, TurnResult t) {
//            playboardItemBean.setPlayerMove(t.getNextPlayerTurn());
//            playboardItemBean.setHandTiles(t.getHandTiles());
//
//            eventsDispatcher.requestEvents();
//        }
//
//        public void playerResigned(long playerId) {
//            playboardItemBean.setPlayerMove(0);
//            playboardItemBean.setLastMoveTime(0);
//            playboardItemBean.setFinishedTime(System.currentTimeMillis());
//
//            eventsDispatcher.requestEvents();
//        }
//
//        public void boardLocked(String message) {
//            PlayboardPanel.this.getEl().mask(message, true);
//        }
//
//        public void boardUnlocked() {
//            PlayboardPanel.this.getEl().unmask();
//        }
//    }
//
//    private static class CollapsingListener implements SettingsListener {
//        private final Settings settings;
//        private final Map<String, Panel> collapsersMap = new HashMap<String, Panel>();
//
//        private CollapsingListener(Settings settings) {
//            this.settings = settings;
//
//            settings.addSettingsListener(this);
//        }
//
//        public void addPanelCollapser(final String propertyName, Panel panel) {
//            collapsersMap.put(propertyName, panel);
//
//            panel.setCollapsed(settings.getBoolean(propertyName));
//            panel.addListener(new PanelListenerAdapter() {
//                @Override
//                public void onCollapse(Panel panel) {
//                    settings.setBoolean(propertyName, true);
//                }
//
//                @Override
//                public void onExpand(Panel panel) {
//                    settings.setBoolean(propertyName, false);
//                }
//            });
//        }
//
//        public void settingsChanged(Settings settings, ParameterInfo parameter, Object oldValue, Object newValue) {
//            final Panel panel = collapsersMap.get(parameter.getName());
//            if (panel != null) {
//                panel.setCollapsed((Boolean) newValue);
//            }
//        }
//    }
}
