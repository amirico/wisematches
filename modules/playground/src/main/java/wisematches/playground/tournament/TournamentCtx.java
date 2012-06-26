package wisematches.playground.tournament;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TournamentCtx implements TournamentEntityCtx<Tournament> {
	private final TournamentState tournamentState;

	private static final long serialVersionUID = -1965505559778928308L;

	public TournamentCtx(TournamentState tournamentState) {
		this.tournamentState = tournamentState;
	}

	public TournamentState getTournamentState() {
		return tournamentState;
	}
}
