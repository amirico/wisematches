package wisematches.server.web.servlet.sdo.board;

import wisematches.playground.GameMessageSource;
import wisematches.playground.GameResolution;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.server.web.servlet.sdo.InternationalisedInfo;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ScribbleStateInfo extends InternationalisedInfo {
	private final ScribbleBoard board;

	public ScribbleStateInfo(ScribbleBoard board, GameMessageSource messageSource, Locale locale) {
		super(messageSource, locale);
		this.board = board;
	}

	public boolean isActive() {
		return board.isActive();
	}

	public Long getPlayerTurn() {
		return board.getPlayerTurn() != null ? board.getPlayerTurn().getId() : null;
	}

	public long getSpentTimeMillis() {
		return messageSource.getSpentMinutes(board) * 1000 * 60;
	}

	public String getSpentTimeMessage() {
		return messageSource.formatSpentTime(board, locale);
	}

	public long getFinishTimeMillis() {
		if (board.getFinishedDate() != null) {
			return board.getFinishedDate().getTime();
		}
		return 0;
	}

	public String getFinishTimeMessage() {
		if (board.getFinishedDate() != null) {
			return messageSource.formatDate(board.getFinishedDate(), locale);
		}
		return null;
	}

	public long getRemainedTimeMillis() {
		return messageSource.getRemainedMinutes(board) * 1000 * 60;
	}

	public String getRemainedTimeMessage() {
		return messageSource.formatRemainedTime(board, locale);
	}

	public GameResolution getGameResolution() {
		return board.getResolution();
	}

	/*
	public static Map<String, Object> convertGameState(final ScribbleBoard board, GameMessageSource messageSource, Locale locale) {
		final Map<String, Object> state = new HashMap<>();
		if (!board.isActive()) {
			final Collection<Personality> wonPlayers = board.getWonPlayers();
			int index = 0;
			final long[] res = new long[wonPlayers.size()];
			for (Personality wonPlayer : wonPlayers) {
				res[index++] = wonPlayer.getId();
			}
			state.put("winners", res);
//			state.put("ratings", board.getRatingChanges());
		}
		return state;
	}
*/
}
