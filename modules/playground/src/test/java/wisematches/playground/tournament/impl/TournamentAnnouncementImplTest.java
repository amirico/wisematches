package wisematches.playground.tournament.impl;

import org.junit.Test;
import wisematches.personality.Language;
import wisematches.playground.tournament.Tournament;
import wisematches.playground.tournament.TournamentSection;

import java.util.Date;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TournamentAnnouncementImplTest {
	public TournamentAnnouncementImplTest() {
	}

	@Test
	public void testBoughtTickets() {
		final Tournament tournament = createMock(Tournament.class);
		expect(tournament.getNumber()).andReturn(1);
		expect(tournament.getScheduledDate()).andReturn(new Date());
		replay(tournament);

		final TournamentAnnouncementImpl announcement = new TournamentAnnouncementImpl(tournament);

		for (Language language : Language.values()) {
			for (TournamentSection category : TournamentSection.values()) {
				assertEquals(0, announcement.getBoughtTickets(language, category));
			}
		}

		int i = 0;
		for (Language language : Language.values()) {
			for (TournamentSection category : TournamentSection.values()) {
				announcement.setBoughtTickets(language, category, i++);
			}
		}

		int j = 0;
		for (Language language : Language.values()) {
			for (TournamentSection category : TournamentSection.values()) {
				assertEquals(j++, announcement.getBoughtTickets(language, category));
			}
		}

		int k = 0;
		for (Language language : Language.values()) {
			for (TournamentSection category : TournamentSection.values()) {
				announcement.changeBoughtTickets(language, category, k++);
			}
		}

		int m = 0;
		for (Language language : Language.values()) {
			for (TournamentSection category : TournamentSection.values()) {
				assertEquals(m++ * 2, announcement.getBoughtTickets(language, category));
			}
		}

		final TournamentAnnouncementImpl announcement2 = new TournamentAnnouncementImpl(announcement);
		for (Language language : Language.values()) {
			for (TournamentSection category : TournamentSection.values()) {
				assertEquals(announcement.getBoughtTickets(language, category), announcement2.getBoughtTickets(language, category));
			}
		}
	}
}
