package wisematches.server.web.services.notify.impl.processor;

import wisematches.server.web.services.notify.*;

/**
 * {@code NotificationProcessor} is base interface for interaction with players. It allows send localized
 * messages to a player using differ systems, like email or internal messages system.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultNotificationProcessor implements NotificationProcessor {
    private boolean manageable;
    private NotificationTransport transport;
    private NotificationTransformer transformer;

    public DefaultNotificationProcessor() {
    }

    @Override
    public boolean isManageable() {
        return manageable;
    }

    @Override
    public boolean publishNotification(NotificationTemplate template) throws Exception {
        NotificationMessage message = transformer.convertNotification(template);
        transport.sendNotification(message);
        return true;
    }

    public void setManageable(boolean manageable) {
        this.manageable = manageable;
    }

    public void setTransport(NotificationTransport transport) {
        this.transport = transport;
    }

    public void setTransformer(NotificationTransformer transformer) {
        this.transformer = transformer;
    }
}
