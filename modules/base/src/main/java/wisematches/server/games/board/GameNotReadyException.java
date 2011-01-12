package wisematches.server.games.board;

/**
 * If game isn't ready for play.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameNotReadyException extends GameMoveException {
	public GameNotReadyException(String message) {
		super(message);
	}
}
