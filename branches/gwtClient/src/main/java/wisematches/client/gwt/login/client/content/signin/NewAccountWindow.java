package wisematches.client.gwt.login.client.content.signin;

import com.smartgwt.client.widgets.Window;

/**
 * This is window for creating a new account.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class NewAccountWindow extends Window {
/*
    private WMErrorField errorField;

    private TextField usernameField;
    private TextField passwordField;
    private TextField repasswordField;
    private TextField mailField;

    private Checkbox termsOfUserCB;

    public NewAccountWindow() {
        initForm();
    }

    private void initForm() {
        setTitle(LOGIN.tltCreateAccount());

        setWidth(700);
        setHeight(450);
        setPlain(true);

        setClosable(true);
        setCloseAction(Window.CLOSE);

        setAnimCollapse(true);
        setMaximizable(true);

        BorderLayoutData eastData = new BorderLayoutData(RegionPosition.WEST);
        eastData.setSplit(false);
        eastData.setMargins(new Margins(0, 0, 5, 0));

        setLayout(new BorderLayout());
        add(createFormPanel(), eastData);
        add(createTermsOfUsePanel(), new BorderLayoutData(RegionPosition.CENTER));
    }

    private FormPanel createFormPanel() {
        FormPanel form = new FormPanel();
        form.setLabelAlign(Position.TOP);
        form.setBorder(false);
        form.setBodyBorder(false);
        form.setFrame(true);
        form.setWidth(260);
        form.setAutoScroll(false);

        errorField = new WMErrorField();

        usernameField = new TextField(COMMON.lblUsername(), "username", 230);
        usernameField.setAllowBlank(false);
        usernameField.setBlankText(MLOGIN.errorEmptyUsername());
        usernameField.setMinLength(3);
        usernameField.setMinLengthText(MLOGIN.errorShortUsername());

        passwordField = new TextField(COMMON.lblChoosePassword(), "lblPasswordord", 230);
        passwordField.setAllowBlank(false);
        passwordField.setBlankText(MCOMMON.errorEmptyPassword());
        passwordField.setPassword(true);

        repasswordField = new TextField(COMMON.lblRetypePassword(), "re-lblPassword", 230);
        repasswordField.setAllowBlank(false);
        repasswordField.setBlankText(MCOMMON.errorEmptyRePassword());
        repasswordField.setPassword(true);

        mailField = new TextField(COMMON.lblEMail(), "email", 230);
        mailField.setVtype(VType.EMAIL);
        mailField.setVtypeText(MCOMMON.errorInvalidMail());
        mailField.setAllowBlank(false);
        mailField.setBlankText(MCOMMON.errorEmptyMail());

        termsOfUserCB = new Checkbox(LOGIN.lblAcceptTermOfUser());
        termsOfUserCB.setChecked(false);
        termsOfUserCB.setWidth(230);

        Button registerButton = new Button(LOGIN.btnRegister());
        registerButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject eventObject) {
                boolean res = usernameField.validate();
                res = res && passwordField.validate();
                res = res && repasswordField.validate();
                res = res && mailField.validate();

                if (res && !termsOfUserCB.getValue()) {
                    errorField.showError(MLOGIN.errorTofuNotAccepted());
                } else if (res) {
                    if (!repasswordField.getText().equals(passwordField.getText())) {
                        passwordField.markInvalid(MCOMMON.errorPasswordArentEquals());
                        repasswordField.markInvalid(MCOMMON.errorPasswordArentEquals());

                        errorField.showError(MCOMMON.errorNotAllFieldsCorrect());
                    } else {
                        showCheckMessage();

                        final CheckPointServiceAsync accountAsync = CheckPointService.App.getInstance();
                        accountAsync.createAccount(getUsername(), getPassword(), getEMail(), new RegisterAccountCallback());
                    }
                } else {
                    errorField.showError(MCOMMON.errorNotAllFieldsCorrect());
                }
            }
        });

        Button cancelButton = new Button(COMMON.btnClose());
        cancelButton.addListener(new ButtonListenerAdapter() {
            @Override
            public void onClick(Button button, EventObject eventObject) {
                close();
            }
        });

        form.add(usernameField);
        form.add(passwordField);
        form.add(repasswordField);
        form.add(mailField);
        form.add(termsOfUserCB);
        form.add(errorField);

        form.addButton(registerButton);
        form.addButton(cancelButton);
        return form;
    }

    private Widget createTermsOfUsePanel() {
        return new TermsPanel(Terms.TERMS_OF_USE, Terms.USER_NAMING, Terms.PRIVACY_POLICY);
    }

    private void showCheckMessage() {
        Ext.getBody().mask(LOGIN.lblRegistring(), true);
    }

    private void hideCheckMessage() {
        Ext.getBody().unmask();
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public String getEMail() {
        return mailField.getText();
    }

    public void showDialog(com.google.gwt.user.client.Element e) {
        PageTracker.trackPageVisit("/signin/registring");

        show(e);
    }

    private class RegisterAccountCallback implements AsyncCallback<CreateAccountResult> {
        public void onFailure(Throwable throwable) {
            ExceptionHandler.showSystemError(throwable);
            hideCheckMessage();
        }

        public void onSuccess(CreateAccountResult result) {
            final CreateAccountResult.Status status = result.getStatus();

            hideCheckMessage();

            switch (status) {
                case SUCCESS:
                    PageTracker.trackPageVisit("/signin/registred");

                    setVisible(false);
                    MessagesBox.showMessage(MLOGIN.tltAccountCreated(), MLOGIN.msgAccountCreated());
                    break;
                case USERNAME_BUSY:
                    errorField.showError(MLOGIN.errorUsernameBusy(getUsername()));
                    break;
                case EMAIL_BUSY:
                    errorField.showError(MLOGIN.errorEmailBusy(getEMail()));
                    break;
                case USERNAME_INVALID:
                    errorField.showError(MLOGIN.errorUsernameInvalid());
                    break;
                case USERNAME_INADMISSIBLE:
                    errorField.showError(MLOGIN.errorUsernameInadmissible(result.getMessage()));
                    break;
                case EMAIL_INVALID:
                    errorField.showError(MCOMMON.errorInvalidMail());
                    break;
                case UNKNOWN_ERROR:
                    MessageBox.alert(MLOGIN.tltRegistration(), MLOGIN.errorUnknownError());
                    break;
            }
        }
    }
*/
}
