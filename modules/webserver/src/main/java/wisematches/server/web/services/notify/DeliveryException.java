package wisematches.server.web.services.notify;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DeliveryException extends Exception {
    public DeliveryException(String message) {
        super(message);
    }

    public DeliveryException(Throwable cause) {
        super(cause);
    }

    public DeliveryException(String message, Throwable cause) {
        super(message, cause);
    }
}
