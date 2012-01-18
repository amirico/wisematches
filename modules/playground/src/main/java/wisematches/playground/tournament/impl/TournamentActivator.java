package wisematches.playground.tournament.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

	private AbstractTournamentManager tournamentManager;
	private AbstractTournamentTicketManager ticketManager;

	private static final Log log = LogFactory.getLog("wisematches.server.tournament");

	public TournamentActivator() {
	}

	/**
	 * Checks and starts new tournament if it's tournament date and tournament wasn't started yet.
	 */
	public void startTournament() {
		if (isTournamentDay()) {
			log.info("It's tournament day! Checking progress or start new tournament");

			final Date nextTournamentDay = getNextTournamentDay();
			final TournamentPoster poster = ticketManager.getTournamentPoster();
			if (poster == null || poster.getScheduledDate().equals(nextTournamentDay)) {
				initializeNewTournament();
			}
		}
	}

	protected boolean isTournamentDay() {
		return cronExpression.isSatisfiedBy(new Date());
	}

	protected Date getNextTournamentDay() {
		// Always midnight time
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

	private void initializeNewTournament() {
		final TournamentPoster poster = ticketManager.announceTournament(getNextTournamentDay());
	}

	public void setTicketManager(AbstractTournamentTicketManager ticketManager) {
		this.ticketManager = ticketManager;
	}

	public void setTournamentManager(AbstractTournamentManager tournamentManager) {
		this.tournamentManager = tournamentManager;
	}
}
