package wisematches.server.services.players.friends;

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
