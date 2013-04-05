package wisematches.client.android.activity.account;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class SignInFailureException extends Exception {
	private final int errorCodeString;

	public SignInFailureException(String errorCodeString) {
		this.errorCodeString = 1;
	}

	public SignInFailureException(int errorCodeString) {
		this.errorCodeString = errorCodeString;
	}

	public int getErrorCodeString() {
		return errorCodeString;
	}
}
