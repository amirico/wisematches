package wisematches.client.android.account;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class LoginFailureException extends Exception {
	private final int errorCodeString;

	public LoginFailureException(int errorCodeString) {
		this.errorCodeString = errorCodeString;
	}

	public int getErrorCodeString() {
		return errorCodeString;
	}
}
