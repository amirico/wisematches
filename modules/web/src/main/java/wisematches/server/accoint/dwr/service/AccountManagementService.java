/*
 * Copyright (c) 2010, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.accoint.dwr.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;
import wisematches.server.accoint.dwr.bean.AccountAvailabilityForm;
import wisematches.server.accoint.dwr.bean.AccountRecoveryForm;
import wisematches.server.accoint.dwr.bean.AccountRegistrationForm;
import wisematches.server.player.*;
import wisematches.server.security.PlayerSecurityService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class AccountManagementService {
	private AccountManager accountManager;
	private PlayerSecurityService playerSecurityService;

	private int defaultRating = 1200;
	private Membership defaultMembership = Membership.BASIC;

	private static final Log log = LogFactory.getLog("wisematches.server.accoint.dwr.account");

	@Transactional
	public ServiceResponse registerAccount(AccountRegistrationForm form) {
		if (log.isDebugEnabled()) {
			log.debug("Register new account: " + form);
		}

		try {
			checkRegistrationForm(form);

			final PlayerEditor editor = new PlayerEditor();
			editor.setEmail(form.getEmail());
			editor.setUsername(form.getUsername());
			editor.setMembership(defaultMembership);
			editor.setRating(defaultRating);
			editor.setLanguage(Language.byCode(form.getLanguage()));
			editor.setPassword(form.getPassword());
			if (playerSecurityService != null) {
				editor.setPassword(playerSecurityService.encodePlayerPassword(editor.createPlayer(), form.getPassword()));
			}

			final Player p = accountManager.createPlayer(editor.createPlayer());
			if (playerSecurityService != null) {
				playerSecurityService.authenticatePlayer(p, form.getPassword());
			}
			return ServiceResponse.SUCCESS;
		} catch (DuplicateAccountException ex) {
			final Set<String> fieldNames = ex.getFieldNames();
			final Map<String, String> checks = new HashMap<String, String>();
			if (fieldNames.contains("email")) {
				checks.put("email", "account.register.form.email.err.busy");
			}
			if (fieldNames.contains("username")) {
				checks.put("username", "account.register.form.username.err.incorrect");
			}
			return ServiceResponse.failure(null, checks);
		} catch (InadmissibleUsernameException ex) {
			return ServiceResponse.failure(null, "username", "account.register.form.username.err.incorrect");
		} catch (Exception ex) {
			log.error("Account can't be created", ex);
			return ServiceResponse.failure("wisematches.error.internal");
		}
	}

	private void checkRegistrationForm(AccountRegistrationForm form) {
	}

	@Transactional
	public ServiceResponse recoveryAccount(AccountRecoveryForm form) {
		System.out.println("recoveryAccount: " + form);
		if (form.getRecoveryEmail().startsWith("test")) {
			return ServiceResponse.failure(null, "recoveryEmail", "dafasd.wer.qwerqw");
		}
		if (!form.getRecoveryToken().equals("test")) {
			return ServiceResponse.failure(null, "recoveryToken", "token.very.bad");
		}
		return ServiceResponse.SUCCESS;
	}

	public ServiceResponse checkAvailability(AccountAvailabilityForm form) {
		final AccountAvailability a = accountManager.checkAccountAvailable(form.getUsername(), form.getEmail());
		if (a.isAvailable()) {
			return ServiceResponse.SUCCESS;
		} else {
			Map<String, String> checks = new HashMap<String, String>();
			if (!a.isEmailAvailable()) {
				checks.put("email", "account.register.form.email.err.busy");
			}
			if (!a.isUsernameAvailable()) {
				checks.put("username", "account.register.form.username.err.busy");
			}
			if (!a.isUsernameProhibited()) {
				checks.put("username", "account.register.form.username.err.incorrect");
			}
			return ServiceResponse.failure(null, checks);
		}
	}

	public ServiceResponse generateRecoveryToken(String tokenEmail) {
		System.out.println("generateRecoveryToken: " + tokenEmail);
		if (tokenEmail.startsWith("test")) {
			return ServiceResponse.failure(null, "tokenEmail", "dafasd.wer.qwerqw");
		}
		return ServiceResponse.SUCCESS;
	}

	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	public void setUserDetailsService(PlayerSecurityService playerSecurityService) {
		this.playerSecurityService = playerSecurityService;
	}

	public void setDefaultRating(int defaultRating) {
		this.defaultRating = defaultRating;
	}

	public void setDefaultMembership(String defaultMembership) {
		this.defaultMembership = Membership.valueOf(defaultMembership.toUpperCase());
	}
}
