package wisematches.playground.tournament.upcoming;

import wisematches.playground.tournament.TournamentSection;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentAnnouncement {
	int getNumber();

	Date getStartDate();

	long getTotalTickets();

	long getBoughtTickets(TournamentSection section);
}
