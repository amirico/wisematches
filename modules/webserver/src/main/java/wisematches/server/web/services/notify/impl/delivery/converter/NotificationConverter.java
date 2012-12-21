package wisematches.server.web.services.notify.impl.delivery.converter;

import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.Notification;
import wisematches.server.web.services.notify.NotificationDescriptor;
import wisematches.server.web.services.notify.NotificationSender;
import wisematches.server.web.services.notify.TransformationException;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationConverter {
	Notification createMessage(NotificationDescriptor descriptor, Account recipient, NotificationSender sender, Object context) throws TransformationException;
}
