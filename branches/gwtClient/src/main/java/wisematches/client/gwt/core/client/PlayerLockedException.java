package wisematches.client.gwt.core.client;

import java.util.Date;

/**
 * Indicates that player was locked. Contains information about lock: when was locked, unlock time,
 * reason and who set a lock.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayerLockedException extends PlayerSecurityException {
    private String username;
    private Date lockDate;
    private Date unlockDate;
    private String reason;

    /**
     * This is GWT serialization only constructor.
     */
    @Deprecated
    public PlayerLockedException() {
    }

    /**
     * Create new locked exception with specified template message. This template can contains
     * four argument:
     * <ul>
     * <li> {0} - the username who create this lock.
     * <li> {1} - the date of lock
     * <li> {2} - the date of unlock
     * <li> {3} - the lock reason
     * </ul>
     *
     * @param messageTemplate the template message.
     * @param username        the username who set a lock.
     * @param lockDate        the lock date.
     * @param unlockDate      the unlock date.
     * @param reason          the lock reason.
     */
    public PlayerLockedException(String messageTemplate, String username,
                                 Date lockDate, Date unlockDate, String reason) {
        super(messageTemplate, username, lockDate, unlockDate, reason);

        this.username = username;
        this.lockDate = lockDate;
        this.unlockDate = unlockDate;
        this.reason = reason;
    }

    /**
     * Creates new lock exception with default template message:
     * {@code You account was locked by {0} from {1} to {2} because {3}}
     *
     * @param username   the username who set a lock.
     * @param lockDate   the lock date.
     * @param unlockDate the unlock date.
     * @param reason     the lock reason.
     */
    public PlayerLockedException(String username, Date lockDate, Date unlockDate, String reason) {
        this("You account was locked by {0} from {1} to {2} because {3}", username, lockDate, unlockDate, reason);
    }

    public String getUsername() {
        return username;
    }

    public Date getLockDate() {
        return lockDate;
    }

    public Date getUnlockDate() {
        return unlockDate;
    }

    public String getReason() {
        return reason;
    }
}