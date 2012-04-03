package wisematches.server.web.services.notify;

import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.NotificationCreator;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class NotificationMessage {
	private final String code;
	private final String subject;
	private final String message;
	private final Account account;
	private final NotificationCreator creator;

	public NotificationMessage(String code, String subject, String message, Account account, NotificationCreator creator) {
		this.code = code;
		this.subject = subject;
		this.message = message;
		this.account = account;
		this.creator = creator;
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

	public NotificationCreator getCreator() {
		return creator;
	}
}
