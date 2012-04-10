package wisematches.server.web.services.notify.impl.publisher;

import wisematches.server.web.services.notify.NotificationTemplate;
import wisematches.server.web.services.notify.PublicationException;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MatchingNotificationPublisher extends FilteringNotificationPublisher {
    private final Set<String> allowedNotifications = new HashSet<String>();

    public MatchingNotificationPublisher() {
    }

    @Override
    public boolean publishNotification(NotificationTemplate notification) throws PublicationException {
        return allowedNotifications.contains(notification.getCode()) && super.publishNotification(notification);
    }

    public void setAllowedNotifications(Set<String> allowedNotifications) {
        this.allowedNotifications.clear();

        if (allowedNotifications != null) {
            this.allowedNotifications.addAll(allowedNotifications);
        }
    }
}
