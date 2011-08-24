package wisematches.server.web.services.notify;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class NotificationTemplateException extends NotificationException {
	public NotificationTemplateException(String message) {
		super(message);
	}

	public NotificationTemplateException(String message, Throwable cause) {
		super(message, cause);
	}
}
