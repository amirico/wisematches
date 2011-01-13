package wisematches.server.core.account;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class InadmissibleUsernameException extends AccountRegistrationException {
	private final String reason;

	public InadmissibleUsernameException(String reason) {
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}
}
