package wisematches.server.core.account;

import wisematches.kernel.player.Player;

/**
 * <code>AccountManager</code> allows create and authentificate players or does search by some criteria.
 * <p/>
 * {@code AccountManager} does not extends {@code PlayerManager} because {@code PlayerManager} has a lot
 * of implementation.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AccountManager extends PlayerManager {
    void addAccountListener(AccountListener l);

    void removeAccountListener(AccountListener l);

    /**
     * Creates new player's account with specified <code>username</code>, <code>password</code> and email address.
     *
     * @param username the new player username.
     * @param password the password
     * @param email    the email address.
     * @return the created player's account.
     * @throws DublicateAccountException    if player with specified username or email already exist.
     * @throws AccountRegistrationException if player account can't be crated by some reasone
     * @throws IllegalArgumentException     if username or email address has incorrect format.
     */
    Player createPlayer(String username, String password, String email) throws AccountRegistrationException;

    /**
     * Authentificate player by it's username and password. Returns authentificated player.
     * <p/>
     * After loading a player the {@link #authentificate(wisematches.kernel.player.Player)} method is
     * invoked to do player authentification.
     *
     * @param username the player's username
     * @param password the player's password
     * @return the authentificated player or <code>null</code> if player isn't authentificated.
     * @throws AccountLockedException   if player account is locked.
     * @throws AccountNotFountException if player with specified parameters is unknown
     * @see #authentificate(wisematches.kernel.player.Player)
     */
    Player authentificate(String username, String password) throws AccountLockedException, AccountNotFountException;

    /**
     * Authentificates specified player.
     * <p/>
     * This method should be invoked each time when player is sign in to account.
     *
     * @param player the player to be authentificate
     * @throws AccountLockedException if player account is locked.
     */
    void authentificate(Player player) throws AccountLockedException;

    /**
     * Returns player by it's email
     *
     * @param email the player's email
     * @return the player with specified email or <code>null</code> if player is unknown
     */
    Player findByEmail(String email);

    /**
     * Returns player by it's username
     *
     * @param username the player's username
     * @return the player with specified username or <code>null</code> if player is unknown
     */
    Player findByUsername(String username);

    /**
     * Removes specified player.
     *
     * @param player the player that must be removed.
     */
    void deletePlayer(Player player);

    /**
     * Returns number of all registred players.
     *
     * @return the number of all registred players.
     */
    int getRegistredPlayersCount();
}
