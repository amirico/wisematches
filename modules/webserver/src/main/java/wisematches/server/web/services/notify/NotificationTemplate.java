package wisematches.server.web.services.notify;

import wisematches.personality.account.Account;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class NotificationTemplate {
	private final String code;
	private final String template;
	private final Date inititated;
	private final Account recipient;
	private final NotificationCreator creator;
	private final Object context;

	public NotificationTemplate(String code, Account recipient, NotificationCreator creator) {
		this(code, recipient, creator, null);
	}

	public NotificationTemplate(String code, Account recipient, NotificationCreator creator, Object context) {
		this(code, code, recipient, creator, context);
	}

	public NotificationTemplate(String code, String template, Account recipient, NotificationCreator creator) {
		this(code, template, recipient, creator, null);
	}

	public NotificationTemplate(String code, String template, Account recipient, NotificationCreator creator, Object context) {
		this.code = code;
		this.inititated = new Date();
		this.context = context;
		this.template = template;
		this.creator = creator;
		this.recipient = recipient;
	}

	public String getCode() {
		return code;
	}

	public Date getInitiated() {
		return inititated;
	}

	public String getTemplate() {
		return template;
	}

	public Account getRecipient() {
		return recipient;
	}

	public NotificationCreator getCreator() {
		return creator;
	}

	public Object getContext() {
		return context;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Notification");
		sb.append("{code='").append(code).append('\'');
		sb.append(", template='").append(template).append('\'');
		sb.append(", recipient=").append(recipient);
		sb.append(", creator=").append(creator);
		sb.append(", context=").append(context);
		sb.append('}');
		return sb.toString();
	}
}
