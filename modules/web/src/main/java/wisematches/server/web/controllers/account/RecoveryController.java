package wisematches.server.web.controllers.account;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wisematches.server.mail.MailException;
import wisematches.server.mail.MailSender;
import wisematches.server.mail.MailService;
import wisematches.server.player.AccountManager;
import wisematches.server.player.Player;
import wisematches.server.security.PlayerSecurityService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/account/recovery")
public class RecoveryController {
	private MailService mailService;
	private AccountManager accountManager;
	private PlayerSecurityService playerSecurityService;

	private static final Log log = LogFactory.getLog("wisematches.server.web.accoint");

	public RecoveryController() {
	}

	@RequestMapping(value = "request")
	public String recoveryRequestPage(Model model,
									  @ModelAttribute("recovery") RecoveryRequestForm form) {
		model.addAttribute("infoId", "recovery/request");
		return "/content/account/layout";
	}

	@RequestMapping(value = "request", method = RequestMethod.POST)
	public String recoveryAccountAction(Model model,
										@Valid @ModelAttribute("recovery") RecoveryRequestForm form,
										BindingResult result) {
		if (log.isInfoEnabled()) {
			log.info("Recovery password for: " + form);
		}

		if (!result.hasErrors()) {
			final Player player = accountManager.findByEmail(form.getEmail());
			if (player == null) {
				if (log.isDebugEnabled()) {
					log.debug("Account for specified email not found");
				}
				result.rejectValue("email", "account.recovery.err.unknown");
				return recoveryRequestPage(model, form);
			} else {
				try {
					final UUID recoveryToken = UUID.randomUUID();

					final Map<String, String> mailModel = new HashMap<String, String>();
					mailModel.put("recoveryToken", recoveryToken.toString());
					mailModel.put("confirmationUrl", "/account/recovery/confirmation.html");

					mailService.sendWarrantyMail(
							MailSender.ACCOUNTS, player, "account/recovery", mailModel);
					//noinspection SpringMVCViewInspection
					return "redirect:/account/recovery/validation.html";
				} catch (MailException ex) {
					log.error("Recovery password email can't be delivered", ex);

					result.rejectValue("email", "account.recovery.err.transport");
					return recoveryRequestPage(model, form);
				}
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Account form is not correct: " + result.toString());
			}
			return recoveryRequestPage(model, form);
		}
	}

	@RequestMapping(value = "validation")
	public String recoveredValidationPage(Model model) {
		model.addAttribute("infoId", "recovery/validation");
		return "/content/account/layout";
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
