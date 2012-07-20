package wisematches.playground.tournament.impl;

import org.quartz.CronExpression;
import wisematches.playground.timer.BreakingDayListener;
import wisematches.playground.tournament.Announcement;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TournamentControlCenter implements BreakingDayListener {
	private final Lock lock = new ReentrantLock();

	private CronExpression cronExpression;
	private TimeZone timeZone = TimeZone.getTimeZone("GMT");

	private HibernateTournamentDao tournamentDao;
	private TournamentSettingsProvider settingsProvider;

	public TournamentControlCenter() {
	}

	@Override
	public void breakingDayTime(Date midnight) {
		lock.lock();
		try {
			final List<Announcement> announcements = tournamentDao.searchTournamentEntities(null, new AnnouncementImpl.Context(), null, null, null);
			for (Announcement announcement : announcements) {
				if (announcement.getScheduledDate().equals(midnight)) {
					// start new tournament
				}
			}
		} finally {
			lock.unlock();
		}
	}


	private Date getMidnight() {
		return new Date((System.currentTimeMillis() / 86400000L) * 86400000L);
	}

	private Date getNextTournamentDate() {
		return cronExpression.getNextValidTimeAfter(getMidnight());
	}

	public void setTournamentDao(HibernateTournamentDao tournamentDao) {
		lock.lock();
		try {
			this.tournamentDao = tournamentDao;
		} finally {
			lock.unlock();
		}
	}

	public void setSettingsProvider(TournamentSettingsProvider settingsProvider) {
		lock.lock();
		try {
			this.settingsProvider = settingsProvider;
		} finally {
			lock.unlock();
		}
	}

	public void setTimeZone(TimeZone timeZone) {
		lock.lock();
		try {
			if (timeZone == null) {
				throw new NullPointerException("TimeZone can't be null");
			}

			this.timeZone = timeZone;

			if (this.cronExpression != null) {
				this.cronExpression.setTimeZone(timeZone);
			}
		} finally {
			lock.unlock();
		}
	}

	public void setCronExpression(CronExpression cronExpression) {
		lock.lock();
		try {
			this.cronExpression = cronExpression;

			if (this.cronExpression != null) {
				this.cronExpression.setTimeZone(timeZone);
			}
		} finally {
			lock.unlock();
		}
	}
}
