package wisematches.server.web.services.notify.impl.delivery;

import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.DeliveryCallback;
import wisematches.server.web.services.notify.NotificationDescriptor;
import wisematches.server.web.services.notify.NotificationSender;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
class NotificationContainer {
    private final Account recipient;
    private final NotificationSender sender;
    private final NotificationDescriptor descriptor;
    private final Object context;
    private final DeliveryCallback deliveryCallback;

    public NotificationContainer(Account recipient, NotificationSender sender, NotificationDescriptor descriptor, Object context, DeliveryCallback callback) {
        this.recipient = recipient;
        this.sender = sender;
        this.descriptor = descriptor;
        this.context = context;
        this.deliveryCallback = callback;
    }

    public Account getRecipient() {
        return recipient;
    }

    public NotificationSender getSender() {
        return sender;
    }

    public NotificationDescriptor getDescriptor() {
        return descriptor;
    }

    public Object getContext() {
        return context;
    }

    public DeliveryCallback getDeliveryCallback() {
        return deliveryCallback;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("NotificationContainer");
        sb.append("{recipient=").append(recipient);
        sb.append(", sender=").append(sender);
        sb.append(", descriptor=").append(descriptor);
        sb.append(", context=").append(context);
        sb.append('}');
        return sb.toString();
    }
}

