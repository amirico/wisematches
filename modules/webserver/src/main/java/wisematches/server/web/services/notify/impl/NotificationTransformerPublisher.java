package wisematches.server.web.services.notify.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.SchedulingTaskExecutor;
import wisematches.personality.account.Account;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notify.publisher.NotificationPublisher;
import wisematches.server.web.services.notify.publisher.NotificationOriginator;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class NotificationTransformerPublisher implements NotificationPublisher {
	private SchedulingTaskExecutor taskExecutor;
	private NotificationTransformer notificationTransformer;

	private static final Log log = LogFactory.getLog("wisematches.server.notify.transform");

	protected NotificationTransformerPublisher() {
	}

	@Override
	public Future<Void> raiseNotification(String code, Account account, NotificationOriginator originator, Map<String, Object> model) {
		if (account == null) {
			throw new NullPointerException("Player can't be null");
		}
		if (code == null) {
			throw new NullPointerException("Code can't be null");
		}
		if (originator == null) {
			throw new NullPointerException("Sender can't be null");
		}
		if (log.isDebugEnabled()) {
			log.debug("Put notification '" + code + "' into queue for " + account);
		}
		return taskExecutor.submit(new NotificationTask(code, account, originator, model));
	}

	@Override
	public final Future<Void> raiseNotification(String code, MemberPlayer player, NotificationOriginator originator, Map<String, Object> model) {
		return raiseNotification(code, player.getAccount(), originator, model);
	}

	protected abstract void raiseNotification(Notification notification) throws Exception;

	private void processNotification(String code, String template, Account account, NotificationOriginator originator, Map<String, Object> model) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Process notification '" + code + "[" + template + "]" + " ' for " + account);
		}
		try {
			raiseNotification(notificationTransformer.createNotification(code, template, account, originator, this, model));
		} catch (Exception ex) {
			log.error("Notification '" + code + "' for '" + account + "' can't be processed", ex);
			throw ex;
		} catch (Throwable th) {
			log.error("Notification '" + code + "' for '" + account + "' can't be processed", th);
			throw new Exception(th);
		}
	}

	public void setTaskExecutor(SchedulingTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public void setNotificationTransformer(NotificationTransformer notificationTransformer) {
		this.notificationTransformer = notificationTransformer;
	}

	private class NotificationTask implements Callable<Void> {
		private final String code;
		private final String template;
		private final Account account;
		private final NotificationOriginator originator;
		private final Map<String, Object> model;

		private NotificationTask(String code, String template, Account account, NotificationOriginator originator, Map<String, Object> model) {
			this.account = account;
			this.template = template;
			this.code = code;
			this.originator = originator;
			this.model = model;
		}

		@Override
		public Void call() throws Exception {
			processNotification(code, template, account, originator, model);
			return null;
		}
	}
}
