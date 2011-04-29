package wisematches.server.playground.board;

/**
 * Exception if thrown if move can't be maden by some reasone.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameMoveException extends Exception {
	public GameMoveException(String message) {
		super(message);
	}

	public GameMoveException(String message, Throwable cause) {
		super(message, cause);
	}

	public GameMoveException(Throwable cause) {
		super(cause);
	}
}
