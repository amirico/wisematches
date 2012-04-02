package wisematches.server.web.services.notify.publisher.transport;

import wisematches.server.web.services.notify.publisher.NotificationMessage;
import wisematches.server.web.services.notify.publisher.NotificationTransport;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MailNotificationTransport implements NotificationTransport {
    public MailNotificationTransport() {
    }

    @Override
    public void sendNotification(NotificationMessage message) {
        throw new UnsupportedOperationException("TODO: not implemented");
    }
}
