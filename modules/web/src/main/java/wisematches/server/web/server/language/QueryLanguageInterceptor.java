/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */
package wisematches.server.web.server.language;

import wisematches.server.player.Language;

import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class QueryLanguageInterceptor extends AbstractLanguageInterceptor {
	@Override
	protected String getRedirectUrl(HttpServletRequest request, Language language) {
		return null;
	}

	@Override
	protected boolean isChangeLanguageRequired(HttpServletRequest request, Language language) {
		return true;
	}

	@Override
	protected Language getUserLanguage(HttpServletRequest request) {
		final String s = request.getParameter("locale");
		if (s != null && s.length() != 0) {
			return Language.byCode(s);
		}
		return null;
	}
}
