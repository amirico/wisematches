package wisematches.server.web.services.notify.impl;

import wisematches.server.web.services.notify.Notification;
import wisematches.server.web.services.notify.NotificationException;
import wisematches.server.web.services.notify.NotificationScope;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationPublisher {
	String getName();

	NotificationScope getNotificationScope();


	void publishNotification(Notification notification) throws NotificationException;
}
