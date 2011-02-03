package wisematches.server.mail;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MailException extends Exception {
	public MailException(String message) {
		super(message);
	}

	public MailException(String message, Throwable cause) {
		super(message, cause);
	}
}
