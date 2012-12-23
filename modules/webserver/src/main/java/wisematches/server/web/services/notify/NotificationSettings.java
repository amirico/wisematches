package wisematches.server.web.services.notify;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class NotificationSettings implements Cloneable {
    private final Map<String, NotificationScope> notifications = new HashMap<>();

    private NotificationSettings() {
    }

    public NotificationSettings(Collection<NotificationDescriptor> descriptors) {
        for (NotificationDescriptor descriptor : descriptors) {
            notifications.put(descriptor.getCode(), descriptor.getScope());
        }
    }

    public Set<String> getNotificationNames() {
        return Collections.unmodifiableSet(notifications.keySet());
    }

    public NotificationScope getNotificationScope(String name) {
        return notifications.get(name);
    }

    public void setNotificationScope(String name, NotificationScope scope) {
        if (!notifications.containsKey(name)) {
            throw new IllegalArgumentException("Unknown notification " + name);
        }
        notifications.put(name, scope);
    }

    @Override
    public NotificationSettings clone() {
        NotificationSettings s;
        try {
            s = (NotificationSettings) super.clone();
        } catch (CloneNotSupportedException e) {
            s = new NotificationSettings();
        }
        s.notifications.putAll(this.notifications);
        return s;
    }

    @Override
    public String toString() {
        return "NotificationSettings{" +
                "notifications=" + notifications +
                '}';
    }
}
