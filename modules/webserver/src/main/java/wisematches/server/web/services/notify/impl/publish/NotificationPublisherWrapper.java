package wisematches.server.web.services.notify.impl.publish;

import wisematches.server.web.services.notify.publisher.NotificationPublisher;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class NotificationPublisherWrapper implements NotificationPublisher {
	protected NotificationPublisher notificationPublisher;

	protected NotificationPublisherWrapper() {
	}

	@Override
	public String getPublisherName() {
		return notificationPublisher.getPublisherName();
	}

	public void setNotificationPublisher(NotificationPublisher notificationPublisher) {
		this.notificationPublisher = notificationPublisher;
	}
}
