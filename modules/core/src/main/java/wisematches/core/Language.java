package wisematches.core;

import java.util.Locale;

/**
 * This is enumeration of all supported languages in this site.
 * <p/>
 * Language is very similar to {@code Locale} but it's singletone ans easy to use.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public enum Language {
	EN("UTF-8", new Locale("en"), new Alphabet("abcdefghijklmnopqrstuvwxyz"), Localization.ENGLISH),
	RU("Cp1251", new Locale("ru"), new Alphabet("абвгдеёжзийклмнопрстуфхцчшщъыьэюя"), Localization.RUSSIAN);

	private final String code;
	private final Locale locale;
	private final Alphabet alphabet;
	private final String nativeCharset;
	private final Localization localization;

	/**
	 * Default language which should be used if another is not specified.
	 */
	public static final Language DEFAULT = EN;

	private Language(String nativeCharset, Locale locale, Alphabet alphabet, Localization localization) {
		this.code = locale.getLanguage();
		this.locale = locale;
		this.alphabet = alphabet;
		this.nativeCharset = nativeCharset;
		this.localization = localization;
	}

	/**
	 * Returns code (or name) of this language
	 *
	 * @return the code of this language.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Returns getLocale for this language.
	 *
	 * @return the getLocale for this language.
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * Returns lowercase alphabet for this language.
	 *
	 * @return the lowercase alphabet for this language.
	 */
	public Alphabet getAlphabet() {
		return alphabet;
	}

	/**
	 * Returns native charset encoding for this language.
	 *
	 * @return the native charset encoding for this language.
	 */
	public String getNativeCharset() {
		return nativeCharset;
	}

	/**
	 * Returns localization interface for this language.
	 *
	 * @return the localization interface for this language.
	 */
	public Localization getLocalization() {
		return localization;
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
		if (EN.code.equalsIgnoreCase(code)) {
			return EN;
		} else if (RU.code.equalsIgnoreCase(code)) {
			return RU;
		}
		return null;
	}

	public static Language byLocale(Locale locale) {
		return byCode(locale.getLanguage());
	}
}