package wisematches.server.web.services.notify.impl.transform;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.hibernate.SessionFactory;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import wisematches.server.web.services.notify.Notification;
import wisematches.server.web.services.notify.NotificationMessage;
import wisematches.server.web.services.notify.NotificationTransformer;
import wisematches.server.web.services.notify.TransformationException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FreeMarkerNotificationTransformer implements NotificationTransformer {
	private String layoutTemplate;
	private MessageSource messageSource;
	private SessionFactory sessionFactory;
	private Configuration freeMarkerConfig;

	public FreeMarkerNotificationTransformer() {
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY, readOnly = true)
	public NotificationMessage createMessage(Notification notification) throws TransformationException {
		final Locale locale = notification.getRecipient().getLanguage().locale();

		final String subject = messageSource.getMessage("notify.subject." + notification.getCode(), null, locale);

		final Map<String, Object> variables = new HashMap<>();
		// info
		variables.put("code", notification.getCode());
		variables.put("template", notification.getTemplate());
		// people
		variables.put("creator", notification.getSender());
		variables.put("principal", notification.getRecipient());
		// common
		variables.put("locale", locale);
		variables.put("context", notification.getContext());

		try {
			final Template ft = freeMarkerConfig.getTemplate(layoutTemplate, locale, "UTF-8");
			final String message = FreeMarkerTemplateUtils.processTemplateIntoString(ft, variables);
			return new NotificationMessage(notification.getCode(), subject, message, notification.getRecipient(), notification.getSender());
		} catch (IOException | TemplateException ex) {
			throw new TransformationException(ex);
		}
	}

	public void setLayoutTemplate(String layoutTemplate) {
		this.layoutTemplate = layoutTemplate;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setFreeMarkerConfig(Configuration freeMarkerConfig) {
		this.freeMarkerConfig = freeMarkerConfig;
	}
}
