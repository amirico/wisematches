package wisematches.client.gwt.login.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CheckPointServiceAsync {

    /**
     * Creates new account with specified <code>username</code>, <code>password</code> and <code>email</code>
     *
     * @param username the username of new account.
     * @param password the password of new account.
     * @param email    the email of new account.
     * @return result of registration.
     */
    void createAccount(String username, String password, String email, AsyncCallback<CreateAccountResult> async);

    /**
     * Checks specified username and password and returns token, associated to this user if it exists and was
     * sign in successfully.
     * <p/>
     * The returned token can be saved in cookies and will authomatical checked each time when user is
     * entered to the game.
     *
     * @param username      the username.
     * @param password      the password.
     * @param generateToken flag that indicates that signin token must be generated. If flag is specified
     *                      <code>SigninToken</code> will be generated anyway but included token will be <code>null</code>.
     * @return the token, associated with user or <code>null</code> if user is unknown.
     */
    void signIn(String username, String password, boolean generateToken, AsyncCallback<SigninToken> async);

    /**
     * Restore username by it's email address. Send email with username to specified address.
     *
     * @param email the email address a email will be send to.
     * @return <code>true</code> if username is known and email was send to specified email;
     *         <code>false</code> - otherwise.
     */
    void restoreUsername(String email, AsyncCallback<Boolean> async);

    /**
     * Restore password by username. The email message will be sent email with activation token.
     *
     * @param username the username to restore a password.
     * @return <code>true</code> if username is known and email was send associated with account email;
     *         <code>false</code> - otherwise.
     */
    void restorePassword(String username, AsyncCallback<Boolean> async);

    /**
     * Does password reset of player with specified <code>playerId</code> if stored resetting token will
     * equals with specified.
     *
     * @param playerId    the player id.
     * @param token       the token that have been sent to player during {@link #restorePassword} procedure.
     * @param newPassword the new password that will be assigned to the player
     * @return <code>true</code> if password was changed; <code>false</code> - otherwise.
     */
    void resetPassword(long playerId, String token, String newPassword, AsyncCallback<RestorePasswordResult> async);
}
