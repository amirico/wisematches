package wisematches.server.services.notify.impl;

import wisematches.server.services.notify.Notification;
import wisematches.server.services.notify.NotificationException;
import wisematches.server.services.notify.NotificationScope;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationPublisher {
	String getName();

	NotificationScope getNotificationScope();


	void publishNotification(Notification notification) throws NotificationException;
}
