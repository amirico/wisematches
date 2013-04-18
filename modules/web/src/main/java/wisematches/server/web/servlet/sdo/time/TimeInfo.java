package wisematches.server.web.servlet.sdo.time;

import wisematches.playground.GameMessageSource;

import java.util.Date;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class TimeInfo {
	private final long millis;
	private final String text;

	public TimeInfo(long millis, String text) {
		this.millis = millis;
		this.text = text;
	}

	public TimeInfo(Date date, GameMessageSource messageSource, Locale locale) {
		this.millis = date.getTime();
		this.text = messageSource.formatDate(date, locale);
	}

	public long getMillis() {
		return millis;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("TimeInfo{");
		sb.append("millis=").append(millis);
		sb.append(", text='").append(text).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
