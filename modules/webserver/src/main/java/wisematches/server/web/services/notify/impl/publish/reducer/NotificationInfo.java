package wisematches.server.web.services.notify.impl.publish.reducer;

import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.NotificationDescription;
import wisematches.server.web.services.notify.NotificationMover;

import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class NotificationInfo {
	final Account account;
	final NotificationMover mover;
	final Map<String, Object> model;
	final NotificationDescription description;

	NotificationInfo(Account account, NotificationMover mover, NotificationDescription description, Map<String, Object> model) {
		this.account = account;
		this.mover = mover;
		this.model = model;
		this.description = description;
	}
}
