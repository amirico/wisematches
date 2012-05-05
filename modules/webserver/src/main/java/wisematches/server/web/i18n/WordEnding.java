package wisematches.server.web.i18n;

import wisematches.personality.Language;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public enum WordEnding {
	RUSSIAN() {
		@Override
		public String getWordEnding(int quantity) {
			if (Math.abs(quantity) == 1) {
				return "а";
			} else if (Math.abs(quantity) < 5) {
				return "ы";
			}
			return "";
		}
	},

	ENGLISH() {
		@Override
		public String getWordEnding(int quantity) {
			if (Math.abs(quantity) < 1) {
				return "";
			}
			return "s";
		}
	};

	public abstract String getWordEnding(int quantity);

	public static WordEnding ending(Locale locale) {
		return ending(Language.byCode(locale.getLanguage()));
	}

	public static WordEnding ending(String language) {
		return ending(Language.byCode(language));
	}

	public static WordEnding ending(Language l) {
		switch (l) {
			case RU:
				return RUSSIAN;
		}
		return ENGLISH;
	}
}
