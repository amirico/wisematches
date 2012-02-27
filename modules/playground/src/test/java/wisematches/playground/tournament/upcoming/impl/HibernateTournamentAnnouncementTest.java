package wisematches.playground.tournament.upcoming.impl;

import org.junit.Test;
import wisematches.personality.Language;
import wisematches.playground.tournament.TournamentSection;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateTournamentAnnouncementTest {
	public HibernateTournamentAnnouncementTest() {
	}

	@Test
	public void testBoughtTickets() {
		final HibernateTournamentAnnouncement announcement = new HibernateTournamentAnnouncement(1, new Date());

		for (Language language : Language.values()) {
			for (TournamentSection section : TournamentSection.values()) {
				assertEquals(0, announcement.getBoughtTickets(language, section));
			}
		}

		int i = 0;
		for (Language language : Language.values()) {
			for (TournamentSection section : TournamentSection.values()) {
				announcement.setBoughtTickets(language, section, i++);
			}
		}

		int j = 0;
		for (Language language : Language.values()) {
			for (TournamentSection section : TournamentSection.values()) {
				assertEquals(j++, announcement.getBoughtTickets(language, section));
			}
		}

		int k = 0;
		for (Language language : Language.values()) {
			for (TournamentSection section : TournamentSection.values()) {
				announcement.changeBoughtTickets(language, section, k++);
			}
		}

		int m = 0;
		for (Language language : Language.values()) {
			for (TournamentSection section : TournamentSection.values()) {
				assertEquals(m++ * 2, announcement.getBoughtTickets(language, section));
			}
		}
	}
}
