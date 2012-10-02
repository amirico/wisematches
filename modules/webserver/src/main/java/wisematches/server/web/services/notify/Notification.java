package wisematches.server.web.services.notify;

import wisematches.personality.account.Account;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class Notification {
	private final String code;
	private final Date initiated;
	private final String template;
	private final Account recipient;
	private final NotificationSender sender;
	private final Object context;

	public Notification(String code, Account recipient, NotificationSender sender) {
		this(code, recipient, sender, null);
	}

	public Notification(String code, Account recipient, NotificationSender sender, Object context) {
		this(code, code, recipient, sender, context);
	}

	public Notification(String code, String template, Account recipient, NotificationSender sender) {
		this(code, template, recipient, sender, null);
	}

	public Notification(String code, String template, Account recipient, NotificationSender sender, Object context) {
		this.code = code;
		this.initiated = new Date();
		this.context = context;
		this.template = template;
		this.sender = sender;
		this.recipient = recipient;
	}

	public String getCode() {
		return code;
	}

	public Date getInitiated() {
		return initiated;
	}

	public String getTemplate() {
		return template;
	}

	public Account getRecipient() {
		return recipient;
	}

	public NotificationSender getSender() {
		return sender;
	}

	public Object getContext() {
		return context;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Notification");
		sb.append("{code='").append(code).append('\'');
		sb.append(", initiated='").append(initiated).append('\'');
		sb.append(", template='").append(template).append('\'');
		sb.append(", recipient=").append(recipient);
		sb.append(", sender=").append(sender);
		sb.append(", context=").append(context);
		sb.append('}');
		return sb.toString();
	}
}
