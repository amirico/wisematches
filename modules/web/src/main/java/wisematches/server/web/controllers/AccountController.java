/*
 * Copyright (c) 2011, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import wisematches.server.player.*;
import wisematches.server.security.PlayerSecurityService;
import wisematches.server.web.forms.AccountRecoveryForm;
import wisematches.server.web.forms.AccountRegistrationForm;

import javax.validation.Valid;
import java.util.Locale;
import java.util.Set;

/**
 * TODO: java docs
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping(value = "/account")
public class AccountController extends AbstractInfoController {
	private AccountManager accountManager;
	private PlayerSecurityService playerSecurityService;

	private int defaultRating = 1200;
	private Membership defaultMembership = Membership.BASIC;

	private static final Log log = LogFactory.getLog("wisematches.server.web.accoint");

	public AccountController() {
		super("classpath:/i18n/server/account/");
	}

	@RequestMapping("login")
	public String login(Model model, Locale locale) {
		if (!processInfoPage("login", model, locale)) {
			return null;
		}
		model.addAttribute("accountBodyPageName", "login");
		return "/content/account/layout";
	}

	/**
	 * This is basic registration form. Just forward it to appropriate FTL page.
	 *
	 * @param model		the associated model where {@code accountBodyPageName} parameter will be stored.
	 * @param registration the registration form.
	 * @return the FTL full page name without extension
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createAccountPage(Model model, @ModelAttribute("registration") AccountRegistrationForm registration) {
		model.addAttribute("accountBodyPageName", "create");
		return "/content/account/layout";
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String createAccountAction(Model model, @Valid @ModelAttribute("registration") AccountRegistrationForm registration,
									  BindingResult result, SessionStatus status) {
		// Validate before next steps
		validateRegistrationForm(registration, result);

		// Create account if no errors
		if (!result.hasErrors()) {
			try {
				final PlayerEditor editor = new PlayerEditor();
				editor.setEmail(registration.getEmail());
				editor.setNickname(registration.getNickname());
				editor.setPassword(registration.getPassword());
				editor.setMembership(defaultMembership);
				editor.setRating(defaultRating);
				editor.setLanguage(Language.byCode(registration.getLanguage()));

				if (playerSecurityService != null) {
					editor.setPassword(playerSecurityService.encodePlayerPassword(editor.createPlayer(), registration.getPassword()));
				}
				final Player p = accountManager.createPlayer(editor.createPlayer());
			} catch (DuplicateAccountException ex) {
				final Set<String> fieldNames = ex.getFieldNames();
				if (fieldNames.contains("email")) {
					result.rejectValue("email", "account.register.form.email.err.busy");
				}
				if (fieldNames.contains("nickname")) {
					result.rejectValue("nickname", "account.register.form.username.err.incorrect");
				}
			} catch (InadmissibleUsernameException ex) {
				result.rejectValue("username", "account.register.form.username.err.incorrect");
			} catch (Exception ex) {
				log.error("Account can't be created", ex);
				result.reject("wisematches.error.internal");
			}
		}

		if (result.hasErrors()) {
			return createAccountPage(model, registration);
		} else {
			model.addAttribute("j_username", registration.getEmail());
			model.addAttribute("j_password", registration.getPassword());

			status.setComplete();
			//noinspection SpringMVCViewInspection
			return "redirect:/account/authMember.html";
		}
	}

	@ResponseBody
	@RequestMapping(value = "checkAvailability")
	private ServiceResponse getAvailabilityStatus(@RequestParam("email") String email, @RequestParam("nickname") String nickname, BindingResult result) {
		final AccountAvailability a = accountManager.checkAccountAvailable(nickname, email);
		if (a.isAvailable()) {
			return ServiceResponse.success();
		} else {
			if (!a.isEmailAvailable()) {
				result.rejectValue("email", "account.register.email.err.busy");
			}
			if (!a.isUsernameAvailable()) {
				result.rejectValue("nickname", "account.register.nickname.err.busy");
			}
			if (!a.isUsernameProhibited()) {
				result.rejectValue("nickname", "account.register.nickname.err.incorrect");
			}
			return ServiceResponse.convert(result);
		}
	}

	/**
	 * This is fake method. Implementation is provided by Spring Security.
	 *
	 * @return always null
	 */
	@RequestMapping("authMember")
	public String authMember() {
		return null;
	}

	private ServiceResponse generateRecoveryToken(String tokenEmail) {
		System.out.println("generateRecoveryToken: " + tokenEmail);
		if (tokenEmail.startsWith("test")) {
			return ServiceResponse.failure(null, "tokenEmail", "dafasd.wer.qwerqw");
		}
		return ServiceResponse.SUCCESS;
	}

	@Transactional
	private ServiceResponse recoveryAccount(AccountRecoveryForm form) {
		System.out.println("recoveryAccount: " + form);
		if (form.getRecoveryEmail().startsWith("test")) {
			return ServiceResponse.failure(null, "recoveryEmail", "dafasd.wer.qwerqw");
		}
		if (!form.getRecoveryToken().equals("test")) {
			return ServiceResponse.failure(null, "recoveryToken", "token.very.bad");
		}
		return ServiceResponse.SUCCESS;
	}


	public void setDefaultRating(int defaultRating) {
		this.defaultRating = defaultRating;
	}

	public void setDefaultMembership(String defaultMembership) {
		this.defaultMembership = Membership.valueOf(defaultMembership.toUpperCase());
	}

	@Autowired
	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	@Autowired
	public void setPlayerSecurityService(PlayerSecurityService playerSecurityService) {
		this.playerSecurityService = playerSecurityService;
	}

	private void validateRegistrationForm(AccountRegistrationForm form, BindingResult result) {
		if (!form.getPassword().equals(form.getConfirm())) {
			result.rejectValue("confirm", "account.register.pwd-cfr.err.mismatch");
		}
		getAvailabilityStatus(form.getEmail(), form.getNickname(), result);
	}
}
