package wisematches.server.player;

/**
 * Indicates that account that should be updated or removed is unknown.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class UnknownAccountException extends AccountException {
	public UnknownAccountException(String message, Player player) {
		super(message, player);
	}

	public UnknownAccountException(String message, Throwable cause, Player player) {
		super(message, cause, player);
	}
}
