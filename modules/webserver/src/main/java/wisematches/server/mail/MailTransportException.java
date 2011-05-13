package wisematches.server.mail;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MailTransportException extends MailException {
	public MailTransportException(String message) {
		super(message);
	}

	public MailTransportException(String message, Throwable cause) {
		super(message, cause);
	}
}
