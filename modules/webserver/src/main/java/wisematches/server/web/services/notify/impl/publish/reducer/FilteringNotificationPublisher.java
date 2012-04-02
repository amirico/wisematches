package wisematches.server.web.services.notify.impl.publish.reducer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.personality.account.Account;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notify.publisher.NotificationOriginator;
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

	private static final Log log = LogFactory.getLog("wisematches.server.notify.filter");

	public FilteringNotificationPublisher() {
	}

	@Override
	public Future<Void> raiseNotification(String code, Account account, NotificationOriginator originator, Map<String, Object> model) {
		if (!acceptedNotifications.contains(code)) {
			if (log.isDebugEnabled()) {
				log.debug("Notification '" + code + "' was rejected: not acceptable");
			}
			return null;
		}
		return notificationPublisher.raiseNotification(code, account, originator, model);
	}

	@Override
	public Future<Void> raiseNotification(String code, MemberPlayer player, NotificationOriginator originator, Map<String, Object> model) {
		return raiseNotification(code, player.getAccount(), originator, model);
	}

	public void setAcceptedNotifications(Set<String> codes) {
		this.acceptedNotifications.clear();

		if (codes != null) {
			this.acceptedNotifications.addAll(codes);
		}
	}
}
