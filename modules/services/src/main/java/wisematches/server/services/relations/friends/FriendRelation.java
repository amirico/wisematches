package wisematches.server.services.relations.friends;

import java.util.Date;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface FriendRelation {
	long getPerson();

	long getFriend();

	String getComment();

	Date getRegistered();
}
