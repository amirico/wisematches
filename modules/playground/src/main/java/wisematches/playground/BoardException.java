package wisematches.playground;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BoardException extends Exception {
	public BoardException(String message) {
		super(message);
	}

	public BoardException(String message, Throwable cause) {
		super(message, cause);
	}
}
