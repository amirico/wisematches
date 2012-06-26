package wisematches.playground.tournament;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentProgressListener {
	void tournamentGroupFinished(Tournament tournament, TournamentGroup group);

	void tournamentRoundFinished(Tournament tournament, TournamentRound round);

	void tournamentSectionFinished(Tournament tournament, TournamentSection section);
}
