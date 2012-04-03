package wisematches.server.web.services.notify.impl.processor;

import wisematches.server.web.services.notify.NotificationProcessor;
import wisematches.server.web.services.notify.NotificationTemplate;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FilteringNotificationProcessor implements NotificationProcessor {
	protected final NotificationProcessor notificationProcessor;

	public FilteringNotificationProcessor(NotificationProcessor notificationProcessor) {
		this.notificationProcessor = notificationProcessor;
	}

	@Override
	public boolean isManageable() {
		return notificationProcessor.isManageable();
	}

	@Override
	public boolean publishNotification(NotificationTemplate template) throws Exception {
		return notificationProcessor.publishNotification(template);
	}
}
