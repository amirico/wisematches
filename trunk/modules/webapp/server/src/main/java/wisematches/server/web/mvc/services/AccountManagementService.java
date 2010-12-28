/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.mvc.services;

import wisematches.server.web.mvc.forms.AccountRegistrationForm;
import wisematches.server.web.mvc.forms.AccountRegistrationStatus;

/**
 * @author klimese
 */
public class AccountManagementService {
	public ServiceResponse registerAccount(AccountRegistrationForm form) {
		System.out.println(form);

		if (form.getEmail().startsWith("test")) {
			return ServiceResponse.failure(null, "email", "This is not real email address");
		}
		return ServiceResponse.SUCCESS;
	}

	public AccountRegistrationStatus checkUsernameAvailability(String username) {
		System.out.println("checkUsernameAvailability: " + username);
		if (username.startsWith("test")) {
			return AccountRegistrationStatus.BUSY;
		}
		return AccountRegistrationStatus.AVAILABLE;
	}

	public AccountRegistrationStatus checkEmailAvailability(String email) {
		System.out.println("checkEmailAvailability: " + email);
		if (email.startsWith("test")) {
			return AccountRegistrationStatus.INCORRECT;
		}
		return AccountRegistrationStatus.AVAILABLE;
	}

	public boolean resetPassword(String email) {
		if (email.startsWith("test")) {
			return false;
		}
		return true;
	}
}
