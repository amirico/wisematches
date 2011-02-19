package wisematches.server.mail;

/**
 * From addresses enum. This enum contains type of emails messages can be sent from.
 */
public enum MailSender {
	/**
	 * This is abstract e-mail notification.
	 */
	UNDEFINED("noreplay"),

	/**
	 * From address is bugs reporter.
	 */
	SUPPORT("support"),

	/**
	 * Mail was sent from game state notifications team
	 */
	GAME("game-noreplay"),

	/**
	 * Mail was sent from accounts support team.
	 */
	ACCOUNTS("account-noreplay");

	private final String defaultName;

	MailSender(String defaultName) {
		this.defaultName = defaultName;
	}

	public String getDefaultName() {
		return defaultName;
	}
}