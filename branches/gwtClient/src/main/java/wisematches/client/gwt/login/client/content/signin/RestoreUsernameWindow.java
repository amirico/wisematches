package wisematches.client.gwt.login.client.content.signin;

import com.smartgwt.client.widgets.Window;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RestoreUsernameWindow extends Window {
/*    public RestoreUsernameWindow() {
        initWindow();
    }

    private void initWindow() {
        setTitle(LOGIN.tltRestoreUsername());
        setWidth(400);
        setAutoHeight(true);
        setResizable(false);

        FormPanel p = new FormPanel();
        p.setFrame(true);
        p.setBodyBorder(false);
        p.setAutoHeight(true);

        final HTML info = new HTML(LOGIN.lblRestoreUsername());

        final TextField email = new TextField(COMMON.lblEMail(), "email", 230);
        email.setVtype(VType.EMAIL);
        email.setVtypeText(MCOMMON.errorInvalidMail());
        email.setAllowBlank(false);
        email.setBlankText(MCOMMON.errorEmptyMail());

        final Button sendButton = new Button(COMMON.btnSubmit());
        sendButton.addListener(new ButtonListenerAdapter() {
            public void onClick(Button button, EventObject eventObject) {
                if (!email.validate()) {
                    return;
                }
                //disable button
                sendButton.disable();

                final CheckPointServiceAsync pointServiceAsync = CheckPointService.App.getInstance();
                pointServiceAsync.restoreUsername(email.getText(), new AsyncCallback<Boolean>() {
                    public void onFailure(Throwable throwable) {
                        // if problem: enable button.
                        sendButton.enable();
                        ExceptionHandler.showSystemError(throwable);
                    }

                    public void onSuccess(Boolean v) {
                        close();

                        final String mail = email.getText();
                        if (v) {
                            MessagesBox.showMessage(MLOGIN.tltUsernameRestoreOk(), MLOGIN.msgUsernameRestoreOk(mail));
                        } else {
                            MessagesBox.showMessage(MLOGIN.tltUsernameRestoreNot(), MLOGIN.msgUsernameRestoreNot(mail));
                        }
                    }
                });
            }
        });

        p.add(info);
        p.add(email);
        p.addButton(sendButton);
        p.setButtonAlign(Position.RIGHT);

        setLayout(new FitLayout());
        add(p);
    }*/
}