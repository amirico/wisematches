package wisematches.server.web.services.notify;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationProcessor {
	boolean isManageable();

	boolean publishNotification(NotificationTemplate template) throws Exception;
}
