package wisematches.server.player;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class AccountAvailability {
	private final boolean usernameAvailable;
	private final boolean emailAvailable;

	public AccountAvailability(boolean usernameAvailable, boolean emailAvailable) {
		this.usernameAvailable = usernameAvailable;
		this.emailAvailable = emailAvailable;
	}

	public boolean isUsernameAvailable() {
		return usernameAvailable;
	}

	public boolean isEmailAvailable() {
		return emailAvailable;
	}

	public boolean isAvailable() {
		return usernameAvailable && emailAvailable;
	}
}
