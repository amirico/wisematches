package wisematches.server.web.services.notify.impl.publish.reducer;

import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notify.NotificationMover;
import wisematches.server.web.services.notify.impl.publish.NotificationPublisherWrapper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FilteringNotificationPublisher extends NotificationPublisherWrapper {
	private final Set<String> acceptedNotifications = new HashSet<String>();

	public FilteringNotificationPublisher() {
	}

	@Override
	public Future<Void> raiseNotification(String code, MemberPlayer player, NotificationMover mover, Map<String, Object> model) {
		if (!acceptedNotifications.contains(code)) {
			return null;
		}
		return notificationPublisher.raiseNotification(code, player, mover, model);
	}

	public void setAcceptedNotifications(Set<String> codes) {
		this.acceptedNotifications.clear();

		if (codes != null) {
			this.acceptedNotifications.addAll(codes);
		}
	}
}
