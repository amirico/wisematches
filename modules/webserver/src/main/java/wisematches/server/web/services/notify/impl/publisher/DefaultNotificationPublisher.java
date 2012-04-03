package wisematches.server.web.services.notify.impl.publisher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import wisematches.personality.account.Account;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notify.*;
import wisematches.server.web.services.notify.impl.processor.DefaultNotificationProcessor;
import wisematches.server.web.services.notify.NotificationProcessor;
import wisematches.server.web.services.state.PlayerStateManager;

import java.util.Collection;
import java.util.concurrent.Future;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultNotificationPublisher implements NotificationPublisher {
	private SchedulingTaskExecutor taskExecutor;
	private PlayerStateManager playerStateManager;
	private NotificationManager notificationManager;
	private TransactionTemplate transactionTemplate;
	private Collection<NotificationProcessor> notificationProcessors;

	private static final Log log = LogFactory.getLog("wisematches.server.notify.distributor");

	public DefaultNotificationPublisher() {
	}

	@Override
	public Future<?> raiseNotification(final String code, final Account recipient, final NotificationCreator creator, final Object context) {
		final NotificationDescriptor descriptor = notificationManager.getDescriptor(code);
		if (descriptor == null) {
			throw new IllegalArgumentException("There is no notification with code " + code);
		}
		return taskExecutor.submit(new Runnable() {
			@Override
			public void run() {
				transactionTemplate.execute(new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status) {
						asd(descriptor, recipient, creator, context);
					}
				});
			}
		});
	}

	private void asd(NotificationDescriptor descriptor, Account recipient, NotificationCreator creator, Object context) {
		final String code = descriptor.getCode();
		final String template = descriptor.getTemplate();
		for (DefaultNotificationProcessor publisher : notificationProcessors) {
			if (publisher.isStateDepending() && descriptor.isOfflineOnly() && playerStateManager.isPlayerOnline(recipient)) {
				log.debug("Notification was ignored: incorrect player's state");
				continue;
			}
			if (publisher.isManageable() && !notificationManager.isNotificationEnabled(recipient, code)) {
				log.debug("Notification was ignored: disabled by player");
				continue;
			}

			try {
				publisher.publishNotification(code, template, recipient, creator, context);
			} catch (Exception e) {
				log.error("Notification can't be sent: code - " + code + ", publisher - " + publisher);
			}
		}
	}

	@Override
	public Future<?> raiseNotification(String code, MemberPlayer recipient, NotificationCreator creator, Object context) {
		return raiseNotification(code, recipient.getAccount(), creator, context);
	}


	public void setTaskExecutor(SchedulingTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void setPlayerStateManager(PlayerStateManager playerStateManager) {
		this.playerStateManager = playerStateManager;
	}

	public void setNotificationManager(NotificationManager notificationManager) {
		this.notificationManager = notificationManager;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		if (transactionManager == null) {
			transactionTemplate = null;
		} else {
			transactionTemplate = new TransactionTemplate(transactionManager, new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW));
		}
	}

	public void setNotificationProcessors(Collection<NotificationProcessor> notificationProcessors) {
		this.notificationProcessors = notificationProcessors;
	}
}
