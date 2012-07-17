package wisematches.playground.tournament;

import wisematches.personality.Language;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class Announcement extends Tournament {
	protected Announcement() {
	}

	public abstract int getTotalTickets(Language language);

	public abstract int getBoughtTickets(Language language, TournamentSection section);
}
