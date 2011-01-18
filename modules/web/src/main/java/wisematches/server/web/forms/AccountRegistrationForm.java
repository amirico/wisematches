/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.forms;

import org.springmodules.validation.bean.conf.loader.annotation.handler.Email;
import org.springmodules.validation.bean.conf.loader.annotation.handler.Length;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AccountRegistrationForm {
	@NotBlank(errorCode = "account.register.email.err.blank")
	@Length(max = 50, errorCode = "account.register.email.err.max", args = "50")
	@Email(errorCode = "account.register.email.err.format")
	private String email;

	private String nickname;

	private String password;

	private String confirm;

	private String language;

	private String timezone;

	private boolean rememberMe = true;

	public AccountRegistrationForm() {
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("AccountRegistrationForm");
		sb.append("{email='").append(email).append('\'');
		sb.append(", nickname='").append(nickname).append('\'');
		sb.append(", language='").append(language).append('\'');
		sb.append(", timezone='").append(timezone).append('\'');
		sb.append(", rememberMe=").append(rememberMe);
		sb.append('}');
		return sb.toString();
	}
}