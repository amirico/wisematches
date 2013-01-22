package wisematches.server.services.friends;

import wisematches.core.Personality;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface FriendsListener {
	void friendAdded(Personality person, Personality fried, FriendRelation relation);

	void friendRemoved(Personality person, Personality fried, FriendRelation relation);
}
