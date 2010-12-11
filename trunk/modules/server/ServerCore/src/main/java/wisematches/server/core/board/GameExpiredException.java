package wisematches.server.core.board;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GameExpiredException extends GameMoveException {
    public GameExpiredException() {
        super("Game expired and no one move can be maden.");
    }
}
