package wisematches.client.gwt.app.client.content.playboard.infos;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class HistoryInfoPanel { //extends Panel {
//    private Store movesStore;
//    private RecordDef recordDef;
//
//    private final PlayboardItemBean playboardItemBean;
//
//    public HistoryInfoPanel(PlayboardItemBean playboardItemBean) {
//        super(PB.ttlMovesHistory());
//        this.playboardItemBean = playboardItemBean;
//        playboardItemBean.addPropertyChangeListener(new ThePropertyChangeListener());
//        initPanel();
//        updateMoves();
//    }
//
//    private void initPanel() {
//        recordDef = new RecordDef(
//                new FieldDef[]{
//                        new ObjectFieldDef("playerMove"),
//                        new ObjectFieldDef("playerBean"),
//                        new StringFieldDef("points")
//                }
//        );
//        movesStore = new Store(recordDef);
//
//        final GridPanel grid = new GridPanel();
//
//
//        final RowNumberingColumnConfig numberingColumnConfig = new RowNumberingColumnConfig();
//        final ColumnConfig wordColumnConfig = new ColumnConfig(PB.lblWordsMemoryWord(), "word", 160, true, new TheHistoryWordRenderer(), "word");
//        final ColumnConfig playerColumnConfig = new PlayerGridColumnConfig("playerBean", 80, false, false);
//        final ColumnConfig pointsColumnConfig = new ColumnConfig(PB.lblWordsMemoryPoints(), "points", 40);
//
//        final BaseColumnConfig[] columns = new BaseColumnConfig[]{
//                numberingColumnConfig,
//                wordColumnConfig,
//                playerColumnConfig,
//                pointsColumnConfig,
//        };
//
//        final ColumnModel columnModel = new ColumnModel(columns);
//        grid.setStore(movesStore);
//        grid.setColumnModel(columnModel);
//        grid.setStripeRows(true);
//        grid.setAutoExpandColumn("word");
//        grid.setSelectionModel(new RowSelectionModel(false));
//
//        setFrame(true);
//        setCollapsible(true);
//        setLayout(new FitLayout());
//
//        add(grid);
//    }
//
//    private void updateMoves() {
//        final List<PlayerMoveBean> moveBeanList = playboardItemBean.getPlayersMoves();
//        final ListIterator<PlayerMoveBean> listIterator = moveBeanList.listIterator(movesStore.getCount());
//        while (listIterator.hasNext()) {
//            final PlayerMoveBean playerMove = listIterator.next();
//            final String recordId = String.valueOf(playerMove.getMoveNumber());
//            final PlayerInfoBean p = playboardItemBean.getPlayerInfoBean(playerMove.getPlayerId());
//            movesStore.add(recordDef.createRecord(recordId, new Object[]{
//                    playerMove, p, playerMove.getPoints()
//            }));
//        }
//    }
//
//    private class ThePropertyChangeListener implements PropertyChangeListener<PlayboardItemBean> {
//        public void propertyChanged(PlayboardItemBean bean, String property, Object oldValue, Object newValue) {
//            if ("playersMoves".equals(property)) {
//                updateMoves();
//            }
//        }
//    }
//
//    private class TheHistoryWordRenderer implements Renderer {
//        public String render(Object o, CellMetadata cellMetadata, Record record, int row, int column, Store store) {
//            final String id = record.getId();
//            final long boardId = playboardItemBean.getBoardId();
//
//            final PlayerMoveBean move = (PlayerMoveBean) record.getAsObject("playerMove");
//            final PlayerMoveBean.Type type = move.getMoveType();
//            if (type == PlayerMoveBean.Type.EXCHANGE) {
//                return "<div>" + PB.lblMoveExhanged() + "</div>";
//            } else if (type == PlayerMoveBean.Type.PASSED) {
//                return "<div>" + PB.lblMovePassed() + "</div>";
//            }
//            return "<div><a href=\"javascript: selectHistoryMove(" + boardId + ", " + id + ");\">" +
//                    move.getWord().toStringWord() + "</a></div>";
//        }
//    }
}
