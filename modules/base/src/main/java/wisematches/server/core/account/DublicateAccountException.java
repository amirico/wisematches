package wisematches.server.core.account;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DublicateAccountException extends AccountRegistrationException {
    private final boolean dublicateUsername;
    private final boolean dublicateEmail;

    public DublicateAccountException(boolean dublicateUsername, boolean dublicateEmail) {
        this.dublicateUsername = dublicateUsername;
        this.dublicateEmail = dublicateEmail;
    }

    public boolean isDublicateUsername() {
        return dublicateUsername;
    }

    public boolean isDublicateEmail() {
        return dublicateEmail;
    }
}
