package wisematches.server.games.scribble.bank;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class UnsupportedLocaleException extends RuntimeException {
    public UnsupportedLocaleException(String message) {
        super(message);
    }

    public UnsupportedLocaleException(String message, Throwable cause) {
        super(message, cause);
    }
}
