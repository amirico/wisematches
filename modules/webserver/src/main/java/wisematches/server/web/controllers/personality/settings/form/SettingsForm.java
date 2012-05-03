package wisematches.server.web.controllers.personality.settings.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SettingsForm {
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

	private String tilesClass;
	private boolean checkWords;
	private boolean cleanMemory;
	private boolean clearByClick;

	private boolean changeEmail = false;
	private boolean changePassword = false;

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

	public boolean isCleanMemory() {
		return cleanMemory;
	}

	public void setCleanMemory(boolean cleanMemory) {
		this.cleanMemory = cleanMemory;
	}

	public boolean isCheckWords() {
		return checkWords;
	}

	public void setCheckWords(boolean checkWords) {
		this.checkWords = checkWords;
	}

	public String getTilesClass() {
		return tilesClass;
	}

	public void setTilesClass(String tilesClass) {
		this.tilesClass = tilesClass;
	}

	public boolean isClearByClick() {
		return clearByClick;
	}

	public void setClearByClick(boolean clearByClick) {
		this.clearByClick = clearByClick;
	}
}
