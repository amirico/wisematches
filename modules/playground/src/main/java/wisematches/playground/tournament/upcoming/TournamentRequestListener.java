package wisematches.playground.tournament.upcoming;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface TournamentRequestListener {
	void playerSubscribed(TournamentRequest request);

	void playerUnsubscribed(TournamentRequest request);
}
