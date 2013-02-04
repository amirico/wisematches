package wisematches.server.services.friends;

import wisematches.core.Player;

import java.util.Collection;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface FriendsManager {
	void addFriendsListener(FriendsListener l);

	void removeFriendsListener(FriendsListener l);


	void addFriend(Player person, Player friend, String comment);

	void removeFriend(Player person, Player friend);

	Collection<Long> getFriendsIds(Player person);

	Collection<FriendRelation> getFriendsList(Player person);
}
