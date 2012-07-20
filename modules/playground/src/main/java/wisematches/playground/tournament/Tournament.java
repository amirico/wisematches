package wisematches.playground.tournament;

import java.util.Date;

/**
 * Base interface for tournament representation. Each tournament has unique number,
 * start date and finish date.
 * <p/>
 * Each tournament contains set of rounds and each round has set of groups.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface Tournament extends TournamentEntity<Tournament, Tournament.Id, Tournament.Context> {
	int getTournament();

	Date getStartedDate();

	Date getFinishedDate();

	/**
	 * Returns current tournament's state based on dates.
	 *
	 * @return the current tournament's state based on dates.
	 */
	TournamentState getTournamentState();


	public final class Id implements TournamentEntity.Id<Tournament, Id, Context> {
		private final int tournament;

		public Id(int tournament) {
			this.tournament = tournament;
		}

		@Override
		public int getTournament() {
			return tournament;
		}
	}

	public final class Context implements TournamentEntity.Context<Tournament, Id, Context> {
		private final TournamentState tournamentState;

		public Context(TournamentState tournamentState) {
			this.tournamentState = tournamentState;
		}

		public TournamentState getTournamentState() {
			return tournamentState;
		}
	}
}
