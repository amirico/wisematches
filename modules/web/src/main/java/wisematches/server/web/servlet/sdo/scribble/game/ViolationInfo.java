package wisematches.server.web.servlet.sdo.scribble.game;

import wisematches.playground.GameMessageSource;
import wisematches.playground.propose.CriterionViolation;
import wisematches.server.web.servlet.sdo.InternationalisedInfo;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ViolationInfo extends InternationalisedInfo {
	private final CriterionViolation violation;

	public ViolationInfo(CriterionViolation violation, GameMessageSource messageSource, Locale locale) {
		super(messageSource, locale);
		this.violation = violation;
	}

	public String getCode() {
		return violation.getCode();
	}

	public String getLongDescription() {
		return messageSource.formatViolation(violation, locale, false);
	}

	public String getShortDescription() {
		return messageSource.formatViolation(violation, locale, true);
	}
}
