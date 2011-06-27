package wisematches.server.web.services.notice;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class NotificationDescription {
	private final String name;
	private final String series;
	private final boolean enabled;
	private final boolean evenOnline;

	public NotificationDescription(String name) {
		this(name, false);
	}

	public NotificationDescription(String name, boolean evenOnline) {
		this(name, "", evenOnline);
	}

	public NotificationDescription(String name, String series, boolean evenOnline) {
		this(name, series, evenOnline, true);
	}

	public NotificationDescription(String name, boolean evenOnline, boolean enabled) {
		this(name, "", evenOnline, enabled);
	}

	public NotificationDescription(String name, String series, boolean evenOnline, boolean enabled) {
		if (name == null) {
			throw new NullPointerException("Notification name can't be null");
		}
		if (series == null) {
			throw new NullPointerException("Notification series can't be null");
		}
		this.name = name;
		this.series = series;
		this.enabled = enabled;
		this.evenOnline = evenOnline;
	}

	public String getName() {
		return name;
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		NotificationDescription that = (NotificationDescription) o;

		return name.equals(that.name) && !(series != null ? !series.equals(that.series) : that.series != null);
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + (series != null ? series.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("NotificationDescription");
		sb.append("{name='").append(name).append('\'');
		sb.append(", series='").append(series).append('\'');
		sb.append(", enabled=").append(enabled);
		sb.append(", evenOnline=").append(evenOnline);
		sb.append('}');
		return sb.toString();
	}
}
