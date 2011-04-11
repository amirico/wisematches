package wisematches.server.web.i18n;

import org.springframework.context.MessageSource;
import wisematches.server.gameplaying.board.GameBoard;
import wisematches.server.gameplaying.board.GameResolution;
import wisematches.server.personality.account.Language;
import wisematches.server.personality.player.Player;
import wisematches.server.personality.player.computer.ComputerPlayer;

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
			FORMAT_MAP.put(lang.locale(), new SimpleDateFormat("MMM dd, yyyy HH:mm z", lang.locale()));
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

	public String formatGameResolution(GameResolution resolution, String language) {
		return formatGameResolution(resolution, Language.byCode(language).locale());
	}

	public String formatGameResolution(GameResolution resolution, Locale locale) {
		return messageSource.getMessage("game.resolution." + resolution.name().toLowerCase(), null, locale);
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

	public long getRemainedTimeMillis(GameBoard board) {
		final int daysPerMove = board.getGameSettings().getDaysPerMove();
		final long elapsedTime = System.currentTimeMillis() - board.getLastMoveTime().getTime();

		final long minutesPerMove = daysPerMove * 24 * 60;
		final long minutesElapsed = (elapsedTime / 1000 / 60);
		return minutesPerMove - minutesElapsed;
	}

	public String getRemainedTime(GameBoard board, Locale locale) {
		return getRemainedTime(getRemainedTimeMillis(board), locale);
	}

	public String getRemainedTime(long time, String language) {
		return getRemainedTime(time, Language.byCode(language).locale());
	}

	public String getRemainedTime(long time, Locale locale) {
		final int days = (int) (time / 60 / 24);
		final int hours = (int) ((time - (days * 24 * 60)) / 60);
		final int minutes = (int) (time % 60);

		final TimeDeclension declension = TimeDeclension.declension(Language.byCode(locale.getLanguage()));
		if (days <= 0 && hours <= 0 && minutes <= 0) {
			return "--" + declension.days(0) + " --" + declension.hours(0);
		}

		if (hours <= 0 && minutes <= 0) {
			return days + " " + declension.days(days);
		}
		if (days <= 0 && minutes <= 0) {
			return hours + " " + declension.hours(hours);
		}
		if (days <= 0 && hours <= 0) {
			return minutes + " " + declension.minutes(minutes);
		}

		final StringBuilder b = new StringBuilder();
		if (days > 0) {
			b.append(days).append(messageSource.getMessage("time.notation.day", null, locale)).append(" ");
		}
		if (hours > 0) {
			b.append(hours).append(messageSource.getMessage("time.notation.hour", null, locale)).append(" ");
		}
		if ((days == 0 || hours == 0) && (minutes > 0)) {
			b.append(minutes).append(messageSource.getMessage("time.notation.minute", null, locale)).append(" ");
		}
		return b.toString();
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}
}
