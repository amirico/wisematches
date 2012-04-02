package wisematches.server.web.services.notify.publisher;

import wisematches.personality.account.Account;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class Notification {
	private final String code;
	private final String subject;
	private final String message;
	private final Account account;
	private final NotificationOriginator originator;

	public Notification(String code, String subject, String message, Account account, NotificationOriginator originator) {
		this.code = code;
		this.subject = subject;
		this.message = message;
		this.account = account;
		this.originator = originator;
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

	public NotificationOriginator getOriginator() {
		return originator;
	}
}
