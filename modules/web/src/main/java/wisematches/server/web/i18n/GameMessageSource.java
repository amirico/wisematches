package wisematches.server.web.i18n;

import org.springframework.context.MessageSource;
import wisematches.server.gameplaying.board.GameBoard;
import wisematches.server.gameplaying.board.GameState;
import wisematches.server.player.Language;
import wisematches.server.player.Player;
import wisematches.server.player.computer.ComputerPlayer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameMessageSource {
	private MessageSource messageSource;

	private static final Map<Locale, DateFormat> FORMAT_MAP = new ConcurrentHashMap<Locale, DateFormat>();

	static {
		for (Language lang : Language.values()) {
			FORMAT_MAP.put(lang.locale(), new SimpleDateFormat("MMM dd, yyyy HH:mm", lang.locale()));
		}
	}

	public GameMessageSource() {
	}

	public String getPlayerNick(Player p, String language) {
		return getPlayerNick(p, Language.byCode(language).locale());
	}

	public String getPlayerNick(Player p, Locale locale) {
		if (p instanceof ComputerPlayer) {
			return messageSource.getMessage("game.player." + p.getNickname(), null, locale);
		}
		return p.getNickname();
	}

	public String formatGameState(GameState gameState, String language) {
		return formatGameState(gameState, Language.byCode(language).locale());
	}

	public String formatGameState(GameState gameState, Locale locale) {
		return messageSource.getMessage("game.status." + gameState.name().toLowerCase(), null, locale);
	}

	public String formatDate(Date date, String language) {
		return formatDate(date, Language.byCode(language).locale());
	}

	public String formatDate(Date date, Locale locale) {
		return FORMAT_MAP.get(locale).format(date);
	}

	public String getRemainedTime(GameBoard board, String language) {
		return getRemainedTime(board, Language.byCode(language).locale());
	}

	public String getRemainedTime(GameBoard board, Locale locale) {
		final int daysPerMove = board.getGameSettings().getDaysPerMove();
		final long elapsedTime = System.currentTimeMillis() - board.getLastMoveTime().getTime();

		final long minutesPerMove = daysPerMove * 24 * 60;
		final long minutesElapsed = (elapsedTime / 1000 / 60);
		final long time = minutesPerMove - minutesElapsed;

		final int days = (int) (time / 60 / 24);
		final int hours = (int) ((time - (days * 24 * 60)) / 60);
		final int minutes = (int) (time % 60);

		final StringBuilder b = new StringBuilder();
		if (days > 0) {
			b.append(days).append(messageSource.getMessage("time.notation.day", null, locale)).append(" ");
		}
		if (hours > 0) {
			b.append(hours).append(messageSource.getMessage("time.notation.hour", null, locale)).append(" ");
		}
		if ((days == 0) && (minutes > 0)) {
			b.append(minutes).append(messageSource.getMessage("time.notation.minute", null, locale)).append(" ");
		}
		return b.toString();
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
