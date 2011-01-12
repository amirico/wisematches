/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.server.web.server.language;

import wisematches.core.user.Language;

import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PathLanguageInterceptor extends AbstractLanguageInterceptor {
	@Override
	protected String getRedirectUrl(HttpServletRequest request, Language language) {
		String uri = request.getRequestURI();
		final int length = uri.length();
		if (uri.length() >= 3 && uri.charAt(length - 3) == '/') {
			uri = uri.substring(0, length - 3);
		}

		if (uri.charAt(uri.length() - 1) != '/') {
			uri += '/';
		}

		final String queryString = request.getQueryString();
		if (queryString != null && queryString.length() != 0) {
			return uri + language.code() + "?" + queryString;
		}
		return uri + language.code();
	}

	@Override
	protected boolean isChangeLanguageRequired(HttpServletRequest request, Language language) {
		final String requestURI = request.getRequestURI();
		return !requestURI.endsWith(language.code());
	}

	@Override
	protected Language getUserLanguage(HttpServletRequest request) {
		final String uri = request.getRequestURI();

		final int localeSeparatorIndex = uri.lastIndexOf('/');
		if (localeSeparatorIndex != -1) {
			final String language = uri.substring(localeSeparatorIndex + 1);
			if (language.length() == 2) { // all languages code is two chars
				return Language.byCode(language);
			}
		}
		return null;
	}
}
