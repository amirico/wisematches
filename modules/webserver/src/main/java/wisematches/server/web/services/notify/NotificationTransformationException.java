package wisematches.server.web.services.notify;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class NotificationTransformationException extends NotificationException {
	public NotificationTransformationException(String message) {
		super(message);
	}

	public NotificationTransformationException(String message, Throwable cause) {
		super(message, cause);
	}
}
