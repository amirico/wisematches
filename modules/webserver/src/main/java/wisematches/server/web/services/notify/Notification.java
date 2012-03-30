package wisematches.server.web.services.notify;

import wisematches.personality.account.Account;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class Notification {
	private final String subject;
	private final String message;
	private final Account account;
	private final NotificationSender sender;

	public Notification(String subject, String message, Account account, NotificationSender sender) {
		this.subject = subject;
		this.message = message;
		this.account = account;
		this.sender = sender;
	}
}
