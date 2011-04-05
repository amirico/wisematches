package wisematches.server.web.i18n;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.WebUtils;
import wisematches.server.personality.account.Language;
import wisematches.server.personality.player.Player;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMPlayerLocaleResolver extends SessionLocaleResolver {
	public WMPlayerLocaleResolver() {
	}

	public Locale resolveLocale(HttpServletRequest request) {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return resolveLocale(super.resolveLocale(request));
		} else {
			Locale locale = (Locale) WebUtils.getSessionAttribute(request, LOCALE_SESSION_ATTRIBUTE_NAME);
			if (locale == null) {
				if (authentication.getDetails() instanceof Player) {
					return ((Player) authentication.getDetails()).getLanguage().locale();
				} else {
					return resolveLocale(super.resolveLocale(request));
				}
			} else {
				return locale;
			}
		}
	}

	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		WebUtils.setSessionAttribute(request, LOCALE_SESSION_ATTRIBUTE_NAME, resolveLocale(locale));
	}

	/**
	 * Resolve specified locale only to supported
	 *
	 * @param locale the locale to be resolve
	 * @return resolved locale
	 */
	protected Locale resolveLocale(Locale locale) {
		return Language.byCode(locale.getLanguage()).locale();
	}
}
