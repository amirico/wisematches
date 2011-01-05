package wisematches.client.gwt.app.client.content.stats;

import com.smartgwt.client.widgets.Canvas;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class TopPlayersPanel extends Canvas {
//    private Store store;
	private static final int PLAYERS_COUNT = 5;

	public TopPlayersPanel() {
//        initPanel();
	}

/*
    private void initPanel() {
        setLayout(new FitLayout());
        setTitle(APP.lblStatsTop5Player());
        setFrame(true);
        setHeight(210);

        add(createGrid());
        refresh();
    }

    private GridPanel createGrid() {
        final RecordDef recordDef = new RecordDef(
                new FieldDef[]{
                        new ObjectFieldDef("player"),
                        new IntegerFieldDef("rating"),
                }
        );

        store = new Store(new ArrayReader(PLAYERS_COUNT, recordDef));
        for (int i = 0; i < PLAYERS_COUNT; i++) {
            store.add(recordDef.createRecord(new Object[]{null, null}));
        }

        final RowNumberingColumnConfig numbersColumn = new RowNumberingColumnConfig();

        final ColumnConfig playerColumn = new PlayerGridColumnConfig("player", 160, false, false);
        playerColumn.setResizable(false);
        playerColumn.setSortable(false);

        final ColumnConfig ratingColumn = new ColumnConfig(APP.lblRating(), "rating", 60);
        ratingColumn.setResizable(false);
        ratingColumn.setSortable(false);

        final BaseColumnConfig[] columns = new BaseColumnConfig[]{
                numbersColumn,
                playerColumn,
                ratingColumn
        };

        final ColumnModel columnModel = new ColumnModel(columns);
        columnModel.setDefaultSortable(false);

        final GridPanel grid = new GridPanel();
        grid.setAutoHeight(true);
        grid.setStore(store);
        grid.setColumnModel(columnModel);
        grid.setDisableSelection(true);
        grid.setStripeRows(false);
        grid.setTrackMouseOver(false);
        grid.setEnableHdMenu(false);
        grid.setEnableColumnMove(false);
        grid.setEnableColumnResize(false);
        grid.setEnableDragDrop(false);
        grid.setEnableColumnHide(false);

        grid.setLoadMask("Loading Top 5 Players...");

        final GridView view = new GridView();
        view.setForceFit(true);
        view.setScrollOffset(0);

        grid.setView(view);
        return grid;
    }

    public void refresh() {
        final ApplicationStatisticServiceAsync serviceAsync = ApplicationStatisticService.App.getInstance();
        serviceAsync.getTopRatedPlayers(new AsyncCallback<PlayerInfoBean[]>() {
            public void onFailure(Throwable throwable) {
                ExceptionHandler.showSystemError(throwable);
            }

            public void onSuccess(PlayerInfoBean[] playerInfos) {
                final int count = playerInfos.length;
                for (int i = 0; i < PLAYERS_COUNT; i++) {
                    final Record record = store.getAt(i);
                    if (i < count) {
                        final PlayerInfoBean info = playerInfos[i];
                        record.set("player", info);
                        record.set("rating", info.getCurrentRating());
                    } else {
                        record.set("player", (Object) null);
                        record.set("rating", (Object) null);
                    }
                    record.commit();
                }
                store.commitChanges();
            }
        });
    }
*/
}
