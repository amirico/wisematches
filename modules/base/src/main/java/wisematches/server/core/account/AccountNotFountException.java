package wisematches.server.core.account;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class AccountNotFountException extends AccountException {
    public AccountNotFountException() {
    }

    public AccountNotFountException(String message) {
        super(message);
    }
}
