package wisematches.server.web.services.notify.impl.transport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wisematches.playground.message.MessageManager;
import wisematches.server.web.services.notify.NotificationMessage;
import wisematches.server.web.services.notify.NotificationTransport;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MessageNotificationTransport implements NotificationTransport {
    private MessageManager messageManager;

    private static final Log log = LogFactory.getLog("wisematches.server.notify.message");

    public MessageNotificationTransport() {
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public void sendNotification(final NotificationMessage message) {
        if (log.isDebugEnabled()) {
            log.debug("Send message notification '" + message.getCode() + "' to player: " + message.getAccount());
        }
        messageManager.sendNotification(message.getAccount(), message.getMessage(), true);
    }

    public void setMessageManager(MessageManager messageManager) {
        this.messageManager = messageManager;
    }
}
