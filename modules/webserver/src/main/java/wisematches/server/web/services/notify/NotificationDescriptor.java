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
	private final boolean enabled;

	public NotificationDescriptor(String code, boolean enabled) {
		this(code, code, enabled);
	}

	public NotificationDescriptor(String code, String template, boolean enabled) {
		this.code = code;
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
	 * Returns section for this descriptor. TournamentCategory is part of {@code code} til last dot.
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
	 * Indicates is the notification enabled by default or not.
	 *
	 * @return {@code true} if by default the notification is enabled; {@code false} - otherwise.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("NotificationDescriptor");
		sb.append("{code='").append(code).append('\'');
		sb.append(", template='").append(template).append('\'');
		sb.append(", enabled=").append(enabled);
		sb.append('}');
		return sb.toString();
	}
}