package wisematches.server.web.services.notify;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationTransformer {
	NotificationMessage convertNotification(NotificationTemplate notificationTemplate) throws Exception;
}
