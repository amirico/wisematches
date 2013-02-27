package wisematches.server.services.notify.impl.publisher;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.core.Player;
import wisematches.server.services.message.MessageManager;
import wisematches.server.services.notify.Notification;
import wisematches.server.services.notify.NotificationScope;
import wisematches.server.services.notify.impl.NotificationPublisher;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MessageNotificationPublisher implements NotificationPublisher {
	private MessageManager messageManager;

	private static final Logger log = LoggerFactory.getLogger("wisematches.notification.MessagePublisher");

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
		final Player target = notification.getTarget();
		log.debug("Send message notification '{}' to player: {}", notification.getCode(), target);
		messageManager.sendNotification(target, notification.getMessage(), true);
	}

	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}
}
