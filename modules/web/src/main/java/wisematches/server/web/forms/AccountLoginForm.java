package wisematches.server.web.forms;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AccountLoginForm {
	public String j_username;

	public String j_password;

	public String rememberMe = "false";

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

	public String getRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(String rememberMe) {
		this.rememberMe = rememberMe;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("AccountLoginForm");
		sb.append("{j_username='").append(j_username).append('\'');
		sb.append(", rememberMe='").append(rememberMe).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
