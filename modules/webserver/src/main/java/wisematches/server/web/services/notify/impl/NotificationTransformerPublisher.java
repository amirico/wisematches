package wisematches.server.web.services.notify.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.personality.account.Account;
import wisematches.personality.player.member.MemberPlayer;
import wisematches.server.web.services.notify.NotificationMover;
import wisematches.server.web.services.notify.NotificationPublisher;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class NotificationTransformerPublisher implements NotificationPublisher {
	private ExecutorService executorService;
	private NotificationTransformer notificationTransformer;

	private static final Log log = LogFactory.getLog("wisematches.server.notify.transform");

	protected NotificationTransformerPublisher() {
	}

	@Override
	public Future<Void> raiseNotification(String code, Account account, NotificationMover mover, Map<String, Object> model) {
		if (account == null) {
			throw new NullPointerException("Player can't be null");
		}
		if (code == null) {
			throw new NullPointerException("Code can't be null");
		}
		if (mover == null) {
			throw new NullPointerException("Sender can't be null");
		}
		if (log.isDebugEnabled()) {
			log.debug("Put notification '" + code + "' into queue for " + account);
		}
		return executorService.submit(new NotificationTask(code, account, mover, model));
	}

	@Override
	public final Future<Void> raiseNotification(String code, MemberPlayer player, NotificationMover mover, Map<String, Object> model) {
		return raiseNotification(code, player.getAccount(), mover, model);
	}

	protected abstract void raiseNotification(Notification notification) throws Exception;

	private void processNotification(String code, Account account, NotificationMover mover, Map<String, Object> model) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Process notification '" + code + "' for " + account);
		}
		try {
			raiseNotification(notificationTransformer.createNotification(code, account, mover, this, model));
		} catch (Exception ex) {
			log.error("Notification '" + code + "' for '" + account + "' can't be processed", ex);
			throw ex;
		} catch (Throwable th) {
			log.error("Notification '" + code + "' for '" + account + "' can't be processed", th);
			throw new Exception(th);
		}
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public void setNotificationTransformer(NotificationTransformer notificationTransformer) {
		this.notificationTransformer = notificationTransformer;
	}

	private class NotificationTask implements Callable<Void> {
		private final String code;
		private final Account account;
		private final NotificationMover mover;
		private final Map<String, Object> model;

		private NotificationTask(String code, Account account, NotificationMover mover, Map<String, Object> model) {
			this.account = account;
			this.code = code;
			this.mover = mover;
			this.model = model;
		}

		@Override
		public Void call() throws Exception {
			processNotification(code, account, mover, model);
			return null;
		}
	}
}
