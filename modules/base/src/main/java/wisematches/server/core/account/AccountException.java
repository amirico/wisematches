package wisematches.server.core.account;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class AccountException extends Exception {
    public AccountException() {
    }

    public AccountException(String message) {
        super(message);
    }
}