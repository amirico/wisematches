package wisematches.server.web.services.notify.impl;

import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.NotificationMover;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class Notification {
	private final String code;
	private final String subject;
	private final String message;
	private final Account account;
	private final NotificationMover mover;

	public Notification(String code, String subject, String message, Account account, NotificationMover mover) {
		this.code = code;
		this.subject = subject;
		this.message = message;
		this.account = account;
		this.mover = mover;
	}

	public String getCode() {
		return code;
	}

	public String getSubject() {
		return subject;
	}

	public String getMessage() {
		return message;
	}

	public Account getAccount() {
		return account;
	}

	public NotificationMover getMover() {
		return mover;
	}
}
