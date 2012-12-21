package wisematches.server.web.services.notify.impl.delivery;

import wisematches.personality.account.Account;
import wisematches.server.web.services.notify.NotificationDescriptor;
import wisematches.server.web.services.notify.NotificationSender;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class NotificationContainer {
	private final Account recipient;
	private final NotificationSender sender;
	private final NotificationDescriptor descriptor;
	private final Object context;

	NotificationContainer(Account recipient, NotificationSender sender, NotificationDescriptor descriptor, Object context) {
		this.recipient = recipient;
		this.sender = sender;
		this.descriptor = descriptor;
		this.context = context;
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

