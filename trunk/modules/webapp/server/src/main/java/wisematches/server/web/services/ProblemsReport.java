/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.services;

/**
 * @author klimese
 */
public class ProblemsReport {
	private String email;
	private String username;
	private String subject;
	private String message;
	private String page;
	private String os;
	private String browser;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("ProblemsReport");
		sb.append("{email='").append(email).append('\'');
		sb.append(", username='").append(username).append('\'');
		sb.append(", subject='").append(subject).append('\'');
		sb.append(", message='").append(message).append('\'');
		sb.append(", page='").append(page).append('\'');
		sb.append(", os='").append(os).append('\'');
		sb.append(", browser='").append(browser).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
