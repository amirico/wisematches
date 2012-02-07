package wisematches.playground.tournament.upcoming;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WrongSubscriptionException extends Exception {
	public WrongSubscriptionException(String message) {
		super(message);
	}

	public WrongSubscriptionException(String message, Throwable cause) {
		super(message, cause);
	}
}
