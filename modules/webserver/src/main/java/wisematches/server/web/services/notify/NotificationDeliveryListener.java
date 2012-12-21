package wisematches.server.web.services.notify;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationDeliveryListener {
	void notificationPublished(Notification notification);

	void notificationRejected(Notification notification, NotificationPublisher publisher, PublicationException exception);
}
