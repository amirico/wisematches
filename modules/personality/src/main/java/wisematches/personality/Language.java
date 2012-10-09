/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */
package wisematches.personality;

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
	EN(new Locale("en"), "a moment", new String[][]{
			{"d", "day", "days", "days"},
			{"h", "hour", "hours", "hours"},
			{"m", "minute", "minutes", "minutes"}}) {
		@Override
		public String getNumeralEnding(int abs) {
			switch (abs) {
				case 1:
					return "st";
				case 2:
					return "nd";
				case 3:
					return "rd";
				default:
					return "th";
			}
		}

		@Override
		public String getWordEnding(int abs) {
			if (abs < 1) {
				return "";
			}
			return "s";
		}
	},

	/**
	 * Russian language.
	 */
	RU(new Locale("ru"), "одно мгновенье", new String[][]{
			{"д", "день", "дня", "дней"},
			{"ч", "час", "часа", "часов"},
			{"м", "минута", "минуты", "минут"}}) {

		private final String[] NUMERALS = new String[]{"ый", "ый", "ой", "ий", "ый", "ый", "ой", "ой", "ой", "ый"};

		@Override
		public String getNumeralEnding(int abs) {
			if (abs >= 9 && abs <= 20) {
				return "ый";
			}
			return NUMERALS[abs % 10];
		}

		@Override
		public String getWordEnding(int abs) {
			if (abs == 1) {
				return "а";
			} else if (abs < 5) {
				return "ы";
			}
			return "";
		}
	};

	private final String code;
	private final Locale locale;
	private final String momentAgo;
	private final String[][] timeDeclension;

	/**
	 * Default language which should be used if another is not specified.
	 */
	public static final Language DEFAULT = EN;

	private Language(Locale locale, String momentAgo, String[][] timeDeclension) {
		this.momentAgo = momentAgo;
		this.timeDeclension = timeDeclension;
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


	public String getDaysCode() {
		return timeDeclension[0][0];
	}

	public String getHoursCode() {
		return timeDeclension[1][0];
	}

	public String getMinutesCode() {
		return timeDeclension[2][0];
	}


	public String getMomentAgoLabel() {
		return momentAgo;
	}

	public String getDaysLabel(int value) {
		return timeDeclension[0][timeDeclensionIndex(value)];
	}

	public String getHoursLabel(int value) {
		return timeDeclension[1][timeDeclensionIndex(value)];
	}

	public String getMinutesLabel(int value) {
		return timeDeclension[2][timeDeclensionIndex(value)];
	}

	public abstract String getWordEnding(int quantity);

	public abstract String getNumeralEnding(int value);


	private static int timeDeclensionIndex(int value) {
		int v = value % 100;
		if (v > 20) {
			v %= 10;
		}
		if (v == 1) {
			return 1;
		} else if (v > 1 && v < 5) {
			return 2;
		}
		return 3;
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