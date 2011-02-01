package wisematches.server.web.mail;

/**
 * From addresses enum. This enum contains type of emails messages can be sent from.
 */
public enum SenderAccount {
	/**
	 * This is abstract e-mail notification.
	 */
	UNDEFINED,
	/**
	 * From address is bugs reporter.
	 */
	SUPPORT,
	/**
	 * Mail was sent from accounts support team.
	 */
	ACCOUNTS
}
