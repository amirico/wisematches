package wisematches.server.web.services.notify.impl.transform;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.publisher.NotificationPublisher;
import wisematches.server.web.services.notify.publisher.NotificationOriginator;
import wisematches.server.web.services.notify.impl.Notification;
import wisematches.server.web.services.notify.impl.NotificationTransformer;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FreeMarkerNotificationTransformer implements NotificationTransformer {
	protected MessageSource messageSource;
	protected Configuration freeMarkerConfig;

	public FreeMarkerNotificationTransformer() {
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
	public Notification createNotification(String code, String template, Account account, NotificationOriginator originator, NotificationPublisher publisher, Map<String, Object> model) throws Exception {
		final Locale locale = account.getLanguage().locale();

		final String subject = messageSource.getMessage("notify.subject." + code, null, locale);

		final Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("code", code);
		variables.put("locale", locale);
		variables.put("originator", originator);
		variables.put("subject", subject);
		variables.put("template", template);
		variables.put("principal", account);
		variables.put("publisher", publisher.getPublisherName());
		if (model != null) {
			variables.putAll(model);
		}

		final Template template2 = freeMarkerConfig.getTemplate("layout.ftl", locale, "UTF-8");
		final String message = FreeMarkerTemplateUtils.processTemplateIntoString(template2, variables);
		return new Notification(code, subject, message, account, originator);
	}

	public void setFreeMarkerConfig(Configuration freeMarkerConfig) {
		this.freeMarkerConfig = freeMarkerConfig;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
