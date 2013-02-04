package wisematches.server.services.friends;

import wisematches.core.Player;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface FriendsListener {
	void friendAdded(Player person, Player fried, FriendRelation relation);

	void friendRemoved(Player person, Player fried, FriendRelation relation);
}
