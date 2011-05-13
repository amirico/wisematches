package wisematches.server.web.controllers.account;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wisematches.personality.account.Account;
import wisematches.personality.account.AccountEditor;
import wisematches.personality.account.AccountManager;
import wisematches.server.mail.MailException;
import wisematches.server.mail.MailSender;
import wisematches.server.mail.MailService;
import wisematches.server.security.PlayerSecurityService;
import wisematches.server.web.controllers.account.form.RecoveryConfirmationForm;
import wisematches.server.web.controllers.account.form.RecoveryRequestForm;
import wisematches.server.web.security.captcha.CaptchaService;
import wisematches.server.web.services.recovery.RecoveryToken;
import wisematches.server.web.services.recovery.RecoveryTokenManager;
import wisematches.server.web.services.recovery.TokenExpiredException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/account/recovery")
public class RecoveryController {
	private MailService mailService;
	private AccountManager accountManager;
	private CaptchaService captchaService;
	private RecoveryTokenManager recoveryTokenManager;
	private PlayerSecurityService playerSecurityService;

	private static final Log log = LogFactory.getLog("wisematches.server.web.accoint");

	public RecoveryController() {
	}

	@RequestMapping(value = "request")
	public String recoveryRequestPage(Model model, @ModelAttribute("recovery") RecoveryRequestForm form) {
		return "/content/account/pages/recovery/request";
	}

	@RequestMapping(value = "request", method = RequestMethod.POST)
	public String recoveryRequestAction(Model model,
										@Valid @ModelAttribute("recovery") RecoveryRequestForm form,
										BindingResult result) {
		if (log.isInfoEnabled()) {
			log.info("Recovery password for: " + form);
		}

		if (!result.hasErrors()) {
			final Account player = accountManager.findByEmail(form.getEmail());
			if (player == null) {
				if (log.isDebugEnabled()) {
					log.debug("Account for specified email not found");
				}
				result.rejectValue("email", "account.recovery.err.unknown");
				return recoveryRequestPage(model, form);
			} else {
				try {
					final RecoveryToken token = recoveryTokenManager.createToken(player);

					final byte[] encode = Base64.encode(token.getToken().getBytes("UTF-8"));
					final Map<String, String> mailModel = new HashMap<String, String>();
					mailModel.put("recoveryToken", new String(encode));
					mailModel.put("confirmationUrl", "account/recovery/confirmation.html");

					mailService.sendWarrantyMail(MailSender.ACCOUNTS, player, "account/recovery", mailModel);

					//noinspection SpringMVCViewInspection
					return "redirect:/account/recovery/expectation.html";
				} catch (MailException ex) {
					log.error("Recovery password email can't be delivered", ex);

					result.rejectValue("email", "account.recovery.err.transport");
					return recoveryRequestPage(model, form);
				} catch (Exception ex) {
					result.rejectValue("email", "account.recovery.err.system");
					return recoveryRequestPage(model, form);
				}
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Account form is not correct: " + result);
			}
			return recoveryRequestPage(model, form);
		}
	}

	@RequestMapping(value = "confirmation")
	public String recoveredConfirmationPage(Model model,
											@ModelAttribute("recovery") RecoveryConfirmationForm form) {
		return "/content/account/pages/recovery/confirmation";
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "confirmation", method = RequestMethod.POST)
	public String recoveredConfirmationAction(HttpServletRequest request, HttpServletResponse response,
											  @Valid @ModelAttribute("recovery") RecoveryConfirmationForm form,
											  BindingResult result, Model model) {
		if (log.isInfoEnabled()) {
			log.info("Process recovery confirmation: " + form);
		}

		if (!form.getPassword().equals(form.getConfirm())) {
			result.rejectValue("confirm", "account.register.pwd-cfr.err.mismatch");
		}
		if (captchaService != null) {
			captchaService.validateCaptcha(request, response, result);
		}

		Account player = null;
		if (!result.hasErrors()) {
			try {
				player = accountManager.findByEmail(form.getEmail());
				if (player == null) {
					result.rejectValue("email", "account.recovery.err.unknown");
				} else {
					final RecoveryToken token = recoveryTokenManager.getToken(player);
					if (token == null) {
						result.rejectValue("email", "account.recovery.err.expired");
					} else if (!token.getToken().equals(new String(Base64.decode(form.getToken().getBytes()), "UTF-8"))) {
						result.rejectValue("email", "account.recovery.err.expired");
					}
				}
			} catch (TokenExpiredException ex) {
				result.rejectValue("email", "account.recovery.err.expired");
			} catch (Exception ex) {
				result.rejectValue("email", "account.recovery.err.system");
			}
		}

		if (result.hasErrors()) {
			if (log.isDebugEnabled()) {
				log.debug("Confirmation form is not correct: " + result);
			}
			return recoveredConfirmationPage(model, form);
		}

		final AccountEditor e = new AccountEditor(player);
		if (playerSecurityService != null) {
			e.setPassword(playerSecurityService.encodePlayerPassword(player, form.getPassword()));
		} else {
			e.setPassword(form.getPassword());
		}

		try {
			accountManager.updateAccount(e.createAccount());
			mailService.sendMail(MailSender.ACCOUNTS, player, "account/updated", null);
			return CreateAccountController.forwardToAuthentication(form.getEmail(), form.getPassword(), form.isRememberMe());
		} catch (Exception e1) {
			if (log.isDebugEnabled()) {
				log.debug("Player password can't be updated by internal error", e1);
			}
			result.rejectValue("email", "account.recovery.err.system");
			return recoveredConfirmationPage(model, form);
		}
	}

	@RequestMapping(value = "expectation")
	public String recoveredExpectationPage(Model model) {
		model.addAttribute("infoId", "recovery/expectation");
		return "/content/account/pages/recovery/expectation";
	}

	@ModelAttribute("headerTitle")
	public String getHeaderTitle() {
		return "title.recovery";
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
	public void setRecoveryTokenManager(RecoveryTokenManager recoveryTokenManager) {
		this.recoveryTokenManager = recoveryTokenManager;
	}

	@Autowired
	public void setPlayerSecurityService(PlayerSecurityService playerSecurityService) {
		this.playerSecurityService = playerSecurityService;
	}
}
