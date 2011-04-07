/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.controllers.account.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RecoveryRequestForm {
	@NotEmpty(message = "account.register.email.err.blank")
	@Length(max = 150, message = "account.register.email.err.max")
	@Email(message = "account.register.email.err.format")
	private String email;

	public RecoveryRequestForm() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("RecoveryRequestForm");
		sb.append("{email='").append(email).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
