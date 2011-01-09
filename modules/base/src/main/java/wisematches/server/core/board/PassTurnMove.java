package wisematches.server.core.board;

/**
 * Predefined move that indicates that turn was passed.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PassTurnMove extends PlayerMove {
    public PassTurnMove(long gamePlayer) {
        super(gamePlayer);
    }
}
