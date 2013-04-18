package wisematches.playground;

/**
 * Exception if thrown if move can't be maden by some reasone.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameMoveException extends BoardUpdatingException {
	public GameMoveException(String message) {
		super(message);
	}

	public GameMoveException(String message, Throwable cause) {
		super(message, cause);
	}
}
