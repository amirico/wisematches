package wisematches.server.web.mail;

import wisematches.core.user.Language;
import wisematches.server.web.ResourceManager;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;

/**
 * From addresses enum. This enum contains type of emails messages can be sent from.
 */
public enum FromTeam {
	/**
	 * From address is bugs reporter.
	 */
	BUG_REPORTER("login.bugs-reporter"),
	/**
	 * This is abstract e-mail notification.
	 */
	ABSTRACT("login.mail-noreply"),
	/**
	 * Mail was sent from accounts support team.
	 */
	ACCOUNTS("login.accounts-noreply");

	private final String resource;

	FromTeam(String resource) {
		this.resource = resource;
	}

	public String getResource() {
		return resource;
	}

	public String getFromAddress(Language locale) {
		return ResourceManager.getString(resource + ".from.address", locale);
	}

	public String getFromPerson(Language locale) {
		return ResourceManager.getString(resource + ".from.person", locale);
	}

	public InternetAddress getInternetAddress(Language locale) {
		try {
			return new InternetAddress(getFromAddress(locale), getFromPerson(locale), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError("UTF-8 encoding is not supported?!!!: " + e.getMessage());
		}
	}
}
