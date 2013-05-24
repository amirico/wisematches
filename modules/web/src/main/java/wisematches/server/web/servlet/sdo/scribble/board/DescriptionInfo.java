package wisematches.server.web.servlet.sdo.scribble.board;

import wisematches.core.Personality;
import wisematches.playground.BoardDescription;
import wisematches.playground.GameMessageSource;
import wisematches.playground.GameRelationship;
import wisematches.playground.GameResolution;
import wisematches.playground.scribble.ScribblePlayerHand;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.server.services.state.PlayerStateManager;
import wisematches.server.web.servlet.sdo.InternationalisedInfo;
import wisematches.server.web.servlet.sdo.person.PersonalityInfo;
import wisematches.server.web.servlet.sdo.scribble.PlayerInfo;
import wisematches.server.web.servlet.sdo.scribble.ScoreInfo;
import wisematches.server.web.servlet.sdo.scribble.SettingsInfo;
import wisematches.server.web.servlet.sdo.time.TimeInfo;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class DescriptionInfo extends InternationalisedInfo {
	private final PlayerStateManager stateManager;
	private final BoardDescription<ScribbleSettings, ScribblePlayerHand> board;

	public DescriptionInfo(BoardDescription<ScribbleSettings, ScribblePlayerHand> board, PlayerStateManager stateManager, GameMessageSource messageSource, Locale locale) {
		super(messageSource, locale);
		this.board = board;
		this.stateManager = stateManager;
	}

	public long getId() {
		return board.getBoardId();
	}

	public SettingsInfo getSettings() {
		return new SettingsInfo(board.getSettings(), board.getRelationship(), messageSource, locale);
	}

	public GameRelationship getRelationship() {
		return board.getRelationship();
	}

	public TimeInfo getStartedTime() {
		return new TimeInfo(board.getStartedDate(), messageSource, locale);
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

	public GameResolution getResolution() {
		return board.getResolution();
	}

	public Long getPlayerTurn() {
		final Personality playerTurn = board.getPlayerTurn();
		if (playerTurn == null) {
			return null;
		}
		return playerTurn.getId();
	}

	public PlayerInfo[] getPlayers() {
		final List<Personality> players = board.getPlayers();

		int index = 0;
		final PlayerInfo[] res = new PlayerInfo[players.size()];
		for (Personality player : players) {
			final ScoreInfo h = new ScoreInfo(board.getPlayerHand(player));
			final PersonalityInfo p = new PersonalityInfo(messageSource.getPersonalityNick(player, locale), player, stateManager);
			res[index++] = new PlayerInfo(h, p);
		}
		return res;
	}

	public long getLastChange() {
		return board.getLastChangeTime().getTime();
	}
}
