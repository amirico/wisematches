package wisematches.server.web.mail;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class MailTemplateException extends MailException {
	public MailTemplateException(String message) {
		super(message);
	}

	public MailTemplateException(String message, Throwable cause) {
		super(message, cause);
	}
}
