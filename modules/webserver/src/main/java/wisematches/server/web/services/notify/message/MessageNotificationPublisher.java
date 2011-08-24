package wisematches.server.web.services.notify.message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.playground.message.MessageManager;
import wisematches.server.web.services.notify.FreeMarkerNotificationPublisher;
import wisematches.server.web.services.notify.NotificationSender;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MessageNotificationPublisher extends FreeMarkerNotificationPublisher {
	private MessageManager messageManager;

	private static final Log log = LogFactory.getLog("wisematches.server.notice.message");

	public MessageNotificationPublisher() {
	}

	@Override
	protected void raiseNotification(String subject, String message, MemberPlayer player, NotificationSender sender) throws Exception {
		messageManager.sendNotification(player, message, true);
	}

	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}
}
