package wisematches.playground.tournament;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TournamentCtx implements TournamentEntityCtx<Tournament> {
    private final TournamentState tournamentState;

    public TournamentCtx(TournamentState tournamentState) {
        this.tournamentState = tournamentState;
    }

    public TournamentState getTournamentState() {
        return tournamentState;
    }
}
