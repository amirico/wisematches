package wisematches.server.web.services.notify;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationDistributorListener {
	void notificationRejected(Notification notification, PublicationType type);

	void notificationPublished(Notification notification, PublicationType type);
}
