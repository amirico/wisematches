package wisematches.client.gwt.core.client;

/**
 * Session exception indicates that player was not registred of it's session is expired.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerSessionException extends PlayerSecurityException {
    /**
     * This is GWT serialization only constructor.
     */
    @Deprecated
    public PlayerSessionException() {
    }

    /**
     * Creates new exception with descriptor.
     *
     * @param message
     */
    public PlayerSessionException(String message) {
        super(message);
    }
}
