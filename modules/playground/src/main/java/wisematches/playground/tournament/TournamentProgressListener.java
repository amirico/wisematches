package wisematches.playground.tournament;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentProgressListener {
	void tournamentSectionFinished(Tournament tournament, TournamentSection section);

	void tournamentRoundFinished(Tournament tournament, TournamentRound round);

	void tournamentGroupFinished(Tournament tournament, TournamentGroup group);
}
