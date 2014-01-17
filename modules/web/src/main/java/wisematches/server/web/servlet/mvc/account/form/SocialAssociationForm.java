package wisematches.server.web.servlet.mvc.account.form;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SocialAssociationForm extends AccountRegistrationForm {
	private Long userId;
	private String finish = "/account/social/finish";

	public SocialAssociationForm() {
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFinish() {
		return finish;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}
}
