package wisematches.server.web.services.notify.impl.delivery.converter;

import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.DeliveryCallback;
import wisematches.server.web.services.notify.NotificationDescriptor;
import wisematches.server.web.services.notify.NotificationSender;
import wisematches.server.web.services.notify.TransformationException;
import wisematches.server.web.services.notify.impl.delivery.TrackingNotificationMessage;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface NotificationConverter {
    TrackingNotificationMessage createMessage(NotificationDescriptor descriptor, Account recipient, NotificationSender sender, Object context, DeliveryCallback callback) throws TransformationException;
}
