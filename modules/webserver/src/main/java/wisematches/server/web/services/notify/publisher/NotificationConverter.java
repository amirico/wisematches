package wisematches.server.web.services.notify.publisher;

import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.NotificationCreator;
import wisematches.server.web.services.notify.NotificationDescriptor;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationConverter {
    NotificationMessage createMessage(NotificationDescriptor descriptor, Account recipient, NotificationCreator creator, Object context) throws Exception;
}
