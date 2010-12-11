package wisematches.client.gwt.app.client;

import com.google.gwt.core.client.EntryPoint;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class Application implements EntryPoint, ApplicationFrame {
/*    private final Panel bodyPanel = new Panel();
    private final TopMenuBar topMenuBar = new TopMenuBar();
    private final Configuration configuration = new Configuration();
    private final AlarmFacilityWidget notificationWidget = AlarmFacilityWidget.getInstance();

    private SettingsManagerTool settingsManager;
    private ApplicationFrameView activeFrameView = null;

    private final ExceptionHandler exceptionHandler = new ExceptionHandler();

    public void onModuleLoad() {
        GWT.setUncaughtExceptionHandler(exceptionHandler);

        History.addValueChangeHandler(new TheHistoryHandler());

        initialize();
    }

    private void initSettingsManager() {
        settingsManager = getApplicationTool(SettingsManagerTool.class);
    }

    private void initialize() {
        final Store store = getApplicationTools();
        final Record[] records = store.getRecords();
        for (Record record : records) {
            ApplicationTool applicationTool = (ApplicationTool) record.getAsObject("tool");
            applicationTool.registerJSCallbacks();
        }

        initializeApplicationTool(records, 0);
    }

    private void initializeApplicationTool(final Record[] tools, final int currentIndex) {
        if (currentIndex == tools.length) {
            final PlayerSessionTool pst = getApplicationTool(PlayerSessionTool.class);
            exceptionHandler.setCurrentPlayerId(pst.getCurrentPlayer());

            initSettingsManager();
            initUI();
            return;
        }

        final Record record = tools[currentIndex];
        ApplicationTool applicationTool = (ApplicationTool) record.getAsObject("tool");
        applicationTool.initializeTool(this, new ToolReadyCallback() {
            public void toolReady(ApplicationTool tool) {
                initializeApplicationTool(tools, currentIndex + 1);
            }
        });
    }

    private void initUI() {
        Panel mainPanel = new Panel();
        mainPanel.setLayout(new BorderLayout());

        initBodyPanel();

        mainPanel.add(createHeaderPanel(), new BorderLayoutData(RegionPosition.NORTH));
        mainPanel.add(bodyPanel, new BorderLayoutData(RegionPosition.CENTER));

        mainPanel.setBodyBorder(false);
        mainPanel.setBorder(false);

        new Viewport(mainPanel);

        final String s = History.getToken();
        if (s.length() == 0) {
            activate(GameboardWidget.ITEM_ID, null);
        } else {
            History.fireCurrentHistoryState();
        }

        final PlayerInfoBean pib = getApplicationTool(PlayerSessionTool.class).getPlayerInfoBean();
        final boolean guestAccount = pib.getMemberType() == MemberType.GUEST;

        PageTracker.trackPageVisit("/app/signin?guest=" + guestAccount);

        if (guestAccount) {
            DeferredCommand.addCommand(new Command() {
                public void execute() {
                    MessagesBox.showMessage(APP.ttlGuestAccount(), APP.lblGuestAccount());
                }
            });
        }
    }

    private void initBodyPanel() {
        bodyPanel.setLayout(new CardLayout());
        bodyPanel.setBodyBorder(false);
        bodyPanel.setWidth(600);
        bodyPanel.setHeight(10);
    }

    private Widget createHeaderPanel() {
        final FlexTable t = new FlexTable();
        final FlexTable.FlexCellFormatter formatter = t.getFlexCellFormatter();
        t.setBorderWidth(0);
        t.setCellPadding(0);
        t.setCellSpacing(0);

        t.setWidget(0, 0, createLogoWidget());
        formatter.setRowSpan(0, 0, 2);

        t.setWidget(0, 1, createMenuWidget());

        t.setWidget(1, 0, notificationWidget);

        formatter.setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
        formatter.setWidth(0, 1, "100%");
        formatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);

        formatter.setWidth(1, 0, "100%");

        Panel p = new Panel();
        p.setLayout(new FitLayout());
        p.setBodyBorder(false);
        p.setBorder(false);
        p.setHeight(72);
        p.setWidth(100);

        p.add(t);

        return p;
    }

    private Widget createMenuWidget() {
        final Store store = getApplicationFrameViews();
        final Record[] records = store.getRecords();
        for (Record record : records) {
            final String id = record.getAsString("id");
            final String name = record.getAsString("name");
            final boolean visible = record.getAsBoolean("visible");

            if (visible) {
                final Hyperlink link = new WMHyperlink(name, false, id);
                link.addClickHandler(new ClickHandler() {
                    public void onClick(ClickEvent clickEvent) {
                        activate(id, null);
                    }
                });

                record.set("link", link);
                topMenuBar.addMainHyperlink(link);
            }
        }

        final Hyperlink reportBug = new WMHyperlink(COMMON.btnReportBug());
        reportBug.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                final ProblemsReportWindow pw = new ProblemsReportWindow();
                pw.setCloseAction(ProblemsReportWindow.CLOSE);
                pw.show(reportBug.getElement());
            }
        });

        topMenuBar.addMainHyperlink(reportBug, false);

        final Hyperlink signOut = new WMHyperlink(APP.lblItemSignout());
        signOut.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                logout();
            }
        });
        topMenuBar.addMainHyperlink(signOut, false);

        return topMenuBar;
    }

    private Widget createLogoWidget() {
        final Image logoImage = new Image(GWT.getModuleBaseURL() + "/images/logo.png");
        final Hyperlink logo = new WMImageExternalHyperlink(logoImage, "/app");
        logo.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                Window.Location.assign("/app");
            }
        });
        return logo;
    }

    @SuppressWarnings("unchecked")
    public <T extends ApplicationFrameView> T getApplicationFrameView(String frameViewId) {
        return (T) getApplicationFrameViews().getById(frameViewId).getAsObject("item");
    }

    @SuppressWarnings("unchecked")
    public <T extends ApplicationTool> T getApplicationTool(Class<? extends T> type) {
        final Record id = getApplicationTools().getById(type.getName());
        return (T) id.getAsObject("tool");
    }

    public void activate(final String frameViewId, final Parameters parameters) {
        activateImpl(frameViewId, parameters, false);
    }

    private void activateImpl(final String frameViewId, final Parameters parameters, boolean fromHistory) {
        if (!fromHistory) {
            History.newItem(frameViewId + (parameters == null ? "" : "?" + parameters.encode()), false);
        }
        PageTracker.trackPageVisit("app/" + frameViewId);

        final Component component = bodyPanel.findByID(frameViewId);
        if (component == null) {
            final Record record = getApplicationFrameViews().getById(frameViewId);
            bodyPanel.getEl().mask(MAPP.lblLoadingFrameView(record.getAsString("name")), true);

            settingsManager.loadSettings(frameViewId, new AsyncCallback<Settings>() {
                public void onFailure(Throwable throwable) {
                    ExceptionHandler.showSystemError(throwable);
                }

                public void onSuccess(Settings settings) {
                    final Component frameViewItem = createFrameViewItem(settings, frameViewId);
                    activateFrameViewItem(frameViewId, parameters);
                    if (frameViewItem instanceof Container) {
                        ((Container) frameViewItem).doLayout();
                    }
                    bodyPanel.getEl().unmask();
                }
            });
        } else {
            activateFrameViewItem(frameViewId, parameters);
        }
    }

    public ApplicationFrameView getActiveFrameView() {
        return activeFrameView;
    }

    private Store getApplicationTools() {
        return configuration.getApplicationTools();
    }

    private Store getApplicationFrameViews() {
        return configuration.getApplicationItems();
    }

    private Component createFrameViewItem(Settings settings, String frameViewId) {
        final Store store = getApplicationFrameViews();
        final Record record = store.getById(frameViewId);

        final ApplicationFrameView item = (ApplicationFrameView) record.getAsObject("item");
        item.initialize(this, settings);

        final Component widget = item.getFrameViewComponent();
        widget.setId(frameViewId);
        bodyPanel.add(widget);
        return widget;
    }

    private void activateFrameViewItem(String frameViewId, Parameters parameters) {
        if (this.activeFrameView != null) {
            this.activeFrameView.deactivate();
        }

        bodyPanel.setActiveItemID(frameViewId);

        final Store applicationFrameViews = getApplicationFrameViews();
        final Record record = applicationFrameViews.getById(frameViewId);

        final ApplicationFrameView frameView = (ApplicationFrameView) record.getAsObject("item");
        this.activeFrameView = frameView;

        final Hyperlink hyperlink = (Hyperlink) record.getAsObject("link");
        topMenuBar.setEnabledLink(hyperlink);

        frameView.activate(parameters);
    }

    public static native void logout() *//*-{
        $wnd.location.replace("/signin");
    }-*//*;

    private class TheHistoryHandler implements ValueChangeHandler<String> {
        public void onValueChange(ValueChangeEvent<String> event) {
            final String token = event.getValue();

            String tokenName;
            String tokenParameters;
            int index = token.indexOf('?');
            if (index != -1) {
                tokenName = token.substring(0, index);
                tokenParameters = token.substring(index + 1);
            } else {
                tokenName = token;
                tokenParameters = null;
            }

            final Record record = getApplicationFrameViews().getById(tokenName);
            if (record != null) {
                activateImpl(record.getId(), (tokenParameters == null ? null : Parameters.decode(tokenParameters)), true);
            }
        }
    }*/

	@Override
	public void onModuleLoad() {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void activate(String frameViewId, Parameters parameters) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public ApplicationFrameView getActiveFrameView() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public <T extends ApplicationFrameView> T getApplicationFrameView(String frameViewId) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public <T extends ApplicationTool> T getApplicationTool(Class<? extends T> type) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}