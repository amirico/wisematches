package wisematches.server.web.forms;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AccountLoginForm {
	@NotEmpty(message = "account.login.email.err.blank")
	@Email(message = "account.login.email.err.format")
	public String j_username;

	@NotEmpty(message = "account.login.password.err.blank")
	public String j_password;

	public String j_remember_me = "true";

	public AccountLoginForm() {
	}

	public String getJ_username() {
		return j_username;
	}

	public void setJ_username(String j_username) {
		this.j_username = j_username;
	}

	public String getJ_password() {
		return j_password;
	}

	public void setJ_password(String j_password) {
		this.j_password = j_password;
	}

	public String getJ_remember_me() {
		return j_remember_me;
	}

	public void setJ_remember_me(String j_remember_me) {
		this.j_remember_me = j_remember_me;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("AccountLoginForm");
		sb.append("{j_username='").append(j_username).append('\'');
		sb.append(", j_remember_me='").append(j_remember_me).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
