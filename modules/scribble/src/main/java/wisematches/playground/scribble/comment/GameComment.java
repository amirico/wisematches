package wisematches.playground.scribble.comment;

import java.util.Date;

/**
 * A scribble comment that has board, person and a message.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface GameComment {
	/**
	 * Returns id of the message
	 *
	 * @return the id of the message
	 */
	long getId();

	/**
	 * Returns board id
	 *
	 * @return the board id
	 */
	long getBoard();

	/**
	 * Returns person id who has created a comment
	 *
	 * @return the person id who has created a comment
	 */
	long getPerson();

	/**
	 * Returns creation date
	 *
	 * @return the creation date
	 */
	Date getCreationDate();

	/**
	 * Returns a message text
	 *
	 * @return the message text
	 */
	String getText();
}
