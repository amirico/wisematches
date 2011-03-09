package wisematches.server.web.controllers.account;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.server.web.controllers.AbstractInfoController;

import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/account")
public class AuthenticationController extends AbstractInfoController {
	private static final Log log = LogFactory.getLog("wisematches.server.web.accoint");

	public AuthenticationController() {
	}

	/**
	 * This is basic login page that shows small information about the site and login form.
	 *
	 * @param form   the empty login form. Required for FTL page.
	 * @param model  the original model
	 * @param locale the locale to get a info page
	 * @return the appropriate FTL layout page.
	 */
	@RequestMapping("login")
	public String loginPage(@ModelAttribute("login") AccountLoginForm form, Model model, Locale locale) {
		enableFullView(model);
		form.setRememberMe("true"); // by default remember me enabled

		return processLoginPage("info/general", model, locale);
	}

	@RequestMapping("loginAuth")
	public String loginFailedDefault(@ModelAttribute("login") AccountLoginForm form, BindingResult result,
									 HttpSession session, Model model, Locale locale) {
		return loginFailedCredentials(form, result, session, model, locale);
	}

	@RequestMapping(value = "loginAuth", params = "error=credential")
	public String loginFailedCredentials(@ModelAttribute("login") AccountLoginForm form, BindingResult result,
										 HttpSession session, Model model, Locale locale) {
		restoreAccountLoginForm(form, session);
		enableFullView(model);

		if (!form.hasUsername()) {
			result.rejectValue("j_username", "account.login.email.err.blank");
		} else {
			result.rejectValue("j_password", "account.login.err.credential");
		}
		return processLoginPage("info/general", model, locale);
	}

	@RequestMapping(value = "loginAuth", params = "error=session")
	public String loginFailedSession(@ModelAttribute("login") AccountLoginForm form, BindingResult result,
									 HttpSession session, Model model, Locale locale) {
		restoreAccountLoginForm(form, session);
		enableShortView(form, model, true);

		return processLoginPage("account/session", model, locale);
	}

	@RequestMapping(value = "loginAuth", params = "error=status")
	public String loginFailedStatus(@ModelAttribute("login") AccountLoginForm form, BindingResult result,
									HttpSession session, Model model, Locale locale) {
		restoreAccountLoginForm(form, session);
		enableFullView(model);

		final AuthenticationException ex = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		if (ex instanceof AccountStatusException) {
			if (ex instanceof LockedException) {
				result.rejectValue("j_password", "account.login.err.status.locked");
			} else if (ex instanceof DisabledException) {
				result.rejectValue("j_password", "account.login.err.status.disabled");
			} else if (ex instanceof CredentialsExpiredException) {
				result.rejectValue("j_password", "account.login.err.status.expired");
			} else if (ex instanceof AccountExpiredException) {
				result.rejectValue("j_password", "account.login.err.status.expired");
			}
		}
		return processLoginPage("account/status", model, locale);
	}

	@RequestMapping(value = "loginAuth", params = "error=insufficient")
	public String loginFailedInsufficient(@ModelAttribute("login") AccountLoginForm form, BindingResult result,
										  HttpSession session, Model model, Locale locale) {
		restoreAccountLoginForm(form, session);
		enableShortView(form, model, false);

		return processLoginPage("account/insufficient", model, locale);
	}

	@RequestMapping(value = "loginAuth", params = "error=system")
	public String loginFailedSystem(@ModelAttribute("login") AccountLoginForm form,
									HttpSession session, Model model, Locale locale) {
		restoreAccountLoginForm(form, session);
		enableFullView(model);

		final AuthenticationException ex = (AuthenticationException) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		log.error("Unknown authentication exception received for " + form, ex);

		return processLoginPage("account/system", model, locale);
	}

	private void restoreAccountLoginForm(AccountLoginForm form, HttpSession session) {
		if (form.getJ_username() == null) {
			String username = (String) session.getAttribute(WebAttributes.LAST_USERNAME);
			if (username == null) {
				final Authentication a = SecurityContextHolder.getContext().getAuthentication();
				if (a != null && a.isAuthenticated() && !(a instanceof AnonymousAuthenticationToken)) {
					username = a.getName();
				}
			}
			form.setJ_username(username);
		}
	}

	private void enableFullView(Model model) {
		model.addAttribute("showRememberMe", Boolean.TRUE);
		model.addAttribute("showRegistration", Boolean.TRUE);
		model.addAttribute("showPredefinedUsername", Boolean.FALSE);
	}

	private void enableShortView(AccountLoginForm form, Model model, Boolean rememberMe) {
		model.addAttribute("showRememberMe", rememberMe);
		model.addAttribute("showRegistration", Boolean.FALSE);
		model.addAttribute("showPredefinedUsername", form.hasUsername());
	}

	private String processLoginPage(String page, Model model, Locale locale) {
		if (!processInfoPage(page, model, locale)) { // process page with a error
			// if appropriate content for error page not found - process with default value
			processInfoPage("info/general", model, locale);
		}
		model.addAttribute("infoId", "general"); // this is CSS class name and FTL page name. Always login.
		return "/content/account/pages/general";
	}
}
