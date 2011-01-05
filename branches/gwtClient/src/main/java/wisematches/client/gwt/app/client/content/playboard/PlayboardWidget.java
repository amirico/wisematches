package wisematches.client.gwt.app.client.content.playboard;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayboardWidget { //extends Panel implements ApplicationFrameView {
//    private PlayerSessionTool playerSessionTool;
//
//    private TabPanel itemsPanel;
//    private CenterMessagePanel emptyMessagePanel;
//
//    private Window gameboardWindow = null;
//    private MessagesBar messagesPanel;
//    private Settings settings;
//
//    private GameboardTool gameboardTool;
//    private EventsDispatcher eventsDispatcher;
//
//    private final RefreshButton refreshButton = new RefreshButton();
//    private final AlarmFacilityWidget alarmFacilityWidget = AlarmFacilityWidget.getInstance();
//
//    private final TheEventsListener eventsListener = new TheEventsListener();
//    private static final Correlator<GameBoardEvent> CORRELATOR = new TheEventsCorrelator();
//
//    public static final String ITEM_ID = "playboard";
//    public static final String BOARD_ID_PARAMETER = "boardid";
//
//    public void initialize(ApplicationFrame applicationFrame, Settings frameViewSettings) {
//        this.settings = frameViewSettings;
//
//        gameboardTool = applicationFrame.getApplicationTool(GameboardTool.class);
//        playerSessionTool = applicationFrame.getApplicationTool(PlayerSessionTool.class);
//
//        final EventsDispatcherTool tool = applicationFrame.getApplicationTool(EventsDispatcherTool.class);
//        eventsDispatcher = tool.getEventsDispatcher();
//
//        initPanel();
//        eventsDispatcher.addEventsListener(CORRELATOR, eventsListener);
//    }
//
//    private void initPanel() {
//        emptyMessagePanel = new CenterMessagePanel();
//        emptyMessagePanel.setTitle(APP.lblNoOpenedBoard());
//        emptyMessagePanel.setMessage(MAPP.lblEmptyPlayboard(
//                "javascript:playboardCreateNewGame();",
//                "javascript:showApplicationItem('" + DashboardWidget.ITEM_ID + "', null)",
//                "javascript:quickOpenBoards();",
//                "javascript:showApplicationItem('" + DashboardWidget.ITEM_ID + "', null)",
//                "javascript:showApplicationItem('" + GameboardWidget.ITEM_ID + "', null)"
//        ));
//        emptyMessagePanel.setClosable(false);
//
//        itemsPanel = createTabPanel();
//
//        setFrame(false);
//        setBodyBorder(false);
//        setBorder(false);
//        setBodyStyle("background: transparent;");
//        setLayout(new FitLayout());
//
//        add(itemsPanel);
//
//        createMenuAndToolbar();
//    }
//
//    private void createMenuAndToolbar() {
//        final Toolbar toolbar = new Toolbar();
//
//        refreshButton.setIcon(GWT.getModuleBaseURL() + "images/dashboard/gameRefresh.png");
//        refreshButton.addRefreshButtonListener(new RefreshButtonListener() {
//            public void refreshData(boolean handRefresh) {
//                refreshBoards();
//            }
//
//            public void refreshIntervalChanged(int oldInterval, int newInterval) {
//            }
//        });
//
//        final ToolbarButton openBoards = new ToolbarButton(PB.lblQuickOpenBoards());
//        openBoards.setIcon(GWT.getModuleBaseURL() + "images/dashboard/gameOpen.png");
//        openBoards.addListener(new ButtonListenerAdapter() {
//            @Override
//            public void onClick(Button button, EventObject eventObject) {
//                quickOpenBoards();
//            }
//        });
//
//        messagesPanel = new MessagesBar();
//
//        toolbar.addButton(openBoards);
//        toolbar.addSeparator();
//        messagesPanel.associateToolbar(toolbar);
//        toolbar.addSeparator();
//        toolbar.addButton(refreshButton);
//        itemsPanel.setTopToolbar(toolbar);
//    }
//
//    private void refreshBoards() {
//        eventsDispatcher.requestEvents();
//    }
//
//    public void quickOpenBoards() {
//        if (gameboardWindow == null) {
//            gameboardWindow = new Window();
//            gameboardWindow.setTitle(PB.lblQuickOpenBoards());
//            gameboardWindow.setClosable(true);
//            gameboardWindow.setWidth(700);
//            gameboardWindow.setHeight(350);
//            gameboardWindow.setPlain(false);
//            gameboardWindow.setLayout(new FitLayout());
//
//            final GameboardTableView activeGamesView = gameboardTool.createActiveGamesView(settings, "playboardCreateNewGame");
//            gameboardWindow.add(activeGamesView.getViewComponent());
//
//            gameboardWindow.setCloseAction(Window.HIDE);
//            gameboardWindow.addButton(new Button(COMMON.btnClose(), new ButtonListenerAdapter() {
//                @Override
//                public void onClick(Button button, EventObject eventObject) {
//                    gameboardWindow.setVisible(false);
//                }
//            }));
//            gameboardWindow.setButtonAlign(Position.RIGHT);
//        }
//
//        if (!gameboardWindow.isVisible()) {
//            PageTracker.trackPageVisit("app/playboard/quickopen");
//
//            gameboardWindow.setVisible(true);
//        }
//    }
//
//    private void onCloseActiveTab() {
//        itemsPanel.remove(itemsPanel.getActiveTab());
//    }
//
//    private void onCloseOthersTabs() {
//        final Component[] items = itemsPanel.getItems();
//        for (Component component : items) {
//            if (!component.getId().equals(itemsPanel.getActiveTab().getId())) {
//                itemsPanel.remove(component);
//            }
//        }
//    }
//
//    private void onCloseAllTabs() {
//        final Component[] items = itemsPanel.getItems();
//        for (Component component : items) {
//            itemsPanel.remove(component);
//        }
//    }
//
//    private TabPanel createTabPanel() {
//        final TabPanel panel = new TabPanel();
//        panel.setAnimScroll(true);
//        panel.setTabPosition(Position.TOP);
//        panel.setMinTabWidth(120);
//        panel.setAutoScroll(true);
//        panel.setEnableTabScroll(true);
//
//        panel.add(emptyMessagePanel);
//
//        final Menu itemsPanelMenu = createTabsContextMenu();
//        panel.addListener(new TabPanelListenerAdapter() {
//            @Override
//            public void onTabChange(TabPanel tabPanel, Panel panel) {
//                PlayboardPanel pp = extractPlayboardPanel(panel);
//                if (pp != null) {
//                    pp.activateBoard();
//                    alarmFacilityWidget.clearAlarms("your.turn." + pp.getPlayboardItemBean().getBoardId());
//                }
//            }
//
//            @Override
//            public void onContextMenu(TabPanel tabPanel, Panel panel, EventObject eventObject) {
//                itemsPanelMenu.showAt(eventObject.getXY());
//            }
//        });
//        panel.addListener(new PanelListenerAdapter() {
//            @Override
//            public void onRemove(Container container, Component component) {
//                if (component.getId().equals(emptyMessagePanel.getId())) {
//                    return;
//                }
//
//                final PlayboardPanel pp = (PlayboardPanel) component;
//                final PlayboardServiceAsync service = PlayboardService.App.getInstance();
//
//                final long boardId = pp.getPlayboardItemBean().getBoardId();
//                pp.closeBoard();
//                service.closeBoard(boardId, new AsyncCallback<Void>() {
//                    public void onFailure(Throwable throwable) {
//                        ExceptionHandler.showSystemError(throwable);
//                    }
//
//                    public void onSuccess(Void aVoid) {
//                    }
//                });
//
//                if (itemsPanel.getComponents().length == 0) {
//                    itemsPanel.add(emptyMessagePanel);
//                }
//            }
//        });
//        return panel;
//    }
//
//    private Menu createTabsContextMenu() {
//        final Menu itemsPanelMenu = new Menu();
//
//        final Item openBoard = new Item(PB.lblQuickOpenBoards());
//        openBoard.addListener(new BaseItemListenerAdapter() {
//            @Override
//            public void onClick(BaseItem baseItem, EventObject eventObject) {
//                quickOpenBoards();
//            }
//        });
//        openBoard.setIcon(GWT.getModuleBaseURL() + "images/dashboard/gameOpen.png");
//        itemsPanelMenu.addItem(openBoard);
//
//        itemsPanelMenu.addSeparator();
//
//        final Item close = new Item(PB.lblCloseActiveBoard());
//        close.addListener(new BaseItemListenerAdapter() {
//            public void onClick(BaseItem item, EventObject e) {
//                onCloseActiveTab();
//            }
//        });
//        itemsPanelMenu.addItem(close);
//
//        final Item closeOthers = new Item(PB.lblCloseOtherBoards());
//        closeOthers.addListener(new BaseItemListenerAdapter() {
//            public void onClick(BaseItem item, EventObject e) {
//                onCloseOthersTabs();
//            }
//        });
//        itemsPanelMenu.addItem(closeOthers);
//
//        final Item closeAll = new Item(PB.lblCloseAllBoards());
//        closeAll.addListener(new BaseItemListenerAdapter() {
//            public void onClick(BaseItem item, EventObject e) {
//                onCloseAllTabs();
//            }
//        });
//        itemsPanelMenu.addItem(closeAll);
//        itemsPanelMenu.addSeparator();
//
//        final RefreshMenu refreshMenu = new RefreshMenu();
//        refreshMenu.addRefreshListener(new RefreshListener() {
//            public void refreshIntervalChanged(int oldInterval, int newInterval) {
//                refreshButton.setRefreshInterval(newInterval);
//            }
//        });
//        itemsPanelMenu.addItem(new MenuItem(APP.btnRefreshEach(), refreshMenu));
//        refreshButton.addRefreshButtonListener(new RefreshButtonListener() {
//            public void refreshData(boolean handRefresh) {
//            }
//
//            public void refreshIntervalChanged(int oldInterval, int newInterval) {
//                refreshMenu.setRefreshInterval(newInterval);
//            }
//        });
//        final Item refresh = new Item(APP.btnRefresh());
//        refresh.setIcon(GWT.getModuleBaseURL() + "images/dashboard/gameRefresh.png");
//        refresh.addListener(new BaseItemListenerAdapter() {
//            public void onClick(BaseItem item, EventObject e) {
//                refreshBoards();
//            }
//        });
//        itemsPanelMenu.addItem(refresh);
//        return itemsPanelMenu;
//    }
//
//    public void activate(Parameters parameters) {
//        if (Log.isInfoEnabled()) {
//            Log.info("Activating playboard panel with parameters: " + parameters + ". Panel already active: " + refreshButton.isActive());
//        }
//
//        String boardId = null;
//        if (parameters != null) {
//            boardId = parameters.getString(BOARD_ID_PARAMETER);
//        }
//
//        if (boardId != null) {
//            openGameBoard(Long.parseLong(boardId));
//        }
//
//        if (!refreshButton.isActive()) {
//            refreshButton.activate();
//        }
//
//        if (gameboardWindow != null && gameboardWindow.isVisible()) {
//            gameboardWindow.setVisible(false);
//        }
//    }
//
//    public void deactivate() {
//        refreshButton.deactivate();
//    }
//
//    public Component getFrameViewComponent() {
//        return this;
//    }
//
//    public ParameterInfo[] getParametersInfos() {
//        return new ParameterInfo[]{
//                //Following parameters taken from GameboardTable.
//                new ParameterInfo("gameboard.orders", "to", int[].class, true),
//                new ParameterInfo("gameboard.hiddens", "th", boolean[].class, true),
//                new ParameterInfo("gameboard.widths", "tw", int[].class, false),
//
//                //Parameters from PlayboardPanel
//                new ParameterInfo("game.info.collapsed", "gic", boolean.class, false, true),
//                new ParameterInfo("tiles.info.collapsed", "tic", boolean.class, false, true),
//                new ParameterInfo("history.info.collapsed", "hic", boolean.class, false, true),
//                new ParameterInfo("players.info.collapsed", "pic", boolean.class, false, true),
//                new ParameterInfo("words.info.collapsed", "wic", boolean.class, false, true),
//                new ParameterInfo("moves.info.collapsed", "mic", boolean.class, false, true),
//        };
//    }
//
//    public void openGameBoard(long boardId) {
//        if (Log.isDebugEnabled()) {
//            Log.debug("Open game board: " + boardId);
//        }
//
//        PageTracker.trackPageVisit("app/playboard/open");
//
//        final String id = "playboardPanel" + boardId;
//
//        if (gameboardWindow != null && gameboardWindow.isVisible()) {
//            gameboardWindow.setVisible(false);
//        }
//
//        final Panel item = itemsPanel.getItem(id);
//        if (item != null) {
//            if (Log.isDebugEnabled()) {
//                Log.debug("Panel already opened. Activate it by id: " + id);
//            }
//            itemsPanel.setActiveTab(id);
//        } else {
//            getEl().mask(PB.lblLoadingBoard());
//
//            if (Log.isDebugEnabled()) {
//                Log.debug("Loading game board from server");
//            }
//            final PlayboardServiceAsync serviceAsync = PlayboardService.App.getInstance();
//            serviceAsync.openBoard(boardId, new AsyncCallback<PlayboardItemBean>() {
//                public void onFailure(Throwable throwable) {
//                    getEl().unmask();
//                    ExceptionHandler.showSystemError(throwable);
//                }
//
//                public void onSuccess(PlayboardItemBean playboardItemBean) {
//                    getEl().unmask();
//
//                    if (Log.isDebugEnabled()) {
//                        Log.debug("Board loaded from server: " + playboardItemBean);
//                    }
//
//                    if (playboardItemBean == null) {
//                        MessagesBox.showMessage(PB.errLoadingTitle(), PB.errLoadingMessage());
//                    } else {
//                        createPlayboardPanel(playboardItemBean, id);
//                    }
//                }
//            });
//        }
//    }
//
//    private void createPlayboardPanel(PlayboardItemBean playboardItemBean, String id) {
//        if (Log.isDebugEnabled()) {
//            Log.debug("Creating playboard panel.");
//        }
//        final PlayboardPanel panel = new PlayboardPanel(playerSessionTool.getCurrentPlayer(), playboardItemBean, messagesPanel, eventsDispatcher, settings);
//
//        final Panel container = new Panel();
//        container.setFrame(false);
//        container.setBodyBorder(false);
//        container.setLayout(new FitLayout());
//        container.add(panel);
//        container.setPaddings(5, 10, 10, 5);
//
//        final Panel pp = new Panel();
//        pp.setTitle(playboardItemBean.getTitle() + " #" + playboardItemBean.getBoardId());
//        pp.setFrame(false);
//        pp.setBodyBorder(false);
//        pp.setId(id);
//        pp.setClosable(true);
//        pp.setAutoScroll(true);
//        pp.add(container);
//        pp.addListener(new PanelListenerAdapter() {
//            @Override
//            public void onResize(BoxComponent boxComponent, int i, int i1, int i2, int i3) {
//                final int height = boxComponent.getHeight();
//                if (height < 600) {
//                    container.setHeight(600);
//                } else {
//                    container.setHeight(height);
//                }
//            }
//        });
//
//        if (Log.isDebugEnabled()) {
//            Log.debug("Remove empty panel if exist and add just created.");
//        }
//        itemsPanel.remove(emptyMessagePanel);
//        itemsPanel.add(pp);
//        itemsPanel.doLayout();
//
//        if (Log.isDebugEnabled()) {
//            Log.debug("Activate new panel");
//        }
//        itemsPanel.setActiveTab(id);
//    }
//
//    public void removeMemoryWord(int boardId, int wordId) {
//        getPlayboardPanel(boardId).removeMemoryWord(wordId);
//    }
//
//    public void selectMemoryWord(int boardId, int wordId) {
//        getPlayboardPanel(boardId).selectMemoryWord(wordId);
//    }
//
//    public void selectHistoryWord(int boardId, int wordId) {
//        getPlayboardPanel(boardId).selectHistoryWord(wordId);
//    }
//
//    private PlayboardPanel getPlayboardPanel(long boardId) {
//        return extractPlayboardPanel(itemsPanel.getItem(String.valueOf("playboardPanel" + boardId)));
//    }
//
//    private PlayboardPanel extractPlayboardPanel(Panel panel) {
//        if (panel == null || panel == emptyMessagePanel) {
//            return null;
//        }
//        final Panel pp = (Panel) panel.getComponent(0);
//        return (PlayboardPanel) pp.getComponent(0);
//    }
//
//    public void playboardCreateNewGame() {
//        PageTracker.trackPageVisit("app/playboard/create");
//
//        new CreateGameDialog(playerSessionTool).showCreateDialog(new CreateGameCallback() {
//            public void gameCreate(long boardId) {
//                openGameBoard(boardId);
//            }
//        });
//    }
//
//    private void raiseYourMoveAlarm(GameBoardEvent event) {
//        boolean showNotification = false;
//        if (event instanceof GameTurnEvent) {
//            final GameTurnEvent e = (GameTurnEvent) event;
//            showNotification = (e.getNextPlayer() == playerSessionTool.getCurrentPlayer());
//        } else if (event instanceof GameStartedEvent) {
//            final GameStartedEvent e = (GameStartedEvent) event;
//            showNotification = (e.getPlayerTrun() == playerSessionTool.getCurrentPlayer());
//        }
//
//        if (!showNotification) {
//            return;
//        }
//
//        final Panel activeTab = itemsPanel.getActiveTab();
//        if (refreshButton.isActive() &&
//                extractPlayboardPanel(activeTab).getPlayboardItemBean().getBoardId() == event.getBoardId()) {
//            return;
//        }
//
//        final GameboardItemBean bean = gameboardTool.getActiveBoard(event.getBoardId());
//        if (bean != null) {
//            final String link = PlayboardTool.getOpenGameBoardLink(bean.getBoardId(), bean.getTitle());
//            alarmFacilityWidget.raiseNotification("your.turn." + bean.getBoardId(),
//                    PB.ttlYouTurnAgain(), AppRes.MPB.msgItsYouTurn(link), AlarmLevel.CRITICAL);
//        }
//    }
//
//    private class TheEventsListener implements EventsListener<GameBoardEvent> {
//        public void eventsReceived(Collection<GameBoardEvent> evens) {
//            for (GameBoardEvent event : evens) {
//                final Component[] items = itemsPanel.getItems();
//                for (Component item : items) {
//                    if (item instanceof Panel) {
//                        final PlayboardPanel pp = extractPlayboardPanel((Panel) item);
//                        if (pp != null) {
//                            final PlayboardItemBean bean = pp.getPlayboardItemBean();
//                            if (bean.getBoardId() == event.getBoardId()) {
//                                pp.processPlayboardEvent(event);
//                            }
//                        }
//                    }
//                }
//                raiseYourMoveAlarm(event);
//            }
//        }
//    }
//
//    private static class TheEventsCorrelator implements Correlator<GameBoardEvent> {
//        public boolean isEventSupported(Event e) {
//            return e instanceof GameBoardEvent;
//        }
//
//        public Correlation eventsColleration(GameBoardEvent event1, GameBoardEvent event2) {
//            return Correlation.NEUTRAL;
//        }
//    }
}