package wisematches.server.playground.board;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BoardManagementException extends Exception {
	public BoardManagementException(String message) {
		super(message);
	}

	public BoardManagementException(String message, Throwable cause) {
		super(message, cause);
	}
}
