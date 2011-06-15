package wisematches.server.web.controllers.personality.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import wisematches.personality.Language;
import wisematches.personality.account.*;
import wisematches.personality.player.Player;
import wisematches.server.mail.MailService;
import wisematches.server.security.PlayerSecurityService;
import wisematches.server.web.controllers.AbstractPlayerController;
import wisematches.server.web.controllers.personality.settings.form.SettingsForm;
import wisematches.server.web.controllers.personality.settings.form.TimeZonesInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/account/modify")
public class SettingsController extends AbstractPlayerController {
	private MailService mailService;
	private AccountManager accountManager;
	private PlayerSecurityService playerSecurityService;

	public SettingsController() {
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String modifyAccountPage(Model model,
									@ModelAttribute("settings")
									SettingsForm form) {
		final Player principal = getPrincipal();
		if (principal.getTimeZone() != null) {
			form.setTimezone(principal.getTimeZone().getDisplayName());
		}
		form.setLanguage(principal.getLanguage().name().toLowerCase());
		form.setEmail(principal.getEmail());
		model.addAttribute("timeZones", TimeZonesInfo.getTimeZones());
		return "/content/personality/settings/template";
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String modifyAccountAction(HttpServletRequest request, HttpServletResponse response,
									  @Valid @ModelAttribute("settings") SettingsForm form,
									  BindingResult errors, Model model, SessionStatus status) {
		final Account account = accountManager.getAccount(getPersonality().getId());
		if (account == null) {
			errors.reject("unknown.player");
		}

		Language language = account.getLanguage();
		if (form.getLanguage() != null) {
			try {
				language = Language.valueOf(form.getLanguage().toUpperCase());
			} catch (IllegalArgumentException ex) {
				errors.rejectValue("language", "unknown.language");
			}
		}

		TimeZone timeZone = account.getTimeZone();
		if (form.getTimezone() != null) {
			timeZone = TimeZone.getTimeZone(form.getTimezone());
			if (timeZone == null) {
				errors.rejectValue("timezone", "unknown.timezone");
			}
		}

		if (form.isChangeEmail() && form.getEmail().trim().isEmpty()) {
			errors.rejectValue("email", "account.register.email.err.blank");
		}

		if (form.isChangePassword()) {
			if (form.getPassword().trim().isEmpty()) {
				errors.rejectValue("password", "account.register.pwd.err.blank");
			}
			if (form.getConfirm().trim().isEmpty()) {
				errors.rejectValue("confirm", "account.register.pwd-cfr.err.blank");
			}

			if (!form.getPassword().equals(form.getConfirm())) {
				errors.rejectValue("confirm", "account.register.pwd-cfr.err.mismatch");
			}
		}

		if (!errors.hasErrors()) {
			final AccountEditor editor = new AccountEditor(account);
			if (language != editor.getLanguage()) {
				editor.setLanguage(language);
			}
			if (!editor.getTimeZone().equals(timeZone)) {
				editor.setTimeZone(timeZone);
			}

			if (form.isChangeEmail() && !editor.getEmail().equals(form.getEmail())) {
				editor.setEmail(form.getEmail());
			}

			if (form.isChangePassword()) {
				if (playerSecurityService != null) {
					editor.setPassword(playerSecurityService.encodePlayerPassword(editor.createAccount(), form.getPassword()));
				} else {
					editor.setPassword(form.getPassword());
				}
			}

			try {
				accountManager.updateAccount(editor.createAccount());
			} catch (UnknownAccountException e) {
				errors.reject("unknown.error");
			} catch (DuplicateAccountException e) {
				errors.reject("unknown.error");
			} catch (InadmissibleUsernameException e) {
				errors.reject("unknown.error");
			}
		}

		return modifyAccountPage(model, form);
	}

	@ModelAttribute("headerTitle")
	public String getHeaderTitle() {
		return "title.settings";
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
