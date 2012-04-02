package wisematches.server.web.services.notify.impl.publish.reducer;

import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.NotificationCreator;
import wisematches.server.web.services.notify.impl.NotificationDescription;

import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class NotificationInfo {
    final Account account;
    final NotificationCreator creator;
    final Map<String, Object> model;
    final NotificationDescription description;

    NotificationInfo(Account account, NotificationCreator creator, NotificationDescription description, Map<String, Object> model) {
        this.account = account;
        this.creator = creator;
        this.model = model;
        this.description = description;
    }
}
