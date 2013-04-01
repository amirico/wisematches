package wisematches.server.web.servlet.sdo.scribble.game;

import wisematches.core.Personality;
import wisematches.playground.GameMessageSource;
import wisematches.playground.scribble.ScribbleDescription;
import wisematches.server.services.state.PlayerStateManager;
import wisematches.server.web.servlet.sdo.person.PersonalityInfo;
import wisematches.server.web.servlet.sdo.scribble.ScoreInfo;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameInfo {
	private final Locale locale;
	private final GameMessageSource messageSource;
	private final ScribbleDescription description;
	private final PlayerStateManager playerStateManager;

	public GameInfo(ScribbleDescription description, GameMessageSource messageSource, PlayerStateManager playerStateManager, Locale locale) {
		this.locale = locale;
		this.description = description;
		this.messageSource = messageSource;
		this.playerStateManager = playerStateManager;
	}

	public long getBoardId() {
		return description.getBoardId();
	}

	public String getTitle() {
		return messageSource.getBoardTitle(description, locale);
	}

	public String getLanguage() {
		return messageSource.getMessage("language." + description.getSettings().getLanguage().name().toLowerCase(), locale);
	}

	public int getMovesCount() {
		return description.getMovesCount();
	}

	public String getResolution() {
		return messageSource.getMessage("game.resolution." + description.getResolution().name().toLowerCase(), locale);
	}

	public String getStartedDate() {
		final Date startedDate = description.getStartedDate();
		if (startedDate != null) {
			return messageSource.formatDate(startedDate, locale);
		}
		return null;
	}

	public String getFinishedDate() {
		final Date finishedDate = description.getFinishedDate();
		if (finishedDate != null) {
			return messageSource.formatDate(finishedDate, locale);
		}
		return null;
	}

	public PersonalityInfo[] getPlayers() {
		final List<Personality> players = description.getPlayers();
		final PersonalityInfo[] ps = new PersonalityInfo[players.size()];
		int index = 0;
		for (Personality person : players) {
			ps[index++] = new PersonalityInfo(messageSource.getPersonalityNick(person, locale), person, playerStateManager);
		}
		return ps;
	}

	public ScoreInfo[] getScores() {
		final List<Personality> players = description.getPlayers();
		final ScoreInfo[] ps = new ScoreInfo[players.size()];
		int index = 0;
		for (Personality person : players) {
			ps[index++] = new ScoreInfo(description.getPlayerHand(person));
		}
		return ps;
	}
}
