package wisematches.server.web.services.notify.impl.processor;

import wisematches.server.web.services.notify.NotificationProcessor;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DependingNotificationProcessor extends FilteringNotificationProcessor {
	public DependingNotificationProcessor(NotificationProcessor notificationProcessor) {
		super(notificationProcessor);
	}
}
