package wisematches.server.core.account;

import wisematches.kernel.player.Player;

import java.util.Date;

/**
 * <code>LockAccountManager</code> allows check is the account is locked or not. This manager also allows
 * lock and unlock account.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface LockAccountManager {
    void addLockAccountListener(LockAccountListener l);

    void removeLockAccountListener(LockAccountListener l);


    void addLockUsernameListener(LockUsernameListener l);

    void removeLockUsernameListener(LockUsernameListener l);


    /**
     * Locks account of specified player by some time or forever. Any lock required two type of reasone:
     * <i>public reason</i> that will be shown the player and other players and <i>private reason</i> that
     * can be shown only moderators.
     * <p/>
     * If player already locked the reasones and unlock date will be updated to specified.
     *
     * @param player        the player to be locked.
     * @param publicReason  the public reason that will be shown the player and sign in.
     * @param privateReason the private reason that will be shown moderators.
     * @param unlockdate    the unlock date or <code>null</code> if player is locked forever.
     */
    void lockAccount(Player player, String publicReason, String privateReason, Date unlockdate);

    /**
     * Unlocks specified player.
     *
     * @param player the player to be unlocked.
     */
    void unlockAccount(Player player);

    /**
     * Checks that player is locked or not. It's fast operation. If your need get some information about lock
     * please use {@link #getLockAccountInfo(wisematches.kernel.player.Player)} method instead.
     *
     * @param player the player to be checked.
     * @return <code>true</code> if player is locked; <code>false</code> - otherwise.
     * @see #getLockAccountInfo(wisematches.kernel.player.Player)
     */
    boolean isAccountLocked(Player player);

    /**
     * Returns lock information about account. This is heavy operation and it's better to use
     * {@link #isAccountLocked(wisematches.kernel.player.Player)} check before call this method.
     *
     * @param player the player
     * @return the information about lock cause or <code>null</code> if player isn't loacked.
     * @see #isAccountLocked(wisematches.kernel.player.Player)
     */
    LockAccountInfo getLockAccountInfo(Player player);


    /**
     * Validates specified username is it locked or not.
     *
     * @param username the username to be checked.
     * @return the reason why username is locked or <code>null</code> if username isn't locked.
     */
    String isUsernameLocked(String username);

    /**
     * Locks the <code>username</code> with specified <code>reason</code>.
     *
     * @param username the username to be locked
     * @param reason   the reason why username is locked.
     */
    void lockUsername(String username, String reason);

    /**
     * Unlocks the <code>username</code>.
     *
     * @param username the username to be unlocked.
     */
    void unlockUsername(String username);
}