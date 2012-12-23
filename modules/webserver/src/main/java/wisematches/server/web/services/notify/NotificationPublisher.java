package wisematches.server.web.services.notify;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationPublisher {
    String getName();

    NotificationScope getNotificationScope();


    void sendNotification(NotificationMessage message) throws PublicationException;
}
