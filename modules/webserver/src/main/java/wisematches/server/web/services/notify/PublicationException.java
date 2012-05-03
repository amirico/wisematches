package wisematches.server.web.services.notify;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PublicationException extends Exception {
    public PublicationException(String message) {
        super(message);
    }

    public PublicationException(Throwable cause) {
        super(cause);
    }

    public PublicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
