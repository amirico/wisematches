package wisematches.server.player;

/**
 * Indicates that account that should be updated or removed is unknown.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class UnknownAccountException extends AccountException {
	public UnknownAccountException(Player player) {
		super("UnknownAccountException: " + player, player);
	}

	public UnknownAccountException(Throwable cause, Player player) {
		super("UnknownAccountException: " + player, cause, player);
	}
}
