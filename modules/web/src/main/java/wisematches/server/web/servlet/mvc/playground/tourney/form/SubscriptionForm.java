package wisematches.server.web.servlet.mvc.playground.tourney.form;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SubscriptionForm {
	private String section;
	private String language;

	public SubscriptionForm() {
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
		return "SubscriptionForm{" +
				"section='" + section + '\'' +
				", language='" + language + '\'' +
				'}';
	}
}
