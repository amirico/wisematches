package wisematches.server.web.services.recovery;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class TokenExpiredException extends Exception {
	private final Date generationDate;

	public TokenExpiredException(String message, Date generationDate) {
		super(message);
		this.generationDate = generationDate;
	}

	public Date getGenerationDate() {
		return generationDate;
	}
}
