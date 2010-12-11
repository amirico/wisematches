package wisematches.client.gwt.app.client.content.dashboard;

import com.smartgwt.client.widgets.Canvas;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
class DashboardTable extends Canvas {
/*    private Store store;
    private RecordDef recordDef;
    private GridPanel grid;

    private ToolbarButton joinButton;
    private RefreshButton refreshButton = new RefreshButton();

    private final Settings settings;
    private final EventsDispatcher eventsDispatcher;
    private final OpenBoardCallback openBoardCallback;
    private final PlayerSessionTool playerSessionTool;

    private final CardLayout cardLayout = new CardLayout(true);
    private final DashboardEventsListener eventsListener = new DashboardEventsListener();
    private final DashboardServiceAsync dashboardService = DashboardService.App.getInstance();

    public DashboardTable(PlayerSessionTool playerSessionTool, OpenBoardCallback openBoardCallback,
                          EventsDispatcher eventsDispatcher, Settings settings) {
        this.playerSessionTool = playerSessionTool;
        this.openBoardCallback = openBoardCallback;
        this.eventsDispatcher = eventsDispatcher;
        this.settings = settings;

        initTable();
    }

    private void initTable() {
        setLayout(cardLayout);
        setTopToolbar(createToolbar());
        setFrame(true);
        setTitle(APP.lblDashboardTitle());

        add(createEmptyGamesPanel());
        add(createGridPanel());
        setActiveItem(0);

        // Load table data.
        DeferredCommand.addCommand(new Command() {
            public void execute() {
                refreshGamesTable();
                eventsDispatcher.addEventsListener(eventsListener, eventsListener);
            }
        });
    }


    private Widget createEmptyGamesPanel() {
        return new CenterMessagePanel(MAPP.lblEmptyDashboard("javascript:createNewGame();"));
    }

    private Widget createGridPanel() {
        recordDef = new RecordDef(new FieldDef[]{
                new StringFieldDef("boardid"),
                new StringFieldDef("title"),
                new ObjectFieldDef("players"),
                new StringFieldDef("locale"),
                new StringFieldDef("permove"),
                new ObjectFieldDef("item")
        });

        store = new Store(new ArrayReader(0, recordDef));
        store.addStoreListener(new StoreListenerAdapter() {
            @Override
            public void onAdd(Store store, Record[] records, int i) {
                checkTableEmpty();
            }

            @Override
            public void onClear(Store store) {
                checkTableEmpty();
            }

            @Override
            public void onRemove(Store store, Record record, int i) {
                checkTableEmpty();
            }

            private void checkTableEmpty() {
                if (store.getCount() == 0) {
                    cardLayout.setActiveItem(0);
                } else {
                    cardLayout.setActiveItem(1);
                }
            }
        });

        final ColumnConfig titleColumn = new ColumnConfig(APP.lblGameColumnTitle(), "title", 260, false);
        titleColumn.setCss("white-space:normal;");
        titleColumn.setRenderer(new TitleRenderer());

        final PlayerGridColumnConfig playersColumn = new PlayerGridColumnConfig("players", 200, false);
        final PlayerGridRenderer gridRenderer = playersColumn.getPlayerGridRenderer();
        gridRenderer.setManyPlayers(true);
        gridRenderer.setShowRating(true);
        gridRenderer.setNoPlayerText(APP.lblWaitingPlayer());

        final ColumnConfig languageColumn = new ColumnConfig(APP.lblGameColumnLocale(), "locale", 120, true);

        final ColumnConfig perMoveColumn = new ColumnConfig(APP.lblGameColumnPerMove(), "permove", 100, true);

        final ColumnConfig optionsColumn = new ColumnConfig(APP.lblGameColumnOptions(), "options", 100, false, new OptionsRenderer());

        final ColumnModel columnModel = new SettingsColumnModel("dashboard", settings, new ColumnConfig[]{
                titleColumn,
                playersColumn,
                languageColumn,
                perMoveColumn,
                optionsColumn
        });

        final RowSelectionModel selectionModel = new RowSelectionModel(true);
        selectionModel.addListener(new RowSelectionListenerAdapter() {
            @Override
            public void onSelectionChange(RowSelectionModel rowSelectionModel) {
                if (rowSelectionModel.hasSelection()) {
                    final Record record = rowSelectionModel.getSelected();
                    final DashboardItemBean item = (DashboardItemBean) record.getAsObject("item");
                    if (checkGameOptions(item) == DashboardService.JoinResult.SUCCESS) {
                        joinButton.enable();
                    } else {
                        joinButton.disable();
                    }
                } else {
                    joinButton.disable();
                }
            }
        });

        grid = new GridPanel();
        grid.setStore(store);
        grid.setColumnModel(columnModel);
        grid.setTrackMouseOver(true);
        grid.setSelectionModel(selectionModel);
        grid.setAutoExpandColumn(1);

        final GridView gridView = grid.getView();
        gridView.setAutoFill(true);
        gridView.setForceFit(true);

        return grid;
    }

    private Toolbar createToolbar() {
        final Toolbar tb = new Toolbar();
        ToolbarButton createButton = new ToolbarButton(APP.lblCreateGame());
        createButton.setIcon(GWT.getModuleBaseURL() + "images/dashboard/gameCreate.png");
        createButton.setTooltip(APP.ttpCreateGame());
        createButton.addListener(new ButtonListenerAdapter() {
            public void onClick(Button button, EventObject eventObject) {
                createNewGame();
            }
        });

        joinButton = new ToolbarButton(APP.lblJoinToGame());
        joinButton.setIcon(GWT.getModuleBaseURL() + "images/dashboard/gameJoin.png");
        joinButton.setTooltip(APP.ttpJoinToGame());
        joinButton.disable();
        joinButton.addListener(new ButtonListenerAdapter() {
            public void onClick(Button button, EventObject eventObject) {
                getEl().mask(APP.maskJoining());

                final Record record = grid.getSelectionModel().getSelected();
                joinToGame(record.getAsString("boardid"));
            }
        });

        refreshButton.setIcon(GWT.getModuleBaseURL() + "images/dashboard/gameRefresh.png");
        refreshButton.setRefreshInterval(settings.getInt("refresh.interval"));
        refreshButton.addRefreshButtonListener(new RefreshButtonListener() {
            public void refreshIntervalChanged(int oldInterval, int newInterval) {
                settings.setInt("refresh.interval", newInterval);
            }

            public void refreshData(boolean handRefresh) {
                if (handRefresh) {
                    refreshGamesTable();
                } else {
                    eventsDispatcher.requestEvents();
                }
            }
        });

        tb.addButton(createButton);
        tb.addSpacer();
        tb.addButton(joinButton);
        tb.addFill();
        tb.addButton(refreshButton);
        return tb;
    }


    protected void createNewGame() {
        PageTracker.trackPageVisit("app/dashboard/create");

        new CreateGameDialog(playerSessionTool).showCreateDialog(openBoardCallback);
    }

    protected void joinToGame(final String boardId) {
        PageTracker.trackPageVisit("app/dashboard/join");

        final long id = Long.parseLong(boardId);

        dashboardService.joinToBoard(id, new AsyncCallback<DashboardService.JoinResult>() {
            public void onFailure(Throwable throwable) {
                getEl().unmask();
                ExceptionHandler.showSystemError(throwable);
            }

            public void onSuccess(DashboardService.JoinResult result) {
                getEl().unmask();

                switch (result) {
                    case ALREDY_IN_GAME:
                        MessagesBox.showMessage(APP.ttlJoinAlredyInGame(), APP.lblJoinAlredyInGame());
                        break;
                    case INTERNAL_ERROR:
                        MessagesBox.showMessage(APP.ttlJoinInternalError(), APP.lblJoinInternalError());
                        break;
                    case TO_MANY_PLAYERS:
                        MessagesBox.showMessage(APP.ttlJoinToManyPlayer(), APP.lblJoinToManyPlayer());
                        break;
                    case UNKNOWN_GAME:
                        MessagesBox.showMessage(APP.ttlJoinUnknownGame(), APP.lblJoinUnknownGame());
                        break;
                    case LOW_RATING:
                        MessagesBox.showMessage(APP.ttlJoinLowRating(), APP.lblJoinLowRating());
                        break;
                    case HIGH_RATING:
                        MessagesBox.showMessage(APP.ttlJoinHighRating(), APP.lblJoinHighRating());
                        break;
                    default:
                        openBoardCallback.gameCreate(id);
                }
            }
        });
    }

    private JoinResult checkGameOptions(DashboardItemBean item) {
        final PlayerInfoBean playerInfoBean = playerSessionTool.getPlayerInfoBean();
        if (playerInfoBean.getMemberType() == MemberType.GUEST) {
            return DashboardService.JoinResult.GUEST_NOT_ALLOWED;
        }

        final long currentPlayer = playerInfoBean.getPlayerId();
        final int currentRating = playerInfoBean.getCurrentRating();
        if (item.getMinRating() != 0 && currentRating < item.getMinRating()) {
            return DashboardService.JoinResult.LOW_RATING;
        } else if (item.getMaxRating() != 0 && currentRating > item.getMaxRating()) {
            return DashboardService.JoinResult.HIGH_RATING;
        } else {
            final PlayerInfoBean[] playerInfoBeans = item.getPlayers();
            for (PlayerInfoBean bean : playerInfoBeans) {
                if (bean != null && bean.getPlayerId() == currentPlayer) {
                    return DashboardService.JoinResult.ALREDY_IN_GAME;
                }
            }
        }
        return DashboardService.JoinResult.SUCCESS;
    }


    protected void activate() {
        refreshButton.activate();
    }

    protected void deactivate() {
        refreshButton.deactivate();
    }


    private Record createDashboardRecord(DashboardItemBean item) {
        final String boardid = String.valueOf(item.getBoardId());
        final JSArray<PlayerInfoBean> arr = new JSArray<PlayerInfoBean>(item.getPlayers());
        return recordDef.createRecord(boardid, new Object[]{
                boardid,
                item.getTitle(),
                arr.getJavaScriptObject(),
                Localization.convertLocale(item.getLocale()),
                item.getDaysPerMove() + " " + APP.lblTimeDays(),
                item
        });
    }

    private void updateDashboardRecord(Record record, PlayerInfoBean playerInfoBean, GamePlayersEvent.Action action) {
        final JSArray<PlayerInfoBean> array = new JSArray<PlayerInfoBean>((JavaScriptObject) record.getAsObject("players"));

        int playerIndex = -1;
        int index = 0;
        for (PlayerInfoBean bean : array) {
            if (bean != null && bean.getPlayerId() == playerInfoBean.getPlayerId()) {
                playerIndex = index;
                break;
            }
            index++;
        }

        if (action == GamePlayersEvent.Action.ADDED) {
            if (playerIndex == -1) {
                index = 0;
                for (PlayerInfoBean bean : array) {
                    if (bean == null) {
                        array.set(index, playerInfoBean);
                        break;
                    }
                    index++;
                }
                record.set("players", array.getJavaScriptObject());
                record.commit();
            }
        } else if (action == GamePlayersEvent.Action.REMOVED) {
            if (playerIndex != -1) {
                array.set(playerIndex, null);
                record.set("players", array.getJavaScriptObject());
                record.commit();
            }
        }
    }

    private void refreshGamesTable() {
        final ExtElement el = getEl();
        el.mask(APP.maskLoading());

        dashboardService.getWaitingDashboardItems(new AsyncCallback<DashboardItemBean[]>() {
            public void onFailure(Throwable throwable) {
                el.unmask();
                ExceptionHandler.showSystemError(throwable);
            }

            public void onSuccess(DashboardItemBean[] dashboardItems) {
                el.unmask();

                store.removeAll();

                int index = 0;
                final Record[] records = new Record[dashboardItems.length];
                for (DashboardItemBean dashboardItem : dashboardItems) {
                    records[index++] = createDashboardRecord(dashboardItem);
                }
                store.add(records);
                store.commitChanges();
            }
        });
    }


    private class TitleRenderer implements Renderer {
        public String render(Object o, CellMetadata cellMetadata, Record record, int row, int col, Store store) {
            final String boardid = record.getAsString("boardid");
            final String title = record.getAsString("title");
            final DashboardItemBean item = (DashboardItemBean) record.getAsObject("item");

            final DashboardService.JoinResult result = checkGameOptions(item);
            switch (result) {
                case ALREDY_IN_GAME:
                    return getBoardNameHTML(boardid, title, APP.ttlJoinAlredyInGame(), APP.lblJoinAlredyInGame());
                case LOW_RATING:
                    return getBoardNameHTML(boardid, title, APP.ttlJoinLowRating(), APP.lblJoinLowRating());
                case HIGH_RATING:
                    return getBoardNameHTML(boardid, title, APP.ttlJoinHighRating(), APP.lblJoinHighRating());
                case GUEST_NOT_ALLOWED:
                    return getBoardNameHTML(boardid, title, APP.ttlJoinGuest(), APP.lblJoinGuest());
                default:
                    ;
            }
            return "<a ext:qtip=\"" + APP.ttpJoinToGame() + "\" href=\"javascript:joinToGame('" +
                    boardid + "');\">" + title + "</a>";
        }

        private String getBoardNameHTML(String boardId, String title, String error, String errorTooltip) {
            String s = "<table class=\"board-name-table\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">";
            s += "<tr><td class=\"title\">" + title + " #" + boardId + "</td></tr>";
            if (error != null) {
                s += "<tr><td class=\"error\" xt:qtip=\"" + errorTooltip + "\">" + error + "</td></tr>";
            }
            s += "</table>";
            return s;
        }
    }

    private class OptionsRenderer implements Renderer {
        public String render(Object o, CellMetadata cellMetadata, Record record, int row, int col, Store store) {
            final DashboardItemBean item = (DashboardItemBean) record.getAsObject("item");
            StringBuilder b = new StringBuilder();
            b.append("<span>");
            boolean t = false;
            if (item.getMinRating() > 0) {
                t = true;
                b.append(APP.lblGameColumnMinR());
                b.append(": ");
                b.append(item.getMinRating());
            }
            if (item.getMaxRating() > 0) {
                if (t) {
                    b.append("<br/>");
                }
                b.append(APP.lblGameColumnMaxR());
                b.append(": ");
                b.append(item.getMaxRating());
            }
            b.append("</span>");

            return b.toString();
        }
    }

    private class DashboardEventsListener implements EventsListener<GameBoardEvent>, Correlator<GameBoardEvent> {
        public boolean isEventSupported(Event e) {
            return e instanceof GameCreatedEvent || e instanceof GameStartedEvent || e instanceof GamePlayersEvent;
        }

        public Correlation eventsColleration(GameBoardEvent event1, GameBoardEvent event2) {
            if (event1.getBoardId() == event2.getBoardId()) { // if events for the same board. 
                if (event2 instanceof GameStartedEvent) { // and event2 indicates that game started - exclude any others.
                    return Correlation.EXCLUSION;
                } else if (event1 instanceof GamePlayersEvent && event2 instanceof GamePlayersEvent) {
                    final GamePlayersEvent e1 = (GamePlayersEvent) event1;
                    final GamePlayersEvent e2 = (GamePlayersEvent) event2;

                    if (e1.getPlayerInfoBean().getPlayerId() == e2.getPlayerInfoBean().getPlayerId()) {
                        return (e1.getAction() != e2.getAction()) ? Correlation.MUTUAL_EXCLUSION : Correlation.EXCLUSION;
                    }
                }
            }
            return Correlation.NEUTRAL;
        }

        public void eventsReceived(Collection<GameBoardEvent> evens) {
            for (GameBoardEvent even : evens) {
                final String boardid = String.valueOf(even.getBoardId());

                final Record record = store.getById(boardid);
                if (even instanceof GameCreatedEvent) {
                    final GameCreatedEvent e = (GameCreatedEvent) even;

                    if (record == null) {
                        store.add(createDashboardRecord(e.getGameSettingsBean()));
                        store.commitChanges();
                    }
                } else if (even instanceof GamePlayersEvent) {
                    final GamePlayersEvent e = (GamePlayersEvent) even;

                    if (record != null) {
                        updateDashboardRecord(record, e.getPlayerInfoBean(), e.getAction());
                    }
                } else if (even instanceof GameStartedEvent) {
                    if (record != null) {
                        store.remove(record);
                        store.commitChanges();
                    }
                }
            }
        }
    }*/
}