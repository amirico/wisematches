package wisematches.client.gwt.app.client.content.profile;

import com.google.gwt.user.client.ui.FlexTable;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GamesInfoPanel extends FlexTable {
/*
    private Record ratingRecord;

    private static final String RATING_FIELD = "rating";
    private static final String PLACE_FIELD = "place";
    private static final String WONS_FIELD = "wons";
    private static final String LOST_FIELD = "lost";
    private static final String DRAWN_FIELD = "drawn";
    private static final String TOTAL_FIELD = "total";

    private static final DateTimeFormat DATE_TIME_FORMAT = DateTimeFormat.getFormat("MMM-yyyy");

    public GamesInfoPanel() {
        initPanel();
    }

    private void initPanel() {
        final Widget gridPanel = crateGridPanel();

        final FlexTable.FlexCellFormatter formatter = getFlexCellFormatter();

        setBorderWidth(0);
        setCellPadding(0);
        setCellSpacing(0);
        setStyleName("board-info-panel");

        setPlayerInfo(new PlayerInfoBean());
        formatter.setWordWrap(0, 0, false);
        formatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);

        setHTML(0, 1, "&nbsp;");

        setRegistredTime(0);
        formatter.setWordWrap(0, 2, false);
        formatter.setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_RIGHT);

        setHTML(0, 3, "&nbsp;");
        formatter.setRowSpan(0, 3, 2);
        formatter.setHorizontalAlignment(0, 3, HasHorizontalAlignment.ALIGN_LEFT);
        formatter.setVerticalAlignment(0, 3, HasVerticalAlignment.ALIGN_TOP);

        setHTML(0, 4, "&nbsp;");
        formatter.setWidth(0, 4, "100%");
        formatter.setRowSpan(0, 4, 2);

        setWidget(1, 0, gridPanel);
        formatter.setColSpan(1, 0, 3);
        formatter.setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
        formatter.setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_LEFT);
    }

    private Widget crateGridPanel() {
        final Panel panel = new Panel();
        panel.setWidth(605);
        panel.setBodyBorder(false);
        panel.setLayout(new ColumnLayout());

        final Panel currentRating = new Panel(getHeader(APP.lblCurrentRating(), null), "");
        currentRating.setWidth(200);

        final Panel wonGames = new Panel(getHeader(APP.lblWonGames(), "#00FF00"), "");
        wonGames.setWidth(100);

        final Panel lostGames = new Panel(getHeader(APP.lblLostGames(), "#FF0000"), "");
        lostGames.setWidth(100);

        final Panel drawnGames = new Panel(getHeader(APP.lblDrawnGames(), "#0000FF"), "");
        drawnGames.setWidth(100);

        final Panel totalGames = new Panel(getHeader(APP.lblTotalGames(), null), "");
        totalGames.setWidth(100);

        final RecordDef recordDef = new RecordDef(
                new FieldDef[]{
                        new IntegerFieldDef(RATING_FIELD),
                        new IntegerFieldDef(PLACE_FIELD),
                        new IntegerFieldDef(WONS_FIELD),
                        new IntegerFieldDef(LOST_FIELD),
                        new IntegerFieldDef(DRAWN_FIELD),
                        new IntegerFieldDef(TOTAL_FIELD)
                }
        );

        final Store store = new Store(recordDef);
        ratingRecord = recordDef.createRecord(new Object[]{
                0, 0, 0, 0, 0, 0
        });
        store.add(ratingRecord);

        store.addStoreListener(new StoreListenerAdapter() {
            @Override
            public void onUpdate(Store store, Record record, Record.Operation operation) {
                currentRating.setHtml(getTableCellValue(record.getAsString(RATING_FIELD), record.getAsString(PLACE_FIELD) +
                        APP.lblPlaceInRating()));

                wonGames.setHtml(getTableCellValue(getRecordValue(WONS_FIELD, record), null));
                lostGames.setHtml(getTableCellValue(getRecordValue(LOST_FIELD, record), null));
                drawnGames.setHtml(getTableCellValue(getRecordValue(DRAWN_FIELD, record), null));
                totalGames.setHtml(getTableCellValue(record.getAsString(TOTAL_FIELD), null));
            }
        });

        panel.add(currentRating);
        panel.add(wonGames);
        panel.add(lostGames);
        panel.add(drawnGames);
        panel.add(totalGames);

        return panel;
    }

    private String getHeader(String name, String color) {
        if (color == null) {
            return name;
        }
        return "<div><div style=\"float: left;\">" + name + "</div><div style=\"width: 5px; height: 5px; float: right; background: " + color + ";\"/></div>";
    }

    private String getRecordValue(String fieldName, Record record) {
        final int games = record.getAsInteger(fieldName);
        final int total = record.getAsInteger(TOTAL_FIELD);
        int percents = 0;
        if (total != 0) {
            percents = games * 100 / total;
        }
        return games + " (" + percents + "%)";
    }

    private String getTableCellValue(String value1, String value2) {
        String v = value2;
        if (value2 == null || value2.length() == 0) {
            v = "&nbsp;";
        }

        return "<div class=\"rating-table-cell\">" + value1 +
                "</div><div class=\"rating-table-cell rating-table-position\">" + v +
                "</div>";
    }

    private void setPlayerInfo(PlayerInfoBean bean) {
        setHTML(0, 0, APP.lblProfilePlayerName() + " <b>" + bean.getPlayerName() + "</b>");
    }

    private void setRegistredTime(long time) {
        setHTML(0, 2, APP.lblProfileRegistredFrom() + ": " + DATE_TIME_FORMAT.format(new Date(time)));
    }

    public void setPlayerProfileBean(PlayerProfileBean bean) {
        final PlayerInfoBean pb = bean.getPlayerInfoBean();
        setPlayerInfo(pb);
        setRegistredTime(bean.getRegistredFrom());

        ratingRecord.set(RATING_FIELD, pb.getCurrentRating());
        ratingRecord.set(PLACE_FIELD, bean.getPlace());
        ratingRecord.set(WONS_FIELD, bean.getWonGames());
        ratingRecord.set(LOST_FIELD, bean.getLostGames());
        ratingRecord.set(DRAWN_FIELD, bean.getDrawGames());
        ratingRecord.set(TOTAL_FIELD, bean.getTotalGames());
        ratingRecord.commit();

        setHTML(0, 3, "<img src=\"" + getCharUrl(bean) + "\"/>");
    }

    private String getCharUrl(PlayerProfileBean bean) {
        return "http://chart.apis.google.com/chart?cht=p3&chd=t:" +
                bean.getWonGames() + "," + bean.getLostGames() + "," + bean.getDrawGames() +
                "&chs=300x100&chco=00FF00,FF0000,0000FF&chl=" +
                APP.lblWins() + "|" + APP.lblLosses() + "|" + APP.lblDraws();
    }
*/
}
