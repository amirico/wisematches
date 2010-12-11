package wisematches.client.gwt.app.client.content.gameboard;

import com.smartgwt.client.widgets.Canvas;
import wisematches.client.gwt.app.client.ApplicationFrame;
import wisematches.client.gwt.app.client.ApplicationFrameView;
import wisematches.client.gwt.app.client.Parameters;
import wisematches.client.gwt.app.client.content.dashboard.OpenBoardCallback;
import wisematches.client.gwt.app.client.content.player.PlayerSessionTool;
import wisematches.client.gwt.app.client.content.profile.PlayerStatisticPanel;
import wisematches.client.gwt.app.client.content.profile.services.PlayerProfileService;
import wisematches.client.gwt.app.client.content.profile.services.PlayerProfileServiceAsync;
import wisematches.client.gwt.app.client.events.EventsDispatcher;
import wisematches.client.gwt.app.client.events.EventsDispatcherTool;
import wisematches.client.gwt.app.client.settings.ParameterInfo;
import wisematches.client.gwt.app.client.settings.Settings;
import wisematches.client.gwt.app.client.widget.refresh.RefreshButton;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GameboardWidget extends Canvas implements ApplicationFrameView {
	private Settings frameViewSettings;
	private ApplicationFrame applicationFrame;

	private GameboardTool gameboardTool;
	private EventsDispatcher eventsDispatcher;
	private PlayerSessionTool playerSessionTool;
	private OpenBoardCallback openBoardCallback;

	private RefreshButton refreshButton;

	private GameboardTableView gameboardTableView;
	private PlayerStatisticPanel playerStatisticPanel;

	private static final PlayerProfileServiceAsync playerStatisticService = PlayerProfileService.App.getInstance();

	private static final int WEST_PANEL_WIDTH = 250;

	public static final String ITEM_ID = "gameboard";

	public void initialize(ApplicationFrame applicationFrame, Settings frameViewSettings) {
		this.applicationFrame = applicationFrame;
		this.frameViewSettings = frameViewSettings;

		eventsDispatcher = applicationFrame.getApplicationTool(EventsDispatcherTool.class).getEventsDispatcher();
		playerSessionTool = applicationFrame.getApplicationTool(PlayerSessionTool.class);
		gameboardTool = applicationFrame.getApplicationTool(GameboardTool.class);

		openBoardCallback = new OpenBoardCallback(applicationFrame);

//        initPanel();
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

//    public Canvas getFrameViewComponent() {
//        return this;
//    }
//
//    public ParameterInfo[] getParametersInfos() {
//        return new ParameterInfo[]{
//                new ParameterInfo("refresh.interval", "ri", int.class, 15, false),
//
//                //Following parameters taken from GameboardTables
//                new ParameterInfo("gameboard.orders", "to", int[].class, true),
//                new ParameterInfo("gameboard.hiddens", "th", boolean[].class, true),
//                new ParameterInfo("gameboard.widths", "tw", int[].class, false)
//        };
//    }
//
//
//    public void activate(Parameters parameters) {
//        refreshButton.activate();
//        playerStatisticPanel.refresh();
//    }
//
//    public void deactivate() {
//        refreshButton.deactivate();
//    }
//
//
//    private void initPanel() {
//        setBodyBorder(false);
//        setFrame(false);
//
//        final BorderLayout borderLayout = new BorderLayout();
//        setLayout(borderLayout);
//        setBodyStyle("background: transparent;");
//
//        add(createWestPanel(), new BorderLayoutData(RegionPosition.WEST, new Margins(5, 5, 5, 5)));
//        add(createCenterPanel(), new BorderLayoutData(RegionPosition.CENTER, new Margins(5, 5, 5, 5)));
//        add(createEastPanel(), new BorderLayoutData(RegionPosition.EAST, new Margins(5, 5, 5, 5)));
//
//    }
//
//
//    private Widget createWestPanel() {
//        final Panel p = new Panel();
//        p.setLayout(new VerticalLayout(10));
//        p.setWidth(WEST_PANEL_WIDTH);
//        p.setHeight(100);
//        p.setFrame(false);
//        p.setBodyBorder(false);
//
//        playerStatisticPanel = new PlayerStatisticPanel(playerSessionTool.getCurrentPlayer(), eventsDispatcher, playerStatisticService);
//        playerStatisticPanel.setWidth(WEST_PANEL_WIDTH);
//
//        p.add(playerStatisticPanel);
//        return p;
//    }
//
//    private Widget createCenterPanel() {
//        final GameboardTableView gameboardTable = gameboardTool.createActiveGamesView(frameViewSettings, "createNewGame");
//        final Panel panel = gameboardTable.getViewComponent();
//        panel.setFrame(true);
//        panel.setTitle(APP.lblGameboardTitle());
//
//        gameboardTableView = gameboardTable;
//
//        final Toolbar tb = new Toolbar();
//        panel.setTopToolbar(tb);
//
//        final ToolbarButton createButton = new ToolbarButton(APP.lblCreateGame());
//        createButton.setIcon(GWT.getModuleBaseURL() + "images/dashboard/gameCreate.png");
//        createButton.setTooltip(APP.ttpCreateGame());
//        createButton.addListener(new ButtonListenerAdapter() {
//            public void onClick(Button button, EventObject eventObject) {
//                createNewGame();
//            }
//        });
//
//        final ToolbarButton openButton = new ToolbarButton(APP.lblOpenGames());
//        openButton.setIcon(GWT.getModuleBaseURL() + "images/dashboard/gameOpen.png");
//        openButton.setTooltip(APP.ttpOpenGames());
//        openButton.disable();
//        openButton.addListener(new ButtonListenerAdapter() {
//            public void onClick(Button button, EventObject eventObject) {
//                openSelectedGames();
//            }
//        });
//
//        gameboardTable.addGameboardSelectionListener(new GameboardSelectionListener() {
//            public void gameboardSelected(GameboardItemBean bean) {
//                openButton.setDisabled(bean == null);
//            }
//        });
//
//        refreshButton = new RefreshButton();
//        refreshButton.setIcon(GWT.getModuleBaseURL() + "images/dashboard/gameRefresh.png");
//        refreshButton.setRefreshInterval(frameViewSettings.getInt("refresh.interval"));
//        refreshButton.addRefreshButtonListener(new RefreshButtonListener() {
//            public void refreshIntervalChanged(int oldInterval, int newInterval) {
//                frameViewSettings.setInt("refresh.interval", newInterval);
//            }
//
//            public void refreshData(boolean handRefresh) {
//                eventsDispatcher.requestEvents();
//            }
//        });
//
//        tb.addButton(createButton);
//        tb.addSpacer();
//        tb.addButton(openButton);
//        tb.addFill();
//        tb.addButton(refreshButton);
//
//        return panel;
//    }
//
//    private Widget createEastPanel() {
//        final Panel sponsorsWidget = new Panel();
//        sponsorsWidget.setFrame(false);
//        sponsorsWidget.setBodyBorder(false);
//        sponsorsWidget.setWidth(SponsorsBlockType.GAMEBOARD.getWidth());
//        sponsorsWidget.setHeight(SponsorsBlockType.GAMEBOARD.getHeight());
//        sponsorsWidget.add(new SponsorsBlockWidget(SponsorsBlockType.GAMEBOARD));
//        return sponsorsWidget;
//    }
//
//
//    private void createNewGame() {
//        PageTracker.trackPageVisit("app/gameboard/create");
//
//        new CreateGameDialog(playerSessionTool).showCreateDialog(openBoardCallback);
//    }
//
//    private void openSelectedGames() {
//        PageTracker.trackPageVisit("app/gameboard/open");
//
//        final long[] longs = gameboardTableView.getSelectedBoards();
//
//        for (long boardId : longs) {
//            final String parameter = PlayboardWidget.BOARD_ID_PARAMETER + "=" + boardId;
//            applicationFrame.activate(PlayboardWidget.ITEM_ID, Parameters.decode(parameter));
//        }
//    }
}