package wisematches.server.web.servlet.mvc.playground.player.settings;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wisematches.core.Language;
import wisematches.core.Member;
import wisematches.core.personality.player.account.*;
import wisematches.playground.scribble.settings.BoardSettings;
import wisematches.playground.scribble.settings.BoardSettingsManager;
import wisematches.server.services.notify.NotificationManager;
import wisematches.server.services.notify.NotificationScope;
import wisematches.server.web.servlet.mvc.UnknownEntityException;
import wisematches.server.web.servlet.mvc.WisematchesController;
import wisematches.server.web.servlet.mvc.playground.player.settings.form.NotificationsTreeView;
import wisematches.server.web.servlet.mvc.playground.player.settings.form.SettingsForm;
import wisematches.server.web.servlet.mvc.playground.player.settings.form.TimeZoneInfo;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/account/modify")
@Deprecated
public class SettingsController extends WisematchesController {
	private AccountManager accountManager;
	private NotificationManager notificationManager;
	private BoardSettingsManager boardSettingsManager;

	private static final Logger log = LoggerFactory.getLogger("wisematches.web.mvc.SettingsController");

	public SettingsController() {
		super("title.settings");
	}

	@RequestMapping(value = "")
	public String modifyAccountPage(Model model, @ModelAttribute("settings") SettingsForm form) {
		final Member member = getPrincipal(Member.class);
		if (member.getTimeZone() != null) {
			form.setTimezone(member.getTimeZone().getID());
		}
		form.setLanguage(member.getLanguage().name().toLowerCase());
		form.setEmail(member.getEmail());
		model.addAttribute("timeZones", TimeZoneInfo.getTimeZones());

		final Map<String, NotificationScope> descriptors = new HashMap<>();
		for (String code : new TreeSet<>(notificationManager.getNotificationCodes())) {
			descriptors.put(code, notificationManager.getNotificationScope(code, member));
		}
		model.addAttribute("notificationsView", new NotificationsTreeView(descriptors));

		final BoardSettings settings = boardSettingsManager.getScribbleSettings(member);
		form.setTilesClass(settings.getTilesClass());
		form.setCheckWords(settings.isCheckWords());
		form.setCleanMemory(settings.isCleanMemory());
		form.setClearByClick(settings.isClearByClick());
		form.setShowCaptions(settings.isShowCaptions());
		form.setEnableShare(settings.isEnableShare());
		return "/content/playground/settings/template";
	}

	@RequestMapping(value = "done")
	public String modifyAccountDone(Model model, @ModelAttribute("settings") SettingsForm form) {
		model.addAttribute("saved", Boolean.TRUE);
		return modifyAccountPage(model, form);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String modifyAccountAction(@Valid @ModelAttribute("settings") SettingsForm form,
									  BindingResult errors, Model model, HttpServletRequest request) throws UnknownEntityException {
		final Member member = getPrincipal(Member.class);
		final Account account = accountManager.getAccount(member.getId());
		if (account == null) {
			throw new UnknownEntityException(null, "account");
		}

		final Set<String> codes = notificationManager.getNotificationCodes();
		for (String code : codes) {
			final String parameter = request.getParameter(code);
			final NotificationScope scope = parameter != null && !parameter.isEmpty() ? NotificationScope.valueOf(parameter.toUpperCase()) : null;
			notificationManager.setNotificationScope(code, member, scope);
		}

		boardSettingsManager.setScribbleSettings(member,
				new BoardSettings(form.isCleanMemory(), form.isCheckWords(), form.isClearByClick(),
						form.isShowCaptions(), form.isEnableShare(), form.getTilesClass()));

		Language language = account.getLanguage();
		if (form.getLanguage() != null) {
			try {
				language = Language.valueOf(form.getLanguage().toUpperCase());
			} catch (IllegalArgumentException ex) {
				errors.rejectValue("language", "account.register.language.err.unknown");
			}
		}

		TimeZone timeZone = account.getTimeZone();
		if (form.getTimezone() != null) {
			timeZone = TimeZone.getTimeZone(form.getTimezone());
			if (timeZone == null) {
				errors.rejectValue("timezone", "account.register.timezone.err.unknown");
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

			String pwd = null;
			if (form.isChangePassword()) {
				pwd = form.getPassword();
			}

			try {
				accountManager.updateAccount(editor.createAccount(), pwd);
				return "redirect:/account/modify#" + form.getOpenedTab();
			} catch (UnknownAccountException e) {
				throw new UnknownEntityException(null, "account");
			} catch (DuplicateAccountException ex) {
				final Set<String> fieldNames = ex.getFieldNames();
				if (fieldNames.contains("email")) {
					errors.rejectValue("email", "account.register.email.err.busy");
				}
				if (fieldNames.contains("nickname")) {
					errors.rejectValue("nickname", "account.register.nickname.err.busy");
				}
			} catch (InadmissibleUsernameException ex) {
				errors.rejectValue("nickname", "account.register.nickname.err.incorrect");
			} catch (Exception ex) {
				log.error("Account can't be created", ex);
				errors.reject("wisematches.error.internal");
			}
		}
		return modifyAccountPage(model, form);
	}

	@Autowired
	public void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	@Autowired
	public void setNotificationManager(NotificationManager notificationManager) {
		this.notificationManager = notificationManager;
	}

	@Autowired
	public void setBoardSettingsManager(BoardSettingsManager boardSettingsManager) {
		this.boardSettingsManager = boardSettingsManager;
	}
}
