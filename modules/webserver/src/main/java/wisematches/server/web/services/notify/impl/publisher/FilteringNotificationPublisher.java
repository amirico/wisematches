package wisematches.server.web.services.notify.impl.publisher;

import wisematches.server.web.services.notify.NotificationPublisherOld;
import wisematches.server.web.services.notify.PublicationException;
import wisematches.server.web.services.notify.impl.delivery.DefaultNotificationDeliveryService;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 * @deprecated NotificationScope must be used instead.
 */
@Deprecated
public class FilteringNotificationPublisher implements NotificationPublisherOld {
	private NotificationPublisherOld notificationPublisher;
	private final Set<String> allowedNotifications = new HashSet<String>();

	public FilteringNotificationPublisher() {
	}

	@Override
	public boolean publishNotification(DefaultNotificationDeliveryService.NotificationOld notification) throws PublicationException {
		return allowedNotifications.contains(notification.getDescriptor().getCode()) && notificationPublisher.publishNotification(notification);
	}

	public void setAllowedNotifications(Set<String> allowedNotifications) {
		this.allowedNotifications.clear();

		if (allowedNotifications != null) {
			this.allowedNotifications.addAll(allowedNotifications);
		}
	}

	public void setNotificationPublisher(NotificationPublisherOld notificationPublisher) {
		this.notificationPublisher = notificationPublisher;
	}
}
