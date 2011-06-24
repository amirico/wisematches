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
		this(name, null, evenOnline);
	}

	public NotificationDescription(String name, String series, boolean evenOnline) {
		this(name, series, evenOnline, true);
	}

	public NotificationDescription(String name, boolean evenOnline, boolean enabled) {
		this(name, null, evenOnline, enabled);
	}

	public NotificationDescription(String name, String series, boolean evenOnline, boolean enabled) {
		if (name == null) {
			throw new NullPointerException("Notification name can't be null");
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
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final NotificationDescription that = (NotificationDescription) o;
		return name.equals(that.name) && series.equals(that.series);
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + series.hashCode();
		return result;
	}
}
