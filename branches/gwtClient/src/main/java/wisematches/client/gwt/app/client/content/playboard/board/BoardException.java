package wisematches.client.gwt.app.client.content.playboard.board;

/**
 * Base exception for any board interaction
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class BoardException extends Exception {
    public BoardException(String message) {
        super(message);
    }
}
