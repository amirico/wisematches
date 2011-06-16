package wisematches.server.web.services.restriction;

import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RestrictionException extends Exception {
	private final String name;
	private final Object expected;
	private final Object actual;

	public RestrictionException(String name, Object expected, Object actual) {
		this.name = name;
		this.expected = expected;
		this.actual = actual;
	}

	public String getName() {
		return name;
	}

	public Object getExpected() {
		return expected;
	}

	public Object getActual() {
		return actual;
	}

	public String format(MessageSource messageSource, Locale locale) {
		return messageSource.getMessage("player.restriction." + name, new Object[]{expected, actual}, locale);
	}
}
