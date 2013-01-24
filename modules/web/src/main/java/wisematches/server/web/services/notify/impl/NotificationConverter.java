package wisematches.server.web.services.notify.impl;

import wisematches.core.personality.player.account.Account;
import wisematches.server.web.services.notify.Notification;
import wisematches.server.web.services.notify.NotificationSender;
import wisematches.server.web.services.notify.TransformationException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationConverter {
	Notification createNotification(String code, Account target, NotificationSender sender, Object context) throws TransformationException;
}
