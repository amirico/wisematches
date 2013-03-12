package wisematches.server.web.servlet.sdo.scribble;

import wisematches.core.Personality;
import wisematches.playground.AbstractBoardDescription;
import wisematches.playground.GameMessageSource;
import wisematches.server.web.servlet.sdo.InternationalisedInfo;
import wisematches.server.web.servlet.sdo.time.TimeInfo;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class StateInfo extends InternationalisedInfo {
	private final AbstractBoardDescription<?, ?> description;

	public StateInfo(AbstractBoardDescription<?, ?> description, GameMessageSource messageSource, Locale locale) {
		super(messageSource, locale);
		this.description = description;
	}

	public boolean isActive() {
		return description.isActive();
	}

	public Long getPlayerTurn() {
		final Personality playerTurn = description.getPlayerTurn();
		if (playerTurn == null) {
			return null;
		}
		return playerTurn.getId();
	}

	public TimeInfo getSpentTime() {
		final long millis = messageSource.getSpentMinutes(description) * 1000 * 60;
		final String text = messageSource.formatSpentTime(description, locale);
		return new TimeInfo(millis, text);
	}

	public long getLastChangeTime() {
		return description.getLastChangeTime().getTime();
	}

	public TimeInfo getRemainedTime() {
		final long millis = messageSource.getRemainedMinutes(description) * 1000 * 60;
		final String text = messageSource.formatRemainedTime(description, locale);
		return new TimeInfo(millis, text);
	}
}
