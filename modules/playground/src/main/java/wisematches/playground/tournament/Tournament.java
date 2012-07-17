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
public abstract class Tournament extends TournamentEntity {
	protected Tournament() {
	}

	/**
	 * Returns number of the tournament.
	 *
	 * @return the number of the tournament.
	 */
	public abstract int getNumber();


	/**
	 * The scheduled date for this tournament. Has meaning only for not initiated tournaments.
	 *
	 * @return the tournament scheduled date.
	 */
	public abstract Date getScheduledDate();


	/**
	 * Returns start date of the tournament. If the date in the future when the tournament
	 * was announced but not started yet.
	 *
	 * @return the start date
	 */
	public abstract Date getStartedDate();

	/**
	 * Returns finish date of the tournament. If date is null when tournament is not finished or not started yet.
	 *
	 * @return the finish date or {@code null} if the tournament isn't finished.
	 */
	public abstract Date getFinishedDate();

	/**
	 * Returns current tournament's state based on dates.
	 *
	 * @return the current tournament's state based on dates.
	 */
	public TournamentState getTournamentState() {
		if (getFinishedDate() != null) {
			return TournamentState.FINISHED;
		}
		if (getStartedDate() != null) {
			return TournamentState.STARTED;
		}
		if (getScheduledDate() != null) {
			return TournamentState.SCHEDULED;
		}
		throw new IllegalStateException("Tournament in incorrect state.");
	}


	public static Id createId(final int tournament) {
		return new Id(tournament);
	}

	public static Context createContext(TournamentState state) {
		return new Context(state);
	}


	public static class Id implements TournamentEntityId<Tournament> {
		private final int tournament;

		protected Id(int tournament) {
			this.tournament = tournament;
		}

		public int getTournament() {
			return tournament;
		}
	}

	public static class Context implements TournamentEntityContext<Tournament> {
		private final TournamentState tournamentState;

		private Context(TournamentState tournamentState) {
			this.tournamentState = tournamentState;
		}

		public TournamentState getTournamentState() {
			return tournamentState;
		}
	}
}
