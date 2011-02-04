package wisematches.server.web.controllers.account;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import wisematches.server.web.security.captcha.CaptchaForm;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RecoveryConfirmationForm extends CaptchaForm {
	@NotEmpty(message = "account.register.email.err.blank")
	@Length(max = 150, message = "account.register.email.err.max")
	@Email(message = "account.register.email.err.format")
	private String email;

	@NotEmpty(message = "account.register.pwd.err.blank")
	@Length(max = 100, message = "account.register.pwd.err.max")
	private String password;

	@NotEmpty(message = "account.register.pwd-cfr.err.blank")
	@Length(max = 100, message = "account.register.pwd-cfr.err.max")
	private String confirm;

	private String token;

	private boolean rememberMe = true;

	public RecoveryConfirmationForm() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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
		sb.append("RecoveryConfirmationForm");
		sb.append("{rememberMe=").append(rememberMe);
		sb.append(", token='").append(token).append('\'');
		sb.append(", email='").append(email).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
