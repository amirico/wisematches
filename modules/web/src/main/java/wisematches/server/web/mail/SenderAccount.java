package wisematches.server.web.mail;

/**
 * From addresses enum. This enum contains type of emails messages can be sent from.
 */
public enum SenderAccount {
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

	SenderAccount(String defaultName) {
		this.defaultName = defaultName;
	}

	public String getDefaultName() {
		return defaultName;
	}
}
