package wisematches.server.web.services.notify.impl.publisher;

import wisematches.server.web.services.notify.DeliveryException;
import wisematches.server.web.services.notify.impl.delivery.NotificationDeliveryServiceImpl;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 * @deprecated NotificationScope must be used instead.
 */
@Deprecated
public class FilteringNotificationPublisher implements NotificationPublisherOld {
    private NotificationPublisherOld notificationPublisher;
    private final Set<String> allowedNotifications = new HashSet<String>();

    public FilteringNotificationPublisher() {
    }

    @Override
    public boolean publishNotification(NotificationDeliveryServiceImpl.NotificationOld notification) throws DeliveryException {
        return allowedNotifications.contains(notification.getDescriptor().getCode()) && notificationPublisher.publishNotification(notification);
    }

    public void setAllowedNotifications(Set<String> allowedNotifications) {
        this.allowedNotifications.clear();

        if (allowedNotifications != null) {
            this.allowedNotifications.addAll(allowedNotifications);
        }
    }

    public void setNotificationPublisher(NotificationPublisherOld notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }
}
