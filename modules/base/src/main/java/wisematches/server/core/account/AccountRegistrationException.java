package wisematches.server.core.account;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class AccountRegistrationException extends AccountException {
    public AccountRegistrationException() {
    }

    public AccountRegistrationException(String message) {
        super(message);
    }
}
