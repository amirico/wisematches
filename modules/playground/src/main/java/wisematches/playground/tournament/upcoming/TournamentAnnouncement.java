package wisematches.playground.tournament.upcoming;

import wisematches.personality.Language;
import wisematches.playground.tournament.TournamentSection;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentAnnouncement {
	int getNumber();

	Date getScheduledDate();

	int getTotalTickets(Language language);

	int getBoughtTickets(Language language, TournamentSection section);
}
