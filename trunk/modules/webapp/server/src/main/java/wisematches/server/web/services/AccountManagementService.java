/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.services;

/**
 * @author klimese
 */
public class AccountManagementService {
	public void registerAccount(AccountRegistrationForm form) {
		System.out.println(form);

		if (form.getUsername().startsWith("incorrect")) {
			throw new IllegalArgumentException("Incorrect form");
		}
	}

	public AccountAvailabilityStatus checkUsernameAvailability(String username) {
		System.out.println("checkUsernameAvailability: " + username);
		if (username.startsWith("test")) {
			return AccountAvailabilityStatus.BUSY;
		}
		return AccountAvailabilityStatus.AVAILABLE;
	}

	public AccountAvailabilityStatus checkEmailAvailability(String email) {
		System.out.println("checkEmailAvailability: " + email);
		if (email.startsWith("test")) {
			return AccountAvailabilityStatus.INCORRECT;
		}
		return AccountAvailabilityStatus.AVAILABLE;
	}
}
