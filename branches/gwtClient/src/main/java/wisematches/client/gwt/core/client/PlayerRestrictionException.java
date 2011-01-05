package wisematches.client.gwt.core.client;

/**
 * This exception is thrown when player does not have access to do some action because does not
 * have required permissions.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerRestrictionException extends PlayerSecurityException {
    /**
     * This is GWT serialization only constructor.
     */
    @Deprecated
    public PlayerRestrictionException() {
    }

    public PlayerRestrictionException(String message) {
        super(message);
    }
}
