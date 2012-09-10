package wisematches.playground.tourney.regular.impl;

import org.quartz.CronExpression;
import wisematches.playground.timer.BreakingDayListener;

import java.util.Date;
import java.util.TimeZone;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TournamentScheduler implements BreakingDayListener {
	private CronExpression cronExpression;
	private TimeZone timeZone = TimeZone.getTimeZone("GMT");

	public TournamentScheduler() {
	}

	@Override
	public void breakingDayTime(Date date) {
		if (cronExpression.isSatisfiedBy(date)) {
//			tournamentManager.initiatePublicTournament(getNextTournamentDate());
		}
	}

	private Date getMidnight() {
		return new Date((System.currentTimeMillis() / 86400000L) * 86400000L);
	}

	private Date getNextTournamentDate() {
		return cronExpression.getNextValidTimeAfter(getMidnight());
	}

	public void setTimeZone(TimeZone timeZone) {
		if (timeZone == null) {
			throw new NullPointerException("TimeZone can't be null");
		}

		this.timeZone = timeZone;

		if (this.cronExpression != null) {
			this.cronExpression.setTimeZone(timeZone);
		}
	}

	public void setCronExpression(CronExpression cronExpression) {
		this.cronExpression = cronExpression;

		if (this.cronExpression != null) {
			this.cronExpression.setTimeZone(timeZone);
		}
	}
}
