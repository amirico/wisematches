package wisematches.server.core.room;

/**
 * This exception is thrown by <code>RoomManager</code> if game board can't be loaded
 * by some reasone.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class BoardLoadingException extends RoomException {
    /**
     * {@inheritDoc}
     */
    public BoardLoadingException(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public BoardLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
