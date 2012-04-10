package wisematches.server.web.services.notify;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationTransport {
    void sendNotification(NotificationMessage message) throws TransmissionException;
}
