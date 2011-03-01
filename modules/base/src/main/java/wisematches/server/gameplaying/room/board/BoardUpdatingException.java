package wisematches.server.gameplaying.room.board;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class BoardUpdatingException extends BoardManagementException {
	public BoardUpdatingException(String message) {
		super(message);
	}

	public BoardUpdatingException(String message, Throwable cause) {
		super(message, cause);
	}
}
