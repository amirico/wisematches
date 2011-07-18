package wisematches.playground.message.impl;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Entity
@Table(name = "player_activity")
public class LastMessagesCheck {
    @Id
    @Column(name = "pid")
    private long pid;

    @Column(name = "last_messages_check")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastCheckTime;

    public LastMessagesCheck() {
    }

    public LastMessagesCheck(long pid, Date lastCheckTime) {
        this.pid = pid;
        this.lastCheckTime = lastCheckTime;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public Date getLastCheckTime() {
        return lastCheckTime;
    }

    public void setLastCheckTime(Date lastCheckTime) {
        this.lastCheckTime = lastCheckTime;
    }
}
