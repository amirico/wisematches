package wisematches.server.web.services.notify.impl.publish.reducer;

import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.NotificationDescription;
import wisematches.server.web.services.notify.NotificationSender;

import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class NotificationInfo {
	final Account account;
	final NotificationSender sender;
	final Map<String, Object> model;
	final NotificationDescription description;

	NotificationInfo(Account account, NotificationSender sender, NotificationDescription description, Map<String, Object> model) {
		this.account = account;
		this.sender = sender;
		this.model = model;
		this.description = description;
	}
}
