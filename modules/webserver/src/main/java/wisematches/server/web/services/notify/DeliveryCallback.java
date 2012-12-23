package wisematches.server.web.services.notify;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface DeliveryCallback {
    void notificationPublished(NotificationMessage notificationMessage);

    void notificationRejected(NotificationMessage notificationMessage, NotificationPublisher publisher, DeliveryException exception);
}
