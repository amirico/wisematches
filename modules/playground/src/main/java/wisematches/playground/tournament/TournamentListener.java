package wisematches.playground.tournament;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentListener {
    void tournamentStarted(Tournament tournament);

    void tournamentFinished(Tournament tournament);


    void tournamentRoundStarted(TournamentRound round);

    void tournamentRoundFinished(TournamentRound round);


    void tournamentSectionStarted(TournamentRound round);

    void tournamentSectionFinished(TournamentRound round);
}
