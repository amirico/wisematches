package wisematches.server.web.services.notify.impl.publisher;

import wisematches.server.web.services.notify.NotificationPublisher;
import wisematches.server.web.services.notify.NotificationTemplate;
import wisematches.server.web.services.notify.PublicationException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FilteringNotificationPublisher implements NotificationPublisher {
    protected NotificationPublisher notificationPublisher;

    public FilteringNotificationPublisher() {
    }

    public FilteringNotificationPublisher(NotificationPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }

    @Override
    public boolean publishNotification(NotificationTemplate template) throws PublicationException {
        return notificationPublisher.publishNotification(template);
    }

    public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }
}
