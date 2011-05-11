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
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import wisematches.server.mail.MailSender;
import wisematches.server.mail.MailService;
import wisematches.server.personality.account.*;
import wisematches.server.security.PlayerSecurityService;
import wisematches.server.web.controllers.ServiceResponse;
import wisematches.server.web.controllers.account.form.AccountRegistrationForm;
import wisematches.server.web.security.captcha.CaptchaService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

/**
 * TODO: java docs
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/account")
public class CreateAccountController {
	private MailService mailService;
	private AccountManager accountManager;
	private CaptchaService captchaService;
	private PlayerSecurityService playerSecurityService;

	private Membership defaultMembership = Membership.BASIC;

	private static final Log log = LogFactory.getLog("wisematches.server.web.account");

	public CreateAccountController() {
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
		return "/content/account/pages/create";
	}

	/**
	 * This is action processor for new account. Get model from HTTP POST request and creates new account, if possible.	 *
	 *
	 * @param model	the all model
	 * @param request  original http request
	 * @param response original http response
	 * @param form	 the form request form
	 * @param result   the errors errors
	 * @param status   the session status. Will be cleared in case of success
	 * @return the create account page in case of error of forward to {@code authMember} page in case of success.
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String createAccountAction(HttpServletRequest request, HttpServletResponse response,
									  @Valid @ModelAttribute("registration") AccountRegistrationForm form,
									  BindingResult result, Model model, SessionStatus status) {
		if (log.isInfoEnabled()) {
			log.info("Create new account request: " + form);
		}

		if (captchaService != null) {
			captchaService.validateCaptcha(request, response, result);
		}
		// Validate before next steps
		validateRegistrationForm(form, result);

		Account player = null;
		// Create account if no errors
		if (!result.hasErrors()) {
			try {
				player = createAccount(form, request);
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

		if (result.hasErrors() || player == null) {
			if (log.isInfoEnabled()) {
				log.info("Account form is not correct: " + result.toString());
			}
			return createAccountPage(model, form);
		} else {
			if (log.isInfoEnabled()) {
				log.info("Account has been created.");
			}

			status.setComplete();
			mailService.sendMail(MailSender.ACCOUNTS, player, "account/created", null);
			return forwardToAuthentication(form.getEmail(), form.getPassword(), form.isRememberMe());
		}
	}

	protected static String forwardToAuthentication(final String email, final String password, final boolean rememberMe) {
		try {
			final StringBuilder b = new StringBuilder();
			b.append("j_username=").append(URLEncoder.encode(email, "UTF-8"));
			b.append("&");
			b.append("j_password=").append(URLEncoder.encode(password, "UTF-8"));
			if (rememberMe) {
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

	@RequestMapping(value = "modify")
	public String modifyAccountPage(Model model) {
		model.addAttribute("pageName", "modifyAccount");
		return "/content/account/pages/modify";
	}

	/**
	 * JSON request for email and username validation.
	 *
	 * @param email	the email to to checked.
	 * @param nickname the nickname to be checked
	 * @param result   the bind errors that will be filled in case of any errors.
	 * @return the service response that also contains information about errors.
	 */
	@ResponseBody
	@RequestMapping(value = "checkAvailability")
	private ServiceResponse getAvailabilityStatus(@RequestParam("email") String email,
												  @RequestParam("nickname") String nickname,
												  Errors result) {
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


	/**
	 * Checks that specified form is valid. Otherwise fills specified errors object.
	 *
	 * @param form   the form to be checked
	 * @param errors the binding errors to be filled in case of any error.
	 */
	private void validateRegistrationForm(AccountRegistrationForm form, Errors errors) {
		if (!form.getPassword().equals(form.getConfirm())) {
			errors.rejectValue("confirm", "account.register.pwd-cfr.err.mismatch");
		}
		getAvailabilityStatus(form.getEmail(), form.getNickname(), errors);
	}

	/**
	 * Creates account based on specified form and returns created user.
	 *
	 * @param registration the new account form
	 * @param request	  the original request
	 * @return the create player
	 * @throws DuplicateAccountException	 if account with the same email or nickname already exist
	 * @throws InadmissibleUsernameException if nickname can't be used.
	 */
	private Account createAccount(AccountRegistrationForm registration, HttpServletRequest request) throws DuplicateAccountException, InadmissibleUsernameException {
		final AccountEditor editor = new AccountEditor();
		editor.setEmail(registration.getEmail());
		editor.setNickname(registration.getNickname());
		editor.setPassword(registration.getPassword());
		editor.setMembership(defaultMembership);
		editor.setLanguage(Language.byCode(registration.getLanguage()));
		editor.setTimeZone(Calendar.getInstance(request.getLocale()).getTimeZone());

		if (playerSecurityService != null) {
			editor.setPassword(playerSecurityService.encodePlayerPassword(editor.createAccount(), registration.getPassword()));
		}
		return accountManager.createAccount(editor.createAccount());
	}


	public void setDefaultMembership(String defaultMembership) {
		this.defaultMembership = Membership.valueOf(defaultMembership.toUpperCase());
	}

	@ModelAttribute("headerTitle")
	public String getHeaderTitle() {
		return "title.account";
	}

	@Autowired
	public void setCaptchaService(CaptchaService captchaService) {
		this.captchaService = captchaService;
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
