package wisematches.server.web.services.notify;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationTransformer {
    NotificationMessage transformNotification(NotificationTemplate notificationTemplate) throws TransformationException;
}
