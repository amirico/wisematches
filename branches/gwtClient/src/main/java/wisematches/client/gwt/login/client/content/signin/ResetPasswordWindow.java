package wisematches.client.gwt.login.client.content.signin;

import com.smartgwt.client.widgets.Window;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ResetPasswordWindow extends Window {
//    public ResetPasswordWindow(String parameters) {
//        final int i = parameters.lastIndexOf('-');
//        if (i == -1) {
//            throw new IllegalArgumentException("Illegal parameter string");
//        }
//
//        String token = parameters.substring(0, i);
//        long playerId = Long.parseLong(parameters.substring(i + 1));
//
//        initWindow(token, playerId);
//    }
//
//    private void initWindow(final String token, final long playerId) {
//        setTitle(LOGIN.tltResetPassword());
//        setWidth(400);
//        setAutoHeight(true);
//        setResizable(false);
//        setModal(true);
//
//        FormPanel p = new FormPanel();
//        p.setFrame(true);
//        p.setBodyBorder(false);
//        p.setAutoHeight(true);
//
//        final HTML info = new HTML(LOGIN.lblResetPassword());
//
//        final TextField password = new TextField(LOGIN.lblPassword(), "password1", 230);
//        password.setAllowBlank(false);
//        password.setBlankText(MCOMMON.errorEmptyPassword());
//        password.setPassword(true);
//
//        final TextField password2 = new TextField(COMMON.lblRetypePassword(), "password2", 230);
//        password2.setAllowBlank(false);
//        password2.setBlankText(MCOMMON.errorEmptyPassword());
//        password2.setPassword(true);
//
//        final Button sendButton = new Button(COMMON.btnSubmit());
//        sendButton.addListener(new ButtonListenerAdapter() {
//            public void onClick(Button button, EventObject eventObject) {
//                if (!password.validate() || !password2.validate()) {
//                    return;
//                }
//
//                if (!password.getText().equals(password2.getText())) {
//                    password.markInvalid(MCOMMON.errorPasswordArentEquals());
//                    password2.markInvalid(MCOMMON.errorPasswordArentEquals());
//                    return;
//                }
//
//                //disable button
//                sendButton.disable();
//
//                final CheckPointServiceAsync pointServiceAsync = CheckPointService.App.getInstance();
//                pointServiceAsync.resetPassword(playerId, token, password.getText(), new AsyncCallback<RestorePasswordResult>() {
//                    public void onFailure(Throwable throwable) {
//                        // if problem: enable button.
//                        sendButton.enable();
//
//                        ExceptionHandler.showSystemError(throwable);
//                    }
//
//                    public void onSuccess(RestorePasswordResult restorePasswordResult) {
//                        close();
//
//                        switch (restorePasswordResult) {
//                            case SUCCESS:
//                                MessagesBox.showMessage(MLOGIN.tltPasswordResetOk(), MLOGIN.msgPasswordResetOk());
//                                break;
//                            case INVALID_TOKEN:
//                                MessagesBox.showMessage(MLOGIN.tltPasswordResetNot(), MLOGIN.msgPasswordResetTokenInvalid());
//                                break;
//                            case TOKEN_EXPIRED:
//                                MessagesBox.showMessage(MLOGIN.tltPasswordResetNot(), MLOGIN.msgPasswordResetTokenExpire());
//                                break;
//                            case UNKNOWN_PLAYER:
//                                MessagesBox.showMessage(MLOGIN.tltPasswordResetNot(), MLOGIN.msgPasswordResetUserUnknown());
//                                break;
//                        }
//                    }
//                });
//            }
//        });
//
//        p.add(info);
//        p.add(password);
//        p.add(password2);
//        p.add(new Hidden("token", token));
//        p.add(new Hidden("playerId", String.valueOf(playerId)));
//
//        p.addButton(sendButton);
//        p.setButtonAlign(Position.RIGHT);
//
//        setLayout(new FitLayout());
//        add(p);
//    }
}
