package wisematches.client.gwt.login.client.content.signin;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RestorePasswordWindow { //extends Window {
//	public RestorePasswordWindow() {
//		initWindow();
//	}
//
//	private void initWindow() {
//		setTitle(LOGIN.tltRestorePassword());
//		setWidth(400);
//		setAutoHeight(true);
//		setResizable(false);
//
//		FormPanel p = new FormPanel();
//		p.setFrame(true);
//		p.setBodyBorder(false);
//		p.setAutoHeight(true);
//
//		final HTML info = new HTML(LOGIN.lblRestorePassword());
//
//		final TextField username = new TextField(COMMON.lblUsername(), "username", 230);
//		username.setAllowBlank(false);
//		username.setBlankText(MLOGIN.errorEmptyUsername());
//
//		final Button sendButton = new Button(COMMON.btnSubmit());
//		sendButton.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent clickEvent) {
//				if (!username.validate()) {
//					return;
//				}
//				//disable button
//				sendButton.disable();
//
//				final CheckPointServiceAsync pointServiceAsync = CheckPointService.App.getInstance();
//				pointServiceAsync.restorePassword(username.getText(), new AsyncCallback<Boolean>() {
//					public void onFailure(Throwable throwable) {
//						// if problem: enable button.
//						sendButton.enable();
//
//						ExceptionHandler.showSystemError(throwable);
//					}
//
//					public void onSuccess(Boolean v) {
//						close();
//
//						final String name = username.getText();
//						if (v) {
//							MessagesBox.showMessage(MLOGIN.tltPasswordRestoreOk(), MLOGIN.msgPasswordRestoreOk(name));
//						} else {
//							MessagesBox.showMessage(MLOGIN.tltPasswordRestoreNot(), MLOGIN.msgPasswordRestoreNot(name));
//						}
//					}
//				});
//			}
//		});
//
//		p.add(info);
//		p.add(username);
//		p.addButton(sendButton);
//		p.setButtonAlign(Position.RIGHT);
//
//		setLayout(new FitLayout());
//		add(p);
//	}
}
