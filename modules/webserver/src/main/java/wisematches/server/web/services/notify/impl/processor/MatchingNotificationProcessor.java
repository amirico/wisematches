package wisematches.server.web.services.notify.impl.processor;

import wisematches.server.web.services.notify.NotificationProcessor;
import wisematches.server.web.services.notify.NotificationTemplate;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MatchingNotificationProcessor extends FilteringNotificationProcessor {
	private final Set<String> allowedNotifications = new HashSet<String>();

	public MatchingNotificationProcessor(NotificationProcessor processor) {
		super(processor);
	}

	@Override
	public boolean publishNotification(NotificationTemplate template) throws Exception {
		return allowedNotifications.contains(template.getCode()) && super.publishNotification(template);
	}

	public void setAllowedNotifications(Set<String> allowedNotifications) {
		this.allowedNotifications.clear();

		if (allowedNotifications != null) {
			this.allowedNotifications.addAll(allowedNotifications);
		}
	}
}
