package wisematches.server.web.services.notify.impl.delivery.converter.impl;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.Notification;
import wisematches.server.web.services.notify.NotificationDescriptor;
import wisematches.server.web.services.notify.NotificationSender;
import wisematches.server.web.services.notify.TransformationException;
import wisematches.server.web.services.notify.impl.delivery.converter.NotificationConverter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FreeMarkerNotificationConverter implements NotificationConverter {
	private String layoutTemplate;
	private MessageSource messageSource;
	private Configuration freeMarkerConfig;

	public FreeMarkerNotificationConverter() {
	}

	@Override
	@Transactional(propagation = Propagation.MANDATORY, readOnly = true)
	public Notification createMessage(NotificationDescriptor descriptor, Account recipient, NotificationSender sender, Object context) throws TransformationException {
		final Locale locale = recipient.getLanguage().locale();

		final String subject = messageSource.getMessage("notify.subject." + descriptor.getCode(), null, locale);

		final Map<String, Object> variables = new HashMap<>();
		// info
		variables.put("code", descriptor.getCode());
		variables.put("template", descriptor.getTemplate());
		// people
		variables.put("creator", sender);
		variables.put("principal", recipient);
		// common
		variables.put("locale", locale);
		variables.put("context", context);

		try {
			final Template ft = freeMarkerConfig.getTemplate(layoutTemplate, locale, "UTF-8");
			final String message = FreeMarkerTemplateUtils.processTemplateIntoString(ft, variables);
			return new Notification(descriptor.getCode(), subject, message, recipient, sender);
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
