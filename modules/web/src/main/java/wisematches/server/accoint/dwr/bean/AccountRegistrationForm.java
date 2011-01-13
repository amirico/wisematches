/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.accoint.dwr.bean;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AccountRegistrationForm {
	private String email;
	private String username;
	private String password;
	private String timezone;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("AccountRegistrationForm");
		sb.append("{username='").append(username).append('\'');
		sb.append(", password='").append(password).append('\'');
		sb.append(", email='").append(email).append('\'');
		sb.append(", timezone=").append(timezone);
		sb.append('}');
		return sb.toString();
	}
}