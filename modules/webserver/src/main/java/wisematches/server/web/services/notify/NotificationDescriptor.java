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
	private final boolean offlineOnly;
	private final boolean enabledByDefault;

	public NotificationDescriptor(String code, boolean offlineOnly, boolean enabledByDefault) {
		this(code, null, offlineOnly, enabledByDefault);
	}

	public NotificationDescriptor(String code, String group, boolean offlineOnly, boolean enabledByDefault) {
		this(code, group, code, offlineOnly, enabledByDefault);
	}

	public NotificationDescriptor(String code, String group, String template, boolean offlineOnly, boolean enabledByDefault) {
		this.code = code;
		this.group = group;
		this.template = template;
		this.offlineOnly = offlineOnly;
		this.enabledByDefault = enabledByDefault;
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
	 * Indicates is the notification can be managed by user or it's system like notification.
	 *
	 * @return {@code true} if notification can be changed by user; {@code false} if it's system
	 *         notification and is sent in any case.
	 */
	public boolean isManageable() {
		return group != null;
	}

	/**
	 * Indicates is the notification enabled by default or not.
	 *
	 * @return {@code true} if by default the notification is enabled; {@code false} - otherwise.
	 */
	public boolean isEnabledByDefault() {
		return enabledByDefault;
	}

	/**
	 * Indicates that the notification should be sent only if a player is offline.
	 *
	 * @return {@code true} if notification should be sent only to offline players;
	 *         {@code false} if notification should be sent in any case.
	 */
	public boolean isOfflineOnly() {
		return offlineOnly;
	}
}