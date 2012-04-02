package wisematches.server.web.services.notify.publisher.converter;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.NotificationCreator;
import wisematches.server.web.services.notify.NotificationDescriptor;
import wisematches.server.web.services.notify.publisher.NotificationConverter;
import wisematches.server.web.services.notify.publisher.NotificationMessage;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FreeMarkerNotificationConverter implements NotificationConverter {
    protected MessageSource messageSource;
    protected Configuration freeMarkerConfig;

    public FreeMarkerNotificationConverter() {
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public NotificationMessage createMessage(NotificationDescriptor descriptor, Account recipient, NotificationCreator creator, Object context) throws Exception {
        final Locale locale = recipient.getLanguage().locale();

        final String subject = messageSource.getMessage("notify.subject." + descriptor.getCode(), null, locale);

        final Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("code", descriptor.getCode());
        variables.put("locale", locale);
        variables.put("creator", creator);
        variables.put("context", context);
        variables.put("subject", subject);
        variables.put("template", descriptor.getTemplate());
        variables.put("principal", recipient);

        final Template template2 = freeMarkerConfig.getTemplate("layout.ftl", locale, "UTF-8");
        final String message = FreeMarkerTemplateUtils.processTemplateIntoString(template2, variables);
        return new NotificationMessage(subject, message, recipient, creator, descriptor);
    }

    public void setFreeMarkerConfig(Configuration freeMarkerConfig) {
        this.freeMarkerConfig = freeMarkerConfig;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
