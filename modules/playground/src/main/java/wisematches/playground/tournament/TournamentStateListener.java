package wisematches.playground.tournament;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentStateListener {
	void tournamentScheduled(Tournament tournament);


	void tournamentStarted(Tournament tournament);

	void tournamentFinished(Tournament tournament);
}
