package wisematches.server.web.services.recovery;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TokenExpiredException extends Exception {
	public TokenExpiredException(String message) {
		super(message);
	}

	public TokenExpiredException(String message, Throwable cause) {
		super(message, cause);
	}
}
