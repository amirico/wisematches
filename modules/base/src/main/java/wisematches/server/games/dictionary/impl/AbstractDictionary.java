package wisematches.server.games.dictionary.impl;

import wisematches.server.games.dictionary.Dictionary;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractDictionary implements Dictionary {
	protected final Locale locale;
	private final String source;

	public AbstractDictionary(Locale locale) {
		this(locale, null);
	}

	public AbstractDictionary(Locale locale, String source) {
		this.locale = locale;
		this.source = source;
	}

	public Locale getLocale() {
		return locale;
	}

	public String getSource() {
		return source;
	}
}
