package wisematches.server.web.servlet.sdo.scribble.comment;

import wisematches.playground.GameMessageSource;
import wisematches.playground.scribble.comment.GameComment;
import wisematches.server.web.servlet.sdo.InternationalisedInfo;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class CommentInfo extends InternationalisedInfo {
	private final GameComment comment;

	public CommentInfo(GameComment comment, GameMessageSource messageSource, Locale locale) {
		super(messageSource, locale);
		this.comment = comment;
	}

	public long getId() {
		return comment.getId();
	}

	public String getText() {
		return comment.getText();
	}

	public long getPerson() {
		return comment.getPerson();
	}

	public String getElapsed() {
		return messageSource.formatElapsedTime(comment.getCreationDate(), locale);
	}
}
