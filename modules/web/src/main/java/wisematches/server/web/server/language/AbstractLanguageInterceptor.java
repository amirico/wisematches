/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */
package wisematches.server.web.server.language;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import wisematches.server.player.Language;
import wisematches.server.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * {@code LanguageChangeInterceptor} is a {@code HandlerInterceptor} which process client request and
 * updates {@code HttpServletRequest} to specify client language. After this interceptor {@code HttpServletRequest}
 * always has attribute {@code Language} which can be taken from
 * request using {@code WebUtils.getRequestAttribute(request, Language.class} method.
 * <p/>
 * This interceptor takes language code from requested uri if uri does not contains language code it will be taken
 * from session and if session does not contains it will be taken from request. If URI is visible uri but does
 * not contains language code, when request will be redirected to uri with language code.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 * @see wisematches.server.web.util.WebUtils
 * @see Language
 */
public abstract class AbstractLanguageInterceptor extends HandlerInterceptorAdapter {
	private final Set<String> visibleUrls = new HashSet<String>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Language language = getUserLanguage(request);
		if (language == null) {
			language = getPredefinedLanguage(request);
		}

		if (language == null) {
			language = Language.DEFAULT;
		}

		WebUtils.setRequestAttribute(request, Language.class, language);

		if (isVisibleUrl(request) && isChangeLanguageRequired(request, language)) {
			changeLanguage(request, language);

			final String redirectUrl = getRedirectUrl(request, language);
			if (redirectUrl != null) {
				response.sendRedirect(redirectUrl);
				return false;
			}
		}
		return true;
	}

	private void changeLanguage(HttpServletRequest request, Language language) {
		final HttpSession session = request.getSession(false);
		if (session != null) {
			session.setAttribute(Language.class.getName(), language);
		}
	}

	private boolean isVisibleUrl(HttpServletRequest request) {
		final String uri = request.getRequestURI();
		for (String visibleUrl : visibleUrls) {
			if (uri.startsWith(visibleUrl)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Changes uri from request and add {@code /languageCode} at the end of URI.
	 *
	 * @param request  the request
	 * @param language the new language
	 * @return the changed request or {@code null} if redirect is not required.
	 */
	protected abstract String getRedirectUrl(HttpServletRequest request, Language language);

	/**
	 * Checks is specified request contains specified language or not.
	 *
	 * @param request  the request to be checked.
	 * @param language the expected language
	 * @return {@code true} if request already contains specfied langiage; {@code false} - otherwise.
	 */
	protected abstract boolean isChangeLanguageRequired(HttpServletRequest request, Language language);

	/**
	 * Returns language specified by user. This language can be specified as a query parameter or as a
	 * part or request path depends on implementation.
	 *
	 * @param request the request to get a language.
	 * @return the user language from specified request or {@code null} if request does not contains
	 *         user language and predefined language should be used.
	 */
	protected abstract Language getUserLanguage(HttpServletRequest request);

	/**
	 * Returns predefined language from specified request. This method first takes language
	 * from associated session and if no language in session when {@code HttpServletRequest#getLocale()} method
	 * is used.
	 *
	 * @param request the request to get predefined language.
	 * @return the predefined language or {@code null} if there is no such language.
	 * @see javax.servlet.http.HttpServletRequest#getLocale()
	 */
	protected Language getPredefinedLanguage(HttpServletRequest request) {
		final HttpSession session = request.getSession(false);
		if (session != null) {
			Language language = (Language) session.getAttribute(Language.class.getName());
			if (language == null) {
				final Locale locale = request.getLocale();
				if (locale != null) {
					language = Language.byCode(locale.getLanguage());
				}
			}
			return language;
		}
		return null;
	}

	public void setVisibleUrls(Set<String> visibleUrls) {
		this.visibleUrls.clear();

		if (visibleUrls != null) {
			this.visibleUrls.addAll(visibleUrls);
		}
	}
}
