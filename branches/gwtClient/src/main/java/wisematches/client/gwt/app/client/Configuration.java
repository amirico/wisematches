package wisematches.client.gwt.app.client;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.FieldType;
import wisematches.client.gwt.app.client.content.SwitchFrameViewTool;
import wisematches.client.gwt.app.client.content.dashboard.DashboardTool;
import wisematches.client.gwt.app.client.content.dashboard.DashboardWidget;
import wisematches.client.gwt.app.client.content.gameboard.GameboardTool;
import wisematches.client.gwt.app.client.content.gameboard.GameboardWidget;
import wisematches.client.gwt.app.client.content.playboard.PlayboardTool;
import wisematches.client.gwt.app.client.content.player.LockMonitoringTool;
import wisematches.client.gwt.app.client.content.player.PlayerSessionTool;
import wisematches.client.gwt.app.client.content.player.PlayerTooltipTool;
import wisematches.client.gwt.app.client.content.profile.PlayerProfileTool;
import wisematches.client.gwt.app.client.events.EventsDispatcherTool;
import wisematches.client.gwt.app.client.settings.SettingsManagerTool;

import static wisematches.client.gwt.app.client.content.i18n.AppRes.APP;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class Configuration {
	private final DataSource store;
	private final DataSource applicationTools;

	public Configuration() {
		store = loadItemsStore();
		applicationTools = loadToolsStore();
	}

	private DataSource loadToolsStore() {
		final ApplicationTool[] tools = getApplicationToolsArray();
		final Object[][] data = new Object[tools.length][2];
		for (int i = 0; i < tools.length; i++) {
			ApplicationTool tool = tools[i];
			data[i][0] = tool.getClass().getName();
			data[i][1] = tool;
		}

		final DataSource ds = new DataSource();
		ds.setFields(new DataSourceTextField("id"), new DataSourceField("tool", FieldType.BINARY));
		ds.addData(new Record());
		return ds;
	}

	private DataSource loadItemsStore() {
/*
        final Object[][] data = getApplicationItemsArray();
        final MemoryProxy proxy = new MemoryProxy(data);

        final RecordDef recordDef = new RecordDef(
                new FieldDef[]{
                        new StringFieldDef("id"),
                        new StringFieldDef("name"),
                        new ObjectFieldDef("item"),
                        new BooleanFieldDef("visible"),
                }
        );

        final ArrayReader reader = new ArrayReader(0, recordDef);
        final Store store = new Store(proxy, reader);
        store.load();
        return store;
*/
		return null;
	}

	/**
	 * Returns store that contains intformation about all {@code ApplicationTool}s objects.
	 * <p/>
	 * Returned store contains records with following fields:
	 * <ol>
	 * <li><strong>id</strong> - <strong>String</strong> type. Contains id of item.
	 * <li><strong>tool</strong> - <strong>ApplicationTool</strong> type. The object that implements {@code ApplicationTool} interface
	 * </ol>
	 *
	 * @return the store that contains information about {@code ApplicationTool}s.
	 * @see ApplicationTool
	 */
	public DataSource getApplicationTools() {
		return applicationTools;
	}

	/**
	 * Returns store that contains intformation about all {@code ApplicationFrameView}s objects.
	 * <p/>
	 * Returned store contains records with following fields:
	 * <ol>
	 * <li><strong>id</strong> - <strong>String</strong> type. Contains id of item.
	 * <li><strong>name</strong> - <strong>String</strong> type. Contains display name of item.
	 * <li><strong>item</strong> - <strong>ApplicationFrameView</strong> type. The object that implements {@code ApplicationFrameView} interface
	 * <li><strong>visible</strong> - <strong>boolean</strong> type. Indicates should this item be added into top menu panel or not.
	 * </ol>
	 *
	 * @return the store that contains information about {@code ApplicationFrameView}s.
	 * @see ApplicationFrameView
	 */
	public DataSource getApplicationItems() {
		return store;
	}

	private Object[][] getApplicationItemsArray() {
		/**
		 * WARNING: Adding items to this array don't forget to create new column in 'user_settings' table!!!
		 */
		return new Object[][]{
				new Object[]{DashboardWidget.ITEM_ID, APP.lblItemDashboard(), new DashboardWidget(), Boolean.TRUE},
				new Object[]{GameboardWidget.ITEM_ID, APP.lblItemGameboard(), new GameboardWidget(), Boolean.TRUE},
//				new Object[]{PlayboardWidget.ITEM_ID, APP.lblItemPlayboard(), new PlayboardWidget(), Boolean.TRUE},
//				new Object[]{PlayerProfileWidget.ITEM_ID, APP.lblItemProfile(), new PlayerProfileWidget(), Boolean.TRUE},
//				new Object[]{LoggingWidget.ITEM_ID, "Logging", new LoggingWidget(), Boolean.FALSE},
		};
	}


	private ApplicationTool[] getApplicationToolsArray() {
		return new ApplicationTool[]{
				new PlayerSessionTool(),
				new EventsDispatcherTool(),
				new SwitchFrameViewTool(),
				new PlayerTooltipTool(),
				new PlayerProfileTool(),
				new LockMonitoringTool(),
				new SettingsManagerTool(),
				new DashboardTool(),
				new PlayboardTool(),
				new GameboardTool(),
		};
	}
}
