package wisematches.playground.tournament;

import wisematches.personality.Language;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentAnnouncement extends Tournament {
	int getTotalTickets(Language language);

	int getBoughtTickets(Language language, TournamentSection section);
}
