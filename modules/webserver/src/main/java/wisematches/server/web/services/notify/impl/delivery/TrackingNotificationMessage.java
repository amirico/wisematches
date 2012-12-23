package wisematches.server.web.services.notify.impl.delivery;

import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.DeliveryCallback;
import wisematches.server.web.services.notify.NotificationMessage;
import wisematches.server.web.services.notify.NotificationSender;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TrackingNotificationMessage extends NotificationMessage {
    private final DeliveryCallback deliveryCallback;

    public TrackingNotificationMessage(String code, String subject, String message, Account account, NotificationSender sender, DeliveryCallback deliveryCallback) {
        super(code, subject, message, account, sender);
        this.deliveryCallback = deliveryCallback;
    }

    DeliveryCallback getDeliveryCallback() {
        return deliveryCallback;
    }
}
