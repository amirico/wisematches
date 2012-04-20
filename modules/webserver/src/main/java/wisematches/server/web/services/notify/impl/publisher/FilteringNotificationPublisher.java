package wisematches.server.web.services.notify.impl.publisher;

import wisematches.server.web.services.notify.Notification;
import wisematches.server.web.services.notify.NotificationPublisher;
import wisematches.server.web.services.notify.PublicationException;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FilteringNotificationPublisher implements NotificationPublisher {
	private NotificationPublisher notificationPublisher;
	private final Set<String> allowedNotifications = new HashSet<String>();

	public FilteringNotificationPublisher() {
	}

	@Override
	public boolean publishNotification(Notification notification) throws PublicationException {
		return allowedNotifications.contains(notification.getCode()) && notificationPublisher.publishNotification(notification);
	}

	public void setAllowedNotifications(Set<String> allowedNotifications) {
		this.allowedNotifications.clear();

		if (allowedNotifications != null) {
			this.allowedNotifications.addAll(allowedNotifications);
		}
	}

	public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
		this.notificationPublisher = notificationPublisher;
	}
}
