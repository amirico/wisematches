package wisematches.personality.account;

/**
 * <code>AccountManager</code> allows create and authentificate players or does search by some criteria.
 * <p/>
 * {@code AccountManager} does not extends {@code PlayerManager} because {@code PlayerManager} has a lot
 * of implementation.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AccountManager {
    void addAccountListener(AccountListener l);

    void removeAccountListener(AccountListener l);

    /**
     * Creates new player based on information in specified player object. This method does not
     * modify original object and returns new object instead.
     *
     * @param account the player to be created.
     * @return created player object possible the same but can be new one so returned object must be
     *         used after this method was called.
     * @throws DuplicateAccountException     if account with specified username or email already exist.
     * @throws InadmissibleUsernameException if select username can't be used by User Naming Policy
     */
    Account createAccount(Account account) throws DuplicateAccountException, InadmissibleUsernameException;

    /**
     * Returns player by it's id.
     *
     * @param playerId the player's id.
     * @return the player by it's id or <code>null</code> if player is unknown.
     */
    Account getAccount(long playerId);

    /**
     * Searches a player by specified email.
     *
     * @param email the email to be searched
     * @return the found player or {@code null} if no player with specified email.
     */
    Account findByEmail(String email);

    /**
     * Searches a player by specified username.
     *
     * @param username the username to be searched
     * @return the found player or {@code null} if no player with specified username.
     */
    Account findByUsername(String username);

    /**
     * Updates specified player. The player must be
     *
     * @param account the player with new information.
     * @return returns updated account
     * @throws UnknownAccountException       if an account for specified player is unknown.
     * @throws DuplicateAccountException     if account with specified username or email already exist.
     * @throws InadmissibleUsernameException if username is not correct and can't be used
     */
    Account updateAccount(Account account) throws UnknownAccountException, DuplicateAccountException, InadmissibleUsernameException;

    /**
     * Removes specified player.
     *
     * @param account the player to be removed.
     * @throws UnknownAccountException if an account for specified player is unknown.
     */
    void removeAccount(Account account) throws UnknownAccountException;

    /**
     * Checks is account with specified username and email can be created or not.
     *
     * @param username the username to be checked
     * @param email    the email to be checked.
     * @return the {@code AccountAvailability} object that contains information about availability.
     */
    AccountAvailability checkAccountAvailable(String username, String email);
}