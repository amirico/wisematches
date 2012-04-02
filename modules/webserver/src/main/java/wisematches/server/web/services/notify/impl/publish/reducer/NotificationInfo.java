package wisematches.server.web.services.notify.impl.publish.reducer;

import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.NotificationDescription;
import wisematches.server.web.services.notify.publisher.NotificationOriginator;

import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class NotificationInfo {
	final Account account;
	final NotificationOriginator originator;
	final Map<String, Object> model;
	final NotificationDescription description;

	NotificationInfo(Account account, NotificationOriginator originator, NotificationDescription description, Map<String, Object> model) {
		this.account = account;
		this.originator = originator;
		this.model = model;
		this.description = description;
	}
}
