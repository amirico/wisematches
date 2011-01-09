package wisematches.server.core.account;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import java.io.Serializable;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Entity
@Table(name = "lock_username")
public class LockUsernameInfo implements Serializable {
    @Id
    @Column(name = "username", length = 100, nullable = false)
    private String username;

    @Column(name = "reason", length = 255, nullable = false)
    private String reason;

    /**
     * For hibernate
     */
    protected LockUsernameInfo() {
    }

    public LockUsernameInfo(String username, String reason) {
        this.username = username;
        this.reason = reason;
    }

    public String getUsername() {
        return username;
    }

    public String getReason() {
        return reason;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
