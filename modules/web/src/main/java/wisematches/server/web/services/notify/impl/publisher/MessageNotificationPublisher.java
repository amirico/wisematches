package wisematches.server.web.services.notify.impl.publisher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.personality.player.account.Account;
import wisematches.playground.message.MessageManager;
import wisematches.server.web.services.notify.Notification;
import wisematches.server.web.services.notify.NotificationScope;
import wisematches.server.web.services.notify.impl.NotificationPublisher;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MessageNotificationPublisher implements NotificationPublisher {
	private MessageManager messageManager;

	private static final Log log = LogFactory.getLog("wisematches.server.notify.message");

	public MessageNotificationPublisher() {
	}

	@Override
	public String getName() {
		return "message";
	}

	@Override
	public NotificationScope getNotificationScope() {
		return NotificationScope.INTERNAL;
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY, readOnly = true)
	public void publishNotification(final Notification notification) {
		final Account target = notification.getTarget();
		if (log.isDebugEnabled()) {
			log.debug("Send message notification '" + notification.getCode() + "' to player: " + target);
		}
		messageManager.sendNotification(target, notification.getMessage(), true);
	}

	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}
}
