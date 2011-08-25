package wisematches.server.web.services.notify.impl.publish.message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.playground.message.MessageManager;
import wisematches.server.web.services.notify.impl.Notification;
import wisematches.server.web.services.notify.impl.NotificationTransformerPublisher;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MessageNotificationPublisher extends NotificationTransformerPublisher {
	private MessageManager messageManager;

	private static final Log log = LogFactory.getLog("wisematches.server.notify.message");

	public MessageNotificationPublisher() {
	}

	@Override
	public String getPublisherName() {
		return "message";
	}

	@Override
	protected void raiseNotification(Notification notification) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Send message '" + notification.getCode() + "' to player: " + notification.getPlayer());
		}
		messageManager.sendNotification(notification.getPlayer(), notification.getMessage());
	}

	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}
}
