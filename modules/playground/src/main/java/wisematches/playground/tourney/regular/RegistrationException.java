package wisematches.playground.tourney.regular;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RegistrationException extends Exception {
	public RegistrationException(String message) {
		super(message);
	}

	public RegistrationException(String message, Throwable cause) {
		super(message, cause);
	}
}
