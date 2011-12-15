package wisematches.playground.tournament;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TournamentSubscriptionException extends TournamentException {
	public TournamentSubscriptionException(String message) {
		super(message);
	}

	public TournamentSubscriptionException(String message, Throwable cause) {
		super(message, cause);
	}
}
