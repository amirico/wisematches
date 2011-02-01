/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.accoint.dwr.service;

import wisematches.server.web.controllers.account.AccountController;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AccountManagementService {
	private AccountController accountController;

	public void setAccountController(AccountController accountController) {
		this.accountController = accountController;
	}
}
