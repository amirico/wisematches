package wisematches.server.web.services.notify.impl.publish.reducer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class NotificationContainer {
	private final Set<String> series = new HashSet<String>();
	private final List<NotificationInfo> notifications = new ArrayList<NotificationInfo>();

	NotificationContainer() {
	}

	boolean addNotification(NotificationInfo info) {
		final String s = info.description.getSeries();
		if (s != null && !s.isEmpty()) {
			if (series.contains(s)) {
				return false;
			} else {
				series.add(s);
			}
		}
		notifications.add(info);
		return true;
	}

	List<NotificationInfo> getNotifications() {
		return notifications;
	}

	void clear() {
		series.clear();
		notifications.clear();
	}
}
