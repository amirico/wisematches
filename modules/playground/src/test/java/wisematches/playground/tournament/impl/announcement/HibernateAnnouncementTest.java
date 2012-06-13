package wisematches.playground.tournament.impl.announcement;

import org.junit.Test;
import wisematches.personality.Language;
import wisematches.playground.tournament.TournamentCategory;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class HibernateAnnouncementTest {
	public HibernateAnnouncementTest() {
	}

	@Test
	public void testBoughtTickets() {
		final HibernateAnnouncement announcement = new HibernateAnnouncement(1, new Date());

		for (Language language : Language.values()) {
			for (TournamentCategory category : TournamentCategory.values()) {
				assertEquals(0, announcement.getBoughtTickets(language, category));
			}
		}

		int i = 0;
		for (Language language : Language.values()) {
			for (TournamentCategory category : TournamentCategory.values()) {
				announcement.setBoughtTickets(language, category, i++);
			}
		}

		int j = 0;
		for (Language language : Language.values()) {
			for (TournamentCategory category : TournamentCategory.values()) {
				assertEquals(j++, announcement.getBoughtTickets(language, category));
			}
		}

		int k = 0;
		for (Language language : Language.values()) {
			for (TournamentCategory category : TournamentCategory.values()) {
				announcement.changeBoughtTickets(language, category, k++);
			}
		}

		int m = 0;
		for (Language language : Language.values()) {
			for (TournamentCategory category : TournamentCategory.values()) {
				assertEquals(m++ * 2, announcement.getBoughtTickets(language, category));
			}
		}
	}
}
