package wisematches.server.web.services.notify;

/**
 * {@code NotificationDescriptor} describes a notification and how it should be processed inside
 * {@code NotificationManager}.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class NotificationDescriptor {
	private final String code;
	private final String group;
	private final String template;
	private final boolean enabled;

	public NotificationDescriptor(String code, boolean enabled) {
		this(code, null, enabled);
	}

	public NotificationDescriptor(String code, String group, boolean enabled) {
		this(code, group, code, enabled);
	}

	public NotificationDescriptor(String code, String group, String template, boolean enabled) {
		this.code = code;
		this.group = group;
		this.template = template;
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
	 * Returns group name for the notification.
	 *
	 * @return the group name or {@code null} if notification doesn't have a group.
	 */
	public String getGroup() {
		return group;
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
	 * Indicates is the notification enabled by default or not.
	 *
	 * @return {@code true} if by default the notification is enabled; {@code false} - otherwise.
	 */
	public boolean isEnabled() {
		return enabled;
	}
}