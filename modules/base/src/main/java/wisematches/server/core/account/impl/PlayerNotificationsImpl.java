package wisematches.server.core.account.impl;

import org.hibernate.annotations.AccessType;
import wisematches.kernel.notification.PlayerNotification;
import wisematches.kernel.notification.PlayerNotifications;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Entity
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
@Table(name = "user_notification")
public class PlayerNotificationsImpl implements PlayerNotifications {
    @Id
    @Column(name = "playerId")
    private long playerId;

    @Basic
    @AccessType("property")
    @Column(name = "notifications")
    private String notifications = "";

    @Transient
    private Set<String> notificationNames = new HashSet<String>();

    PlayerNotificationsImpl() {
    }

    long getPlayerId() {
        return playerId;
    }

    void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getNotifications() {
        return notifications;
    }

    void setNotifications(String notifications) {
        this.notifications = notifications;

        final StringTokenizer st = new StringTokenizer(notifications, "|");
        while (st.hasMoreTokens()) {
            notificationNames.add(st.nextToken());
        }
    }

    public void addDisabledNotification(PlayerNotification type) {
        if (type == null) {
            throw new NullPointerException("Type can't be null");
        }

        notificationNames.add(notificationName(type));
        updateString();
    }

    public void removeDisabledNotification(PlayerNotification type) {
        if (type == null) {
            throw new NullPointerException("Type can't be null");
        }

        notificationNames.remove(notificationName(type));
        updateString();
    }

    public boolean isNotificationEnabled(PlayerNotification type) {
        if (type == null) {
            throw new NullPointerException("Type can't be null");
        }
        return !notificationNames.contains(notificationName(type));
    }

    private String notificationName(PlayerNotification type) {
        return type.type() + "-" + type.name();
    }

    private void updateString() {
        if (notificationNames.size() == 0) {
            notifications = "";
        } else {
            final StringBuilder b = new StringBuilder();
            for (String notificationName : notificationNames) {
                b.append(notificationName);
                b.append("|");
            }
            b.deleteCharAt(b.length() - 1);
            notifications = b.toString();
        }
    }
}