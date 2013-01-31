package wisematches.playground;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameStateException extends GameMoveException {
	public GameStateException(String message) {
		super(message);
	}

	public GameStateException(String message, Throwable cause) {
		super(message, cause);
	}
}
