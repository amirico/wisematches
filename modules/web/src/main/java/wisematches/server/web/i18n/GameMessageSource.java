package wisematches.server.web.i18n;

import org.springframework.context.MessageSource;
import wisematches.server.gameplaying.board.GameBoard;
import wisematches.server.personality.account.Language;
import wisematches.server.personality.player.Player;
import wisematches.server.personality.player.computer.ComputerPlayer;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class GameMessageSource {
	private MessageSource messageSource;

	private static final Map<Locale, DateFormat> DATE_FORMATTER = new ConcurrentHashMap<Locale, DateFormat>();

	static {
		for (Language lang : Language.values()) {
			DATE_FORMATTER.put(lang.locale(), DateFormat.getDateInstance(DateFormat.LONG, lang.locale()));
//			DATE_FORMATTER.put(lang.locale(), new SimpleDateFormat("dd-MMM-yyyy HH:mm", lang.locale()));
//			DATE_FORMATTER.put(lang.locale(), new SimpleDateFormat("MMM dd, yyyy HH:mm z", lang.locale()));
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

	public String getMessage(String code, Locale locale, Object... args) {
		return messageSource.getMessage(code, args, locale);
	}

	public String formatDate(Date date, String language) {
		return formatDate(date, Language.byCode(language).locale());
	}

	public String formatDate(Date date, Locale locale) {
		return DATE_FORMATTER.get(locale).format(date);
	}

	public String formatSpentTime(GameBoard board, String language) {
		return formatSpentTime(board, Language.byCode(language).locale());
	}

	public String formatSpentTime(GameBoard board, Locale locale) {
		return formatMinutes(getSpentMinutes(board), locale);
	}

	public String formatRemainedTime(GameBoard board, String language) {
		return formatRemainedTime(board, Language.byCode(language).locale());
	}

	public String formatRemainedTime(GameBoard board, Locale locale) {
		return formatMinutes(getRemainedMinutes(board), locale);
	}

	public long getTimeMillis(Date date) {
		return date.getTime();
	}

	public long getSpentMinutes(GameBoard board) {
		final long startTime = board.getStartedTime().getTime();
		final long endTime;
		if (board.isGameActive()) {
			endTime = System.currentTimeMillis();
		} else {
			endTime = board.getFinishedTime().getTime();
		}
		return (endTime - startTime) / 1000 / 60;
	}

	public long getRemainedMinutes(GameBoard board) {
		final int daysPerMove = board.getGameSettings().getDaysPerMove();
		final long elapsedTime = System.currentTimeMillis() - board.getLastMoveTime().getTime();

		final long minutesPerMove = daysPerMove * 24 * 60;
		final long minutesElapsed = (elapsedTime / 1000 / 60);
		return minutesPerMove - minutesElapsed;
	}

	public String formatMinutes(long time, String language) {
		return formatMinutes(time, Language.byCode(language).locale());
	}

	public String formatMinutes(long time, Locale locale) {
		final int days = (int) (time / 60 / 24);
		final int hours = (int) ((time - (days * 24 * 60)) / 60);
		final int minutes = (int) (time % 60);

		final TimeDeclension declension = TimeDeclension.declension(Language.byCode(locale.getLanguage()));
		if (days == 0 && hours == 0 && minutes == 0) {
			return declension.momentAgo();
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
			b.append(days).append(declension.days()).append(" ");
		}
		if (hours > 0) {
			b.append(hours).append(declension.hours()).append(" ");
		}
		if ((days == 0 || hours == 0) && (minutes > 0)) {
			b.append(minutes).append(declension.minutes()).append(" ");
		}
		return b.toString();
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
