package wisematches.server.web.services.notify;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class NotificationCondition {
    private final Map<String, Boolean> notifications = new HashMap<String, Boolean>();

    public NotificationCondition() {
    }

    public Set<String> getNotificationNames() {
        return Collections.unmodifiableSet(notifications.keySet());
    }

    public boolean isEnabled(String name) {
        final Boolean aBoolean = notifications.get(name);
        if (aBoolean == null) {
            return false;
        }
        return aBoolean;
    }

    public void setEnabled(String name, boolean enabled) {
        notifications.put(name, enabled);
    }

    @Override
    public String toString() {
        return "NotificationSettings{" +
                "notifications=" + notifications +
                '}';
    }
}
