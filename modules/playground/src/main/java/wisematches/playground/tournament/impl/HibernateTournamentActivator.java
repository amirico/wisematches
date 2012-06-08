package wisematches.playground.tournament.impl;

import wisematches.playground.tournament.AnnouncementManager;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTournamentActivator {
	private AnnouncementManager announcementManager;

	public HibernateTournamentActivator() {
	}

	public void setAnnouncementManager(AnnouncementManager announcementManager) {
		this.announcementManager = announcementManager;
	}
}
