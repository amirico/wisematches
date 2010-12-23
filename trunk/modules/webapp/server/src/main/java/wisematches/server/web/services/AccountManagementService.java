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

	public String checkUsernameAvailability(String username) {
		System.out.println("checkUsernameAvailability: " + username);
		if (username.startsWith("test")) {
			return "Fuck off!";
		}
		return null;
	}
}
