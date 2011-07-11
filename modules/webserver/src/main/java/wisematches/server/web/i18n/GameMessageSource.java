package wisematches.server.web.i18n;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.springframework.context.MessageSource;
import wisematches.personality.Language;
import wisematches.personality.player.Player;
import wisematches.personality.player.computer.ComputerPlayer;
import wisematches.playground.GameBoard;
import wisematches.playground.GameSettings;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.ViolatedRestrictionException;

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
	private static final Map<Locale, DateFormat> TIME_FORMATTER = new ConcurrentHashMap<Locale, DateFormat>();

	static {
		for (Language lang : Language.values()) {
			DATE_FORMATTER.put(lang.locale(), DateFormat.getDateInstance(DateFormat.LONG, lang.locale()));
			TIME_FORMATTER.put(lang.locale(), DateFormat.getTimeInstance(DateFormat.SHORT, lang.locale()));
		}
	}

	public GameMessageSource() {
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

	public String formatDate(Date date, Locale locale) {
		return DATE_FORMATTER.get(locale).format(date);
	}

	public String formatTime(Date date, Locale locale) {
		return TIME_FORMATTER.get(locale).format(date);
	}

	public String formatSpentTime(GameBoard board, Locale locale) {
		return formatMinutes(getSpentMinutes(board), locale);
	}

	public String formatRemainedTime(GameBoard board, Locale locale) {
		return formatMinutes(getRemainedMinutes(board), locale);
	}

	public int getAge(Date date) {
		DateMidnight birthdate = new DateMidnight(date);
		DateTime now = new DateTime();
		Years age = Years.yearsBetween(birthdate, now);
		return age.getYears();
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

	public String formatJoinException(GameProposal<? extends GameSettings> proposal, Player player, Locale locale) {
		try {
			proposal.isSuitablePlayer(player);
			return null;
		} catch (ViolatedRestrictionException ex) {
			return getMessage("game.error.restriction." + ex.getCode() + ".label", locale, ex.getExpectedValue(), ex.getActualValue());
		}
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
