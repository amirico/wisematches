package wisematches.playground.tournament;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentStateListener {
	void tournamentStarted(Tournament tournament);

	void tournamentFinished(Tournament tournament);

	void tournamentScheduled(Tournament tournament);
}
