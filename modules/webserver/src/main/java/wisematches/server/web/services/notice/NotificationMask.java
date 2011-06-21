package wisematches.server.web.services.notice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class NotificationMask {
    private final Map<String, Boolean> notifications = new HashMap<String, Boolean>();

    public NotificationMask() {
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
}
