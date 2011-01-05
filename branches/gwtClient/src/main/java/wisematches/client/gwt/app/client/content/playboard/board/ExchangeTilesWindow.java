package wisematches.client.gwt.app.client.content.playboard.board;

import com.smartgwt.client.widgets.Window;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ExchangeTilesWindow extends Window {
/*
    private Button exchangeButton;

    private final int tilesInBank;
    private final ScribbleBoard scribbleBoard;
    private final TileSelectionCallback tileSelectionCallback = new TheTileSelectionCallback();

    private final WMErrorMarker errorMarker = new WMErrorMarker();
    private final Set<TileWidget> selectedTiles = new HashSet<TileWidget>();
    private AsyncCallback<int[]> asyncCallback;

    public ExchangeTilesWindow(ScribbleBoard scribbleBoard, int tilesInBank) {
        super(PB.ttlExchangeTiles());

        this.scribbleBoard = scribbleBoard;
        this.tilesInBank = Math.min(tilesInBank, scribbleBoard.getHandTilesCount());

        initWindow();
    }

    private void initWindow() {
        final Tile[] handTiles = scribbleBoard.getHandTiles();
        setWidth(400);
        setModal(true);
        setPlain(false);
        setPaddings(10);
        setResizable(false);
        setCloseAction(CLOSE);

        final Panel p = new Panel();
        p.setBodyStyle("background: transparent;");
        p.setLayout(new VerticalLayout(0));

        final HTML html = new HTML(MAPP.msgExchangeInfo(tilesInBank));
        html.addStyleName("messages-panel");

        final HTML html1 = new HTML(MAPP.lblExchangeMessage());
        html1.addStyleName("messages-panel");

        p.add(html1);
        p.add(createTilesWidget(handTiles));
        p.add(html);

        add(p);

        exchangeButton = new Button(PB.btnExchangeTiles());
        exchangeButton.setDisabled(true);
        exchangeButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject eventObject) {
                setVisible(false);

                int index = 0;
                final int[] tiles = new int[selectedTiles.size()];
                for (TileWidget selectedTile : selectedTiles) {
                    tiles[index++] = selectedTile.getTile().getNumber();
                }
                asyncCallback.onSuccess(tiles);
            }
        });

        final Button cancel = new Button(COMMON.btnCancel());
        cancel.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject eventObject) {
                setVisible(false);
            }
        });

        addButton(cancel);
        addButton(exchangeButton);

        validateErrors();
    }

    private Widget createTilesWidget(Tile[] handTiles) {
        final FlexTable table = new FlexTable();
        final FlexTable.FlexCellFormatter formatter = table.getFlexCellFormatter();

        table.addStyleName("messages-panel");
        table.addStyleName("board-exchange");
        table.setBorderWidth(0);
        table.setCellPadding(0);
        table.setCellSpacing(0);

        final Panel toolbarPanel = new Panel();
        toolbarPanel.setBodyBorder(false);
        toolbarPanel.setBodyStyle("background: transparent;");
        toolbarPanel.setLayout(new HorizontalLayout(5));

        final WMFlatHyperlink allHyperlink = new WMFlatHyperlink(APP.lblAll(), "green-flat-hyperlink");
        allHyperlink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                selectTileWidgets(table, true);
            }
        });
        final WMFlatHyperlink noneHyperlink = new WMFlatHyperlink(APP.lblNone(), "green-flat-hyperlink");
        noneHyperlink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                selectTileWidgets(table, false);
            }
        });

        toolbarPanel.add(allHyperlink);
        toolbarPanel.add(noneHyperlink);

        table.setWidget(0, 0, toolbarPanel);
        formatter.setColSpan(0, 0, handTiles.length);

        for (int i = 0; i < handTiles.length; i++) {
            final Tile handTile = handTiles[i];

            final TileWidget widgetTile = new TileWidget(handTile);
            widgetTile.setSelectionEnabled(true);
            widgetTile.setSelectionCallback(tileSelectionCallback);

            table.setWidget(1, i, widgetTile);
        }

        table.setWidget(1, handTiles.length, errorMarker);

        formatter.setColSpan(2, 0, handTiles.length);
        errorMarker.addStyleName("padding-left-10");
        errorMarker.setInvalidElement(formatter.getElement(2, 0));
        return table;
    }

    private void selectTileWidgets(FlexTable table, final boolean selected) {
        final int count = table.getCellCount(1);
        for (int i = 0; i < count - 1; i++) {
            final TileWidget tw = (TileWidget) table.getWidget(1, i);
            tw.setSelected(selected);
        }
    }

    private void validateErrors() {
        final int count = selectedTiles.size();
        if (count == 0) {
            errorMarker.showInvalidMessage(MAPP.msgNoExchangeTilesSelected());
            exchangeButton.setDisabled(true);
        } else if (count > tilesInBank) {
            errorMarker.showInvalidMessage(MAPP.msgTooManeExchangeTilesSelected(count, tilesInBank));
            exchangeButton.setDisabled(true);
        } else {
            errorMarker.hideInvalidMessage();
            exchangeButton.setDisabled(false);
        }
    }

    public void doExchange(AsyncCallback<int[]> asyncCallback) {
        this.asyncCallback = asyncCallback;
        setVisible(true);
    }

    private class TheTileSelectionCallback implements TileSelectionCallback {
        public void tileSelected(TileWidget tileWidget, boolean selected) {
            if (selected) {
                selectedTiles.add(tileWidget);
            } else {
                selectedTiles.remove(tileWidget);
            }
            validateErrors();
        }
    }
*/
}
