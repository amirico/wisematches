package wisematches.server.web.services.notify.publisher;

import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.NotificationCreator;
import wisematches.server.web.services.notify.NotificationDescriptor;

/**
 * {@code NotificationPublisher} is base interface for interaction with players. It allows send localized
 * messages to a player using differ systems, like email or internal messages system.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class NotificationPublisher {
    private final NotificationConverter converter;
    private final NotificationTransport transport;

    public NotificationPublisher(NotificationConverter converter, NotificationTransport transport) {
        this.converter = converter;
        this.transport = transport;
    }

    public void publishNotification(NotificationDescriptor descriptor, Account recipient, NotificationCreator creator, Object context) throws Exception {
        NotificationMessage message = converter.createMessage(descriptor, recipient, creator, context);
        transport.sendNotification(message);
    }
}
