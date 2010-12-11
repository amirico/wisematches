package wisematches.client.gwt.core.client;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class GuestRestrictionException extends PlayerSecurityException {
    /**
     * This is GWT serialization constructor.
     */
    @Deprecated
    public GuestRestrictionException() {
    }

    public GuestRestrictionException(String message) {
        super(message);
    }

    public GuestRestrictionException(String template, Object... arguments) {
        super(template, arguments);
    }
}
