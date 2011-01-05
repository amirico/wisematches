package wisematches.client.gwt.login.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import wisematches.client.gwt.core.client.analiytics.PageTracker;
import wisematches.client.gwt.core.client.content.footer.CopyrightPanel;
import wisematches.client.gwt.core.client.content.locale.LanguageComboBox;
import wisematches.client.gwt.login.client.content.info.AboutPanel;
import wisematches.client.gwt.login.client.content.info.InfoPanel;
import wisematches.client.gwt.login.client.content.signin.SigninPanel;

import static wisematches.client.gwt.login.client.content.i18n.Resources.LOGIN;

/**
 * TODO: Not refactored
 */
public class LoginEntryPoint implements EntryPoint {
	private final DeckPanel centerPanel = new DeckPanel();

	private Hyperlink signInGuest;
	private Hyperlink createAccountLink;

	//    private Hyperlink aboutHiperlink;
//    private Hyperlink featuresHyperlink;
//
//    private NewAccountWindow newAccountWindow;
//

	public void onModuleLoad() {
		initView();
		initHistory();

		RootPanel.getBodyElement().removeChild(RootPanel.get("loadingWrapper").getElement());

		PageTracker.attachTrackerToListener("signin");

//		checkBrowser();
//		checkResetPasswordRequest();
	}

	private void initView() {
		final DockPanel p = new DockPanel();

		final Widget welcomePanel = createWelcomePanel();
		final Widget centerPanel = createCenterPanel();
		final Widget copyrightPanel = createCopyrightPanel();
		final Widget eastPanel = createEastPanel();

		p.add(welcomePanel, DockPanel.NORTH);
		p.add(copyrightPanel, DockPanel.SOUTH);
		p.add(eastPanel, DockPanel.EAST);
		p.add(centerPanel, DockPanel.CENTER);

		p.setWidth("100%");
		p.setCellWidth(centerPanel, "100%");
		p.setVerticalAlignment(DockPanel.ALIGN_TOP);
		p.setHorizontalAlignment(DockPanel.ALIGN_LEFT);
		p.setSpacing(0);

		RootPanel.get().add(p);
	}

	private void initHistory() {
		History.addValueChangeHandler(new TheHistoryListener());
		History.fireCurrentHistoryState();
	}

	private void checkBrowser() {
//        DeferredCommand.addCommand(new Command() {
//            public void execute() {
//                BrowsersChecker.checkBrowser();
//            }
//        });
	}

	private void checkResetPasswordRequest() {
//        final Dictionary dictionary = Dictionary.getDictionary("applicationProperties");
//        if (dictionary.keySet().contains("rp")) {
//            DeferredCommand.addCommand(new Command() {
//                public void execute() {
//                    try {
//                        new ResetPasswordWindow(dictionary.get("rp")).show();
//                    } catch (Throwable th) {
//                        MessagesBox.showMessage(MLOGIN.tltPasswordResetNot(), MLOGIN.msgPasswordResetUrlInvalid());
//                    }
//                }
//            });
//        }
	}

	private Widget createCenterPanel() {
		final InfoPanel main = new InfoPanel();
		final AboutPanel about = new AboutPanel("about", LOGIN.tltAboutHeader(), LOGIN.lblAboutInfo(), true);
		final AboutPanel features = new AboutPanel("features", LOGIN.tltFeatureHeader(), null, false);

		centerPanel.add(main);
		centerPanel.add(about);
		centerPanel.add(features);

		centerPanel.showWidget(0);
		return centerPanel;
	}

	private Widget createCopyrightPanel() {
		return new CopyrightPanel();
	}

	private Widget createWelcomePanel() {
		final Image image = new Image(GWT.getModuleBaseURL() + "/images/logo.png");

		final com.smartgwt.client.widgets.Label label = new com.smartgwt.client.widgets.Label(LOGIN.lblWelcom());
		label.setValign(VerticalAlignment.BOTTOM);
		label.setStyleName("welcome");
		label.setWrap(false);
		label.setHeight100();

		final LanguageComboBox languageComboBox = new LanguageComboBox(true, false);

		final Canvas canvas = new Canvas();
		canvas.setWidth("*");

		final HLayout res = new HLayout();
		res.setWidth100();
		res.setHeight(72);
		res.addMember(image);
		res.addMember(label);
		res.addMember(canvas);
		res.addMember(languageComboBox);

		return res;
	}

	private Widget createEastPanel() {
		final VLayout layout = new VLayout();
		layout.setWidth(260);

		final Canvas panel1 = createLoginPanel();
//        final Canvas panel2 = createNewAccountPanel();
//        panel2.setWidth(260);

		layout.addMember(panel1);
//        layout.add(Box.createVerticalBox(15));
//        layout.add(panel2);
		return layout;
	}

	private Canvas createLoginPanel() {
		return new SigninPanel();
	}

//    private Panel createNewAccountPanel() {
//        Panel p = new Panel();
//        p.setFrame(true);
//        p.setBorder(true);
//
//        createAccountLink = new WMHyperlink(LOGIN.lblCreateAccount(), "createAccount");
//        createAccountLink.setStyleName("createAccount");
//        createAccountLink.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent clickEvent) {
//                if (createAccountLink.getTargetHistoryToken().equals(History.getToken())) {
//                    showNewAccountWindow();
//                }
//            }
//        });
//
//        signInGuest = new WMExternalHyperlink("/app?signinGuest", LOGIN.lblSignInGuest());
//        signInGuest.setTitle(LOGIN.ttpSignInGuest());
//        signInGuest.setStyleName("signinGuest");
//        signInGuest.addClickHandler(new ClickHandler() {
//            public void onClick(ClickEvent clickEvent) {
//                signInGuest();
//            }
//        });
//
//        aboutHiperlink = new WMHyperlink(LOGIN.lblAbout(), "about");
//        aboutHiperlink.setStyleName("about-link");
//
//        featuresHyperlink = new WMHyperlink(LOGIN.lblFeatures(), "features");
//        featuresHyperlink.setStyleName("features-link");
//
//        MultiFieldPanel namePanel = new MultiFieldPanel();
//        namePanel.addToRow(aboutHiperlink, new ColumnLayoutData(.5));
//        namePanel.addToRow(featuresHyperlink, new ColumnLayoutData(0.5));
//        namePanel.setPaddings(10);
//
//        p.add(createAccountLink);
//        p.add(signInGuest);
//        p.add(namePanel);
//
//        return p;
//    }
//
//    public static native void signIn() /*-{
//        $wnd.location.replace("/app");
//    }-*/;
//
//    public static native void signInGuest() /*-{
//        $wnd.location.replace("/app?signinGuest");
//    }-*/;


//    private void showNewAccountWindow() {
//        if (newAccountWindow != null && newAccountWindow.isVisible()) {
//            return;
//        }
//
//        newAccountWindow = new NewAccountWindow();
//        newAccountWindow.setCloseAction(NewAccountWindow.CLOSE);
//        newAccountWindow.showDialog(createAccountLink.getElement());
//    }
//

	private class TheHistoryListener implements ValueChangeHandler<String> {
		public void onValueChange(ValueChangeEvent<String> event) {
//			final String token = event.getValue();
//			if ("".equals(token)) {
//				centerPanel.showWidget(0);
//			} else if (aboutHiperlink.getTargetHistoryToken().equals(token)) {
//				centerPanel.showWidget(1);
//			} else if (featuresHyperlink.getTargetHistoryToken().equals(token)) {
//				centerPanel.showWidget(2);
//			}
//
//			if (createAccountLink.getTargetHistoryToken().equals(token)) {
//				showNewAccountWindow();
//			} else if (newAccountWindow != null && newAccountWindow.isVisible()) {
//				newAccountWindow.hide();
//			}
		}
	}
}
