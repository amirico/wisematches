package wisematches.client.gwt.app.client.content.gameboard;

/**
 * {@code GameboardTable} is a component that can show information about games and sort form. It show game
 * title, game players, player's move and time to left.
 * <p/>
 * This table can be attached to {@code EventsDispatcher} to listen all changes and update the table.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
class GameboardTable {//extends Panel implements GameboardTableView {
//    private Store store;
//    private GridPanel gridPanel;
//
//    private final Settings settings;
//    private final long currentPlayerId;
//    private final String createGameMethodName;
//
//    private final CardLayout cardLayout = new CardLayout(true);
//    private final RefreshGridTimer refreshGridTimer = new RefreshGridTimer();
//
//    GameboardTable(Store gamesInfo, Settings frameViewSettings, long currentPlayerId) {
//        this(gamesInfo, frameViewSettings, currentPlayerId, "createNewGame");
//    }
//
//    GameboardTable(Store gamesInfo, Settings frameViewSettings, long currentPlayerId, String createNewGameLink) {
//        this.store = gamesInfo;
//        this.settings = frameViewSettings;
//        this.currentPlayerId = currentPlayerId;
//        createGameMethodName = createNewGameLink;
//
//        initTable();
//    }
//
//    private void initTable() {
//        setLayout(cardLayout);
//
//        add(createEmptyGamesPanel());
//        add(createGridPanel());
//
//        store.addStoreListener(new StoreListenerAdapter() {
//            @Override
//            public void onAdd(Store store, Record[] records, int i) {
//                checksGamesExist();
//            }
//
//            @Override
//            public void onClear(Store store) {
//                checksGamesExist();
//            }
//
//            @Override
//            public void onRemove(Store store, Record record, int i) {
//                checksGamesExist();
//            }
//        });
//        checksGamesExist();
//    }
//
//    private Widget createEmptyGamesPanel() {
//        final CenterMessagePanel p = new CenterMessagePanel();
//        p.setMessage(MAPP.lblEmptyGameboard("javascript:" + createGameMethodName + "();",
//                "javascript:showApplicationItem('" + DashboardWidget.ITEM_ID + "', null)"));
//        return p;
//    }
//
//    private Widget createGridPanel() {
//        final ColumnConfig titleColumn = new ColumnConfig(APP.lblGameColumnTitle(), "title", 300, true, new TitleRenderer());
//        titleColumn.setCss("white-space:normal;");
//
//        final PlayerGridColumnConfig playersColumn = new PlayerGridColumnConfig("players", 160, false);
//        playersColumn.setHeader(APP.lblGameOpponents());
//
//        final PlayerGridRenderer gridRenderer = playersColumn.getPlayerGridRenderer();
//        gridRenderer.setManyPlayers(true);
//        gridRenderer.setShowRating(false);
//        gridRenderer.setRowCls("dashed-row");
//        gridRenderer.setNoPlayerText(APP.lblWaitingPlayer());
//
//        final ColumnConfig scoresColumn = new ColumnConfig(APP.lblGameScores(), "scores", 60, false);
//        scoresColumn.setRenderer(new ScoreRenderer());
//
//        final ColumnConfig languageColumn = new ColumnConfig(APP.lblGameColumnLocale(), "locale", 100, true);
//
//        final PlayerGridColumnConfig playerMoveColumn = new PlayerGridColumnConfig("playermove", 160, true);
//        final PlayerMoveRenderer playerMoveRenderer = new PlayerMoveRenderer("playermove", currentPlayerId, false);
//        playerMoveRenderer.setNoPlayerText(APP.lblWaitingOpponent());
//        playerMoveColumn.setRenderer(playerMoveRenderer);
//        playerMoveColumn.setHeader(APP.lblGameColumnPlayerMove());
//
//        final PlayerGridRenderer renderer = playerMoveColumn.getPlayerGridRenderer();
//        renderer.setShowRating(false);
//        renderer.setManyPlayers(false);
//        renderer.setNoPlayerText(APP.lblWaitingOpponent());
//
//        final ColumnConfig lastMoveTime = new ColumnConfig(APP.lblGameColumnTimeLeft(), "lastMoveTime", 100, true);
//        lastMoveTime.setRenderer(new MoveTimeRenderer("lastMoveTime"));
//
//        final ColumnModel columnModel = new SettingsColumnModel("gameboard", settings, new ColumnConfig[]{
//                titleColumn,
//                playersColumn,
//                scoresColumn,
//                languageColumn,
//                playerMoveColumn,
//                lastMoveTime
//        });
//
//        gridPanel = new GridPanel();
//        gridPanel.setStore(store);
//        gridPanel.setColumnModel(columnModel);
//        gridPanel.setTrackMouseOver(true);
//        gridPanel.setLoadMask(true);
//        gridPanel.setSelectionModel(new RowSelectionModel(false));
//
//        final GridView gridView = gridPanel.getView();
//        gridView.setAutoFill(true);
//        gridView.setForceFit(true);
//        gridPanel.setAutoExpandColumn(1);
//
//        return gridPanel;
//    }
//
//
//    public void addGameboardSelectionListener(GameboardSelectionListener l) {
//        gridPanel.getSelectionModel().addListener(new TheListenerAdapter(l));
//    }
//
//    public long[] getSelectedBoards() {
//        final Record[] records = gridPanel.getSelectionModel().getSelections();
//        final long[] res = new long[records.length];
//        for (int i = 0; i < records.length; i++) {
//            res[i] = Long.valueOf(records[i].getAsString("boardid"));
//        }
//        return res;
//    }
//
//    public Panel getViewComponent() {
//        return this;
//    }
//
//
//    private void checksGamesExist() {
//        final int count = store.getCount();
//        if (count == 0) {
//            refreshGridTimer.deactivate();
//            setActiveItem(0);
//        } else {
//            refreshGridTimer.activate();
//            setActiveItem(1);
//        }
//    }
//
//    private class RefreshGridTimer extends Timer {
//        @Override
//        public void run() {
//            gridPanel.getView().refresh(false);
//        }
//
//        public void activate() {
//            scheduleRepeating(60000);
//        }
//
//        public void deactivate() {
//            cancel();
//        }
//    }
//
//    private static class TitleRenderer implements Renderer {
//        public String render(Object o, CellMetadata cellMetadata, Record record, int row, int col, Store store) {
//            final String boardid = record.getAsString("boardid");
//            final String title = record.getAsString("title");
//            return PlayboardTool.getOpenGameBoardLink(boardid, title);
//        }
//    }
//
//    private static class ScoreRenderer implements Renderer {
//        public String render(Object o, CellMetadata cellMetadata, Record record, int i, int i1, Store store) {
//            final JavaScriptObject obj = (JavaScriptObject) record.getAsObject("players");
//            final JSArray<PlayerInfoBean> ar = new JSArray<PlayerInfoBean>(obj);
//            final StringBuilder res = new StringBuilder();
//            res.append("<table width=\"100%\" class=\"player-info dashed-row\" border=\"0\" cellspacing=\"0\" cellpandding=\"0\">");
//            for (final PlayerInfoBean bean : ar) {
//                res.append("<tr height=\"19px\">");
//                if (bean != null) {
//                    res.append("<td>");
//                    res.append(bean.getCurrentRating());
//                } else {
//                    res.append("<td class=\"renderer-player-no-row\">");
//                    res.append(APP.lblNA());
//                }
//                res.append("</td>");
//                res.append("</tr>");
//            }
//            res.append("</table>");
//            return res.toString();
//        }
//    }
//
//    private static class TheListenerAdapter extends RowSelectionListenerAdapter {
//        private final GameboardSelectionListener listener;
//
//        private TheListenerAdapter(GameboardSelectionListener listener) {
//            this.listener = listener;
//        }
//
//        @Override
//        public void onRowSelect(RowSelectionModel rowSelectionModel, int i, Record record) {
//            final GameboardItemBean bean = (GameboardItemBean) record.getAsObject("item");
//            listener.gameboardSelected(bean);
//        }
//
//        @Override
//        public void onRowDeselect(RowSelectionModel rowSelectionModel, int i, Record record) {
//            listener.gameboardSelected(null);
//        }
//    }
//
//    /**
// * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
//     */
//    private static class MoveTimeRenderer implements Renderer {
//        private final String columnName;
//
//        public MoveTimeRenderer(String columnName) {
//            this.columnName = columnName;
//        }
//
//        public String render(Object o, CellMetadata cellMetadata, Record record, int row, int col, Store store) {
//            final GameboardItemBean item = (GameboardItemBean) record.getAsObject("item");
//            final Date date = record.getAsDate(columnName);
//            if (date == null || date.getTime() == 0) {
//                return APP.lblNA();
//            }
//            return TimeFormatter.convertTimeout(item.getMinutesToTimeout());
//        }
//    }
}
