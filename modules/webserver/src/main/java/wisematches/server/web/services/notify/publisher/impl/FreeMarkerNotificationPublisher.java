package wisematches.server.web.services.notify.publisher.impl;

import wisematches.personality.account.Account;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notify.publisher.NotificationOriginator;
import wisematches.server.web.services.notify.publisher.NotificationPublisher;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class FreeMarkerNotificationPublisher implements NotificationPublisher {
	private final String publisherName;

	protected FreeMarkerNotificationPublisher(String publisherName) {
		this.publisherName = publisherName;
	}

	@Override
	public String getPublisherName() {
		return publisherName;
	}

	@Override
	public Future<Void> raiseNotification(String code, Account account, NotificationOriginator originator, Map<String, Object> model) {
		return asd();
	}

	@Override
	public Future<Void> raiseNotification(String code, MemberPlayer player, NotificationOriginator originator, Map<String, Object> model) {
		return raiseNotification(code, player.getAccount(), originator, model);
	}

	protected abstract Future<Void> asd();
}
