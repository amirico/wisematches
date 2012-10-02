package wisematches.server.web.controllers.playground.tournament.form;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SubscriptionForm {
	private int announcement;
	private String section;
	private String language;

	public SubscriptionForm() {
	}

	public int getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(int announcement) {
		this.announcement = announcement;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("SubscriptionForm");
		sb.append("{announcement=").append(announcement);
		sb.append(", section='").append(section).append('\'');
		sb.append(", language='").append(language).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
