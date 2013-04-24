package wisematches.client.android.http;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class CommunicationException extends Exception {
	private final int statusCode;
	private final String statusReason;

	public CommunicationException(int statusCode, String statusReason) {
		super(statusCode + ": " + statusReason);
		this.statusReason = statusReason;
		this.statusCode = statusCode;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getStatusReason() {
		return statusReason;
	}
}
