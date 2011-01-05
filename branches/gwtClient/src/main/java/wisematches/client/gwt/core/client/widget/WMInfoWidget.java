package wisematches.client.gwt.core.client.widget;

import com.smartgwt.client.widgets.Canvas;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class WMInfoWidget extends Canvas {
//    protected FlexTable table;
//
//    private final boolean inverted;
//
//    private Map<String, Integer> integerMap = new HashMap<String, Integer>();

	public WMInfoWidget(String title) {
		super(title);
	}

/*
    public WMInfoWidget(String title, boolean inverted) {
        this.inverted = inverted;
        setTitle(title);

        setLayout(new FitLayout());
        setFrame(true);

        setCollapsible(true);
        setAnimCollapse(true);

        table = new FlexTable();
        table.getFlexCellFormatter().setWidth(0, 1, "100%");
        table.setCellPadding(0);
        table.setCellSpacing(0);
        table.setBorderWidth(0);
        table.addStyleName("board-info-panel");

        add(table);
    }

    public void addInfoItem(String id, Widget widget) {
        final int row = table.getRowCount();
        integerMap.put(id, row);
        table.getFlexCellFormatter().setWordWrap(row, 0, false);
        table.getFlexCellFormatter().setColSpan(row, 0, 2);
        table.setWidget(row, 0, widget);
    }

    public void addInfoItem(String id, String label, Widget widget) {
        final int row = table.getRowCount();
        integerMap.put(id, row);
        table.getFlexCellFormatter().setWordWrap(row, 0, false);
        table.getFlexCellFormatter().setStyleName(row, 0, "label");
        table.setHTML(row, inverted ? 1 : 0, label + (inverted ? "" : ": "));
        table.setWidget(row, inverted ? 0 : 1, widget);
    }

    public void addInfoItem(String id, String label, String html) {
        final int row = table.getRowCount();
        integerMap.put(id, row);
        table.getFlexCellFormatter().setWordWrap(row, 0, false);
        table.getFlexCellFormatter().setStyleName(row, 0, "label");
        table.setHTML(row, inverted ? 1 : 0, label + (inverted ? "" : ": "));
        table.setHTML(row, inverted ? 0 : 1, html);
    }

    public void addSeparator() {
        final int row = table.getRowCount();
        table.getFlexCellFormatter().setColSpan(row, 0, 2);
        table.setHTML(row, 0, "<div class=\"separator\"/>");
    }

    public void setInfoValue(String id, String html) {
        final Integer integer = integerMap.get(id);
        table.getFlexCellFormatter().setStyleName(integer, 1, "value");
        table.setHTML(integer, 1, html);
    }

    public void setInfoValue(String id, int value) {
        setInfoValue(id, String.valueOf(value));
    }

    public void setInfoValue(String id, long value) {
        setInfoValue(id, String.valueOf(value));
    }
*/
}
