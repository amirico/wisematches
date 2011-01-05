package wisematches.client.gwt.app.client.content.profile.edit;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ChangePrivacyWindow { //extends Window {
//    private TextField mailField;
//    private TextField passwordField;
//    private TextField repasswordField;
//
//    private final PlayerSettingsBean playerSettingsBean;
//
//    public ChangePrivacyWindow(PlayerSettingsBean playerSettingsBean) {
//        this.playerSettingsBean = playerSettingsBean;
//        initWindow();
//    }
//
//    private void initWindow() {
//        setTitle(APP.tltChangePrivacy());
//        setWidth(432);
//        setHeight(200);
//        setPlain(true);
//        setClosable(true);
//        setCloseAction(Window.CLOSE);
//        setAnimCollapse(true);
//        setResizable(false);
//        setLayout(new FitLayout());
//
//        final FormPanel form = new FormPanel();
//        form.setLabelAlign(Position.LEFT);
//        form.setBorder(false);
//        form.setBodyBorder(false);
//        form.setFrame(true);
//        form.setAutoScroll(false);
//        form.setLabelWidth(150);
//
//        passwordField = new TextField(COMMON.lblChoosePassword(), "lblPasswordord", 230);
//        passwordField.setAllowBlank(true);
//        passwordField.setBlankText(MCOMMON.errorEmptyPassword());
//        passwordField.setPassword(true);
//
//        repasswordField = new TextField(COMMON.lblRetypePassword(), "re-lblPassword", 230);
//        repasswordField.setAllowBlank(true);
//        repasswordField.setBlankText(MCOMMON.errorEmptyRePassword());
//        repasswordField.setPassword(true);
//
//        mailField = new TextField(COMMON.lblEMail(), "email", 230);
//        mailField.setVtype(VType.EMAIL);
//        mailField.setVtypeText(MCOMMON.errorInvalidMail());
//        mailField.setAllowBlank(false);
//        mailField.setBlankText(MCOMMON.errorEmptyMail());
//        mailField.setValue(playerSettingsBean.getEmail());
//
//        form.add(new HTML("<div style=\"padding-bottom: 5px;\">Specify new password and/or email. Left password blank if you want leave previous value.</div>"));
//        form.add(mailField);
//        form.add(new HTML("<div class=\"separator\"></div>"));
//        form.add(passwordField);
//        form.add(repasswordField);
//
//        add(form);
//        addButton(new Button(COMMON.btnOk(), new ButtonListenerAdapter() {
//            @Override
//            public void onClick(Button button, EventObject eventObject) {
//                boolean res = passwordField.validate();
//                res = res && repasswordField.validate();
//                res = res && mailField.validate();
//
//                if (res) {
//                    if (!repasswordField.getText().equals(passwordField.getText())) {
//                        passwordField.markInvalid(MCOMMON.errorPasswordArentEquals());
//                        repasswordField.markInvalid(MCOMMON.errorPasswordArentEquals());
//                    } else {
//                        playerSettingsBean.setEmail(mailField.getValueAsString());
//
//                        final String newPassword = passwordField.getValueAsString();
//                        if (newPassword != null && newPassword.trim().length() != 0) {
//                            playerSettingsBean.setPassword(newPassword);
//                        }
//                        close();
//                    }
//                }
//            }
//        }));
//        addButton(new Button(COMMON.btnCancel(), new ButtonListenerAdapter() {
//            @Override
//            public void onClick(Button button, EventObject eventObject) {
//                close();
//            }
//        }));
//    }
}
