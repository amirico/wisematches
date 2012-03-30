package wisematches.server.web.services.notify;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class NotificationDescription {
	private final String name;
	private final String group;
	private final String template;
	private final boolean enabled;
	private final boolean evenOnline;

	public NotificationDescription(String name, String group, boolean enabled, boolean evenOnline) {
		this(name, group, name, enabled, evenOnline);
	}

	public NotificationDescription(String name, String group, String template, boolean enabled, boolean evenOnline) {
		if (name == null) {
			throw new NullPointerException("Notification name can't be null");
		}
		this.name = name;
		this.template = template;
		this.group = group;
		this.enabled = enabled;
		this.evenOnline = evenOnline;
	}

	public String getName() {
		return name;
	}

	public String getGroup() {
		return group;
	}

	public String getTemplate() {
		return template;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public boolean isEvenOnline() {
		return evenOnline;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("NotificationDescription");
		sb.append("{name='").append(name).append('\'');
		sb.append(", group='").append(group).append('\'');
		sb.append(", template='").append(template).append('\'');
		sb.append(", enabled=").append(enabled);
		sb.append(", evenOnline=").append(evenOnline);
		sb.append('}');
		return sb.toString();
	}
}
