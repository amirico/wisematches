package wisematches.server.web.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.server.web.forms.AccountLoginForm;

import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/account")
public class LoginController extends AbstractInfoController {
	private AuthenticationTrustResolver authenticationTrustResolver = new AuthenticationTrustResolverImpl();

	private static final Log log = LogFactory.getLog("wisematches.server.web.accoint");

	public LoginController() {
		super("classpath:/i18n/server/account/");
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
		// but default remember me enabled
		form.setRememberMe("true");

		if (processInfoPage("login", model, locale)) {
			return "/content/account/layout";
		}
		return null;
	}

	@RequestMapping("loginAuth")
	public String loginAuth(@RequestParam(value = "error", required = false) String errorType,
							@ModelAttribute("login") AccountLoginForm form, BindingResult result,
							HttpSession session, Model model, Locale locale) {
		final Authentication a = SecurityContextHolder.getContext().getAuthentication();
		form.setJ_username((String) session.getAttribute(WebAttributes.LAST_USERNAME));
		if (form.getJ_username() == null && a != null) {
			form.setJ_username(a.getName());
		}

		if (errorType == null) {
			final Exception ex = (Exception) session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
			if (ex != null) {
				log.error("Unexpected error code received. There is no exceptions mapping for this", ex);
			}
		} else {
			if (form.getJ_username() == null || form.getJ_username().trim().length() == 0) {
				result.rejectValue("j_username", "account.login.email.err.blank");
			} else {
				result.rejectValue("j_password", "account.login.err." + errorType);
			}
		}

		model.addAttribute("loginErrorType", errorType);

		final String page = (errorType == null) ? "login" : errorType;
		if (!processInfoPage(page, model, locale)) { // process page with a error
			// if appropriate content for error page not found - process with default value
			processInfoPage("login", model, locale);
		}
		model.addAttribute("infoId", "login");
		return "/content/account/layout";
	}
}
