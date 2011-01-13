package wisematches.server.player;

/**
 * Base account exception that can be thrown by any of {@code AccountManager} methods.
 * <p/>
 * This exception also contains the original player object that caused this exception.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AccountException extends Exception {
	private final Player player;

	public AccountException(String message, Player player) {
		super(message);
		this.player = player;
	}

	public AccountException(String message, Throwable cause, Player player) {
		super(message, cause);
		this.player = player;
	}

	/**
	 * Returns original player object that caused this exception.
	 *
	 * @return the player object.
	 */
	public Player getPlayer() {
		return player;
	}
}
