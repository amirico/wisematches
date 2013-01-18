package wisematches.server.web.i18n;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import wisematches.core.Language;
import wisematches.server.security.WMAuthorities;
import wisematches.server.security.impl.WMPlayerDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WMPlayerLocaleResolver extends SessionLocaleResolver {
	public WMPlayerLocaleResolver() {
	}

	/**
	 * This implementation uses {@code Authentication} object from {@code SecurityContextHolder}
	 * to get {@code Player} object and if it's set when player's locale is used.
	 *
	 * @param request the request
	 * @return the locale
	 */
	public Locale resolveLocale(HttpServletRequest request) {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return super.resolveLocale(request);
		}
		if (authentication.getAuthorities().contains(WMAuthorities.GUEST)) {
			return super.resolveLocale(request);
		}

		final Object details = authentication.getPrincipal();
		if (details instanceof WMPlayerDetails) {
			return ((WMPlayerDetails) details).getPlayer().getLanguage().locale();
		} else {
			return super.resolveLocale(request);
		}
	}

	@Override
	protected Locale determineDefaultLocale(HttpServletRequest request) {
		return resolveLocale(super.determineDefaultLocale(request));
	}

	@Override
	public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
		super.setLocale(request, response, resolveLocale(locale));
	}

	/**
	 * Resolve specified locale only to supported
	 *
	 * @param locale the locale to be resolve
	 * @return resolved locale
	 */
	protected Locale resolveLocale(Locale locale) {
		Language language = Language.byLocale(locale);
		if (language != null) {
			return language.locale();
		}
		return Language.DEFAULT.locale();
	}
}
