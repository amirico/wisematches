package wisematches.server.web.servlet.sdo.scribble;

import wisematches.core.Personality;
import wisematches.playground.AbstractBoardDescription;
import wisematches.playground.GameMessageSource;
import wisematches.playground.GameResolution;
import wisematches.server.web.servlet.sdo.InternationalisedInfo;
import wisematches.server.web.servlet.sdo.time.TimeInfo;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class OutcomesInfo extends InternationalisedInfo {
	private final AbstractBoardDescription<?, ?> description;

	public OutcomesInfo(AbstractBoardDescription<?, ?> description, GameMessageSource messageSource, Locale locale) {
		super(messageSource, locale);
		this.description = description;
	}

	public ScoreInfo[] getScores() {
		final List<Personality> players = description.getPlayers();
		int index = 0;
		final ScoreInfo[] res = new ScoreInfo[players.size()];
		for (Personality player : players) {
			res[index++] = new ScoreInfo(description.getPlayerHand(player));
		}
		return res;
	}

	public TimeInfo getFinishedTime() {
		final Date finishedDate = description.getFinishedDate();
		if (finishedDate != null) {
			return new TimeInfo(finishedDate, messageSource, locale);
		}
		return null;
	}

	public GameResolution getResolution() {
		return description.getResolution();
	}
}
