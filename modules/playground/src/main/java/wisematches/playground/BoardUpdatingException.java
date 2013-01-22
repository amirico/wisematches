package wisematches.playground;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BoardUpdatingException extends BoardException {
	public BoardUpdatingException(String message) {
		super(message);
	}

	public BoardUpdatingException(String message, Throwable cause) {
		super(message, cause);
	}
}
