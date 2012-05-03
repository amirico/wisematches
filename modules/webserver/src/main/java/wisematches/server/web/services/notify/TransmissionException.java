package wisematches.server.web.services.notify;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TransmissionException extends PublicationException {
    public TransmissionException(String message) {
        super(message);
    }

    public TransmissionException(Throwable cause) {
        super(cause);
    }

    public TransmissionException(String message, Throwable cause) {
        super(message, cause);
    }
}
