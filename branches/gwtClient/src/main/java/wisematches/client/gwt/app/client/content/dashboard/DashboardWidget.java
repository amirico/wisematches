package wisematches.client.gwt.app.client.content.dashboard;

import com.smartgwt.client.widgets.Canvas;
import wisematches.client.gwt.app.client.ApplicationFrame;
import wisematches.client.gwt.app.client.ApplicationFrameView;
import wisematches.client.gwt.app.client.Parameters;
import wisematches.client.gwt.app.client.content.player.PlayerSessionTool;
import wisematches.client.gwt.app.client.content.profile.PlayerStatisticPanel;
import wisematches.client.gwt.app.client.content.profile.services.PlayerProfileService;
import wisematches.client.gwt.app.client.content.profile.services.PlayerProfileServiceAsync;
import wisematches.client.gwt.app.client.content.stats.TopPlayersPanel;
import wisematches.client.gwt.app.client.content.stats.TotalsStatisticPanel;
import wisematches.client.gwt.app.client.content.stats.services.ApplicationStatisticService;
import wisematches.client.gwt.app.client.content.stats.services.ApplicationStatisticServiceAsync;
import wisematches.client.gwt.app.client.events.EventsDispatcher;
import wisematches.client.gwt.app.client.settings.ParameterInfo;
import wisematches.client.gwt.app.client.settings.Settings;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class DashboardWidget extends Canvas implements ApplicationFrameView {
	private TopPlayersPanel topPlayersPanel;
	private TotalsStatisticPanel totalsStatisticPanel;
	private PlayerStatisticPanel playerStatisticPanel;

	private Settings settings;
	private EventsDispatcher eventsDispatcher;
	private PlayerSessionTool playerSessionTool;

	private final PlayerProfileServiceAsync playerProfileService = PlayerProfileService.App.getInstance();
	private final ApplicationStatisticServiceAsync applicationStatisticService = ApplicationStatisticService.App.getInstance();

	public static final String ITEM_ID = "dashboard";

	private static final int WEST_PANEL_WIDTH = 300;
	private static final int EAST_PANEL_WIDTH = 250;

	private DashboardTable dashboardTable;
	private OpenBoardCallback openBoardCallback;

	public DashboardWidget() {
	}
/*
    public void initialize(ApplicationFrame applicationFrame, Settings frameViewSettings) {
        settings = frameViewSettings;

        final EventsDispatcherTool tool = applicationFrame.getApplicationTool(EventsDispatcherTool.class);
        eventsDispatcher = tool.getEventsDispatcher();

        playerSessionTool = applicationFrame.getApplicationTool(PlayerSessionTool.class);
        openBoardCallback = new OpenBoardCallback(applicationFrame);

        setBodyBorder(false);
        setFrame(false);

        final BorderLayout borderLayout = new BorderLayout();
        setLayout(borderLayout);
        setBodyStyle("background: transparent;");

        add(createWestPanel(), new BorderLayoutData(RegionPosition.WEST, new Margins(5, 5, 5, 5)));
        add(createGridPanel(), new BorderLayoutData(RegionPosition.CENTER, new Margins(5, 5, 5, 5)));
        add(createEastPanel(), new BorderLayoutData(RegionPosition.EAST, new Margins(5, 5, 5, 5)));
    }

    private Widget createWestPanel() {
        final Panel p = new Panel();
        p.setLayout(new BorderLayout());
        p.setWidth(WEST_PANEL_WIDTH);
        p.setHeight(100);
        p.setFrame(false);
        p.setBodyBorder(false);
        p.setBodyStyle("background: transparent;");

        topPlayersPanel = new TopPlayersPanel();
        topPlayersPanel.setWidth(WEST_PANEL_WIDTH);

        final Panel sponsorsWidget = new Panel();
        sponsorsWidget.setFrame(false);
        sponsorsWidget.setBodyBorder(false);
        sponsorsWidget.add(new SponsorsBlockWidget(SponsorsBlockType.DASHBOARD));
        sponsorsWidget.setWidth(SponsorsBlockType.DASHBOARD.getWidth());
        sponsorsWidget.setHeight(SponsorsBlockType.DASHBOARD.getHeight());

        p.add(topPlayersPanel, new BorderLayoutData(RegionPosition.NORTH));
        p.add(sponsorsWidget, new BorderLayoutData(RegionPosition.CENTER, new Margins(15, 0, 0, 0)));
        return p;
    }

    private Widget createEastPanel() {
        final Panel p = new Panel();
        p.setLayout(new VerticalLayout(10));
        p.setWidth(EAST_PANEL_WIDTH);
        p.setHeight(100);
        p.setFrame(false);
        p.setBodyBorder(false);

        totalsStatisticPanel = new TotalsStatisticPanel(applicationStatisticService);
        totalsStatisticPanel.setWidth(EAST_PANEL_WIDTH);

        playerStatisticPanel = new PlayerStatisticPanel(playerSessionTool.getCurrentPlayer(), eventsDispatcher, playerProfileService);
        playerStatisticPanel.setWidth(EAST_PANEL_WIDTH);

        p.add(totalsStatisticPanel);
        p.add(playerStatisticPanel);
        return p;
    }

    private Widget createGridPanel() {
        dashboardTable = new DashboardTable(playerSessionTool, openBoardCallback, eventsDispatcher, settings);
        return dashboardTable;
    }

    public Component getFrameViewComponent() {
        return this;
    }

    public ParameterInfo[] getParametersInfos() {
        return new ParameterInfo[]{
                new ParameterInfo("refresh.interval", "ri", int.class, 15, false),

                //Following parameters taken from DashboardTables
                new ParameterInfo("dashboard.orders", "to", int[].class, true),
                new ParameterInfo("dashboard.hiddens", "th", boolean[].class, true),
                new ParameterInfo("dashboard.widths", "tw", int[].class, false)
        };
    }

    public void activate(Parameters parameters) {
        topPlayersPanel.refresh();
        totalsStatisticPanel.refresh();
        playerStatisticPanel.refresh();

        dashboardTable.activate();
    }

    public void deactivate() {
        dashboardTable.deactivate();
    }

    void createNewGame() {
        dashboardTable.createNewGame();
    }

    void joinToGame(final String boardId) {
        dashboardTable.joinToGame(boardId);
    }*/

	@Override
	public void initialize(ApplicationFrame applicationFrame, Settings frameViewSettings) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Canvas getFrameViewComponent() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public ParameterInfo[] getParametersInfos() {
		return new ParameterInfo[0];  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void activate(Parameters parameters) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void deactivate() {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}