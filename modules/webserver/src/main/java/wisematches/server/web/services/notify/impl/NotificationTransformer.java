package wisematches.server.web.services.notify.impl;

import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.NotificationCreator;
import wisematches.server.web.services.notify.publisher.NotificationPublisher;

import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationTransformer {
    Notification createNotification(String code, String template, Account account, NotificationCreator creator, NotificationPublisher publisher, Map<String, Object> model) throws Exception;
}
