package wisematches.server.gameplaying.board;

/**
 * If game isn't ready for play.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameNotReadyException extends GameStateException {
	public GameNotReadyException(String message) {
		super(message);
	}
}
