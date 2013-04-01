package wisematches.server.web.servlet.sdo.scribble.board;

import wisematches.core.Personality;
import wisematches.playground.GameMessageSource;
import wisematches.playground.GameResolution;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.server.web.servlet.sdo.InternationalisedInfo;
import wisematches.server.web.servlet.sdo.time.TimeInfo;

import java.util.Date;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class StatusInfo extends InternationalisedInfo {
	private final ScribbleBoard board;

	public StatusInfo(ScribbleBoard board, GameMessageSource messageSource, Locale locale) {
		super(messageSource, locale);
		this.board = board;
	}

	public TimeInfo getSpentTime() {
		final long millis = messageSource.getSpentMinutes(board) * 1000 * 60;
		final String text = messageSource.formatSpentTime(board, locale);
		return new TimeInfo(millis, text);
	}

	public TimeInfo getRemainedTime() {
		final long millis = messageSource.getRemainedMinutes(board) * 1000 * 60;
		final String text = messageSource.formatRemainedTime(board, locale);
		return new TimeInfo(millis, text);
	}

	public TimeInfo getFinishedTime() {
		final Date finishedDate = board.getFinishedDate();
		if (finishedDate != null) {
			return new TimeInfo(finishedDate, messageSource, locale);
		}
		return null;
	}

	public boolean isActive() {
		return board.isActive();
	}

	public Long getPlayerTurn() {
		final Personality playerTurn = board.getPlayerTurn();
		if (playerTurn == null) {
			return null;
		}
		return playerTurn.getId();
	}

	public long getLastChange() {
		return board.getLastChangeTime().getTime();
	}

	public GameResolution getResolution() {
		return board.getResolution();
	}
}
