package wisematches.client.gwt.login.client.content.signin;

import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import wisematches.client.gwt.core.client.widget.WMErrorField;

import static wisematches.client.gwt.login.client.content.i18n.Resources.LOGIN;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class SigninPanel extends Window {
	private WMErrorField errorField;

	private RestoreUsernameWindow restoreUsernameWindow;
	private RestorePasswordWindow restorePasswordWindow;

	public SigninPanel() {
		initPanel();
	}

	private void initPanel() {
		setTitle(LOGIN.lblSigInTitle());
		setStyleName("signin");

		setAutoSize(true);
		setAutoHeight();
		setCanDrag(false);

		setShowEdges(true);
		setKeepInParentRect(true);

		addItem(new Label("adasdad"));
	}
/*
        setFrame(true);
        setBorder(true);

        final FormPanel loginPanel = new FormPanel();
        loginPanel.setBorder(false);
        loginPanel.setFrame(false);
        loginPanel.setLabelWidth(50);

        errorField = new WMErrorField();

        final TextField login = new TextField(LOGIN.lblLogin(), "login", 130);
        login.setAllowBlank(false);
        login.setBlankText(MLOGIN.errorEmptyUsername());

        final TextField password = new TextField(LOGIN.lblPassword(), "password", 130);
        password.setPassword(true);
        password.setAllowBlank(false);
        password.setBlankText(MCOMMON.errorEmptyPassword());

        final Checkbox checkbox = new Checkbox();
        checkbox.setChecked(false);
        checkbox.setBoxLabel(LOGIN.lblRememberMe());

        loginPanel.add(login);
        loginPanel.add(password);
        loginPanel.add(errorField);
        loginPanel.add(checkbox);

        Button signIn = new Button(LOGIN.btnSignin());
        signIn.addListener(new ButtonListenerAdapter() {
            public void onClick(Button button, EventObject eventObject) {
                performLogin(login.getText(), password.getText(), checkbox.getValue());
            }
        });

        loginPanel.addButton(signIn);

        final Hyperlink restoreUserLink = new WMHyperlink(LOGIN.lblForgotLogin(), "restoreUsername");
        restoreUserLink.setStyleName("restoreUsername");
        restoreUserLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                showRestoreUsernameWindow(restoreUserLink);
            }
        });

        final Hyperlink restorePassworLink = new WMHyperlink(LOGIN.lblForgotPassword(), "restorePassword");
        restorePassworLink.setStyleName("restorePassword");
        restorePassworLink.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                showRestorePasswordWindow(restorePassworLink);
            }
        });

        MultiFieldPanel linksPanel = new MultiFieldPanel();
        linksPanel.addToRow(restoreUserLink, new ColumnLayoutData(.5));
        linksPanel.addToRow(restorePassworLink, new ColumnLayoutData(0.5));
        linksPanel.setPaddings(10);

        add(loginPanel);
        add(linksPanel);

        History.addValueChangeHandler(new ValueChangeHandler<String>() {
            public void onValueChange(ValueChangeEvent<String> stringValueChangeEvent) {
                final String s = stringValueChangeEvent.getValue();
                if (restoreUserLink.getTargetHistoryToken().equals(s)) {
                    showRestoreUsernameWindow(restoreUserLink);
                } else if (restoreUsernameWindow != null && restoreUsernameWindow.isVisible()) {
                    restoreUsernameWindow.hide();
                }

                if (restorePassworLink.getTargetHistoryToken().equals(s)) {
                    showRestorePasswordWindow(restorePassworLink);
                } else if (restorePasswordWindow != null && restorePasswordWindow.isVisible()) {
                    restorePasswordWindow.hide();
                }
            }
        });
    }

    private void showRestoreUsernameWindow(Hyperlink restoreUserLink) {
        if (restoreUsernameWindow != null && restoreUsernameWindow.isVisible()) {
            return;
        }

        restoreUsernameWindow = new RestoreUsernameWindow();
        restoreUsernameWindow.setCloseAction(RestoreUsernameWindow.CLOSE);
        restoreUsernameWindow.show(restoreUserLink.getElement());
    }

    private void showRestorePasswordWindow(Hyperlink restorePassworLink) {
        if (restorePasswordWindow != null && restorePasswordWindow.isVisible()) {
            return;
        }

        restorePasswordWindow = new RestorePasswordWindow();
        restorePasswordWindow.setCloseAction(RestorePasswordWindow.CLOSE);
        restorePasswordWindow.show(restorePassworLink.getElement());
    }

    private boolean performLogin(final String username, final String password, final boolean remember) {
        Ext.getBody().mask(LOGIN.lblPerformSignin(), true);

        final CheckPointServiceAsync pointServiceAsync = CheckPointService.App.getInstance();
        pointServiceAsync.signIn(username, password, remember, new TheSignInCallback(remember));
        return false;
    }

    private final class TheSignInCallback implements AsyncCallback<SigninToken> {
        private final boolean remember;

        public TheSignInCallback(boolean remember) {
            this.remember = remember;
        }

        public void onFailure(Throwable throwable) {
            Ext.getBody().unmask();

            ExceptionHandler.showSystemError(throwable);
        }

        public void onSuccess(SigninToken token) {
            Ext.getBody().unmask();

            if (token == null) {
                errorField.showError(LOGIN.errorUsernameAndPassword());
            } else {
                if (remember) {
                    final Date never = new Date(Long.MAX_VALUE);
                    Cookies.setCookie(CheckPointService.PLAYER_LOGIN_ID, String.valueOf(token.getPlayerId()), never);
                    Cookies.setCookie(CheckPointService.PLAYER_LOGIN_TOKEN, token.getToken(), never);
                }
                LoginEntryPoint.signIn();
            }
        }
    }
*/
}
