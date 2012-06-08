package wisematches.playground.tournament.impl;

import wisematches.playground.tournament.Announcement;
import wisematches.playground.tournament.AnnouncementManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TournamentActivator {
	private AnnouncementManager announcementManager;

	public TournamentActivator() {
	}

	public void activateTournament(int announcement) {
		final Announcement tournamentAnnouncement = announcementManager.getTournamentAnnouncement();
	}

	public void setAnnouncementManager(AnnouncementManager announcementManager) {
		this.announcementManager = announcementManager;
	}
}
