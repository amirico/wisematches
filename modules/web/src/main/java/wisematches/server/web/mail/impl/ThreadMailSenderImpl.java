package wisematches.server.web.mail.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import wisematches.server.player.Player;
import wisematches.server.web.mail.FromTeam;
import wisematches.server.web.mail.MailSender;
import wisematches.server.web.mail.ToTeam;

import java.util.EnumSet;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Implementation of {@code MailSender} that sends all E-Mails in separate threads.
 * <p/>
 * This implemetation uses {@code Executor} to process mail sendind. By default signle thread executor is used (
 * see {@link Executors#newSingleThreadExecutor} method for more info).
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ThreadMailSenderImpl extends AbstractMailSender {
	private MailSender originalMailSender;
	private ExecutorService executorService = Executors.newSingleThreadExecutor(new CustomizableThreadFactory("MailSender"));

	private static final Log log = LogFactory.getLog(ThreadMailSenderImpl.class);

	public void sendMail(FromTeam from, Player player, String resource, Map<String, ?> model) {
		executorService.execute(new PlayerMailJob(from, player, resource, model));
	}

	public void sendSystemMail(FromTeam from, EnumSet<ToTeam> to, String subject, String template, Map<String, ?> model) {
		executorService.execute(new SystemMailJob(from, to, subject, template, model));
	}

	public void setExecutorService(ExecutorService executorService) {
		if (executorService == null) {
			throw new NullPointerException("Executor can't be null");
		}
		this.executorService = executorService;
	}

	public void setOriginalMailSender(MailSender originalMailSender) {
		if (originalMailSender == null) {
			throw new NullPointerException("Original Mail sender can't be null");
		}
		this.originalMailSender = originalMailSender;
	}

	public void destroy() {
		executorService.shutdown();
	}

	private class PlayerMailJob implements Runnable {
		private final FromTeam from;
		private final Player player;
		private final String resource;
		private final Map<String, ?> model;

		private PlayerMailJob(FromTeam from, Player player, String resource, Map<String, ?> model) {
			this.from = from;
			this.player = player;
			this.resource = resource;
			this.model = model;
		}

		public void run() {
			try {
				originalMailSender.sendMail(from, player, resource, model);
			} catch (Throwable th) {
				log.error("Mail can't be sent to player", th);
			}
		}
	}

	private class SystemMailJob implements Runnable {
		private final FromTeam from;
		private final EnumSet<ToTeam> to;
		private final String subject;
		private final String template;
		private final Map<String, ?> model;

		private SystemMailJob(FromTeam from, EnumSet<ToTeam> to, String subject, String template, Map<String, ?> model) {
			this.from = from;
			this.to = to;
			this.subject = subject;
			this.template = template;
			this.model = model;
		}

		public void run() {
			try {
				originalMailSender.sendSystemMail(from, to, subject, template, model);
			} catch (Throwable th) {
				log.error("Mail can't be sent to support team", th);
			}
		}
	}
}
