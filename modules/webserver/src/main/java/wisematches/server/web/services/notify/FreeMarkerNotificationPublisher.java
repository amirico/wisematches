package wisematches.server.web.services.notify;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.context.MessageSource;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import wisematches.personality.player.member.MemberPlayer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class FreeMarkerNotificationPublisher implements NotificationPublisher {
	private ExecutorService executorService;

	protected MessageSource messageSource;
	protected Configuration freeMarkerConfig;

	protected FreeMarkerNotificationPublisher() {
	}

	@Override
	public Future<Void> raiseNotification(MemberPlayer player, String code, NotificationSender sender, Map<String, Object> model) {
		if (player == null) {
			throw new NullPointerException("Player can't be null");
		}
		if (code == null) {
			throw new NullPointerException("Code can't be null");
		}
		if (sender == null) {
			throw new NullPointerException("Sender can't be null");
		}

		final Template template;
		final Locale locale = player.getLanguage().locale();
		final String subject = getNotifySubject(code, locale);
		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("code", code);
		variables.put("locale", locale);
		variables.put("player", player);
		variables.put("sender", sender);
		variables.put("subject", subject);
		if (model != null) {
			variables.putAll(model);
		}
		try {
			template = getNotifyTemplate(code, locale);
		} catch (IOException e) {
			throw new IllegalArgumentException("Template for code '" + code + "' can't be loaded", e);
		}
		return executorService.submit(new NotificationTask(player, subject, template, sender, variables));
	}

	protected String getNotifySubject(String code, Locale locale) {
		return messageSource.getMessage("notify.subject." + code, null, "WiseMatches Notification", locale);
	}

	protected Template getNotifyTemplate(String code, Locale locale) throws IOException {
		return freeMarkerConfig.getTemplate(code + ".ftl", locale, "UTF-8");
	}

	protected abstract void raiseNotification(String subject, String message, MemberPlayer player, NotificationSender sender) throws Exception;

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setFreeMarkerConfig(Configuration freeMarkerConfig) {
		this.freeMarkerConfig = freeMarkerConfig;
	}

	private class NotificationTask implements Callable<Void> {
		private final MemberPlayer player;
		private final String subject;
		private final Template template;
		private final NotificationSender sender;
		private final Map<String, Object> variables;

		public NotificationTask(MemberPlayer player, String subject, Template template, NotificationSender sender, Map<String, Object> variables) {
			this.player = player;
			this.subject = subject;
			this.template = template;
			this.sender = sender;
			this.variables = variables;
		}

		@Override
		public Void call() throws Exception {
			final String message = FreeMarkerTemplateUtils.processTemplateIntoString(template, variables);
			raiseNotification(subject, message, player, sender);
			return null;
		}
	}
}