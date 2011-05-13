package wisematches.server.mail.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import wisematches.server.mail.MailException;
import wisematches.server.mail.MailSender;
import wisematches.server.mail.MailService;
import wisematches.server.personality.account.Account;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AsynchronousMailService implements MailService {
	private MailService originalMailService;
	private ExecutorService executorService = Executors.newSingleThreadExecutor(new CustomizableThreadFactory("MailSender"));

	protected static final Log log = LogFactory.getLog("wisematches.server.mail");

	public AsynchronousMailService() {
	}

	@Override
	public void sendMail(MailSender from, Account to, String msgCode, Map<String, ?> model) {
		executorService.submit(new PlayerMailJob(from, to, msgCode, model));
	}

	@Override
	public void sendWarrantyMail(MailSender from, Account to, String msgCode, Map<String, ?> model) throws MailException {
		originalMailService.sendWarrantyMail(from, to, msgCode, model);
	}

	@Override
	public void sendSupportRequest(String subject, String msgCode, Map<String, ?> model) throws MailException {
		originalMailService.sendSupportRequest(subject, msgCode, model);
	}

	public void setExecutorService(ExecutorService executorService) {
		if (executorService == null) {
			throw new NullPointerException("Executor can't be null");
		}
		this.executorService = executorService;
	}

	public void setOriginalMailService(MailService originalMailService) {
		if (originalMailService == null) {
			throw new NullPointerException("Original Mail sender can't be null");
		}
		this.originalMailService = originalMailService;
	}

	public void destroy() {
		executorService.shutdown();
	}

	private class PlayerMailJob implements Runnable {
		private final MailSender from;
		private final Account to;
		private final String msgCode;
		private final Map<String, ?> model;

		private PlayerMailJob(MailSender from, Account to, String msgCode, Map<String, ?> model) {
			this.from = from;
			this.to = to;
			this.msgCode = msgCode;
			this.model = model;
		}

		public void run() {
			try {
				originalMailService.sendWarrantyMail(from, to, msgCode, model);
			} catch (Throwable th) {
				log.error("Mail can't be sent to support team", th);
			}
		}
	}
}
