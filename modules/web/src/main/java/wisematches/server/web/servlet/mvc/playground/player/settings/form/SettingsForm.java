package wisematches.server.web.servlet.mvc.playground.player.settings.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import wisematches.server.web.servlet.mvc.playground.scribble.settings.form.BoardSettingsForm;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Deprecated
public class SettingsForm extends BoardSettingsForm {
	@NotEmpty(message = "account.register.language.err.blank")
	private String language;

	@NotEmpty(message = "account.register.timezone.err.blank")
	private String timezone;

	@Length(max = 150, message = "account.register.email.err.max")
	@Email(message = "account.register.email.err.format")
	private String email;

	@Length(max = 100, message = "account.register.pwd.err.max")
	private String password;

	@Length(max = 100, message = "account.register.pwd-cfr.err.max")
	private String confirm;

	private boolean changeEmail = false;

	private boolean changePassword = false;

	private String openedTab = "commonTab";

	public SettingsForm() {
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
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

	public boolean isChangeEmail() {
		return changeEmail;
	}

	public void setChangeEmail(boolean changeEmail) {
		this.changeEmail = changeEmail;
	}

	public boolean isChangePassword() {
		return changePassword;
	}

	public void setChangePassword(boolean changePassword) {
		this.changePassword = changePassword;
	}

	public String getOpenedTab() {
		return openedTab;
	}

	public void setOpenedTab(String openedTab) {
		this.openedTab = openedTab;
	}
}
