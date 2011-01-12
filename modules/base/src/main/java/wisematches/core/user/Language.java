/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */
package wisematches.core.user;

import java.util.Locale;

/**
 * This is enumeration of all supported languages in this site.
 * <p/>
 * Language is very similar to {@code Locale} but it's singletone ans easy to use.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public enum Language {
	/**
	 * English language.
	 */
	ENGLISH(new Locale("en")),
	/**
	 * Russian language.
	 */
	RUSSIAN(new Locale("ru"));

	/**
	 * Default language which should be used if another is not specified.
	 */
	public static final Language DEFAULT = ENGLISH;

	private final String code;
	private final Locale locale;


	Language(Locale locale) {
		this.code = locale.getLanguage();
		this.locale = locale;
	}

	/**
	 * Returns code (or name) of this language
	 *
	 * @return the code of this language.
	 */
	public String code() {
		return code;
	}


	/**
	 * Returns locale for this language.
	 *
	 * @return the locale for this language.
	 */
	public Locale locale() {
		return locale;
	}

	/**
	 * Returns language by specified code.
	 *
	 * @param code the language code
	 * @return language by specified code or {@code null} if language is unknown or code is null
	 */
	public static Language byCode(String code) {
		if (code == null) {
			return null;
		}
		if (ENGLISH.code.equalsIgnoreCase(code)) {
			return ENGLISH;
		} else if (RUSSIAN.code.equalsIgnoreCase(code)) {
			return RUSSIAN;
		}
		return null;
	}
}