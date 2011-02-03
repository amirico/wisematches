/*
 * Copyright (c) 2011, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.controllers.account;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import wisematches.server.mail.MailService;
import wisematches.server.player.*;
import wisematches.server.security.PlayerSecurityService;
import wisematches.server.web.controllers.AbstractInfoController;
import wisematches.server.web.controllers.ServiceResponse;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Set;

/**
 * TODO: java docs
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/account")
public class CreationController extends AbstractInfoController {
	private MailService mailService;
	private AccountManager accountManager;
	private PlayerSecurityService playerSecurityService;

	private int defaultRating = 1200;
	private Membership defaultMembership = Membership.BASIC;

	private static final Log log = LogFactory.getLog("wisematches.server.web.accoint");

	public CreationController() {
		super("classpath:/i18n/server/account/");
	}

	/**
	 * This is basic form form. Just forward it to appropriate FTL page.
	 *
	 * @param model the associated model where {@code accountBodyPageName} parameter will be stored.
	 * @param form  the form form.
	 * @return the FTL full page name without extension
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createAccountPage(Model model,
									@ModelAttribute("registration")
									AccountRegistrationForm form) {
		model.addAttribute("infoId", "create");
		return "/content/account/layout";
	}

	/**
	 * This is action processor for new account. Get model from HTTP POST request and creates new account, if possible.	 *
	 *
	 * @param model  the all model
	 * @param form   the form request form
	 * @param result the errors result
	 * @param status the session status. Will be cleared in case of success
	 * @return the create account page in case of error of forward to {@code authMember} page in case of success.
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String createAccountAction(Model model,
									  @Valid @ModelAttribute("registration") AccountRegistrationForm form,
									  BindingResult result, SessionStatus status) {
		if (log.isInfoEnabled()) {
			log.info("Create new account request: " + form);
		}

		// Validate before next steps
		validateRegistrationForm(form, result);

		// Create account if no errors
		if (!result.hasErrors()) {
			try {
				createAccount(form);
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
			if (log.isInfoEnabled()) {
				log.info("Account form is not correct: " + result.toString());
			}
			return createAccountPage(model, form);
		} else {
			if (log.isInfoEnabled()) {
				log.info("Account has been created.");
			}

			status.setComplete();

			try {
				final StringBuilder b = new StringBuilder();
				b.append("j_username=").append(URLEncoder.encode(form.getEmail(), "UTF-8"));
				b.append("&");
				b.append("j_password=").append(URLEncoder.encode(form.getPassword(), "UTF-8"));
				if (form.isRememberMe()) {
					b.append("&").append("rememberMe=true");
				}
				//noinspection SpringMVCViewInspection
				return "forward:/account/loginProcessing.html?" + b;
			} catch (UnsupportedEncodingException ex) {
				log.error("Very strange exception that mustn't be here", ex);
				//noinspection SpringMVCViewInspection
				return "redirect:/account/login.html";
			}
		}
	}

	@RequestMapping(value = "modify")
	public String modifyAccountPage(Model model) {
		model.addAttribute("infoId", "modify");
		return "/content/account/layout";
	}

	/**
	 * JSON request for email and username validation.
	 *
	 * @param email	the email to to checked.
	 * @param nickname the nickname to be checked
	 * @param result   the bind result that will be filled in case of any errors.
	 * @return the service response that also contains information about errors.
	 */
	@ResponseBody
	@RequestMapping(value = "checkAvailability")
	private ServiceResponse getAvailabilityStatus(@RequestParam("email") String email,
												  @RequestParam("nickname") String nickname,
												  BindingResult result) {
		if (log.isDebugEnabled()) {
			log.debug("Check account validation for: " + email + " (\"" + nickname + "\")");
		}

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


	// ==========================
	// Private implementation
	// ==========================

	/**
	 * Checks that specified form is valid. Otherwise fills specified result object.
	 *
	 * @param form   the form to be checked
	 * @param result the binding result to be filled in case of any error.
	 */
	private void validateRegistrationForm(AccountRegistrationForm form, BindingResult result) {
		if (!form.getPassword().equals(form.getConfirm())) {
			result.rejectValue("confirm", "account.register.pwd-cfr.err.mismatch");
		}
		getAvailabilityStatus(form.getEmail(), form.getNickname(), result);
	}

	/**
	 * Creates account based on specified form and returns created user.
	 *
	 * @param registration the new account form
	 * @return the create player
	 * @throws wisematches.server.player.DuplicateAccountException
	 *          if account with the same email or nickname already exist
	 * @throws wisematches.server.player.InadmissibleUsernameException
	 *          if nickname can't be used.
	 */
	private Player createAccount(AccountRegistrationForm registration) throws DuplicateAccountException, InadmissibleUsernameException {
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
		return accountManager.createPlayer(editor.createPlayer());
	}


	// ==========================
	// Public Bean methods
	// ==========================

	public void setDefaultRating(int defaultRating) {
		this.defaultRating = defaultRating;
	}

	public void setDefaultMembership(String defaultMembership) {
		this.defaultMembership = Membership.valueOf(defaultMembership.toUpperCase());
	}

	@Autowired
	public void setMailService(@Qualifier("mailService") MailService mailService) {
		this.mailService = mailService;
	}

	@Autowired
	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	@Autowired
	public void setPlayerSecurityService(PlayerSecurityService playerSecurityService) {
		this.playerSecurityService = playerSecurityService;
	}
}
