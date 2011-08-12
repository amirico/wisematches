package wisematches.server.web.services.notice.impl.publisher;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import wisematches.personality.Language;
import wisematches.playground.message.MessageManager;
import wisematches.server.web.services.notice.Notification;
import wisematches.server.web.services.notice.NotificationDescription;
import wisematches.server.web.services.notice.NotificationPublisher;

import java.io.IOException;
import java.util.Collections;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MessageNotificationPublisher implements NotificationPublisher {
	private Configuration configuration;
	private MessageManager messageManager;

	private static final Log log = LogFactory.getLog("wisematches.server.notice.message");

	public MessageNotificationPublisher() {
	}

	@Override
	public void publishNotification(Notification notification, boolean enabled) {
		final NotificationDescription description = notification.getDescription();
		if (description.isEvenOnline()) {
			if (log.isDebugEnabled()) {
				log.debug("Send message notification " + notification.getDescription().getName());
			}

			try {
				final Template template = getTemplate(notification, notification.getMember().getLanguage());
				final String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, Collections.singletonMap("context", notification.getContext()));
				messageManager.sendNotification(notification.getMember(), text, true);
			} catch (Exception ex) {
				log.error("Notification message can't be sent", ex);
			}
		}
	}

	protected Template getTemplate(Notification notification, Language language) throws IOException {
		return configuration.getTemplate(notification.getDescription().getName().replaceAll("\\.", "/") + ".ftl", language.locale(), "UTF-8");
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void setMessageManager(MessageManager messageManager) {
		this.messageManager = messageManager;
	}
}
