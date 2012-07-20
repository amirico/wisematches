package wisematches.playground.tournament;

import wisematches.personality.Language;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface Announcement extends TournamentEntity<Announcement, Announcement.Id, Announcement.Context> {
	int getTournament();

	/**
	 * The scheduled date for this tournament. Has meaning only for not initiated tournaments.
	 *
	 * @return the tournament scheduled date.
	 */
	Date getScheduledDate();


	int getTotalTickets(Language language);

	int getBoughtTickets(Language language, TournamentSection section);


	public final class Id implements TournamentEntity.Id<Announcement, Id, Context> {
		private final int tournament;

		public Id(int tournament) {
			this.tournament = tournament;
		}

		@Override
		public int getTournament() {
			return tournament;
		}
	}

	public final class Context implements TournamentEntity.Context<Announcement, Id, Context> {
		public Context() {
		}
	}
}
