package wisematches.server.services.message;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface Message {
	long getId();

	Date getCreationDate();

	long getRecipient();

	long getSender();

	String getText();

	boolean isNotification();

	long getOriginal();
}
