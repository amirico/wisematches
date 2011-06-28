package wisematches.server.web.services.notice;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class NotificationDescription {
	private final String name;
	private final String group;
	private final String series;
	private final boolean enabled;
	private final boolean evenOnline;

	public NotificationDescription(String name, String group, boolean enabled, boolean evenOnline) {
		this(name, group, null, enabled, evenOnline);
	}

	public NotificationDescription(String name, String group, String series, boolean enabled, boolean evenOnline) {
		if (name == null) {
			throw new NullPointerException("Notification name can't be null");
		}
		this.name = name;
		this.group = group;
		this.series = series;
		this.enabled = enabled;
		this.evenOnline = evenOnline;
	}

	public String getName() {
		return name;
	}

	public String getGroup() {
		return group;
	}

	public String getSeries() {
		return series;
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
		sb.append(", series='").append(series).append('\'');
		sb.append(", enabled=").append(enabled);
		sb.append(", evenOnline=").append(evenOnline);
		sb.append('}');
		return sb.toString();
	}
}
