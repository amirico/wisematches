package wisematches.client.gwt.app.client.content.playboard.infos;

import com.smartgwt.client.widgets.Canvas;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class TilesInfoPanel extends Canvas {
//    public TilesInfoPanel(Tile[] bankTiles) {
//        super(PB.ttlTilesInfo());
//        initPanel(bankTiles);
//    }
//
//    private void initPanel(Tile[] bankTiles) {
//        setFrame(true);
//        setHeight(94);
//        setCollapsible(true);
//
//        addTool(new Tool(new Tool.ToolType("info"), new ToolHandler() {
//            public void onClick(EventObject eventObject, ExtElement extElement, Panel panel) {
//                final RulesWindow w = new RulesWindow(COMMON.tltScribbleRules(), RuleInfo.LETTERS_DISTRIBUTION);
//                w.setCloseAction(RulesWindow.CLOSE);
//                w.setModal(true);
//                w.show(extElement.getDOM());
//            }
//        }, COMMON.ttpOpenLettersDistributionDialog()));
//
//        final FlexTable table = new FlexTable();
//        table.setWidth("100%");
//        table.setStyleName("tiles-info-table");
//        table.setCellPadding(0);
//        table.setCellSpacing(0);
//        table.setBorderWidth(0);
//
//        final FlexTable.FlexCellFormatter formatter = table.getFlexCellFormatter();
//
//        final Map<Integer, List<Tile>> tilesMapByCost = new HashMap<Integer, List<Tile>>();
//        for (final Tile tile : bankTiles) {
//            List<Tile> list = tilesMapByCost.get(tile.getCost());
//            if (list == null) {
//                list = new ArrayList<Tile>();
//                tilesMapByCost.put(tile.getCost(), list);
//            }
//            list.add(tile);
//        }
//
//        int row = 0;
//        for (Map.Entry<Integer, List<Tile>> entry : tilesMapByCost.entrySet()) {
//            final int cost = entry.getKey();
//            final List<Tile> tiles = entry.getValue();
//
//            table.setHTML(row, 0, "" + cost + " " + PB.lblWordsMemoryPoints() + ": ");
//            formatter.setAlignment(row, 0, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_TOP);
//            formatter.setWordWrap(row, 0, false);
//
//            table.setWidget(row, 1, createTilesPanel(tiles));
//            row++;
//        }
//
//        final FlexTable tt = new FlexTable();
//        tt.setStyleName("tiles-info-table");
//
//        final FlexTable.FlexCellFormatter ft = tt.getFlexCellFormatter();
//        row = 0;
//        ft.setColSpan(row, 0, 5);
//        tt.setHTML(row, 0, "<div class=\"separator\"/>");
//
//        row++;
//        tt.setHTML(row, 1, " - " + PB.lblTilesInfoDL());
//        ScribbleBoardPanel.updateCellBonus(ft, row, 0, ScoreBonus.Type.DOUBLE_LETTER);
//
//        tt.setHTML(row, 2, " ");
//        formatter.setWidth(row, 2, "20px");
//
//        tt.setHTML(row, 4, " - " + PB.lblTilesInfoTL());
//        ScribbleBoardPanel.updateCellBonus(ft, row, 3, ScoreBonus.Type.TRIPLE_LETTER);
//
//        row++;
//        tt.setHTML(row, 1, " - " + PB.lblTilesInfoDW());
//        ScribbleBoardPanel.updateCellBonus(ft, row, 0, ScoreBonus.Type.DOUBLE_WORD);
//
//        tt.setHTML(row, 2, " ");
//        ft.setWidth(row, 2, "20px");
//
//        tt.setHTML(row, 4, " - " + PB.lblTilesInfoTW());
//        ScribbleBoardPanel.updateCellBonus(ft, row, 3, ScoreBonus.Type.TRIPLE_WORD);
//
//        add(table);
//        add(tt);
//    }
//
//    private Widget createTilesPanel(List sortedTile) {
//        final FlexTable tt = new FlexTable();
//
//        tt.setCellPadding(0);
//        tt.setCellSpacing(0);
//        tt.setBorderWidth(0);
//
//        final int countInRow = 7;
//
//        int row = 0;
//        int number = 0;
//        while (number < sortedTile.size()) {
//            for (int i = 0; i < countInRow && number < sortedTile.size(); i++) {
//                final Tile tile = (Tile) sortedTile.get(number++);
//
//                final TileWidget tileWidget = new TileWidget(tile, false);
//                tileWidget.getElement().setTitle(MPB.msgTilesInBank(tile.getNumber()));
//                tt.setWidget(row, i, tileWidget);
//            }
//            setHeight(getHeight() + TileWidget.TILE_SIZE);
//            row++;
//        }
//        return tt;
//    }
}
