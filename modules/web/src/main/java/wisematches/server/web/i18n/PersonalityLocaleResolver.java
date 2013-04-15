package wisematches.server.web.i18n;

import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import wisematches.core.Language;
import wisematches.core.Player;
import wisematches.core.security.PersonalityContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PersonalityLocaleResolver extends SessionLocaleResolver {
	public PersonalityLocaleResolver() {
	}

	/**
	 * This implementation uses {@code Authentication} object from {@code SecurityContextHolder}
	 * to get {@code Player} object and if it's set when player's locale is used.
	 *
	 * @param request the request
	 * @return the locale
	 */
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		final Player player = PersonalityContext.getPrincipal();
		if (player != null) {
			return player.getLanguage().getLocale();
		}
		return super.resolveLocale(request);
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
		if (locale != null) {
			final Language language = Language.byLocale(locale);
			if (language != null) {
				return language.getLocale();
			}
		}
		return Language.DEFAULT.getLocale();
	}
}
