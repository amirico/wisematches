package wisematches.server.web.security.captcha;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class CaptchaForm {
	private String captcha;

	public CaptchaForm() {
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
}
