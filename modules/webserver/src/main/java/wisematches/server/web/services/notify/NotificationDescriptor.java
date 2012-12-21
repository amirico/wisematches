package wisematches.server.web.services.notify;

/**
 * {@code NotificationDescriptor} describes a notification and how it should be processed inside
 * {@code NotificationManager}.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class NotificationDescriptor {
	private final String code;
	private final String template;
	private final NotificationScope scope;
	private final boolean enabled;

	public NotificationDescriptor(String code, NotificationScope scope) {
		this(code, scope, true);
	}

	public NotificationDescriptor(String code, NotificationScope scope, boolean enabled) {
		this(code, code, scope, enabled);
	}

	public NotificationDescriptor(String code, String template, NotificationScope scope) {
		this(code, template, scope, true);
	}

	public NotificationDescriptor(String code, String template, NotificationScope scope, boolean enabled) {
		this.code = code;
		this.template = template;
		this.scope = scope;
		this.enabled = enabled;
	}

	/**
	 * Returns unique code of the notification.
	 *
	 * @return the unique code of the notification.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Indicates is the notification enabled by default or not.
	 *
	 * @return {@code true} if by default the notification is enabled; {@code false} - otherwise.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Returns section for this descriptor. TournamentSection is part of {@code code} til last dot.
	 *
	 * @return the section for this descriptor
	 */
	public String getSection() {
		return code.substring(0, code.lastIndexOf("."));
	}

	/**
	 * Template name for notification. Some notification can share the same template for
	 * many variations.
	 *
	 * @return the template name or {@code null} if there is no predefined template.
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * Returns scope for this notification.
	 *
	 * @return the notification scope.
	 */
	public NotificationScope getScope() {
		return scope;
	}


	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("NotificationDescriptor");
		sb.append("{code='").append(code).append('\'');
		sb.append(", template='").append(template).append('\'');
		sb.append(", scope=").append(scope);
		sb.append(", enabled=").append(enabled);
		sb.append('}');
		return sb.toString();
	}
}