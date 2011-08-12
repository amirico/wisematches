package wisematches.server.web.services.notice;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationPublisher {
	void publishNotification(Notification notification, boolean enabled);
}
