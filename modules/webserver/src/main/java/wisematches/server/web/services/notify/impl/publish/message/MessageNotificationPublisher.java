package wisematches.server.web.services.notify.impl.publish.message;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.playground.message.MessageManager;
import wisematches.server.web.services.notify.impl.Notification;
import wisematches.server.web.services.notify.impl.NotificationTransformerPublisher;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MessageNotificationPublisher extends NotificationTransformerPublisher {
	private MessageManager messageManager;
	private TransactionTemplate transactionTemplate;

	private static final Log log = LogFactory.getLog("wisematches.server.notify.message");

	public MessageNotificationPublisher() {
	}

	@Override
	public String getPublisherName() {
		return "message";
	}

	@Override
	protected void raiseNotification(final Notification notification) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Send message notification '" + notification.getCode() + "' to player: " + notification.getAccount());
		}
		transactionTemplate.execute(new TransactionCallback<Object>() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				messageManager.sendNotification(notification.getAccount(), notification.getMessage());
				return null;
			}
		});
	}

	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}


	public void setTransactionRequiresNew(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
}
