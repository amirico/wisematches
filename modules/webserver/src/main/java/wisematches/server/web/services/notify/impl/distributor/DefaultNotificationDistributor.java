package wisematches.server.web.services.notify.impl.distributor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.TaskExecutor;
import wisematches.personality.account.Account;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notify.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DefaultNotificationDistributor implements NotificationDistributor {
	private TaskExecutor taskExecutor;
	private NotificationManager notificationManager;
	private final Collection<NotificationPublisher> unimpededPublishers = new ArrayList<NotificationPublisher>();
	private final Collection<NotificationPublisher> customizablePublishers = new ArrayList<NotificationPublisher>();

	private static final Log log = LogFactory.getLog("wisematches.server.notify.distributor");

	public DefaultNotificationDistributor() {
	}

	@Override
	public void raiseNotification(final String code, final Account recipient, final NotificationCreator creator, final Object context) {
		final NotificationDescriptor descriptor = notificationManager.getDescriptor(code);
		if (descriptor == null) {
			throw new IllegalArgumentException("There is no notification with code " + code);
		}
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				processNotification(descriptor, recipient, creator, context);
			}
		});
	}

	private void processNotification(NotificationDescriptor descriptor, Account recipient, NotificationCreator creator, Object context) {
		final String code = descriptor.getCode();
		final NotificationTemplate template = new NotificationTemplate(code, descriptor.getTemplate(), recipient, creator, context);

		for (NotificationPublisher publisher : unimpededPublishers) {
			try {
				publisher.publishNotification(template);
			} catch (Exception e) {
				log.error("Notification can't be sent: code - " + code + ", distributor - " + publisher);
			}
		}

		if (notificationManager.isNotificationEnabled(recipient, code)) {
			for (NotificationPublisher publisher : customizablePublishers) {
				try {
					publisher.publishNotification(template);
				} catch (Exception e) {
					log.error("Notification can't be sent: code - " + code + ", distributor - " + publisher);
				}
			}
		} else {
			log.debug("Notification '" + descriptor.getCode() + "' was ignored: disabled by player");
		}
	}

	@Override
	public void raiseNotification(String code, MemberPlayer recipient, NotificationCreator creator, Object context) {
		raiseNotification(code, recipient.getAccount(), creator, context);
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void setNotificationManager(NotificationManager notificationManager) {
		this.notificationManager = notificationManager;
	}

	public void setUnimpededPublishers(Collection<NotificationPublisher> unimpededPublishers) {
		this.unimpededPublishers.clear();
		if (unimpededPublishers != null) {
			this.unimpededPublishers.addAll(unimpededPublishers);
		}
	}

	public void setCustomizablePublishers(Collection<NotificationPublisher> customizablePublishers) {
		this.customizablePublishers.clear();
		if (customizablePublishers != null) {
			this.customizablePublishers.addAll(customizablePublishers);
		}
	}
}
