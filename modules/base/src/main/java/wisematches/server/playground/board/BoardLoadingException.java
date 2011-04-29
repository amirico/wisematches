package wisematches.server.playground.board;

/**
 * This exception is thrown by <code>BoardManager</code> if game board can't be loaded
 * by some reason.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class BoardLoadingException extends BoardManagementException {
	public BoardLoadingException(String message) {
		super(message);
	}

	public BoardLoadingException(String message, Throwable cause) {
		super(message, cause);
	}
}
