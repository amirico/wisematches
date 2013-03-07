package wisematches.server.web.servlet.sdo;

import wisematches.playground.GameMessageSource;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class InternationalisedInfo {
	protected final Locale locale;
	protected final GameMessageSource messageSource;

	public InternationalisedInfo(GameMessageSource messageSource, Locale locale) {
		this.locale = locale;
		this.messageSource = messageSource;
	}
}
