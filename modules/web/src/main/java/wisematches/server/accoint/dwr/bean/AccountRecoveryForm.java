/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.accoint.dwr.bean;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AccountRecoveryForm {
	private String recoveryEmail;
	private String recoveryToken;
	private String recoveryPassword;

	public String getRecoveryEmail() {
		return recoveryEmail;
	}

	public void setRecoveryEmail(String recoveryEmail) {
		this.recoveryEmail = recoveryEmail;
	}

	public String getRecoveryToken() {
		return recoveryToken;
	}

	public void setRecoveryToken(String recoveryToken) {
		this.recoveryToken = recoveryToken;
	}

	public String getRecoveryPassword() {
		return recoveryPassword;
	}

	public void setRecoveryPassword(String recoveryPassword) {
		this.recoveryPassword = recoveryPassword;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("AccountRecoveryForm");
		sb.append("{recoveryEmail='").append(recoveryEmail).append('\'');
		sb.append(", recoveryToken='").append(recoveryToken).append('\'');
		sb.append(", recoveryPassword='").append(recoveryPassword).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
