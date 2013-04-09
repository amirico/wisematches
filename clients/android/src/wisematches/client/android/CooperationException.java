package wisematches.client.android;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class CooperationException extends Exception {
	public CooperationException(String detailMessage) {
		super(detailMessage);
	}

	public CooperationException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}
}
