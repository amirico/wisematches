package wisematches.server.web.services.notify.impl.transform;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.context.MessageSource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import wisematches.server.web.services.notify.NotificationMessage;
import wisematches.server.web.services.notify.NotificationTemplate;
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
    private Configuration freeMarkerConfig;

    public FreeMarkerNotificationTransformer() {
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY, readOnly = true)
    public NotificationMessage transformNotification(NotificationTemplate notificationTemplate) throws TransformationException {
        final Locale locale = notificationTemplate.getRecipient().getLanguage().locale();

        final String subject = messageSource.getMessage("notify.subject." + notificationTemplate.getCode(), null, locale);

        final Map<String, Object> variables = new HashMap<String, Object>();
        // info
        variables.put("code", notificationTemplate.getCode());
        variables.put("template", notificationTemplate.getTemplate());
        // people
        variables.put("creator", notificationTemplate.getCreator());
        variables.put("principal", notificationTemplate.getRecipient());
        // common
        variables.put("locale", locale);
        variables.put("context", notificationTemplate.getContext());

        try {
            final Template ft = freeMarkerConfig.getTemplate(layoutTemplate, locale, "UTF-8");
            final String message = FreeMarkerTemplateUtils.processTemplateIntoString(ft, variables);
            return new NotificationMessage(notificationTemplate.getCode(), subject, message, notificationTemplate.getRecipient(), notificationTemplate.getCreator());
        } catch (IOException ex) {
            throw new TransformationException(ex);
        } catch (TemplateException ex) {
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
