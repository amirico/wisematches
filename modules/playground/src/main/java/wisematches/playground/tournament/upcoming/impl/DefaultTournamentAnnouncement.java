package wisematches.playground.tournament.upcoming.impl;

import wisematches.playground.tournament.TournamentSection;
import wisematches.playground.tournament.upcoming.TournamentAnnouncement;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class DefaultTournamentAnnouncement implements TournamentAnnouncement {
	private final HibernateAnnouncementInfo announcementInfo;
	private final int[] values;

	DefaultTournamentAnnouncement(HibernateAnnouncementInfo announcementInfo, int[] values) {
		this.announcementInfo = announcementInfo;
		this.values = values.clone();
	}

	@Override
	public int getNumber() {
		return announcementInfo.getNumber();
	}

	@Override
	public Date getStartDate() {
		return announcementInfo.getScheduledDate();
	}

	@Override
	public int getTotalTickets() {
		int res = 0;
		for (int value : values) {
			res += value;
		}
		return res;
	}

	@Override
	public int getBoughtTickets(TournamentSection section) {
		return values[section.ordinal()];
	}
}
