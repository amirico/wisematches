package wisematches.server.core;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WisematchesException extends Exception {
    public WisematchesException() {
    }

    public WisematchesException(String message) {
        super(message);
    }

    public WisematchesException(String message, Throwable cause) {
        super(message, cause);
    }

    public WisematchesException(Throwable cause) {
        super(cause);
    }
}
