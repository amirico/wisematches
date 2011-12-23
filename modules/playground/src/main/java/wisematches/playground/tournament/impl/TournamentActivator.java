package wisematches.playground.tournament.impl;

import org.quartz.CronExpression;
import wisematches.playground.tournament.TournamentManager;
import wisematches.playground.tournament.TournamentPoster;
import wisematches.playground.tournament.TournamentTicketManager;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TournamentActivator {
	private CronExpression cronExpression;
	private TimeZone timeZone = TimeZone.getTimeZone("GMT");

	private TournamentManager tournamentManager;
	private TournamentTicketManager ticketManager;

//	private final CronTrigger tr = new CronTrigger();

	public TournamentActivator() {
	}

	/**
	 * Checks and starts new tournament if it's tournament date and tournament wasn't started yet.
	 */
	public void startTournament() {
		if (isTournamentDay()) {
			final Date nextTournamentDay = getNextTournamentDay();
			final TournamentPoster poster = ticketManager.getTournamentPoster();
			if (poster == null || poster.getScheduledDate().equals(nextTournamentDay)) {
			}
		}
	}

	protected boolean isTournamentDay() {
		return cronExpression.isSatisfiedBy(new Date());
	}

	protected Date getNextTournamentDay() {
		// TODO: must return midnight time
		final long date = ((System.currentTimeMillis() + 86400000L) / 86400000L) * 86400000L;
		return cronExpression.getNextValidTimeAfter(new Date(date));
	}

	public void setCronExpression(String expression) throws ParseException {
		this.cronExpression = new CronExpression(expression);
		if (timeZone != null) {
			cronExpression.setTimeZone(timeZone);
		}
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
		if (cronExpression != null) {
			cronExpression.setTimeZone(timeZone);
		}
	}

	public void setTournamentManager(TournamentManager tournamentManager) {
		this.tournamentManager = tournamentManager;
	}

	public void setTournamentTicketManager(TournamentTicketManager ticketManager) {
		this.ticketManager = ticketManager;
	}
}
