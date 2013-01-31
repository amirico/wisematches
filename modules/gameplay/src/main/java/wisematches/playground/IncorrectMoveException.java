package wisematches.playground;

/**
 * Exception is thrown if move isn't allowed or incorrect.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class IncorrectMoveException extends GameMoveException {
	public IncorrectMoveException(String message) {
		super(message);
	}

	public IncorrectMoveException(String message, Throwable cause) {
		super(message, cause);
	}
}
