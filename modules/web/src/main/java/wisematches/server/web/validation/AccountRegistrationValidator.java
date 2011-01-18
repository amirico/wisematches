package wisematches.server.web.validation;

import org.springframework.validation.Errors;
import wisematches.server.web.forms.AccountRegistrationForm;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AccountRegistrationValidator {
	public void validate(AccountRegistrationForm registration, Errors errors) {
		System.out.println(registration);
/*
		if (!StringUtils.hasLength(registration.getNickname())) {
			errors.rejectValue("nickname", "account.register.nickname.err.blank");
		} else if (registration.getNickname().length() > 50) {
			errors.rejectValue("nickname", "account.register.nickname.err.long");
		}

		if (!StringUtils.hasLength(registration.getEmail())) {
			errors.rejectValue("nickname", "account.register.email.err.blank");
		} else if (registration.getEmail().length() > 50) {
			errors.rejectValue("nickname", "account.register.email.err.long");
		}
*/
	}
}
