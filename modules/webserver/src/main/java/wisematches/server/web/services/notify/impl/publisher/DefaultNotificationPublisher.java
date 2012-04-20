package wisematches.server.web.services.notify.impl.publisher;

import wisematches.server.web.services.notify.*;

/**
 * {@code NotificationPublisher} is base interface for interaction with players. It allows send localized
 * messages to a player using differ systems, like email or internal messages system.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultNotificationPublisher implements NotificationPublisher {
    private NotificationTransport transport;
    private NotificationTransformer transformer;


    public DefaultNotificationPublisher() {
    }

    public boolean publishNotification(Notification notification) throws PublicationException {
        NotificationMessage message = transformer.createMessage(notification);
        transport.sendNotification(message);
        return true;
    }

    public void setTransport(NotificationTransport transport) {
        this.transport = transport;
    }

    public void setTransformer(NotificationTransformer transformer) {
        this.transformer = transformer;
    }
}
