package wisematches.server.web.i18n;

import wisematches.server.personality.account.Language;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class TimeDeclension {
	public static final TimeDeclension RUSSIAN = new TimeDeclension(
			new String[]{"день", "дня", "дней"},
			new String[]{"час", "часа", "часов"},
			new String[]{"минута", "минуты", "минут"});

	public static final TimeDeclension ENGLISH = new TimeDeclension(
			new String[]{"day", "days", "days"},
			new String[]{"hour", "hours", "hours"},
			new String[]{"minute", "minutes", "minutes"}
	);

	private final String[] DAYS;
	private final String[] HOURS;
	private final String[] MINUTES;

	private TimeDeclension(String[] DAYS, String[] HOURS, String[] MINUTES) {
		this.DAYS = DAYS;
		this.HOURS = HOURS;
		this.MINUTES = MINUTES;
	}

	public String days(int days) {
		return DAYS[index(days)];
	}

	public String hours(int hours) {
		return HOURS[index(hours)];
	}

	public String minutes(int minutes) {
		return MINUTES[index(minutes)];
	}

	private static int index(int value) {
		int v = value % 100;
		if (v > 20) {
			v %= 10;
		}
		if (v == 1) {
			return 0;
		} else if (v > 1 && v < 5) {
			return 1;
		}
		return 2;
	}

	public static TimeDeclension declension(String language) {
		return declension(Language.byCode(language));
	}

	public static TimeDeclension declension(Language l) {
		switch (l) {
			case RUSSIAN:
				return RUSSIAN;
		}
		return ENGLISH;
	}
}
